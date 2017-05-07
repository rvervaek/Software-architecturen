package org.soft.assignment1.lagom.task.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.soft.assignment1.lagom.task.api.TaskStatus;
import org.soft.assignment1.lagom.task.impl.PTask.PTaskColor;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;

import akka.Done;

public class PTaskEventProcessor extends ReadSideProcessor<PTaskEvent> {

	private final CassandraSession session;
	private final CassandraReadSide readSide;
	
	private PreparedStatement insertTaskStatement = null;
	
	private PreparedStatement updateTaskStatement = null;
	private PreparedStatement updateTaskStatusStatement = null;
	
	@Inject
	public PTaskEventProcessor(CassandraSession session, CassandraReadSide readSide) {
		this.session = session;
		this.readSide = readSide;
	}
	
	@Override
	public ReadSideHandler<PTaskEvent> buildHandler() {
		return readSide.<PTaskEvent>builder("pTaskEventOffset")
				.setGlobalPrepare(this::createTables)
				.setPrepare(tag -> prepareStatements())
				.setEventHandler(PTaskEvent.Created.class, evt -> insertTask(evt.getTask()))
				.setEventHandler(PTaskEvent.Updated.class, evt -> updateTask(evt.getId(), evt.getTitle(), evt.getDetails(), evt.getColor()))
				.setEventHandler(PTaskEvent.Backlogged.class, evt -> updateTaskStatus(evt.getId(), evt.getStatus()))
				.setEventHandler(PTaskEvent.Scheduled.class, evt -> updateTaskStatus(evt.getId(), evt.getStatus()))
				.setEventHandler(PTaskEvent.Started.class, evt -> updateTaskStatus(evt.getId(), evt.getStatus()))
				.setEventHandler(PTaskEvent.Completed.class, evt -> updateTaskStatus(evt.getId(), evt.getStatus()))
				.setEventHandler(PTaskEvent.Archived.class, evt -> updateTaskStatus(evt.getId(), evt.getStatus()))
				.build();
	}

	@Override
	public PSequence<AggregateEventTag<PTaskEvent>> aggregateTags() {
		return TreePVector.singleton(PTaskEventTag.INSTANCE);
	}
	
	public void setInsertTaskStatement(PreparedStatement insertTaskStatement) {
		this.insertTaskStatement = insertTaskStatement;
	}

	public void setUpdateTaskStatement(PreparedStatement updateTaskStatement) {
		this.updateTaskStatement = updateTaskStatement;
	}

	public void setUpdateTaskStatusStatement(PreparedStatement updateTaskStatusStatement) {
		this.updateTaskStatusStatement = updateTaskStatusStatement;
	}

	private void registerCodec(Session session, TypeCodec<?> codec) {
		session.getCluster().getConfiguration().getCodecRegistry().register(codec);
	}

	private CompletionStage<Done> createTables() {
		return session.executeCreateTable(
				"CREATE TABLE IF NOT EXISTS task (taskId timeuuid PRIMARY KEY, title text, details text, red int, green int, blue int, status text, boardId timeuuid)");
	}
	
	private CompletionStage<Done> prepareStatements() {
		return session
				.underlying()
				.thenAccept(s -> registerCodec(s, new EnumNameCodec<>(TaskStatus.class)))
				.thenCombine(prepareInsertTaskStatement(), (c1, c2) -> Done.getInstance())
				.thenCombine(prepareUpdateTaskStatement(), (c1, c2) -> Done.getInstance())
				.thenCombine(prepareUpdateTaskStatusStatement(), (c1, c2) -> Done.getInstance())
				.thenApply(ack -> Done.getInstance());
	}

	private CompletionStage<Done> prepareUpdateTaskStatusStatement() {
		return session
				.prepare("UPDATE task SET title = ?, details = ?, red = ?, green = ?, blue = ? WHERE taskId = ?")
				.thenApply(ps -> {
					setUpdateTaskStatement(ps);
					return Done.getInstance();
				});
	}

	private CompletionStage<Done> prepareUpdateTaskStatement() {
		return session
				.prepare("UPDATE task SET status = ? WHERE taskId = ?")
				.thenApply(ps -> {
					setUpdateTaskStatement(ps);
					return Done.getInstance();
				});
	}

	private CompletionStage<Done> prepareInsertTaskStatement() {
		return session.prepare("INSERT INTO task (taskId, title, details, red, green, blue, status, boardId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)").thenApply(ps -> {
			setInsertTaskStatement(ps);
			return Done.getInstance();
		});
	}
	
	private CompletionStage<List<BoundStatement>> insertTask(PTask task) {
		return CassandraReadSide.completedStatement(
				insertTaskStatement.bind(task.getId(), task.getTitle(), task.getDetails(), task.getColor().getRed(),
						task.getColor().getGreen(), task.getColor().getBlue(), task.getStatus(), task.getBoardId()));
	}

	private CompletionStage<List<BoundStatement>> updateTask(UUID id, String title, String details, PTaskColor color) {
		return CassandraReadSide.completedStatement(updateTaskStatement.bind(title, details, color.getRed(), color.getGreen(), color.getBlue(), id));
	}
	
	private CompletionStage<List<BoundStatement>> updateTaskStatus(UUID id, PTaskStatus status) {
		return CassandraReadSide.completedStatement(updateTaskStatusStatement.bind(status, id));
	}
}

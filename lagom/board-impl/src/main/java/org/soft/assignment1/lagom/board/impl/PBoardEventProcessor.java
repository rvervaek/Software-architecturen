package org.soft.assignment1.lagom.board.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.pcollections.PSequence;
import org.pcollections.TreePVector;

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

public class PBoardEventProcessor extends ReadSideProcessor<PBoardEvent> {

	private final CassandraSession session;
	private final CassandraReadSide readSide;
	
	private PreparedStatement insertBoardStatement = null;
	
	private PreparedStatement updateBoardStatement = null;
	private PreparedStatement updateBoardStatusStatement = null;
		
	@Inject
	public PBoardEventProcessor(CassandraSession session, CassandraReadSide readSide) {
		this.session = session;
		this.readSide = readSide;
	}
	
	@Override
	public ReadSideHandler<PBoardEvent> buildHandler() {
		return readSide.<PBoardEvent>builder("pBoardEventOffset")
				.setGlobalPrepare(this::createTables)
				.setPrepare(tag -> prepareStatements())
				.setEventHandler(PBoardEvent.Created.class, e -> insertBoard(e.getBoard()))
				.setEventHandler(PBoardEvent.Updated.class, e -> updateBoard(e.getId(), e.getTitle()))
				.setEventHandler(PBoardEvent.Activated.class, e -> updateBoardStatus(e.getId(), e.getStatus()))
				.setEventHandler(PBoardEvent.Archived.class, e -> updateBoardStatus(e.getId(), e.getStatus()))
				.build();
	}
	
	@Override
	public PSequence<AggregateEventTag<PBoardEvent>> aggregateTags() {
		return TreePVector.singleton(PBoardEventTag.INSTANCE);
	}
	
	public void setInsertBoardStatement(PreparedStatement insertBoardStatement) {
		this.insertBoardStatement = insertBoardStatement;
	}
	
	public void setUpdateBoardStatement(PreparedStatement updateBoardStatement) {
		this.updateBoardStatement = updateBoardStatement;
	}

	public void setUpdateBoardStatusStatement(PreparedStatement updateBoardStatusStatement) {
		this.updateBoardStatusStatement = updateBoardStatusStatement;
	}
	
	private CompletionStage<Done> createTables() {
		return session
				.executeCreateTable("CREATE TABLE IF NOT EXISTS board (boardId timeuuid PRIMARY KEY, title text, status text)");
	}
	
	private void registerCodec(Session session, TypeCodec<?> codec) {
		session.getCluster().getConfiguration().getCodecRegistry().register(codec);
	}
	
	private CompletionStage<Done> prepareStatements() {
		return session
				.underlying()
				.thenAccept(s -> registerCodec(s, new EnumNameCodec<>(PBoardStatus.class)))
				.thenCombine(prepareInsertBoardStatement(), (c1, c2) -> Done.getInstance())
				.thenCombine(prepareUpdateBoardStatement(), (c1, c2) -> Done.getInstance())
				.thenCombine(prepareUpdateBoardStatusStatement(), (c1, c2) -> Done.getInstance())
				.thenApply(ack -> Done.getInstance());
	}
	
	private CompletionStage<Done> prepareInsertBoardStatement() {
		return session.prepare("INSERT INTO board (boardId, title, status) VALUES (?, ?, ?)").thenApply(ps -> {
			setInsertBoardStatement(ps);
			return Done.getInstance();
		});
	}
	
	private CompletionStage<Done> prepareUpdateBoardStatement() {
		return session
				.prepare("UPDATE board set title = ? where boardId = ?")
				.thenApply(ps -> {
					setUpdateBoardStatement(ps);
					return Done.getInstance();
				});
	}
	
	private CompletionStage<Done> prepareUpdateBoardStatusStatement() {
		return session
				.prepare("UPDATE board set status = ? where boardId = ?")
				.thenApply(ps -> {
					setUpdateBoardStatusStatement(ps);
					return Done.getInstance();
				});
	}
	
	private CompletionStage<List<BoundStatement>> insertBoard(PBoard board) {
		return CassandraReadSide.completedStatement(insertBoardStatement.bind(board.getId(), board.getTitle(), board.getStatus()));
	}

	private CompletionStage<List<BoundStatement>> updateBoard(UUID id, String title) {
		return CassandraReadSide.completedStatement(updateBoardStatement.bind(title, id));
	}
	
	private CompletionStage<List<BoundStatement>> updateBoardStatus(UUID id, PBoardStatus status) {
		return CassandraReadSide.completedStatement(updateBoardStatusStatement.bind(status, id));
	}

}

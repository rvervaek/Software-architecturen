package org.soft.assignment1.lagom.task.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.soft.assignment1.lagom.board.api.Board;
import org.soft.assignment1.lagom.board.api.BoardService;
import org.soft.assignment1.lagom.board.api.BoardStatus;
import org.soft.assignment1.lagom.task.api.Task;
import org.soft.assignment1.lagom.task.api.TaskService;
import org.soft.assignment1.lagom.task.api.TaskStatus;

import com.datastax.driver.core.utils.UUIDs;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;

import akka.NotUsed;

public class TaskServiceImpl implements TaskService {

	private final BoardService boardService;
	
	private final PersistentEntityRegistry persistentEntityRegistry;
	private final CassandraSession database;
	
	@Inject
	public TaskServiceImpl(PersistentEntityRegistry persistentEntityRegistry, ReadSide readSide, CassandraSession database, BoardService boardService) {
		this.persistentEntityRegistry = persistentEntityRegistry;
		this.database = database;
		this.boardService = boardService;
		persistentEntityRegistry.register(PTaskEntity.class);
		readSide.register(PTaskEventProcessor.class);
	}

	@Override
	public ServiceCall<Task, NotUsed> create() {
		return task -> {
			
			/* Request the board linked to the task */
			CompletionStage<Board> response = boardService.get(task.getBoardId()).invoke();
			/* Throw an exception when the board was not found */
			response.exceptionally(t -> {
				throw new NotFound(t);
			});
			/* Block until the board was retrieved */
			Board board = response.toCompletableFuture().join();					
			/* Throw an exception when the board was already ARCHIVED */
			if (board.getStatus() == BoardStatus.ARCHIVED) {
				throw new IllegalStateException("Board " + task.getBoardId() + " was already archived");
			}
			/* Persist the new task */
			return persistentEntityRegistry
					.refFor(PTaskEntity.class, UUIDs.timeBased().toString())
					.ask(new PTaskCommand.Create(Mappers.fromApi(task)))
					.thenApply(ack -> NotUsed.getInstance());
		};
	}

	@Override
	public ServiceCall<NotUsed, Task> get(UUID id) {
		return request -> {
			return persistentEntityRegistry
					.refFor(PTaskEntity.class, id.toString())
					.ask(new PTaskCommand.Get(id));
		};
	}

	@Override
	public ServiceCall<Task, NotUsed> update(UUID id) {
		return task -> {
			return persistentEntityRegistry
					.refFor(PTaskEntity.class, id.toString())
					.ask(new PTaskCommand.Update(id, Mappers.fromApi(task)))
					.thenApply(ack -> NotUsed.getInstance());
		};
	}

	@Override
	public ServiceCall<TaskStatus, NotUsed> updateStatus(UUID id) {
		return status -> {
			return persistentEntityRegistry
					.refFor(PTaskEntity.class, id.toString())
					.ask(new PTaskCommand.UpdateStatus(id, PTaskStatus.get(status)))
					.thenApply(ack -> NotUsed.getInstance());
		};
	}

	@Override
	public ServiceCall<NotUsed, PSequence<Task>> getByBoardId(UUID boardId) {
		return request -> {
		CompletionStage<PSequence<Task>> result = database.selectAll("SELECT * FROM task WHERE boardId = ?", boardId)
				.thenApply(rows -> {
					List<Task> tasks = rows
							.stream()
							.map(row -> new Task(
									row.getUUID("id"), 
									row.getString("title"), 
									row.getString("details"), 
									new Task.TaskColor(
											row.getInt("red"),
											row.getInt("green"),
											row.getInt("blue")), 
									row.get("status", TaskStatus.class), 
									row.getUUID("boardId")))
							.collect(Collectors.toList());
					return TreePVector.from(tasks);
				});
		return result;
		};
	}

}

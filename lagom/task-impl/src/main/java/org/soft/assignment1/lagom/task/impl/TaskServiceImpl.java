package org.soft.assignment1.lagom.task.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.soft.assignment1.lagom.task.api.Task;
import org.soft.assignment1.lagom.task.api.TaskService;
import org.soft.assignment1.lagom.task.api.TaskStatus;

import com.datastax.driver.core.utils.UUIDs;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;

import akka.NotUsed;

public class TaskServiceImpl implements TaskService {

	private final PersistentEntityRegistry persistentEntityRegistry;
	private final CassandraSession database;
	
	public TaskServiceImpl(PersistentEntityRegistry persistentEntityRegistry, CassandraSession database) {
		this.persistentEntityRegistry = persistentEntityRegistry;
		this.database = database;
		persistentEntityRegistry.register(TaskEntity.class);
	}

	@Override
	public ServiceCall<Task, NotUsed> create() {
		return task -> {
			UUID itemId = UUIDs.timeBased();
			return persistentEntityRegistry
				.refFor(TaskEntity.class, itemId.toString())
				.ask(new TaskCommand.Create(new Task(itemId, task.title, task.details, task.color, task.status, task.boardId)))
				.thenApply(ack -> NotUsed.getInstance());	
		};
	}

	@Override
	public ServiceCall<NotUsed, Task> get(UUID id) {
		return request -> {
			return persistentEntityRegistry
					.refFor(TaskEntity.class, id.toString())
					.ask(new TaskCommand.Get(id));
		};
	}

	@Override
	public ServiceCall<Task, NotUsed> update(UUID id) {
		return task -> {
			return persistentEntityRegistry
					.refFor(TaskEntity.class, id.toString())
					.ask(new TaskCommand.Update(task))
					.thenApply(ack -> NotUsed.getInstance());
		};
	}

	@Override
	public ServiceCall<TaskStatus, NotUsed> updateStatus(UUID id) {
		return status -> {
			return persistentEntityRegistry
					.refFor(TaskEntity.class, id.toString())
					.ask(new TaskCommand.UpdateStatus(status))
					.thenApply(ack -> NotUsed.getInstance());
		};
	}

	@Override
	public ServiceCall<NotUsed, PSequence<Task>> getByBoardId(String boardId) {
		return request -> {
		CompletionStage<PSequence<Task>> result = database.selectAll("SELECT * FROM task WHERE boardId = ?", boardId)
				.thenApply(rows -> {
					List<Task> tasks = rows
							.stream()
							.map(row -> new Task(
									row.get("id", UUID.class), 
									row.getString("title"), 
									row.getString("details"), 
									new Task.TaskColor(
											row.getInt("red"),
											row.getInt("green"),
											row.getInt("blue")), 
									row.get("status", TaskStatus.class), 
									row.getString("boardId")))
							.collect(Collectors.toList());
					return TreePVector.from(tasks);
				});
		return result;
		};
	}

}

package org.soft.assignment1.lagom.task.impl;

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import org.soft.assignment1.lagom.task.api.Task;
import org.soft.assignment1.lagom.task.api.Task.TaskColor;
import org.soft.assignment1.lagom.task.api.TaskStatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;

import akka.Done;

public interface TaskCommand extends Jsonable {

	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public final class Create implements TaskCommand, PersistentEntity.ReplyType<Done> {

		public final Task task;
		
		@JsonCreator
		public Create(Task task) {
			this.task = task;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public final class Get implements TaskCommand, PersistentEntity.ReplyType<Task> {

		public final UUID id;
		
		@JsonCreator
		public Get(UUID id) {
			this.id = id;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public final class Update implements TaskCommand, PersistentEntity.ReplyType<Done> {

		public final String title;
		public final String details;
		public final TaskColor color;
		
		@JsonCreator
		public Update(Task task) {
			this.title = task.title;
			this.details = task.details;
			this.color = task.color;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public final class UpdateStatus implements TaskCommand, PersistentEntity.ReplyType<Done> {

		public final TaskStatus status;
		
		@JsonCreator
		public UpdateStatus(TaskStatus status) {
			this.status = status;
		}
	}
}

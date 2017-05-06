package org.soft.assignment1.lagom.task.impl;

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import org.soft.assignment1.lagom.task.api.Task;
import org.soft.assignment1.lagom.task.impl.PTask.PTaskColor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;

import akka.Done;

public interface PTaskCommand extends Jsonable {

	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public final class Create implements PTaskCommand, PersistentEntity.ReplyType<Done> {

		private final PTask task;
		
		@JsonCreator
		public Create(PTask task) {
			this.task = task;
		}

		public PTask getTask() {
			return task;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public final class Get implements PTaskCommand, PersistentEntity.ReplyType<Task> {

		private final UUID id;
		
		@JsonCreator
		public Get(UUID id) {
			this.id = id;
		}

		public UUID getId() {
			return id;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public final class Update implements PTaskCommand, PersistentEntity.ReplyType<Done> {

		private final UUID id;
		
		private final String title;
		private final String details;
		private final PTaskColor color;
		
		@JsonCreator
		public Update(UUID id, PTask task) {
			this.id = id;
			this.title = task.getTitle();
			this.details = task.getDetails();
			this.color = task.getColor();
		}

		public String getTitle() {
			return title;
		}

		public String getDetails() {
			return details;
		}

		public PTaskColor getColor() {
			return color;
		}
		
		public UUID getId() {
			return id;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public final class UpdateStatus implements PTaskCommand, PersistentEntity.ReplyType<Done> {

		private final UUID id;
		
		public final PTaskStatus status;
		
		@JsonCreator
		public UpdateStatus(UUID id, PTaskStatus status) {
			this.id = id;
			this.status = status;
		}

		public PTaskStatus getStatus() {
			return status;
		}
		
		public UUID getId() {
			return id;
		}
	}
}

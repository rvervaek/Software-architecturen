package org.soft.assignment1.lagom.task.impl;

import java.awt.Color;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.soft.assignment1.lagom.task.api.TaskStatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;

public interface TaskEvent extends Jsonable, AggregateEvent<TaskEvent> {

	@Override
	default public AggregateEventTagger<TaskEvent> aggregateTag() {
		return TaskEventTag.INSTANCE;
	}

	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class BoardCreated implements TaskEvent {

		public final String title;
		public final String details;
		public final Color color;
		public final String boardId;

		@JsonCreator
		public BoardCreated(String title, String details, Color color, String boardId) {
			this.title = Preconditions.checkNotNull(title, "title");
			this.details = Preconditions.checkNotNull(details, "details");
			this.color = Preconditions.checkNotNull(color, "color");
			this.boardId = Preconditions.checkNotNull(boardId, "boardId");
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class BoardUpdated implements TaskEvent {

		public final String title;
		public final String details;
		public final Color color;

		@JsonCreator
		public BoardUpdated(@Nullable String title, @Nullable String details, @Nullable Color color) {
			this.title = title;
			this.details = details;
			this.color = color;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class BoardStatusUpdated implements TaskEvent {

		public final TaskStatus status;

		@JsonCreator
		public BoardStatusUpdated(TaskStatus status) {
			this.status = Preconditions.checkNotNull(status, "status");
		}
	}
}

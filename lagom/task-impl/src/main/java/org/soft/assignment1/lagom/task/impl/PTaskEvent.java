package org.soft.assignment1.lagom.task.impl;

import javax.annotation.concurrent.Immutable;

import org.soft.assignment1.lagom.task.impl.PTask.PTaskColor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;

public interface PTaskEvent extends Jsonable, AggregateEvent<PTaskEvent> {

	@Override
	default public AggregateEventTagger<PTaskEvent> aggregateTag() {
		return TaskEventTag.INSTANCE;
	}

	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Created implements PTaskEvent {

		private final PTask task;

		@JsonCreator
		public Created(PTask task) {
			this.task = Preconditions.checkNotNull(task);
		}

		public PTask getTask() {
			return task;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Backlogged implements PTaskEvent {

		public final PTaskStatus status;

		@JsonCreator
		public Backlogged(PTaskStatus status) {
			assert status == PTaskStatus.BACKLOG;
			this.status = status;
		}

		public PTaskStatus getStatus() {
			return status;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Updated implements PTaskEvent {

		private final String title;
		private final String details;
		private final PTaskColor color;

		@JsonCreator
		public Updated(String title, String details, PTaskColor color) {
			this.title = title;
			this.details = details;
			this.color = color;
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
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Scheduled implements PTaskEvent {

		public final PTaskStatus status;

		@JsonCreator
		public Scheduled(PTaskStatus status) {
			assert status == PTaskStatus.SCHEDULED;
			this.status = status;
		}

		public PTaskStatus getStatus() {
			return status;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Started implements PTaskEvent {

		public final PTaskStatus status;

		@JsonCreator
		public Started(PTaskStatus status) {
			assert status == PTaskStatus.STARTED;
			this.status = status;
		}

		public PTaskStatus getStatus() {
			return status;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Completed implements PTaskEvent {

		public final PTaskStatus status;

		@JsonCreator
		public Completed(PTaskStatus status) {
			assert status == PTaskStatus.COMPLETED;
			this.status = status;
		}

		public PTaskStatus getStatus() {
			return status;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Archived implements PTaskEvent {

		public final PTaskStatus status;

		@JsonCreator
		public Archived(PTaskStatus status) {
			assert status == PTaskStatus.ARCHIVED;
			this.status = status;
		}

		public PTaskStatus getStatus() {
			return status;
		}
	}
}

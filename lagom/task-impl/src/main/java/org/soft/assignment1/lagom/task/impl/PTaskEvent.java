package org.soft.assignment1.lagom.task.impl;

import java.util.UUID;

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
		return PTaskEventTag.INSTANCE;
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

		private final UUID id;
		
		private final PTaskStatus status;

		@JsonCreator
		public Backlogged(UUID id, PTaskStatus status) {
			assert status == PTaskStatus.BACKLOG;
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
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Updated implements PTaskEvent {

		private final UUID id;
		
		private final String title;
		private final String details;
		private final PTaskColor color;

		@JsonCreator
		public Updated(UUID id, String title, String details, PTaskColor color) {
			this.id = id;
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
		
		public UUID getId() {
			return id;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Scheduled implements PTaskEvent {

		private final UUID id;
		
		private final PTaskStatus status;

		@JsonCreator
		public Scheduled(UUID id, PTaskStatus status) {
			assert status == PTaskStatus.SCHEDULED;
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
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Started implements PTaskEvent {

		private final UUID id;
		
		private final PTaskStatus status;

		@JsonCreator
		public Started(UUID id, PTaskStatus status) {
			assert status == PTaskStatus.STARTED;
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
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Completed implements PTaskEvent {

		private final UUID id;
		
		private final PTaskStatus status;

		@JsonCreator
		public Completed(UUID id, PTaskStatus status) {
			assert status == PTaskStatus.COMPLETED;
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
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Archived implements PTaskEvent {

		private final UUID id;
		
		private final PTaskStatus status;

		@JsonCreator
		public Archived(UUID id, PTaskStatus status) {
			assert status == PTaskStatus.ARCHIVED;
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

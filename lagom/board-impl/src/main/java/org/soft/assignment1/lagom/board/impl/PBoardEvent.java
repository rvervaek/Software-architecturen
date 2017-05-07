package org.soft.assignment1.lagom.board.impl;

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;

public interface PBoardEvent extends Jsonable, AggregateEvent<PBoardEvent> {

	@Override
	default public AggregateEventTagger<PBoardEvent> aggregateTag() {
		return PBoardEventTag.INSTANCE;
	}

	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Created implements PBoardEvent {

		private final PBoard board;

		@JsonCreator
		public Created(PBoard board) {
			this.board = board;
		}

		public PBoard getBoard() {
			return board;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((board == null) ? 0 : board.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Created other = (Created) obj;
			if (board == null) {
				if (other.board != null)
					return false;
			} else if (!board.equals(other.board))
				return false;
			return true;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Activated implements PBoardEvent {

		private final UUID id;
		
		private final PBoardStatus status;

		@JsonCreator
		public Activated(@JsonProperty("id") UUID id, @JsonProperty("status") PBoardStatus status) {
			assert status == PBoardStatus.CREATED;
			this.id = id;
			this.status = status;
		}

		public PBoardStatus getStatus() {
			return status;
		}

		public UUID getId() {
			return id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Activated other = (Activated) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Updated implements PBoardEvent {

		private final UUID id;
		
		private final String title;

		@JsonCreator
		public Updated(@JsonProperty("id") UUID id, @JsonProperty("title") String title) {
			this.id = id;
			this.title = Preconditions.checkNotNull(title, "title");
		}

		public String getTitle() {
			return title;
		}
		
		public UUID getId() {
			return id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Updated other = (Updated) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Archived implements PBoardEvent {

		private final UUID id;
		
		private final PBoardStatus status;

		@JsonCreator
		public Archived(@JsonProperty("id") UUID id, @JsonProperty("status") PBoardStatus status) {
			assert status == PBoardStatus.ARCHIVED;
			this.id = id;
			this.status = status;
		}

		public PBoardStatus getStatus() {
			return status;
		}
		
		public UUID getId() {
			return id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Archived other = (Archived) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
	}
}

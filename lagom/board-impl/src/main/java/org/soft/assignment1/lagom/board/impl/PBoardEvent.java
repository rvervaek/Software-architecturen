package org.soft.assignment1.lagom.board.impl;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;

public interface PBoardEvent extends Jsonable, AggregateEvent<PBoardEvent> {

	@Override
	default public AggregateEventTagger<PBoardEvent> aggregateTag() {
		return BoardEventTag.INSTANCE;
	}

	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Created implements PBoardEvent {

		private final PBoard board;

		@JsonCreator
		public Created(PBoard board) {
			this.board = Preconditions.checkNotNull(board, "board");
		}

		public PBoard getBoard() {
			return board;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Updated implements PBoardEvent {

		private final String title;

		@JsonCreator
		public Updated(String title) {
			this.title = Preconditions.checkNotNull(title, "title");
		}

		public String getTitle() {
			return title;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public class Archived implements PBoardEvent {

		private final PBoardStatus status;

		@JsonCreator
		public Archived(PBoardStatus status) {
			assert status == PBoardStatus.ARCHIVED;
			this.status = Preconditions.checkNotNull(status, "status");
		}

		public PBoardStatus getStatus() {
			return status;
		}
	}
}

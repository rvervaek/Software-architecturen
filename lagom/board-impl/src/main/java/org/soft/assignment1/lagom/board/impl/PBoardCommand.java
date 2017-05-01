package org.soft.assignment1.lagom.board.impl;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;

import akka.Done;

public interface PBoardCommand extends Jsonable {

	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public final class Create implements PBoardCommand, PersistentEntity.ReplyType<Done> {

		private final PBoard board;
		
		@JsonCreator
		public Create(PBoard board) {
			this.board = board;
		}

		public PBoard getBoard() {
			return board;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public final class Update implements PBoardCommand, PersistentEntity.ReplyType<Done> {
		
		private final String title;
		
		@JsonCreator
		public Update(String title) {
			this.title = title;
		}
		
		public String getTitle() {
			return title;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public final class UpdateStatus implements PBoardCommand, PersistentEntity.ReplyType<Done> {
		
		private final PBoardStatus status;
		
		@JsonCreator
		public UpdateStatus(PBoardStatus status) {
			this.status = status;
		}
		
		public PBoardStatus getStatus() {
			return status;
		}
	}

}

package org.soft.assignment1.lagom.board.impl;

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import org.soft.assignment1.lagom.board.api.Board;

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
	public final class Get implements PBoardCommand, PersistentEntity.ReplyType<Board> {
				
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
	public final class Update implements PBoardCommand, PersistentEntity.ReplyType<Done> {
		
		private final UUID id;
		
		private final String title;
		
		@JsonCreator
		public Update(UUID id, String title) {
			this.id = id;
			this.title = title;
		}
		
		public String getTitle() {
			return title;
		}
		
		public UUID getId() {
			return id;
		}
	}
	
	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	public final class UpdateStatus implements PBoardCommand, PersistentEntity.ReplyType<Done> {
		
		private final UUID id;
		
		private final PBoardStatus status;
		
		@JsonCreator
		public UpdateStatus(UUID id, PBoardStatus status) {
			this.id = id;
			this.status = status;
		}
		
		public PBoardStatus getStatus() {
			return status;
		}
		
		public UUID getId() {
			return id;
		}
	}

}

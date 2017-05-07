package org.soft.assignment1.lagom.board.impl;

import javax.annotation.concurrent.Immutable;

import org.soft.assignment1.lagom.board.api.BoardStatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize
@JsonDeserialize
public enum PBoardStatus {

	NOT_CREATED {
		@Override
		BoardStatus toBoardStatus() {
			throw new IllegalStateException("Publicly exposed board can't be not created");
		}	
	},
	CREATED {
		@Override
		BoardStatus toBoardStatus() {
			return BoardStatus.CREATED;
		}	
	},
	ARCHIVED {
		@Override
		BoardStatus toBoardStatus() {
			return BoardStatus.ARCHIVED;
		}	
	};
	
	abstract BoardStatus toBoardStatus();
	
	@JsonCreator
	public static PBoardStatus get(String status) {
		System.out.println(status);
		for (PBoardStatus s : PBoardStatus.values()) {
			if (s.name().equals(status)) {
				return s;
			}
		}
		return null;
	}
	
	public static PBoardStatus get(BoardStatus status) {
		switch(status) {
		case ARCHIVED: return PBoardStatus.ARCHIVED;
		case CREATED: return PBoardStatus.CREATED;
		default:
			throw new IllegalStateException("Cannot convert state " + status + " to one of states of PBoardStatus");
		}
	}
}

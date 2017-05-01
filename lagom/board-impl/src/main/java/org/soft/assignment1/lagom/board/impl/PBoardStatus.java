package org.soft.assignment1.lagom.board.impl;

import org.soft.assignment1.lagom.board.api.BoardStatus;

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
	
	public static PBoardStatus get(BoardStatus status) {
		switch(status) {
		case ARCHIVED: return PBoardStatus.ARCHIVED;
		case CREATED: return PBoardStatus.CREATED;
		default:
			throw new IllegalStateException("Cannot convert state " + status + " to one of states of PBoardStatus");
		}
	}
}

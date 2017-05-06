package org.soft.assignment1.lagom.board.impl;

import org.soft.assignment1.lagom.board.api.Board;

public final class Mappers {

	public static Board toApi(PBoard board) {
        return new Board(
        		board.getId(),
        		board.getTitle(), 
        		board.getStatus().toBoardStatus());
    }

	public static PBoard fromApi(Board data) {
		return new PBoard(
				data.getId(),
				data.getTitle());
	}
}

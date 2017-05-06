package org.soft.assignment1.lagom.board.impl;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.Jsonable;

@SuppressWarnings("serial")
@Immutable
@JsonDeserialize
public final class PBoardState implements Jsonable {

	private final Optional<PBoard> board;

	@JsonCreator
	public PBoardState(Optional<PBoard> board) {
		this.board = board;
	}

	public static PBoardState empty() {
        return new PBoardState(Optional.empty());
    }

    public static PBoardState create(PBoard board) {
        return new PBoardState(Optional.of(board));
    }

    public PBoardState updateTitle(UUID id, String title) {
        return update(i -> i.updateTitle(id, title));
    }

    public PBoardState updateStatus(UUID id, PBoardStatus status) {
        return update(i -> i.updateStatus(id, status));
    }
    
    public Optional<PBoard> getBoard() {
		return board;
	}

	public PBoardStatus getStatus() {
        return board.map(PBoard::getStatus).orElse(PBoardStatus.NOT_CREATED);
    }

    private PBoardState update(Function<PBoard, PBoard> updateFunction) {
        assert board.isPresent();
        return new PBoardState(board.map(updateFunction));
    }
}

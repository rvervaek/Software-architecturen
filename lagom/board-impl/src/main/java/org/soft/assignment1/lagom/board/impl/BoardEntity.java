package org.soft.assignment1.lagom.board.impl;

import java.util.Optional;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

public class BoardEntity extends PersistentEntity<BoardCommand, BoardEvent, BoardState> {

	@Override
	public PersistentEntity<BoardCommand, BoardEvent, BoardState>.Behavior initialBehavior(Optional<BoardState> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

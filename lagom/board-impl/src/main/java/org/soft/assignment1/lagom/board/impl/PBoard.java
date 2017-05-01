package org.soft.assignment1.lagom.board.impl;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;

@Immutable
@JsonSerialize
public class PBoard {

	private final String title;
	private final PBoardStatus status;	

	@JsonCreator
	public PBoard(String title) {
		this(title, PBoardStatus.CREATED);
	}
	
	@JsonCreator
	public PBoard(String title, PBoardStatus status) {
		this.title = Preconditions.checkNotNull(title);
		this.status = Preconditions.checkNotNull(status);
	}

	public PBoard updateTitle(String title) {
		assert status == PBoardStatus.CREATED;
		return new PBoard(title, status);
	}

	public PBoard updateStatus(PBoardStatus status) {
		return new PBoard(title, status);
	}

	public String getTitle() {
		return title;
	}
	
	public PBoardStatus getStatus() {
		return status;
	}
}

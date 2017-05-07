package org.soft.assignment1.lagom.board.impl;

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Preconditions;

@Immutable
public class PBoard {

	private final UUID id;
	private final String title;
	private final PBoardStatus status;	

	public PBoard(UUID id, String title) {
		this(id, title, PBoardStatus.CREATED);
	}
	
	public PBoard(UUID id, String title, PBoardStatus status) {
		this.id = Preconditions.checkNotNull(id);
		this.title = Preconditions.checkNotNull(title);
		this.status = Preconditions.checkNotNull(status);
	}

	public PBoard updateTitle(UUID id, String title) {
		assert status == PBoardStatus.CREATED;
		return new PBoard(id, title, status);
	}

	public PBoard updateStatus(UUID id, PBoardStatus status) {
		return new PBoard(id, title, status);
	}

	public UUID getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}
	
	public PBoardStatus getStatus() {
		return status;
	}
}

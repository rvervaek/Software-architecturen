package org.soft.assignment1.lagom.board.api;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;

@Immutable
@JsonSerialize
public class Board {

	public final String title;
	public final BoardStatus status;	
	
	@JsonCreator
	public Board(String title, String status) {
		this(title, BoardStatus.get(status));
	}
	
	@JsonCreator
	public Board(String title, BoardStatus status) {
		this.title = Preconditions.checkNotNull(title);
		this.status = Preconditions.checkNotNull(status);
	}

}

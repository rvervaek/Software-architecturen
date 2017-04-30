package org.soft.assignment1.board_api.request;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize
public class Board {

	public final String title;
	public final BoardStatus status;
	
	@JsonCreator
	public Board(String title, BoardStatus status) {
		this.title = title;
		this.status = status;
	}
}

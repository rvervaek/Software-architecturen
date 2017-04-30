package org.soft.assignment1.lagom.board.api;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Immutable
@JsonDeserialize
public enum BoardStatus {

	CREATED,
	ARCHIVED;

}

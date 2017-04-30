package org.soft.assignment1.board_api.request;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Immutable
@JsonDeserialize
public enum BoardStatus {

	CREATED,
	ARCHIVED;

}

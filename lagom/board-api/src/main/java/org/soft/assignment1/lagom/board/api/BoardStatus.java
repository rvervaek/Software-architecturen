package org.soft.assignment1.lagom.board.api;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize
@JsonDeserialize
public enum BoardStatus {

	CREATED,
	ARCHIVED;

	@JsonCreator
	public static BoardStatus get(String status) {
		for (BoardStatus s : BoardStatus.values()) {
			if (s.name().equals(status)) {
				return s;
			}
		}
		return null;
	}
}

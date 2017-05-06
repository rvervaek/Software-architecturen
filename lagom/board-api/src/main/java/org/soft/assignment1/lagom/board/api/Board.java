package org.soft.assignment1.lagom.board.api;

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;

@Immutable
@JsonSerialize
@JsonDeserialize
public class Board {

	private final UUID id;
	private final String title;
	private final BoardStatus status;	
	
	@JsonCreator
	public Board(UUID id, String title, String status) {
		this(id, title, BoardStatus.get(status));
	}
	
	@JsonCreator
	public Board(UUID id, String title, BoardStatus status) {
		this.id = Preconditions.checkNotNull(id);
		this.title = Preconditions.checkNotNull(title);
		this.status = Preconditions.checkNotNull(status);
	}

	public UUID getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public BoardStatus getStatus() {
		return status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Board [title=" + title + "]";
	}
}

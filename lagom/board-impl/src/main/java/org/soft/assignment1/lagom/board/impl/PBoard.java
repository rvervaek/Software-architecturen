package org.soft.assignment1.lagom.board.impl;

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize
@JsonDeserialize
public class PBoard {

	private final UUID id;
	private final String title;
	private final PBoardStatus status;	

	public PBoard(UUID id, String title) {
		this(id, title, PBoardStatus.CREATED);
	}
	
	@JsonCreator
	public PBoard(@JsonProperty("id") UUID id, @JsonProperty("title") String title, @JsonProperty("status") PBoardStatus status) {
		this.id = id;
		this.title = title;
		this.status = status;
		System.out.println("Created pboard: " + toString());
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
		PBoard other = (PBoard) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PBoard [id=" + id + ", title=" + title + ", status=" + status + "]";
	}
}

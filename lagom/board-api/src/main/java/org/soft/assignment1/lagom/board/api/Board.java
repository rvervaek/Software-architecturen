package org.soft.assignment1.lagom.board.api;

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize
@JsonDeserialize
public class Board {

	private final UUID id;
	private final String title;
	private final BoardStatus status;	
	
	public Board(UUID id, String title, String status) {
		this(id, title, BoardStatus.get(status));
	}
	
	/**
	 * Constructor for changing board status.
	 * @param id
	 * @param status
	 */
	@JsonCreator
	public Board(@JsonProperty("id") UUID id, @JsonProperty("title") String title, @JsonProperty("status") BoardStatus status) {
		this.id = id;
		this.title = title;
		this.status = status;
		System.out.println("Created board: " + toString());
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
		return "Board [id=" + id + ", title=" + title + ", status=" + status + "]";
	}
}

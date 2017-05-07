package org.soft.assignment1.lagom.task.api;

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize
@JsonDeserialize
public class Task {

	public final UUID id;
	public final String title;
	public final String details;
	public final TaskColor color;
	public final TaskStatus status;
	public final UUID boardId;
	
	@JsonCreator
	public Task(UUID id, String title, String details, TaskColor color, TaskStatus status, UUID boardId) {
		this.id = id;
		this.title = title;
		this.details = details;
		this.color = color;
		this.status = status;
		this.boardId = boardId;
	}
	
	@Immutable
	@JsonSerialize
	@JsonDeserialize
	public static final class TaskColor {
		
		private final int red;
		private final int green;
		private final int blue;
		
		@JsonCreator
		public TaskColor(int red, int green, int blue) {
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		public int getRed() {
			return red;
		}

		public int getGreen() {
			return green;
		}

		public int getBlue() {
			return blue;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + blue;
			result = prime * result + green;
			result = prime * result + red;
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
			TaskColor other = (TaskColor) obj;
			if (blue != other.blue)
				return false;
			if (green != other.green)
				return false;
			if (red != other.red)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "TaskColor [red=" + red + ", green=" + green + ", blue=" + blue + "]";
		}
	}

	public UUID getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDetails() {
		return details;
	}

	public TaskColor getColor() {
		return color;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public UUID getBoardId() {
		return boardId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boardId == null) ? 0 : boardId.hashCode());
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
		Task other = (Task) obj;
		if (boardId == null) {
			if (other.boardId != null)
				return false;
		} else if (!boardId.equals(other.boardId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Task [title=" + title + "]";
	}
}

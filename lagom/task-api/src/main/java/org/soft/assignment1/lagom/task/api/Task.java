package org.soft.assignment1.lagom.task.api;

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize
public class Task {

	public final UUID id;
	public final String title;
	public final String details;
	public final TaskColor color;
	public final TaskStatus status;
	public final String boardId;
	
	@JsonCreator
	public Task(UUID id, String title, String details, TaskColor color, TaskStatus status, String boardId) {
		this.id = id;
		this.title = title;
		this.details = details;
		this.color = color;
		this.status = status;
		this.boardId = boardId;
	}
	
	@Immutable
	@JsonSerialize
	public static final class TaskColor {
		
		public final int red;
		public final int green;
		public final int blue;
		
		@JsonCreator
		public TaskColor(int red, int green, int blue) {
			this.red = red;
			this.green = green;
			this.blue = blue;
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
}

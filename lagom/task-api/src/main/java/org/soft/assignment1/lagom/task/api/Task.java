package org.soft.assignment1.lagom.task.api;

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize
public class Task {

	private final UUID id;
	private final String title;
	private final String details;
	private final TaskColor color;
	private final TaskStatus status;
	private final String boardId;
	
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

	public String getBoardId() {
		return boardId;
	}
}

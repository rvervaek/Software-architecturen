package org.soft.assignment1.lagom.task.impl;

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;

@Immutable
public class PTask {

	private final UUID id;
	private final String title;
	private final String details;
	private final PTaskColor color;
	private final PTaskStatus status;
	private final UUID boardId;
	
	@Immutable
	public static final class PTaskColor {
		
		private final int red;
		private final int green;
		private final int blue;
		
		@JsonCreator
		public PTaskColor(int red, int green, int blue) {
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

	public PTask(UUID id, String title, String details, PTaskColor color, UUID boardId) {
		this.id = id;
		this.title = title;
		this.details = details;
		this.color = color;
		this.status = PTaskStatus.BACKLOG;
		this.boardId = boardId;
	}
	
	public PTask(UUID id, String title, String details, PTaskColor color, PTaskStatus status, UUID boardId) {
		this.id = id;
		this.title = title;
		this.details = details;
		this.color = color;
		this.status = status;
		this.boardId = boardId;
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

	public PTaskColor getColor() {
		return color;
	}

	public PTaskStatus getStatus() {
		return status;
	}

	public UUID getBoardId() {
		return boardId;
	}
	
	/**
	 * Update the task with the parameters, if applicable.
	 * @param title
	 * @param details
	 * @param color
	 * @return
	 */
	public PTask update(String title, String details, PTaskColor color) {
		return new PTask(
				id, 
				StringUtils.isEmpty(title) ? this.title : title, 
				StringUtils.isEmpty(details) ? this.details : details, 
				color == null ? this.color : color, 
				status, 
				boardId);
	}

	/**
	 * Update the task with a new status.
	 * @param status
	 * @return
	 */
	public PTask updateStatus(PTaskStatus status) {
		return new PTask(
				id, 
				title, 
				details, 
				color, 
				status, 
				boardId);
	}
}

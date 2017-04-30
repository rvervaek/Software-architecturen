package org.soft.assignment1.lagom.task.api;

import java.awt.Color;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize
public class Task {

	public final String title;
	public final String details;
	public final Color color;
	public final TaskStatus status;
	public final Long boardId;
	
	@JsonCreator
	public Task(String title, String details, Color color, TaskStatus status, Long boardId) {
		super();
		this.title = title;
		this.details = details;
		this.color = color;
		this.status = status;
		this.boardId = boardId;
	}
}

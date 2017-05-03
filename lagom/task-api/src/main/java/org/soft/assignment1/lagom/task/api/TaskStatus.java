package org.soft.assignment1.lagom.task.api;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Immutable
@JsonDeserialize
public enum TaskStatus {

	BACKLOG,
	SCHEDULED,
	STARTED,
	COMPLETED,
	ARCHIVED;
	
	public static TaskStatus get(String status) {
		for (TaskStatus s : TaskStatus.values()) {
			if (s.name().equals(status)) {
				return s;
			}
		}
		return null;
	}
}

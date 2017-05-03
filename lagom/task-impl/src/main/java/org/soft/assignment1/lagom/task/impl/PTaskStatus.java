package org.soft.assignment1.lagom.task.impl;

import javax.annotation.concurrent.Immutable;

import org.soft.assignment1.lagom.task.api.TaskStatus;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Immutable
@JsonDeserialize
public enum PTaskStatus {

	NOT_CREATED {
		@Override
		TaskStatus toTaskStatus() {
			throw new IllegalStateException("Publicly exposed task can't be not created");
		}
	},
	BACKLOG {
		@Override
		TaskStatus toTaskStatus() {
			return TaskStatus.BACKLOG;
		}
	},
	SCHEDULED {
		@Override
		TaskStatus toTaskStatus() {
			return TaskStatus.SCHEDULED;
		}
	},
	STARTED {
		@Override
		TaskStatus toTaskStatus() {
			return TaskStatus.STARTED;
		}
	},
	COMPLETED {
		@Override
		TaskStatus toTaskStatus() {
			return TaskStatus.COMPLETED;
		}
	},
	ARCHIVED {
		@Override
		TaskStatus toTaskStatus() {
			return TaskStatus.ARCHIVED;
		}
	};
	
	abstract TaskStatus toTaskStatus();
	
	public static PTaskStatus get(TaskStatus status) {
		switch(status) {
		case BACKLOG: return PTaskStatus.BACKLOG;
		case SCHEDULED: return PTaskStatus.SCHEDULED;
		case STARTED: return PTaskStatus.STARTED;
		case COMPLETED: return PTaskStatus.COMPLETED;
		case ARCHIVED: return PTaskStatus.ARCHIVED;
		default:
			throw new IllegalStateException("Cannot convert state " + status + " to one of states of PBoardStatus");
		}
	}
}

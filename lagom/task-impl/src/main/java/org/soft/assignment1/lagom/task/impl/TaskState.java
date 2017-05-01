package org.soft.assignment1.lagom.task.impl;

import java.util.Optional;

import javax.annotation.concurrent.Immutable;

import org.soft.assignment1.lagom.task.api.Task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.Jsonable;

@SuppressWarnings("serial")
@Immutable
@JsonDeserialize
public final class TaskState implements Jsonable {

	public final Optional<Task> task;
	
	@JsonCreator
	public TaskState(Optional<Task> task) {
		this.task = task;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((task == null) ? 0 : task.hashCode());
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
		TaskState other = (TaskState) obj;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaskState [task=" + task + "]";
	}
}

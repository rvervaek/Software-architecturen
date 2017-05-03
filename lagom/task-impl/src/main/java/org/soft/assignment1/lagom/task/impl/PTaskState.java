package org.soft.assignment1.lagom.task.impl;

import java.util.Optional;
import java.util.function.Function;

import javax.annotation.concurrent.Immutable;

import org.soft.assignment1.lagom.task.impl.PTask.PTaskColor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.Jsonable;

@SuppressWarnings("serial")
@Immutable
@JsonDeserialize
public final class PTaskState implements Jsonable {

	private final Optional<PTask> task;
	
	@JsonCreator
	public PTaskState(Optional<PTask> task) {
		this.task = task;
	}
	
	public static PTaskState empty() {
        return new PTaskState(Optional.empty());
    }

	public static PTaskState create(PTask task) {
		return new PTaskState(Optional.of(task));
	}
	
	public PTaskState update(String title, String details, PTaskColor color) {
		return update(i -> i.update(title, details, color));
	}
	
	public PTaskState updateStatus(PTaskStatus status) {
		return update(i -> i.updateStatus(status));
	}
	
	public PTaskStatus getStatus() {
        return task.map(PTask::getStatus).orElse(PTaskStatus.NOT_CREATED);
    }
	
	public Optional<PTask> getTask() {
		return task;
	}

	private PTaskState update(Function<PTask, PTask> updateFunction) {
        assert task.isPresent();
        return new PTaskState(task.map(updateFunction));
    }
}

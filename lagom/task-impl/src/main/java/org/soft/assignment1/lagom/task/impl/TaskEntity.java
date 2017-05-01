package org.soft.assignment1.lagom.task.impl;

import java.util.Optional;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

public class TaskEntity extends PersistentEntity<TaskCommand, TaskEvent, TaskState> {

	@Override
	public PersistentEntity<TaskCommand, TaskEvent, TaskState>.Behavior initialBehavior(Optional<TaskState> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

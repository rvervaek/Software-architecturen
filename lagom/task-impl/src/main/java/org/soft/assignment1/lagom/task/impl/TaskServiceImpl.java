package org.soft.assignment1.lagom.task.impl;

import org.pcollections.PSequence;
import org.soft.assignment1.lagom.task.api.Task;
import org.soft.assignment1.lagom.task.api.TaskService;
import org.soft.assignment1.lagom.task.api.TaskStatus;

import com.lightbend.lagom.javadsl.api.ServiceCall;

import akka.NotUsed;

public class TaskServiceImpl implements TaskService {

	@Override
	public ServiceCall<Task, NotUsed> create(Long boardId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceCall<NotUsed, Task> get(String title, Long boardId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceCall<Task, NotUsed> update(String title, Long boardId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceCall<TaskStatus, NotUsed> updateStatus(String title, Long boardId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceCall<NotUsed, PSequence<Task>> getByBoardId(Long boardId) {
		// TODO Auto-generated method stub
		return null;
	}

}

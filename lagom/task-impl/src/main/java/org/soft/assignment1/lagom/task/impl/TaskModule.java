package org.soft.assignment1.lagom.task.impl;

import org.soft.assignment1.lagom.board.api.BoardService;
import org.soft.assignment1.lagom.task.api.TaskService;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

public class TaskModule extends AbstractModule implements ServiceGuiceSupport {

	@Override
	protected void configure() {
		bindServices(serviceBinding(TaskService.class, TaskServiceImpl.class));
		bindClient(BoardService.class);
	}

}

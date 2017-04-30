package org.soft.assignment1.lagom.task.api;

import org.pcollections.PSequence;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import akka.NotUsed;

public interface TaskService extends Service {

	public static final String SERVICE_NAME = "taskservice";
	public static final String SERVICE_URI = "/api/task/";
	
	/**
	 * Create a new task.
	 * @return
	 */
	ServiceCall<Task, NotUsed> create(Long boardId);
	
	ServiceCall<NotUsed, Task> get(String title, Long boardId);
	
	ServiceCall<Task, NotUsed> update(String title, Long boardId);
	
	ServiceCall<TaskStatus, NotUsed> updateStatus(String title, Long boardId);
	
	ServiceCall<NotUsed, PSequence<Task>> getByBoardId(Long boardId);
	
	/*
	 * (non-Javadoc)
	 * @see com.lightbend.lagom.javadsl.api.Service#descriptor()
	 */
	@Override
	default Descriptor descriptor() {
		return Service.named(SERVICE_NAME)
				.withCalls(
						Service.restCall(Method.POST, SERVICE_URI + "board/:boardId", this::create),
						Service.restCall(Method.GET, SERVICE_URI + ":title/board/:boardId", this::get),
						Service.restCall(Method.PUT, SERVICE_URI + ":title/board/:boardId", this::update),
						Service.restCall(Method.PUT, SERVICE_URI + ":title/board/:boardId", this::updateStatus),
						Service.restCall(Method.GET, SERVICE_URI + "board/:boardId", this::getByBoardId))
				.withAutoAcl(true);
	}
}

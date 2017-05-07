package org.soft.assignment1.lagom.task.api;

import java.util.UUID;

import org.pcollections.PSequence;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.deser.PathParamSerializers;
import com.lightbend.lagom.javadsl.api.transport.Method;

import akka.NotUsed;

public interface TaskService extends Service {

	public static final String SERVICE_NAME = "taskservice";
	public static final String SERVICE_URI = "/api/task";
	
	/**
	 * Create a new task.
	 * @return
	 */
	ServiceCall<Task, NotUsed> create();
	
	/**
	 * Get the task with id.
	 * @param id
	 * @return
	 */
	ServiceCall<NotUsed, Task> get(UUID id);
	
	/**
	 * Update the task with id.
	 * @param id
	 * @return
	 */
	ServiceCall<Task, NotUsed> update(UUID id);
	
	/**
	 * Update the status of task with id.
	 * @param id
	 * @return
	 */
	ServiceCall<TaskStatus, NotUsed> updateStatus(UUID id);
	
	/**
	 * Get all tasks on the board with id.
	 * @param boardId
	 * @return
	 */
	ServiceCall<NotUsed, PSequence<Task>> getByBoardId(UUID boardId);
	
	@Override
	default Descriptor descriptor() {
		return Service.named(SERVICE_NAME)
				.withCalls(
						Service.restCall(Method.POST, SERVICE_URI, this::create),
						Service.restCall(Method.GET, SERVICE_URI + "/:id", this::get),
						Service.restCall(Method.PUT, SERVICE_URI + "/:id", this::update),
						Service.restCall(Method.PUT, SERVICE_URI + "/:id/status", this::updateStatus),
						Service.restCall(Method.GET, SERVICE_URI + "/board/:boardId", this::getByBoardId))
				.withAutoAcl(true)
				.withPathParamSerializer(UUID.class, PathParamSerializers.UUID)
				.withPathParamSerializer(TaskStatus.class, PathParamSerializers.required("status", TaskStatus::get, TaskStatus::name));
	}
}

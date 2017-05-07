package org.soft.assignment1.lagom.board.api;

import java.util.UUID;

import org.pcollections.PSequence;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.deser.PathParamSerializers;
import com.lightbend.lagom.javadsl.api.transport.Method;

import akka.NotUsed;

public interface BoardService extends Service {
	
	public static final String SERVICE_NAME = "boardservice";
	public static final String SERVICE_URI = "/api/board";
	
	/**
	 * Create a new board.
	 * @return
	 */
	ServiceCall<Board, NotUsed> create();
	
	/**
	 * Update the title of a board.
	 * @param title The title of the board to update
	 * @return
	 */
	ServiceCall<Board, NotUsed> update(UUID id);
	
	/**
	 * Update the status of a board.
	 * @param title The title of the board to update
	 * @returns
	 */
	ServiceCall<BoardStatus, NotUsed> updateStatus(UUID id);
	
	/**
	 * Get a board by its title.
	 * @param title
	 * @return
	 */
	ServiceCall<NotUsed, Board> get(UUID id);
	
	/**
	 * Get all boards.
	 * @return
	 */
	ServiceCall<NotUsed, PSequence<Board>> getAll();

	@Override
	default Descriptor descriptor() {
		return Service.named(SERVICE_NAME)
				.withCalls(
						Service.restCall(Method.POST, SERVICE_URI, this::create),
						Service.restCall(Method.PUT, SERVICE_URI + "/:id", this::update),
						Service.restCall(Method.PUT, SERVICE_URI + "/:id/status", this::updateStatus),
						Service.restCall(Method.GET, SERVICE_URI + "/:id", this::get),
						Service.restCall(Method.GET, SERVICE_URI, this::getAll))
				.withAutoAcl(true)
				.withPathParamSerializer(UUID.class, PathParamSerializers.UUID)
				.withPathParamSerializer(BoardStatus.class, PathParamSerializers.required("status", BoardStatus::get, BoardStatus::name));
	}
}

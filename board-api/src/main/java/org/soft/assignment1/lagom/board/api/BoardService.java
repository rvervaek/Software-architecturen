package org.soft.assignment1.lagom.board.api;

import org.pcollections.PSequence;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import akka.NotUsed;

public interface BoardService extends Service {

	public static final String SERVICE_NAME = "boardservice";
	public static final String SERVICE_URI = "/api/board/";
	
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
	ServiceCall<Board, NotUsed> update(String title);
	
	/**
	 * Update the status of a board.
	 * @param title The title of the board to update
	 * @returns
	 */
	ServiceCall<BoardStatus, NotUsed> updateStatus(String title);
	
	/**
	 * Get all boards.
	 * @return
	 */
	ServiceCall<NotUsed, PSequence<Board>> getAll();

	/*
	 * (non-Javadoc)
	 * @see com.lightbend.lagom.javadsl.api.Service#descriptor()
	 */
	@Override
	default Descriptor descriptor() {
		return Service.named(SERVICE_NAME)
				.withCalls(
						Service.restCall(Method.POST, SERVICE_URI, this::create),
						Service.restCall(Method.PUT, SERVICE_URI + ":title", this::update),
						Service.restCall(Method.PUT, SERVICE_URI + ":title/status", this::updateStatus),
						Service.restCall(Method.GET, SERVICE_URI, this::getAll))
				.withAutoAcl(true);
	}
}

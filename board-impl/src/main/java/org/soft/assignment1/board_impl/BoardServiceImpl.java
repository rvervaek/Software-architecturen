package org.soft.assignment1.board_impl;

import org.pcollections.PSequence;
import org.soft.assignment1.board_api.BoardService;
import org.soft.assignment1.board_api.request.Board;
import org.soft.assignment1.board_api.request.BoardStatus;

import com.lightbend.lagom.javadsl.api.ServiceCall;

import akka.NotUsed;

public class BoardServiceImpl implements BoardService {

	
	
	@Override
	public ServiceCall<Board, NotUsed> create() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceCall<Board, NotUsed> update(String title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceCall<BoardStatus, NotUsed> updateStatus(String title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceCall<NotUsed, PSequence<Board>> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}

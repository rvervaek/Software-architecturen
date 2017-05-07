package org.soft.assignment1.lagom.board.impl;


import org.soft.assignment1.lagom.board.api.BoardService;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

public class BoardModule extends AbstractModule implements ServiceGuiceSupport {
	
	@Override
	protected void configure() {
		bindServices(serviceBinding(BoardService.class, BoardServiceImpl.class));
	}
}

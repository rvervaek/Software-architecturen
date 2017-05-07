package org.soft.assignment1.lagom.board.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.soft.assignment1.lagom.board.api.Board;
import org.soft.assignment1.lagom.board.api.BoardService;
import org.soft.assignment1.lagom.board.api.BoardStatus;

import com.datastax.driver.core.utils.UUIDs;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;

import akka.NotUsed;

public class BoardServiceImpl implements BoardService {

	private final PersistentEntityRegistry persistentEntityRegistry;
	private final CassandraSession database;
	
	@Inject
	public BoardServiceImpl(PersistentEntityRegistry persistentEntityRegistry, CassandraSession database, ReadSide readSide) {
		this.persistentEntityRegistry = persistentEntityRegistry;
		this.database = database;
		persistentEntityRegistry.register(PBoardEntity.class);
		readSide.register(PBoardEventProcessor.class);	
	}

	@Override
	public ServiceCall<Board, NotUsed> create() {
		return board -> {	
			return persistentEntityRegistry
					.refFor(PBoardEntity.class, UUIDs.timeBased().toString())
					.ask(new PBoardCommand.Create(Mappers.fromApi(board)))
					.thenApply(ack -> NotUsed.getInstance());
		};
	}

	@Override
	public ServiceCall<Board, NotUsed> update(UUID id) {
		return board -> {
			return persistentEntityRegistry
					.refFor(PBoardEntity.class, id.toString())
					.ask(new PBoardCommand.Update(id, board.getTitle()))
					.thenApply(ack -> NotUsed.getInstance());
		};
	}

	@Override
	public ServiceCall<BoardStatus, NotUsed> updateStatus(UUID id) {
		return status -> {
			return persistentEntityRegistry.refFor(PBoardEntity.class, id.toString())
					.ask(new PBoardCommand.UpdateStatus(id, PBoardStatus.get(status)))
					.thenApply(ack -> NotUsed.getInstance());
		};
	}

	@Override
	public ServiceCall<NotUsed, Board> get(UUID id) {
		return request -> {
			return persistentEntityRegistry.refFor(PBoardEntity.class, id.toString())
					.ask(new PBoardCommand.Get(id))
					.thenApply(resp -> resp);
		};
	}

	@Override
	public ServiceCall<NotUsed, PSequence<Board>> getAll() {
		return request -> {
			CompletionStage<PSequence<Board>> result = database.selectAll("SELECT boardId, title, status FROM board").thenApply(rows -> {
				List<Board> boards = rows
						.stream()
						.map(row -> new Board(row.getUUID("boardId"), row.getString("title"), row.getString("status")))
						// I could have filtered this in the query, but I'm trying to use as much lambda functions as I can to learn.
						.filter(board -> board.getStatus() != BoardStatus.ARCHIVED)
						.collect(Collectors.toList());
				return TreePVector.from(boards);
			});
			return result;
		};
	}
}

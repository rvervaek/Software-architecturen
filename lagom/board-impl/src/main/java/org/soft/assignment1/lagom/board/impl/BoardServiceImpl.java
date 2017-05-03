package org.soft.assignment1.lagom.board.impl;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.soft.assignment1.lagom.board.api.Board;
import org.soft.assignment1.lagom.board.api.BoardService;
import org.soft.assignment1.lagom.board.api.BoardStatus;

import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;

import akka.NotUsed;

public class BoardServiceImpl implements BoardService {

	private final PersistentEntityRegistry persistentEntityRegistry;
	private final CassandraSession database;

	@Inject
	public BoardServiceImpl(PersistentEntityRegistry persistentEntityRegistry, CassandraSession database) {
		this.persistentEntityRegistry = persistentEntityRegistry;
		this.database = database;
		persistentEntityRegistry.register(PBoardEntity.class);
	}

	@Override
	public ServiceCall<Board, NotUsed> create() {
		return board -> {
			return persistentEntityRegistry
					.refFor(PBoardEntity.class, board.getTitle())
					.ask(new PBoardCommand.Create(Mappers.fromApi(board)))
					.thenApply(ack -> NotUsed.getInstance());
		};
	}

	@Override
	public ServiceCall<Board, NotUsed> update(String title) {
		return board -> {
			return persistentEntityRegistry
					.refFor(PBoardEntity.class, board.getTitle())
					.ask(new PBoardCommand.Update(board.getTitle()))
					.thenApply(ack -> NotUsed.getInstance());
		};
	}

	@Override
	public ServiceCall<BoardStatus, NotUsed> updateStatus(String title) {
		return status -> {
			return persistentEntityRegistry.refFor(PBoardEntity.class, title)
					.ask(new PBoardCommand.UpdateStatus(PBoardStatus.get(status)))
					.thenApply(ack -> NotUsed.getInstance());
		};
	}

	@Override
	public ServiceCall<NotUsed, Board> get(String title) {
		return request -> {
			CompletionStage<Board> result = database.selectOne("SELECT * FROM board where title = ?", title)
					.thenApply(row -> {
						if (row.isPresent()) {
							return new Board(row.get().getString("title"), row.get().getString("status"));
						} else {
							throw new NotFound("Board with title " + title + " not found");
						}
					});
			return result;
		};
	}

	@Override
	public ServiceCall<NotUsed, PSequence<Board>> getAll() {
		return request -> {
			CompletionStage<PSequence<Board>> result = database.selectAll("SELECT * FROM board").thenApply(rows -> {
				List<Board> boards = rows
						.stream()
						.map(row -> new Board(row.getString("title"), row.getString("status")))
						// I could have filtered this in the query, but I'm trying to use as much lambda functions as I can to learn.
						.filter(board -> board.getStatus() != BoardStatus.ARCHIVED)
						.collect(Collectors.toList());
				return TreePVector.from(boards);
			});
			return result;
		};
	}
}

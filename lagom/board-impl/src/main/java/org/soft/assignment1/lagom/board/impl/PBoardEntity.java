package org.soft.assignment1.lagom.board.impl;

import java.util.Optional;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import akka.Done;

public class PBoardEntity extends PersistentEntity<PBoardCommand, PBoardEvent, PBoardState> {

	@Override
	public PersistentEntity<PBoardCommand, PBoardEvent, PBoardState>.Behavior initialBehavior(Optional<PBoardState> snapshotState) {

		PBoardStatus status = snapshotState.map(PBoardState::getStatus).orElse(PBoardStatus.NOT_CREATED);
        switch (status) {
            case NOT_CREATED:
                return empty();
            case CREATED:
                return created(snapshotState.get());
            case ARCHIVED:
                return archived(snapshotState.get());
            default:
                throw new IllegalStateException("Unknown status: " + status);
        }
	}

	private PersistentEntity<PBoardCommand, PBoardEvent, PBoardState>.Behavior empty() {

		BehaviorBuilder builder = newBehaviorBuilder(PBoardState.empty());

		/* CREATE */
		builder.setCommandHandler(PBoardCommand.Create.class, (cmd, ctx) -> ctx
				.thenPersist(new PBoardEvent.Created(cmd.getBoard()), evt -> ctx.reply(Done.getInstance())));
		// Creating a new board changes the current behavior
		builder.setEventHandlerChangingBehavior(PBoardEvent.Created.class,
				evt -> created(PBoardState.create(evt.getBoard())));

		/* UPDATE */
		builder.setReadOnlyCommandHandler(PBoardCommand.Update.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Update not valid in state " + state().getStatus())));

		/* UPDATESTATUS */
		builder.setReadOnlyCommandHandler(PBoardCommand.UpdateStatus.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Update status not valid in state " + state().getStatus())));

		return builder.build();
	}

	private PersistentEntity<PBoardCommand, PBoardEvent, PBoardState>.Behavior created(PBoardState state) {
		
		BehaviorBuilder builder = newBehaviorBuilder(state);
		
		/* CREATE */
		builder.setReadOnlyCommandHandler(PBoardCommand.Create.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Create not valid in state " + state().getStatus())));

		/* UPDATE */
		builder.setCommandHandler(PBoardCommand.Update.class, (cmd, ctx) -> {
			PBoard board = state().getBoard().get();
			// Don't emit event when not necessary
			if (board.getTitle().equals(cmd.getTitle())) {
				return ctx.done();
			}
			return ctx.thenPersist(new PBoardEvent.Updated(cmd.getTitle()), reply -> ctx.reply(Done.getInstance()));
		});
		builder.setEventHandler(PBoardEvent.Updated.class, evt -> state().updateTitle(evt.getTitle()));

		/* UPDATESTATUS */
		builder.setCommandHandler(PBoardCommand.UpdateStatus.class, (cmd, ctx) -> {
			PBoardStatus status = state().getStatus();
			// Don't emit event when not necessary
			if (status == cmd.getStatus()) {
				return ctx.done();
			}
			// Emit the appropriate event for the new status
			switch (cmd.getStatus()) {
			case ARCHIVED:
				return ctx.thenPersist(new PBoardEvent.Archived(cmd.getStatus()),
						evt -> state().updateStatus(evt.getStatus()));
			default:
				ctx.commandFailed(new InvalidCommandException("Unexpected update status " + cmd.getStatus()));
				return ctx.done();
			}
		});
		// Updating the board status to ARCHIVED changes the current behavior
		builder.setEventHandlerChangingBehavior(PBoardEvent.Archived.class,
				evt -> archived(state().updateStatus(evt.getStatus())));

        return builder.build();
	}

	private PersistentEntity<PBoardCommand, PBoardEvent, PBoardState>.Behavior archived(PBoardState state) {
		
		BehaviorBuilder builder = newBehaviorBuilder(state);

		/* CREATE */
		builder.setReadOnlyCommandHandler(PBoardCommand.Create.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Create not valid in state " + state().getStatus())));
        
		/* UPDATE */
		builder.setReadOnlyCommandHandler(PBoardCommand.Update.class,
				(cmd, ctx) -> ctx.invalidCommand("Can't update board if it is already in state " + state().getStatus()));
		
		/* UPDATESTATUS */
		builder.setCommandHandler(PBoardCommand.UpdateStatus.class, (cmd, ctx) -> {
			PBoardStatus status = state().getStatus();
			// Don't emit event when not necessary
			if (status == cmd.getStatus()) {
				return ctx.done();
			}
			// Emit the appropriate event for the new status
			switch (cmd.getStatus()) {
			case CREATED:
				return ctx.thenPersist(new PBoardEvent.Created(cmd.getStatus()),
						evt -> state().updateStatus(evt.getStatus()));
			default:
				ctx.commandFailed(new InvalidCommandException("Unexpected update status " + cmd.getStatus()));
				return ctx.done();
			}
		});

        // Ignore these commands, they may come due to at least once messaging
        builder.setReadOnlyCommandHandler(UpdatePrice.class, this::alreadyDone);
        builder.setReadOnlyCommandHandler(FinishAuction.class, this::alreadyDone);

        return builder.build();
	}



	
//	BehaviorBuilder b = newBehaviorBuilder(snapshotState.orElse(new PBoardState(Optional.empty())));
//	
//	/*
//	 * Define command handlers.
//	 */
//	
//	b.setCommandHandler(PBoardCommand.Create.class, (cmd, ctx) -> {
//		// If the board is already defined
//		if (state().board.isPresent()) {
//			ctx.invalidCommand("Board " + entityId() + " is already created");
//			return ctx.done();
//		}
//		
//		// Persist the new board by creating a BoardCreated event
//		return ctx.thenPersist(new PBoardEvent.BoardCreated(cmd.board.title), evt -> ctx.reply(Done.getInstance()));
//	});
//	
//	b.setCommandHandler(PBoardCommand.Update.class, (cmd, ctx) -> {
//		// If the board was not found
//		if (!state().board.isPresent()) {
//			ctx.invalidCommand("Board " + entityId() + " does not exists");
//			return ctx.done();
//		} 
//		
//		PBoard board = state().board.get();
//		
//		// If the status of the board is ARCHIVED, don't allow the update
////		if (board.status == PBoardStatus.ARCHIVED) {
////			ctx.invalidCommand("Board " + entityId() + " is already archived, can't change the board");
////			return ctx.done();
////		}
//		
//		// Don't update the board if the new title is the same as the current title
////		if (board.title.equals(cmd.title)) {
////			return ctx.done();
////		}
//		
//		// Persist the update of the board by creating a BoardUpdated event
//		return ctx.thenPersist(new PBoardEvent.BoardUpdated(cmd.title), evt -> ctx.reply(Done.getInstance()));
//	});
//	
//	b.setCommandHandler(PBoardCommand.UpdateStatus.class, (cmd, ctx) -> {
//		// If the board was not found
//		if (!state().board.isPresent()) {
//			ctx.invalidCommand("Board " + entityId() + " does not exists");
//			return ctx.done();
//		}
//		
//		// Persist the update of the board status by creating a BoardStatusUpdated event
//		return ctx.thenPersist(new PBoardEvent.BoardStatusUpdated(cmd.status), evt -> ctx.reply(Done.getInstance()));
//	});
//	
//	/*
//	 * Define event handlers.
//	 */
//	
//	b.setEventHandler(PBoardEvent.BoardCreated.class,
//			evt -> new PBoardState(Optional.of(new PBoard(evt.title, PBoardStatus.CREATED))));
//	
//	b.setEventHandler(PBoardEvent.BoardUpdated.class,
//			evt -> new PBoardState(Optional.of(new PBoard(evt.title, state().board.get().status))));
//
//	b.setEventHandler(PBoardEvent.BoardStatusUpdated.class,
//			evt -> new PBoardState(Optional.of(new PBoard(state().board.get().title, evt.status))));
//	
//	
//
//	return b.build();
}
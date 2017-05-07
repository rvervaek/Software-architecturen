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

	protected PersistentEntity<PBoardCommand, PBoardEvent, PBoardState>.Behavior empty() {

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

	protected PersistentEntity<PBoardCommand, PBoardEvent, PBoardState>.Behavior created(PBoardState state) {
		
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
			return ctx.thenPersist(new PBoardEvent.Updated(cmd.getId(), cmd.getTitle()), reply -> ctx.reply(Done.getInstance()));
		});
		builder.setEventHandler(PBoardEvent.Updated.class, evt -> state().updateTitle(evt.getId(), evt.getTitle()));

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
				return ctx.thenPersist(new PBoardEvent.Archived(cmd.getId(), cmd.getStatus()),
						evt -> state().updateStatus(evt.getId(), evt.getStatus()));
			default:
				ctx.commandFailed(new InvalidCommandException("Unexpected update status " + cmd.getStatus()));
				return ctx.done();
			}
		});
		// Updating the board status to ARCHIVED changes the current behavior
		builder.setEventHandlerChangingBehavior(PBoardEvent.Archived.class,
				evt -> archived(state().updateStatus(evt.getId(), evt.getStatus())));

        return builder.build();
	}

	protected PersistentEntity<PBoardCommand, PBoardEvent, PBoardState>.Behavior archived(PBoardState state) {
		
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
				/*
				 * Since the requirements don't clearly say whether or not it is
				 * allowed for a board to become 'unarchived' (active) again, we allow
				 * this functionality via changing the board status back to
				 * CREATED and emitting an ACTIVATED event containing the new
				 * status.
				 */
				return ctx.thenPersist(new PBoardEvent.Activated(cmd.getId(), cmd.getStatus()),
						evt -> state().updateStatus(evt.getId(), evt.getStatus()));
			default:
				ctx.commandFailed(new InvalidCommandException("Unexpected update status " + cmd.getStatus()));
				return ctx.done();
			}
		});
		/*
		 * This event handler brings the current behavior back to the 'create'
		 * behavior, since the behavior is the same when a board becomes
		 * 'active' again after being archived.
		 */
		builder.setEventHandlerChangingBehavior(PBoardEvent.Activated.class,
				evt -> created(state().updateStatus(evt.getId(), evt.getStatus())));

        return builder.build();
	}
}

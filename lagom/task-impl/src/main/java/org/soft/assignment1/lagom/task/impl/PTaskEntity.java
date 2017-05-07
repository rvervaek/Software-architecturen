package org.soft.assignment1.lagom.task.impl;

import java.util.Optional;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import akka.Done;

public class PTaskEntity extends PersistentEntity<PTaskCommand, PTaskEvent, PTaskState> {

	@Override
	public PersistentEntity<PTaskCommand, PTaskEvent, PTaskState>.Behavior initialBehavior(Optional<PTaskState> snapshotState) {
		PTaskStatus status = snapshotState.map(PTaskState::getStatus).orElse(PTaskStatus.NOT_CREATED);
        switch (status) {
            case NOT_CREATED:
                return empty();
            case BACKLOG:
                return backlog(snapshotState.get());
            case SCHEDULED:
            	return scheduled(snapshotState.get());
            case STARTED: 
            	return started(snapshotState.get());
            case COMPLETED:
            	return completed(snapshotState.get());
            case ARCHIVED:
                return archived(snapshotState.get());
            default:
                throw new IllegalStateException("Unknown status: " + status);
        }
	}

	private PersistentEntity<PTaskCommand, PTaskEvent, PTaskState>.Behavior empty() {
		
		BehaviorBuilder builder = newBehaviorBuilder(PTaskState.empty());
		
		/* CREATE */
		builder.setCommandHandler(PTaskCommand.Create.class, (cmd, ctx) -> {
			return ctx.thenPersist(new PTaskEvent.Created(cmd.getTask()), evt -> ctx.reply(Done.getInstance()));
		});
		builder.setEventHandlerChangingBehavior(PTaskEvent.Created.class,
				evt -> backlog(PTaskState.create(evt.getTask())));

		/* GET */
		builder.setReadOnlyCommandHandler(PTaskCommand.Get.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Get not valid in state " + state().getStatus())));
		
		/* UPDATE */
		builder.setReadOnlyCommandHandler(PTaskCommand.Update.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Update not valid in state " + state().getStatus())));

		/* UPDATESTATUS */
		builder.setReadOnlyCommandHandler(PTaskCommand.UpdateStatus.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Update status not valid in state " + state().getStatus())));

		return builder.build();
	}

	private PersistentEntity<PTaskCommand, PTaskEvent, PTaskState>.Behavior backlog(PTaskState state) {
		
		BehaviorBuilder builder = newBehaviorBuilder(state);
		
		/* CREATE */
		builder.setReadOnlyCommandHandler(PTaskCommand.Create.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Create not valid in state " + state().getStatus())));
		
		/* GET */
		builder.setReadOnlyCommandHandler(PTaskCommand.Get.class,
				(cmd, ctx) -> ctx.reply(Mappers.toApi(state().getTask().get())));
		
		/* UPDATE */
		builder.setCommandHandler(PTaskCommand.Update.class, (cmd, ctx) -> {
			PTask task = state().getTask().get();
			// Don't emit event when not necessary
			if (task.getTitle().equals(cmd.getTitle()) 
					&& task.getDetails().equals(cmd.getDetails()) 
					&& task.getColor().equals(cmd.getColor())) {
				return ctx.done();
			}
			return ctx.thenPersist(new PTaskEvent.Updated(cmd.getId(), cmd.getTitle(), cmd.getDetails(), cmd.getColor()),
					evt -> ctx.reply(Done.getInstance()));
		});
		builder.setEventHandler(PTaskEvent.Updated.class,
				evt -> state().update(evt.getTitle(), evt.getDetails(), evt.getColor()));

		/* UPDATESTATUS */
		builder.setCommandHandler(PTaskCommand.UpdateStatus.class, (cmd, ctx) -> {
			PTaskStatus status = state().getStatus();
			// Don't emit event when not necessary
			if (status == cmd.getStatus()) {
				return ctx.done();
			}
			// Emit the appropriate event for the new status
			switch (cmd.getStatus()) {
			case SCHEDULED:
				return ctx.thenPersist(new PTaskEvent.Scheduled(cmd.getId(), cmd.getStatus()),
						evt -> state().updateStatus(evt.getStatus()));
			case STARTED:
				return ctx.thenPersist(new PTaskEvent.Started(cmd.getId(), cmd.getStatus()),
						evt -> state().updateStatus(cmd.getStatus()));
			case COMPLETED:
				return ctx.thenPersist(new PTaskEvent.Completed(cmd.getId(), cmd.getStatus()),
						evt -> state().updateStatus(cmd.getStatus()));
			default:
				ctx.commandFailed(new InvalidCommandException("Unexpected update status " + cmd.getStatus()));
				return ctx.done();
			}
		});
		builder.setEventHandlerChangingBehavior(PTaskEvent.Scheduled.class,
				evt -> scheduled(state().updateStatus(evt.getStatus())));
		builder.setEventHandlerChangingBehavior(PTaskEvent.Started.class,
				evt -> started(state().updateStatus(evt.getStatus())));
		builder.setEventHandlerChangingBehavior(PTaskEvent.Completed.class,
				evt -> completed(state().updateStatus(evt.getStatus())));

		return builder.build();
	}

	private PersistentEntity<PTaskCommand, PTaskEvent, PTaskState>.Behavior scheduled(PTaskState state) {
		
		BehaviorBuilder builder = newBehaviorBuilder(state);
		
		/* CREATE */
		builder.setReadOnlyCommandHandler(PTaskCommand.Create.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Create not valid in state " + state().getStatus())));
		
		/* GET */
		builder.setReadOnlyCommandHandler(PTaskCommand.Get.class,
				(cmd, ctx) -> ctx.reply(Mappers.toApi(state().getTask().get())));
		
		/* UPDATE */
		builder.setCommandHandler(PTaskCommand.Update.class, (cmd, ctx) -> {
			PTask task = state().getTask().get();
			// Don't emit event when not necessary
			if (task.getTitle().equals(cmd.getTitle()) 
					&& task.getDetails().equals(cmd.getDetails()) 
					&& task.getColor().equals(cmd.getColor())) {
				return ctx.done();
			}
			return ctx.thenPersist(new PTaskEvent.Updated(cmd.getId(), cmd.getTitle(), cmd.getDetails(), cmd.getColor()),
					evt -> ctx.reply(Done.getInstance()));
		});
		builder.setEventHandler(PTaskEvent.Updated.class,
				evt -> state().update(evt.getTitle(), evt.getDetails(), evt.getColor()));

		/* UPDATESTATUS */
		builder.setCommandHandler(PTaskCommand.UpdateStatus.class, (cmd, ctx) -> {
			PTaskStatus status = state().getStatus();
			// Don't emit event when not necessary
			if (status == cmd.getStatus()) {
				return ctx.done();
			}
			// Emit the appropriate event for the new status
			switch (cmd.getStatus()) {
			case BACKLOG:
				// We allow to change the state to BACKLOG when it was SCHEDULED
				return ctx.thenPersist(new PTaskEvent.Backlogged(cmd.getId(), cmd.getStatus()),
						evt -> state().updateStatus(evt.getStatus()));
			case STARTED:
				return ctx.thenPersist(new PTaskEvent.Started(cmd.getId(), cmd.getStatus()),
						evt -> state().updateStatus(cmd.getStatus()));
			case COMPLETED:
				return ctx.thenPersist(new PTaskEvent.Completed(cmd.getId(), cmd.getStatus()),
						evt -> state().updateStatus(cmd.getStatus()));
			default:
				ctx.commandFailed(new InvalidCommandException("Unexpected update status " + cmd.getStatus()));
				return ctx.done();
			}
		});
		builder.setEventHandlerChangingBehavior(PTaskEvent.Backlogged.class,
				evt -> backlog(state().updateStatus(evt.getStatus())));
		builder.setEventHandlerChangingBehavior(PTaskEvent.Started.class,
				evt -> started(state().updateStatus(evt.getStatus())));
		builder.setEventHandlerChangingBehavior(PTaskEvent.Completed.class,
				evt -> completed(state().updateStatus(evt.getStatus())));
		
		return builder.build();
	}

	private PersistentEntity<PTaskCommand, PTaskEvent, PTaskState>.Behavior started(PTaskState state) {
		
		BehaviorBuilder builder = newBehaviorBuilder(state);
		
		/* CREATE */
		builder.setReadOnlyCommandHandler(PTaskCommand.Create.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Create not valid in state " + state().getStatus())));
		
		/* GET */
		builder.setReadOnlyCommandHandler(PTaskCommand.Get.class,
				(cmd, ctx) -> ctx.reply(Mappers.toApi(state().getTask().get())));
		
		/* UPDATE */
		builder.setCommandHandler(PTaskCommand.Update.class, (cmd, ctx) -> {
			PTask task = state().getTask().get();
			// Don't emit event when not necessary
			if (task.getTitle().equals(cmd.getTitle()) 
					&& task.getDetails().equals(cmd.getDetails()) 
					&& task.getColor().equals(cmd.getColor())) {
				return ctx.done();
			}
			return ctx.thenPersist(new PTaskEvent.Updated(cmd.getId(), cmd.getTitle(), cmd.getDetails(), cmd.getColor()),
					evt -> ctx.reply(Done.getInstance()));
		});
		builder.setEventHandler(PTaskEvent.Updated.class,
				evt -> state().update(evt.getTitle(), evt.getDetails(), evt.getColor()));

		/* UPDATESTATUS */
		builder.setCommandHandler(PTaskCommand.UpdateStatus.class, (cmd, ctx) -> {
			PTaskStatus status = state().getStatus();
			// Don't emit event when not necessary
			if (status == cmd.getStatus()) {
				return ctx.done();
			}
			// Emit the appropriate event for the new status
			switch (cmd.getStatus()) {
			case BACKLOG:
				// We allow to change the state to BACKLOG when it was STARTED
				return ctx.thenPersist(new PTaskEvent.Backlogged(cmd.getId(), cmd.getStatus()),
						evt -> state().updateStatus(evt.getStatus()));
			case SCHEDULED:
				// We allow to change the state to SCHEDULED when it was STARTED
				return ctx.thenPersist(new PTaskEvent.Scheduled(cmd.getId(), cmd.getStatus()),
						evt -> state().updateStatus(cmd.getStatus()));
			case COMPLETED:
				return ctx.thenPersist(new PTaskEvent.Completed(cmd.getId(), cmd.getStatus()),
						evt -> state().updateStatus(cmd.getStatus()));
			default:
				ctx.commandFailed(new InvalidCommandException("Unexpected update status " + cmd.getStatus()));
				return ctx.done();
			}
		});
		builder.setEventHandlerChangingBehavior(PTaskEvent.Backlogged.class,
				evt -> backlog(state().updateStatus(evt.getStatus())));
		builder.setEventHandlerChangingBehavior(PTaskEvent.Scheduled.class,
				evt -> scheduled(state().updateStatus(evt.getStatus())));
		builder.setEventHandlerChangingBehavior(PTaskEvent.Completed.class,
				evt -> completed(state().updateStatus(evt.getStatus())));
		
		return builder.build();
	}

	private PersistentEntity<PTaskCommand, PTaskEvent, PTaskState>.Behavior completed(PTaskState state) {
		
		BehaviorBuilder builder = newBehaviorBuilder(state);
		
		/* CREATE */
		builder.setReadOnlyCommandHandler(PTaskCommand.Create.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Create not valid in state " + state().getStatus())));
		
		/* GET */
		builder.setReadOnlyCommandHandler(PTaskCommand.Get.class,
				(cmd, ctx) -> ctx.reply(Mappers.toApi(state().getTask().get())));
		
		/* UPDATE */
		builder.setCommandHandler(PTaskCommand.Update.class, (cmd, ctx) -> {
			PTask task = state().getTask().get();
			// Don't emit event when not necessary
			if (task.getTitle().equals(cmd.getTitle()) 
					&& task.getDetails().equals(cmd.getDetails()) 
					&& task.getColor().equals(cmd.getColor())) {
				return ctx.done();
			}
			return ctx.thenPersist(new PTaskEvent.Updated(cmd.getId(), cmd.getTitle(), cmd.getDetails(), cmd.getColor()),
					evt -> ctx.reply(Done.getInstance()));
		});
		builder.setEventHandler(PTaskEvent.Updated.class,
				evt -> state().update(evt.getTitle(), evt.getDetails(), evt.getColor()));

		/* UPDATESTATUS */
		builder.setCommandHandler(PTaskCommand.UpdateStatus.class, (cmd, ctx) -> {
			PTaskStatus status = state().getStatus();
			// Don't emit event when not necessary
			if (status == cmd.getStatus()) {
				return ctx.done();
			}
			// Emit the appropriate event for the new status
			switch (cmd.getStatus()) {
			case BACKLOG:
			case SCHEDULED:
			case STARTED:
				ctx.invalidCommand("A task can't be set to a previous status when it already complete");
				return ctx.done();
			case ARCHIVED:
				return ctx.thenPersist(new PTaskEvent.Archived(cmd.getId(), cmd.getStatus()),
						evt -> state().updateStatus(cmd.getStatus()));
			default:
				ctx.commandFailed(new InvalidCommandException("Unexpected update status " + cmd.getStatus()));
				return ctx.done();
			}
		});
		builder.setEventHandlerChangingBehavior(PTaskEvent.Archived.class,
				evt -> archived(state().updateStatus(evt.getStatus())));
		
		return builder.build();
	}

	private PersistentEntity<PTaskCommand, PTaskEvent, PTaskState>.Behavior archived(PTaskState state) {
		
		BehaviorBuilder builder = newBehaviorBuilder(state);
		
		/* CREATE */
		builder.setReadOnlyCommandHandler(PTaskCommand.Create.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Create not valid in state " + state().getStatus())));
		
		/* GET */
		builder.setReadOnlyCommandHandler(PTaskCommand.Get.class,
				(cmd, ctx) -> ctx.reply(Mappers.toApi(state().getTask().get())));
		
		/* UPDATE */
		builder.setReadOnlyCommandHandler(PTaskCommand.Update.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Update not valid in state " + state().getStatus())));

		/* UPDATESTATUS */
		builder.setReadOnlyCommandHandler(PTaskCommand.UpdateStatus.class, (cmd, ctx) -> ctx
				.commandFailed(new InvalidCommandException("Update status not valid in state " + state().getStatus())));

		return builder.build();
	}

}

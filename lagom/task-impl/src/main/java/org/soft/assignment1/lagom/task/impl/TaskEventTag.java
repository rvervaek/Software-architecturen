package org.soft.assignment1.lagom.task.impl;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class TaskEventTag {

	public static final AggregateEventTag<PTaskEvent> INSTANCE = AggregateEventTag.of(PTaskEvent.class);

}

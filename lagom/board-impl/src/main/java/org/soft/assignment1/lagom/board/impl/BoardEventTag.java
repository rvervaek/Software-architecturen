/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package org.soft.assignment1.lagom.board.impl;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class BoardEventTag {

	public static final AggregateEventTag<PBoardEvent> INSTANCE = AggregateEventTag.of(PBoardEvent.class);

}

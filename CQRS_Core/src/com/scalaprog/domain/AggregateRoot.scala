package com.scalaprog.domain;

import com.scalaprog.events.AbstractEvent
import java.util.UUID
import com.scalaprog.engine.Server
;

abstract class AggregateRoot(val id: UUID) {

  // The current version of this domain root.
  // This is used to ensure concurrent modifications
  var version = 0


  var unCommitedEvent = List[AbstractEvent]()

	def applyEvent(cmd: AbstractEvent , storeEvent: Boolean)

  def loadFromHistory(events: List[AbstractEvent]) = {
		for ( abstractEvent <- events) {
      println("Applying event "+abstractEvent)
			applyEvent(abstractEvent, false)
		}
    this
	}
}

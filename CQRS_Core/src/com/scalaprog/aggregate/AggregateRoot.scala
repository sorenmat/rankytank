package com.scalaprog.aggregate;

import com.scalaprog.events.AbstractEvent
import java.util.UUID
import com.scalaprog.engine.Server
;

abstract class AggregateRoot(id: UUID) {

	def applyEvent(cmd: AbstractEvent , storeEvent: Boolean)
	
	def loadFromHistory() = {
    val events = Server.eventStore.getEvents(id).reverse
    println("-----------------------------")
		for ( abstractEvent <- events) {
      println("Applying event "+abstractEvent)
			applyEvent(abstractEvent, false)
		}
    this
	}
}

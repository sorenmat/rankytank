package com.scalaprog.engine;

import com.scalaprog.events.AbstractEvent
import com.scalaprog.projections.engine.EventListener
;

object ProjectionEngine {

	var listeners = List[EventListener]()

	def registerListener( listener: EventListener) {
		listeners = listener :: listeners
	}

	def deRegisterListener(listener: EventListener) {
    listeners = listeners.filterNot(f => f==listener)
	}

	def publishEvent(event: AbstractEvent) {
		for (listener <- listeners) {
			listener.eventPublished(event);
		}
	}
}

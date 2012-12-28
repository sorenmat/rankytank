package com.scalaprog.projections.engine;

import com.scalaprog.events.AbstractEvent;

abstract class EventListener {

	def publish(event: AbstractEvent )
}

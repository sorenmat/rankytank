package com.scalaprog.projections.engine;

import com.scalaprog.events.AbstractEvent
import com.scalaprog.engine.Server

abstract class EventListener {
    def eventPublished(event: AbstractEvent )
}

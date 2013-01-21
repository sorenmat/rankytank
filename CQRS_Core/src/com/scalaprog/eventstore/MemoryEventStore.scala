package com.scalaprog.eventstore


import com.scalaprog.events.AbstractEvent
import java.lang.String
import java.util.UUID
import com.scalaprog.engine.ProjectionEngine


object MemoryEventStore extends EventStore {

  var store = List[(UUID, String, AbstractEvent)]()

  def save(event: AbstractEvent, id: UUID) {
    store = List((id, event.getClass().getName(), event)) ::: store
    ProjectionEngine.publishEvent(event)
  }

  def getEvents(aggregateId: UUID): List[AbstractEvent] = {
    store.filter(p => p._1 == aggregateId).map(eventAndObject => {
      val eventClassStr = eventAndObject._2
      val event = eventAndObject._3
      event
    })
  }


  def getEventLog: List[AbstractEvent] = {
    var events = List[AbstractEvent]()
    store.map(v => {

      val eventClassStr = v._2
      val event = v._3
      event
    })

  }

  def clear {
    store = List[(UUID, String, AbstractEvent)]()
  }

  def save(events: List[AbstractEvent], id: UUID) {
    for(event <- events)
      save(event, id)
  }
}


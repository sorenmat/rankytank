package com.scalaprog.eventstore


import com.scalaprog.events.AbstractEvent
import java.lang.String
import com.google.gson.Gson
import java.util.UUID
import com.scalaprog.engine.ProjectionEngine


object MemoryEventStore extends EventStore {

  val gson = new Gson()
  var store = List[(UUID,String, String)]()

  def save(event: AbstractEvent, id: UUID) {
    store = List ( (id, event.getClass().getName(), gson.toJson(event).toString()) ) ::: store
    ProjectionEngine.publishEvent(event)
  }

  def getEvents(aggregateId: UUID): List[AbstractEvent] = {
    store.filter(p => p._1 == aggregateId).map(eventAndObject => {
        val eventClassStr = eventAndObject._2
        val event = eventAndObject._3

        val eventObj = gson.fromJson(event, Class.forName(eventClassStr)).asInstanceOf[AbstractEvent]
        eventObj
      })
  }


  def getEventLog: List[AbstractEvent] = {
    var events = List[AbstractEvent]()
    store.map(v => {

        val eventClassStr = v._2
        val event = v._3

        val eventObj = gson.fromJson(event, Class.forName(eventClassStr)).asInstanceOf[AbstractEvent]
        eventObj
      })

  }
}


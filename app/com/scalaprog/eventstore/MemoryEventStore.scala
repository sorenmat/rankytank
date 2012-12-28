package com.scalaprog.eventstore

import eventstore.EventStore
import play.libs.Json
import projections.engine.ProjectionEngine

import com.mongodb.BasicDBObject
import com.mongodb.Mongo
import com.scalaprog.events.AbstractEvent
import java.lang.{Exception, String}
import com.google.gson.Gson
import java.util.UUID


object MemoryEventStore extends EventStore {

  var store = List[(UUID,String, String)]()

  def save(event: AbstractEvent, id: UUID) {
    store = List ( (id, event.getClass().getName(), new Gson().toJson(event).toString()) ) ::: store
    ProjectionEngine.publishEvent(event)
  }

  def getEvents(aggregateId: UUID): List[AbstractEvent] = {
    store.filter(p => p._1 == aggregateId).map(eventAndObject => {
        val eventClassStr = eventAndObject._2
        val event = eventAndObject._3

        val eventObj = new Gson().fromJson(event, Class.forName(eventClassStr)).asInstanceOf[AbstractEvent]
        eventObj
      })
  }


  def getEventLog: List[AbstractEvent] = {
    var events = List[AbstractEvent]()
    store.map(v => {

        val eventClassStr = v._2
        val event = v._3

        val eventObj = new Gson().fromJson(event, Class.forName(eventClassStr)).asInstanceOf[AbstractEvent]
        eventObj
      })

  }
}


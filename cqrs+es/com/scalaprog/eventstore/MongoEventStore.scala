package com.scalaprog.eventstore

import play.libs.Json
import projections.engine.ProjectionEngine

import com.mongodb.BasicDBObject
import com.mongodb.Mongo
import com.scalaprog.events.AbstractEvent
import java.lang.{Exception, String}
import com.google.gson.Gson
import java.util.UUID


object MongoEventStore {

  val m = new Mongo()


  val db = m.getDB("eventStore")
  val coll = db.getCollection("events")

  def save(event: AbstractEvent, id: UUID) {
    val doc = new BasicDBObject()
    doc.put("id", id)
    doc.put("eventClass", event.getClass().getName())
    doc.put("event", new Gson().toJson(event).toString())
    coll.insert(doc)

    ProjectionEngine.publishEvent(event)
  }

  def getEvents(aggregateId: UUID): List[AbstractEvent] = {
    var events = List[AbstractEvent]()
    try {
      val db = m.getDB("eventStore")
      val coll = db.getCollection("events")

      val query = new BasicDBObject()
      System.out.println("id:" + aggregateId)
      query.put("id", aggregateId)

      val cursor = coll.find(query)

      try {
        while (cursor.hasNext()) {
          val next = cursor.next()
          val eventClassStr = next.get("eventClass").toString()
          val event = next.get("event").toString()

          //val eventObj =  Json.fromJson(Json.parse(event), Class.forName(eventClassStr)).asInstanceOf[AbstractEvent]

          val eventObj = new Gson().fromJson(event, Class.forName(eventClassStr)).asInstanceOf[AbstractEvent]
          events = eventObj :: events
        }
      } finally {
        cursor.close()
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
    return events
  }


  def getEventLog: List[AbstractEvent] = {
    var events = List[AbstractEvent]()
    try {
      val db = m.getDB("eventStore")
      val coll = db.getCollection("events")

      val query = new BasicDBObject()

      val cursor = coll.find(query)

      try {
        while (cursor.hasNext()) {
          val next = cursor.next()
          val eventClassStr = next.get("eventClass").toString()
          val event = next.get("event").toString()

          //val eventObj =  Json.fromJson(Json.parse(event), Class.forName(eventClassStr)).asInstanceOf[AbstractEvent]

          val eventObj = new Gson().fromJson(event, Class.forName(eventClassStr)).asInstanceOf[AbstractEvent]
          events = eventObj :: events
        }
      } finally {
        cursor.close()
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
    return events
  }
}


package com.scalaprog.eventstore


import com.mongodb.{MongoURI, MongoOptions, BasicDBObject, Mongo}
import com.scalaprog.events.AbstractEvent
import java.lang.Exception
import com.google.gson.Gson
import java.util.UUID
import com.scalaprog.engine.ProjectionEngine
import com.codahale.jerkson.Json
import java.net.URI


object MongoEventStore extends EventStore{


  //val mongoURI = new URI(System.getenv("MONGOHQ_URL"));
  val mongoURI = new MongoURI(System.getenv("MONGOHQ_URL"));

  val gson = new Gson()

  val db = mongoURI.connectDB()

  //val db = m.getDB("eventStore")
  val coll = db.getCollection("events")

  def save(event: AbstractEvent, id: UUID) {
    val doc = new BasicDBObject()
    doc.put("id", id)
    doc.put("eventClass", event.getClass().getName())
    doc.put("event", Json.generate(event))
    coll.insert(doc)

    ProjectionEngine.publishEvent(event)
  }

  def getEvents(aggregateId: UUID): List[AbstractEvent] = {
    var events = List[AbstractEvent]()
    try {
      val coll = db.getCollection("events")

      val query = new BasicDBObject()
      query.put("id", aggregateId)

      val cursor = coll.find(query)

      try {
        while (cursor.hasNext()) {
          val next = cursor.next()
          val eventClassStr = next.get("eventClass").toString()
          val event = next.get("event").toString()
          val eventObj = gson.fromJson(event, Class.forName(eventClassStr)).asInstanceOf[AbstractEvent]
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
      val coll = db.getCollection("events")

      val query = new BasicDBObject()

      val cursor = coll.find(query)

      try {
        while (cursor.hasNext()) {
          val next = cursor.next()
          val eventClassStr = next.get("eventClass").toString()
          val event = next.get("event").toString()

          val eventObj = gson.fromJson(event, Class.forName(eventClassStr)).asInstanceOf[AbstractEvent]
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


package eventstore


import play.libs.Json
import projections.engine.ProjectionEngine

import com.mongodb.BasicDBObject
import com.mongodb.Mongo
import com.scalaprog.events.AbstractEvent
import java.lang.{Exception, String}
import com.google.gson.Gson
import java.util.UUID


trait EventStore {

  def save(event: AbstractEvent, id: UUID)

  def getEvents(aggregateId: UUID): List[AbstractEvent]

  def getEventLog: List[AbstractEvent]

  case class StorageUnit(id: String, event: AbstractEvent)

}
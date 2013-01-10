package com.scalaprog.eventstore


import com.scalaprog.events.AbstractEvent
import java.lang.String
import java.util.UUID


trait EventStore {

  def save(event: AbstractEvent, id: UUID)

  def getEvents(aggregateId: UUID): List[AbstractEvent]

  def getEventLog: List[AbstractEvent]

  def clear

  case class StorageUnit(id: String, event: AbstractEvent)

}
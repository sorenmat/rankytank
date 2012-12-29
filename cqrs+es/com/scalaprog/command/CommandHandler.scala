package com.scalaprog.command

import aggregate.AggregateRoot
import java.util.UUID
import com.scalaprog.engine.Server

/**
 * User: soren
 */
trait CommandHandler {

  def handle(cmd: AbstractCommand)

  def getAggreateById[T <: AggregateRoot](id: UUID, c: Class[T]) = {
    val obj = c.getConstructor(classOf[UUID]).newInstance(id)
    for(event <- Server.eventStore.getEvents(id))
      obj.applyEvent(event, false)
    obj
  }
}

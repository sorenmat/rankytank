package com.scalaprog.command

import java.util.UUID
import com.scalaprog.engine.Server
import com.scalaprog.domain.{IRepository, AggregateRoot}

/**
 * User: soren
 */
abstract class CommandHandler[A <: AggregateRoot](repository: IRepository[A]) {

  def handle(cmd: AbstractCommand)

  def getAggreateById[T <: AggregateRoot](id: UUID, c: Class[T]) = {
    val obj = c.getConstructor(classOf[UUID]).newInstance(id)
    for(event <- Server.eventStore.getEvents(id)) {
      obj.applyEvent(event, false)
    }
    obj
  }
}

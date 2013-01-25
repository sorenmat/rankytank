package com.scalaprog.engine

import com.scalaprog.command.{AbstractCommand, CommandHandler}
import com.scalaprog.eventstore.{MongoEventStore, EventStore, MemoryEventStore}
import com.scalaprog.domain.AggregateRoot

/**
 * User: soren
 */
object Server {

  var eventStore:EventStore = MongoEventStore//MemoryEventStore

  var handlers = List[CommandHandler[_ <: AggregateRoot]]()

  def register(handler: CommandHandler[_ <: AggregateRoot]) {
    handlers = handler :: handlers
  }

  def execute(cmd: AbstractCommand) {
    for (handler <- handlers)
      handler.handle(cmd)
  }
}


package com.scalaprog.engine

import com.scalaprog.command.{AbstractCommand, CommandHandler}
import com.scalaprog.eventstore.{MongoEventStore, EventStore, MemoryEventStore}

/**
 * User: soren
 */
object Server {

  var eventStore:EventStore = MongoEventStore//MemoryEventStore

  var handlers = List[CommandHandler]()

  def register(handler: CommandHandler) {
    handlers = handler :: handlers
  }

  def execute(cmd: AbstractCommand) {
    for (handler <- handlers)
      handler.handle(cmd)
  }
}


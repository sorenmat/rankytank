package com.scalaprog.rankytank.test

import com.scalaprog.engine.Server
import com.scalaprog.eventstore.MemoryEventStore
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * User: soren
 */
class BasicEventTest extends FunSuite with BeforeAndAfter {

  Server.eventStore = MemoryEventStore

  before {
    println("*********** RUNNING BEFORE **********************")
    Server.eventStore.clear
    Server.handlers = Nil
    println("Size of eventlog: "+Server.eventStore.getEventLog)
  }

}

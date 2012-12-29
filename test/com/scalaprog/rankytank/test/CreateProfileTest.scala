package com.scalaprog.rankytank.test

import aggregates.ProfileAggregate
import com.scalaprog.engine.Server
import commands.CreateUserProfile
import eventstore.EventStore
import java.util.UUID
import org.scalatest.FunSuite

class CreateProfileTest extends FunSuite {

  test("command test") {
    val agg = new ProfileAggregate()
    agg.createProfile(new CreateUserProfile(UUID.randomUUID(), "Soren", "1234", "soren@test.com"))

    val events = Server.eventStore.getEvents(agg.id)
    assert(events.size === 1)
    events.foreach(f => println(f))
  }
}

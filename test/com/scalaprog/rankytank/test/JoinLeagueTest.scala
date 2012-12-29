package com.scalaprog.rankytank.test

import aggregates.{LeagueAggregate, ProfileAggregate}
import commandhandlers.{ProfileHandler, LeaugeHandler}
import commands.{JoinLeague, CreateLeague, CreateUserProfile}
import eventstore.EventStore
import java.util.UUID
import org.scalatest.FunSuite
import com.scalaprog.engine.Server

class JoinLeagueTest extends FunSuite {

  test("command test") {
    Server.register(new LeaugeHandler())
    Server.register(new ProfileHandler())

    val userId: UUID = UUID.randomUUID()
    Server.execute(new CreateUserProfile(userId, "Soren", "1234", "soren@test.com"))

    val leagueName: String = "Test League"
    val leagueId = UUID.randomUUID()
    val createLeagueCmd = CreateLeague(leagueId, leagueName, "password")
    Server.execute(createLeagueCmd)
    //leagueAgg.createLeague(createLeagueCmd)

    // Asserts
    assert(Server.eventStore.getEvents(userId).size === 1)
    assert(Server.eventStore.getEvents(leagueId).size === 1)


    Server.execute(JoinLeague(leagueId, userId))
    assert(Server.eventStore.getEvents(leagueId).size === 2)
    try {
      Server.execute(JoinLeague(leagueId, userId))
      fail("Should give a error saying that a user can only be added once")
    } catch {
      case e: RuntimeException => // ignore expected error
      case _ => fail("Should give a error saying that a user can only be added once")
    }

    println("EventLog")
    println(Server.eventStore.getEventLog.mkString("\n\t"))
  }
}

package com.scalaprog.rankytank.test

import commandhandlers.{ProfileHandler, LeagueHandler}
import commands.{JoinLeague, CreateLeague, CreateUserProfile}
import java.util.UUID
import org.scalatest.FunSuite
import com.scalaprog.engine.Server

class JoinLeagueTest extends BasicEventTest {

  test("command test") {
    Server.register(new LeagueHandler())
    Server.register(new ProfileHandler())

    val userId: UUID = UUID.randomUUID()
    Server.execute(new CreateUserProfile(userId, "Soren"+userId.toString, "1234", "soren@test.com"))

    val leagueName: String = "Test League"
    val leagueId = UUID.randomUUID()
    val createLeagueCmd = CreateLeague(leagueId, leagueName, "password")
    Server.execute(createLeagueCmd)
    //leagueAgg.createLeague(createLeagueCmd)

    // Asserts
    assert(Server.eventStore.getEvents(UUID.fromString("da1b24e5-294b-4919-b733-1d31af371951")).size === 1) // hardcoded Profile aggregate id, fix this..
    assert(Server.eventStore.getEvents(leagueId).size === 1)


    Server.execute(JoinLeague(leagueId, userId))
    assert(Server.eventStore.getEvents(leagueId).size === 2)
    try {
      Server.execute(JoinLeague(leagueId, userId))
      fail("Should give a error saying that a user can only be added once")
    } catch {
      case e: java.lang.RuntimeException => println("this should happen.........................")// ignore expected error
      case _ => fail("Should give a error saying that a user can only be added once")
    }

    println("EventLog")
    println(Server.eventStore.getEventLog.mkString("\n\t"))
  }
}

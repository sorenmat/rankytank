package com.scalaprog.rankytank.test.projection

import commandhandlers.{ProfileHandler, LeagueHandler}
import commands.{RegisterMatchScore, JoinLeague, CreateLeague, CreateUserProfile}
import java.util.UUID
import org.scalatest.FunSuite
import com.scalaprog.engine.{ProjectionEngine, Server}
import readside.projection.MatchInfoProjection

class MatchInfoProjectionTest extends FunSuite {

  test("command test") {
    Server.register(new LeagueHandler())
    Server.register(new ProfileHandler())
    ProjectionEngine.registerListener(MatchInfoProjection)

    val userId: UUID = UUID.randomUUID()
    Server.execute(new CreateUserProfile(userId, "Soren", "1234", "soren@test.com"))

    val allanuserId: UUID = UUID.randomUUID()
    Server.execute(new CreateUserProfile(allanuserId, "Allan", "1234", "allan@test.com"))

    val jonasuserId: UUID = UUID.randomUUID()
    Server.execute(new CreateUserProfile(jonasuserId, "Jonas", "1234", "jonas@test.com"))

    val peteruserId: UUID = UUID.randomUUID()
    Server.execute(new CreateUserProfile(peteruserId, "Peter", "1234", "peter@test.com"))


    val leagueName: String = "Test League"
    val leagueId = UUID.randomUUID()
    Server.execute(CreateLeague(leagueId, leagueName, "password"))
    //leagueAgg.createLeague(createLeagueCmd)

    // Asserts
    assert(Server.eventStore.getEventLog.size === 5)


    println("********************")
    Server.execute(JoinLeague(leagueId, userId))
    Server.execute(JoinLeague(leagueId, allanuserId))
    Server.execute(JoinLeague(leagueId, jonasuserId))
    Server.execute(JoinLeague(leagueId, peteruserId))
    //assert(Server.eventStore.getEventLog.size === 6)

    val teamOne = (allanuserId, userId)
    val teamTwo = (jonasuserId, peteruserId)
    Server.execute(RegisterMatchScore(leagueId, teamOne, teamTwo, 10, 5))
    Server.execute(RegisterMatchScore(leagueId, teamOne, teamTwo, 10, 9))
    //Server.execute(RegisterMatchScore(leagueId, teamOne, teamTwo, 9, 10))


    println("EventLog")
    println("\t" + Server.eventStore.getEventLog.mkString("\n\t"))
    println("Scores")
    println("---------------")
    println(MatchInfoProjection.scores.mkString("\n"))
    println("---------------")
    println(MatchInfoProjection.getScores.mkString("\n"))
  }
}

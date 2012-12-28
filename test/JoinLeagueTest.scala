import aggregates.{LeagueAggregate, ProfileAggregate}
import commandhandlers.LeaugeHandler
import commands.{JoinLeague, CreateLeague, CreateUserProfile}
import eventstore.EventStore
import java.util.UUID
import org.scalatest.FunSuite
import com.scalaprog.engine.Server

class JoinLeagueTest extends FunSuite {

  test("command test") {
    Server.register(new LeaugeHandler())
    val agg = new ProfileAggregate()
    val userId: UUID = UUID.randomUUID()
    agg.createProfile(new CreateUserProfile(userId, "Soren", "1234", "soren@test.com"))

    val leagueName: String = "Test League"
    val leagueId = UUID.randomUUID()
    val createLeagueCmd = CreateLeague(leagueId, leagueName, "password")
    Server.execute(createLeagueCmd)
    //leagueAgg.createLeague(createLeagueCmd)

    // Asserts
    assert(Server.eventStore.getEvents(agg.id).size === 1)
    assert(Server.eventStore.getEvents(leagueId).size === 1)


    Server.execute(JoinLeague(leagueId, userId))
    assert(Server.eventStore.getEvents(leagueId).size === 2)

    println("EventLog")
    println(Server.eventStore.getEventLog.mkString("\n\t"))
  }
}

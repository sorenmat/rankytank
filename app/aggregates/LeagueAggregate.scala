package aggregates

import com.scalaprog.events.AbstractEvent
import commands.{RegisterMatchScore, JoinLeague, CreateLeague}
import events.{MatchScoreRegistered, LeagueJoined, LeagueCreated}
import java.util.UUID
import com.scalaprog.engine.Server
import com.scalaprog.aggregate.AggregateRoot

class LeagueAggregate(val id: UUID) extends AggregateRoot(id) {

  var created = false

  var usersInLeague: List[UUID] = Nil

  def this() {
    this(UUID.randomUUID())
  }

  def createLeague(cmd: CreateLeague) {
    require(cmd.name != null && cmd.name != "")
    require(cmd.password != null && cmd.password != "")
    require(!created)
    val event = LeagueCreated(cmd.id, cmd.name, cmd.password)
    applyEvent(event, true)
  }

  def addUserToLeague(cmd: JoinLeague) {
    require(cmd.leagueId != null)
    require(cmd.userId != null)
    if (!usersInLeague.find(id => id == cmd.userId).isEmpty)
      throw new RuntimeException("Can only add user to league once")
    val event = LeagueJoined(cmd.leagueId, cmd.userId)
    applyEvent(event, true)
  }

  def registerMatch(cmd: RegisterMatchScore) {
    require(created)
    require(cmd.leagueId == id)
    require(cmd.teamOneScore == 10 || cmd.teamTwoScore == 10)
    //do a check to see if the players exsists..
    applyEvent(MatchScoreRegistered(cmd.leagueId, cmd.teamOne, cmd.teamTwo, cmd.teamOneScore, cmd.teamTwoScore), true)
  }

  def applyEvent(cmd: AbstractEvent, storeEvent: Boolean) {
    cmd match {
      case c: LeagueCreated => {
        this.created = true
      }
      case c: LeagueJoined => {
        val userId = c.userId
        this.usersInLeague = userId :: usersInLeague
      }

      case c: MatchScoreRegistered =>
    }
    if (storeEvent)
      Server.eventStore.save(cmd, id)

  }
}

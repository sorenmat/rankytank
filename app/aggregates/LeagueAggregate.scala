package aggregates

import aggregate.AggregateRoot
import com.scalaprog.events.AbstractEvent
import commands.{RegisterMatchScore, JoinLeague, CreateLeague, CreateUserProfile}
import events.{LeagueJoined, LeagueCreated, ProfileCreated}
import eventstore.EventStore
import java.util.UUID
import com.scalaprog.engine.Server

class LeagueAggregate(val id: UUID) extends AggregateRoot(id) {

  var created = false


  def this() {
    this(UUID.randomUUID())
  }

  def createLeague(cmd: CreateLeague) {
    require(cmd.name != null && cmd.name != "")
    require(cmd.password != null && cmd.password != "")
    require(!created)
    val event = LeagueCreated(cmd.name, cmd.password)
    applyEvent(event, true)
  }

  def addUserToLeague(cmd: JoinLeague) {
    require(cmd.leagueId != null)
    require(cmd.userId != null)
    val event = LeagueJoined(cmd.leagueId, cmd.userId)
    applyEvent(event, true)
  }

  def registerMatch(cmd: RegisterMatchScore) {
    require(cmd.leagueId == id)
    require(cmd.teamOneScore == 10 || cmd.teamTwoScore == 10)
    //do a check to see if the players exsists..

  }

  def applyEvent(cmd: AbstractEvent, storeEvent: Boolean) {
    cmd match {
      case c: LeagueCreated => {
        this.created = true

      }
      case c: LeagueJoined => {}
    }
    if(storeEvent)
      Server.eventStore.save(cmd, id)

  }
}

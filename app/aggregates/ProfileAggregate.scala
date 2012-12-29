package aggregates

import aggregate.AggregateRoot
import com.scalaprog.events.AbstractEvent
import commands.CreateUserProfile
import events.ProfileCreated
import java.util.UUID
import com.scalaprog.engine.Server

class ProfileAggregate(val id: UUID) extends AggregateRoot(id) {

  def this() {
    this(UUID.randomUUID())
  }

  def createProfile(cmd: CreateUserProfile) {
    require(cmd.name != null && cmd.name != "")
    require(cmd.password != null && cmd.password != "")
    require(cmd.email != null && cmd.email != "")

    val event = ProfileCreated(cmd.id, cmd.name, cmd.password, cmd.email)
    applyEvent(event, true)
  }

  def applyEvent(cmd: AbstractEvent, storeEvent: Boolean) {
    cmd match {
      case c: ProfileCreated => {

      }
    }
    if (storeEvent)
      Server.eventStore.save(cmd, id)

  }
}

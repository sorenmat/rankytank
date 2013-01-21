package aggregates

import com.scalaprog.events.AbstractEvent
import commands.CreateUserProfile
import events.ProfileCreated
import java.util.UUID
import com.scalaprog.engine.Server
import com.scalaprog.domain.AggregateRoot

class ProfilesAggregate(id: UUID) extends AggregateRoot(id) {

  var userNames = List[String]()
  var emails = List[String]()

  def this() {
    this(UUID.randomUUID())
  }

  def createProfile(cmd: CreateUserProfile) {
    require(cmd.name != null && cmd.name != "")
    require(cmd.password != null && cmd.password != "")
    require(cmd.email != null && cmd.email != "")

    if (userNames.contains(cmd.name))
      throw new RuntimeException("Username '"+cmd.name+"' already taken")

    if (emails.contains(cmd.email))
      throw new RuntimeException("email already in use")

    val event = ProfileCreated(cmd.id, cmd.name, cmd.password, cmd.email)
    applyEvent(event, true)
  }

  def applyEvent(cmd: AbstractEvent, storeEvent: Boolean) {
    cmd match {
      case c: ProfileCreated => {
        userNames = c.name :: userNames
        emails = c.email :: emails
      }
    }
    if (storeEvent)
      Server.eventStore.save(cmd, id)

  }
}

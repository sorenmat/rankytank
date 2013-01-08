package events

import com.scalaprog.events.AbstractEvent
import java.util.UUID

case class LeagueCreated(id: UUID, name: String, password: String) extends AbstractEvent {

}

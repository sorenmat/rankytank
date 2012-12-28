package events

import com.scalaprog.events.AbstractEvent
import java.util.UUID

case class ProfileCreated(id: UUID, name: String, password: String, email: String) extends AbstractEvent

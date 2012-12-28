package events

import com.scalaprog.events.AbstractEvent

case class LeagueCreated(name: String, password: String) extends AbstractEvent {

}

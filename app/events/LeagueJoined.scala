package events

import com.scalaprog.events.AbstractEvent
import java.util.UUID

/**
 * User: soren
 */
case class LeagueJoined(leagueName: UUID, userId: UUID) extends AbstractEvent
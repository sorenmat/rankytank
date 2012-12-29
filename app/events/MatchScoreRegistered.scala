package events

import java.util.UUID
import com.scalaprog.events.AbstractEvent

case class MatchScoreRegistered(leagueId: UUID, teamOne: (UUID, UUID), teamTwo: (UUID, UUID), teamOneScore: Int, teamTwoScore: Int) extends AbstractEvent

package commands

import com.scalaprog.command.AbstractCommand
import java.util.UUID

case class RegisterMatchScore(leagueId: UUID, teamOne: (UUID, UUID), teamTwo: (UUID, UUID), teamOneScore: Int, teamTwoScore: Int) extends AbstractCommand

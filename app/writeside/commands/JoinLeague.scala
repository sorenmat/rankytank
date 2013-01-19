package commands

import com.scalaprog.command.AbstractCommand
import java.util.UUID

/**
 * User: soren
 */
case class JoinLeague(leagueId: UUID, userId: UUID) extends AbstractCommand

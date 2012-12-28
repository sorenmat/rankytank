package commandhandlers

import com.scalaprog.command.{AbstractCommand, CommandHandler}
import commands.{RegisterMatchScore, JoinLeague, CreateLeague}
import aggregates.LeagueAggregate

/**
 * User: soren
 */
class LeaugeHandler extends CommandHandler {
  def handle(cmd: AbstractCommand) {
    cmd match {
      case c: CreateLeague => {
        new LeagueAggregate(c.id).createLeague(c)
      }
      case c: JoinLeague => new LeagueAggregate(c.leagueId).addUserToLeague(c)
      case c: RegisterMatchScore => new LeagueAggregate(c.leagueId).registerMatch(c)
    }
  }
}

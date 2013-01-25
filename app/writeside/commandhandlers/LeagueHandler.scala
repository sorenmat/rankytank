package commandhandlers

import com.scalaprog.command.{AbstractCommand, CommandHandler}
import commands.{RegisterMatchScore, JoinLeague, CreateLeague}
import aggregates.LeagueAggregate
import com.scalaprog.domain.IRepository

/**
 * User: soren
 */
class LeagueHandler(repo: IRepository[LeagueAggregate]) extends CommandHandler(repo) {

  def handle(cmd: AbstractCommand) {
    cmd match {
      case c: CreateLeague => {
        val agg = getAggreateById(c.id, classOf[LeagueAggregate])
        agg.createLeague(c)
      }
      case c: JoinLeague => {
        val agg = getAggreateById(c.leagueId, classOf[LeagueAggregate])
        agg.addUserToLeague(c)
      }
      case c: RegisterMatchScore => getAggreateById(c.leagueId, classOf[LeagueAggregate]).registerMatch(c)
      case _ =>
    }
  }


}

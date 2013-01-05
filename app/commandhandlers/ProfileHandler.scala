package commandhandlers

import com.scalaprog.command.{AbstractCommand, CommandHandler}
import commands._
import aggregates._

/**
 * CommandHandler for handling commands related to User profiles
 * User: soren
 */
class ProfileHandler extends CommandHandler {
  def handle(cmd: AbstractCommand) {
    cmd match {
      case c: CreateUserProfile => {
        new ProfileAggregate(c.id).createProfile(c)
      }
      case _ =>
    }
  }
}

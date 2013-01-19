package commandhandlers

import com.scalaprog.command.{AbstractCommand, CommandHandler}
import commands._
import aggregates._
import java.util.UUID

/**
 * CommandHandler for handling writeside.commands related to User profiles
 * User: soren
 */
class ProfileHandler extends CommandHandler {
  def handle(cmd: AbstractCommand) {
    cmd match {
      case c: CreateUserProfile => {
        //TODO how do we handle this singleton aggregate thing
        getAggreateById(UUID.fromString("da1b24e5-294b-4919-b733-1d31af371951"), classOf[ProfilesAggregate]).createProfile(c)
      }
      case _ =>
    }
  }
}

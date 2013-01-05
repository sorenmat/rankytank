import com.scalaprog.engine.Server
import commandhandlers.{LeagueHandler, ProfileHandler}
import play.api._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Server.register(new ProfileHandler)
    Server.register(new LeagueHandler)
    Logger.info("Application has started")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}
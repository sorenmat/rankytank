import com.scalaprog.engine.{ProjectionEngine, Server}
import commandhandlers.{LeagueHandler, ProfileHandler}
import commands.CreateLeague
import java.util.UUID
import play.api._
import projection.{ProfileProjection, MatchInfoProjection, LeagueProjection}

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    ProjectionEngine.registerListener(MatchInfoProjection)
    ProjectionEngine.registerListener(ProfileProjection)
    // prefill projections
    for(e <- Server.eventStore.getEventLog) {
      LeagueProjection.eventPublished(e)
      MatchInfoProjection.eventPublished(e)
      ProfileProjection.eventPublished(e)
    }

    Server.register(new ProfileHandler)
    Server.register(new LeagueHandler)
    if(LeagueProjection.leagues.isEmpty)
      Server.execute(CreateLeague(UUID.randomUUID(), "Schantz", "12345q"))
    Logger.info("Application has started")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}
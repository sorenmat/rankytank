import aggregates.{LeagueAggregate, ProfilesAggregate}
import com.scalaprog.domain.Repository
import com.scalaprog.engine.{ProjectionEngine, Server}
import com.scalaprog.eventstore.MongoEventStore
import commandhandlers.{LeagueHandler, ProfileHandler}
import commands.CreateLeague
import java.util.UUID
import play.api._
import readside.projection.{ProfileProjection, MatchInfoProjection, LeagueProjection}

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    ProjectionEngine.registerListener(MatchInfoProjection)
    ProjectionEngine.registerListener(ProfileProjection)
    ProjectionEngine.registerListener(LeagueProjection)
    // prefill projections
    for(e <- Server.eventStore.getEventLog) {
      LeagueProjection.eventPublished(e)
      MatchInfoProjection.eventPublished(e)
      ProfileProjection.eventPublished(e)
    }

    val storage = MongoEventStore

    // Register the command handlers with the repositories
    Server.register(new ProfileHandler(new Repository[ProfilesAggregate](storage)))
    Server.register(new LeagueHandler(new Repository[LeagueAggregate](storage)))

    if(LeagueProjection.leagues.isEmpty)
      Server.execute(CreateLeague(UUID.randomUUID(), "Schantz", "12345q"))
    Logger.info("Application has started")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}
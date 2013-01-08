package projection

import com.scalaprog.projections.engine.EventListener
import com.scalaprog.events.AbstractEvent
import events.{LeagueCreated, ProfileCreated, MatchScoreRegistered}
import java.util.UUID
import scala.collection.mutable.Map
import com.scalaprog.engine.Server


/**
 * User: soren
 */
object LeagueProjection extends EventListener {
  var leagues = Map[UUID, String]()

  println("League Projection constructor")

  def eventPublished(event: AbstractEvent) {
    event match {
      case e: LeagueCreated => {
        leagues(e.id) = e.name
      }
      case _ =>
    }

  }
}

package readside.projection

import com.scalaprog.projections.engine.EventListener
import com.scalaprog.events.AbstractEvent
import events.{ProfileCreated, MatchScoreRegistered}
import java.util.UUID
import scala.collection.mutable.Map


/**
 * User: soren
 */
object ProfileProjection extends EventListener {
  var namesAndUUIDS = Map[UUID, String]()
  var names = List[String]()

  def eventPublished(event: AbstractEvent) {
    event match {
      case e: ProfileCreated => {

        namesAndUUIDS(e.id) = e.name
        names = e.name :: names
      }

      case _ =>
    }

  }
}

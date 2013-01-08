package projection

import com.scalaprog.projections.engine.EventListener
import com.scalaprog.events.AbstractEvent
import events.{ProfileCreated, MatchScoreRegistered}
import java.util.UUID
import scala.collection.mutable.Map


/**
 * User: soren
 */
object MatchInfoProjection extends EventListener {
  var scores = Map[UUID, Int]()
  var names = Map[UUID, String]()


  def eventPublished(event: AbstractEvent) {
    event match {
      case e: MatchScoreRegistered => {
        val teamOneAdjustment = if (e.teamOneScore == 10) {
          10 + (e.teamOneScore - e.teamTwoScore)
        } else {
          -5 - (e.teamTwoScore - e.teamOneScore)
        }


        val teamTwoAdjustment = if (e.teamTwoScore == 10) {
          10 + (e.teamTwoScore - e.teamOneScore)
        } else {
          -5 - (e.teamOneScore - e.teamTwoScore)
        }

        // add the new points to the map
        scores(e.teamOne._1) = scores.get(e.teamOne._1).getOrElse(0) + teamOneAdjustment
        scores(e.teamOne._2) = scores.get(e.teamOne._2).getOrElse(0) + teamOneAdjustment

        scores(e.teamTwo._1) = scores.get(e.teamTwo._1).getOrElse(0) + teamTwoAdjustment
        scores(e.teamTwo._2) = scores.get(e.teamTwo._2).getOrElse(0) + teamTwoAdjustment


      }

      case e: ProfileCreated => names(e.id) = e.name

      case _ =>
    }

  }

  def getScoresAsString = {
    scores.toSeq.sortWith(_._2 > _._2).map(score => names(score._1)+"\t"+score._2)
  }
  def getScores = {
    scores.toSeq.sortWith(_._2 > _._2).map(score => (names(score._1), score._2))
  }
}

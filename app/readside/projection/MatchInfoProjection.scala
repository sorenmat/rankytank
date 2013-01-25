package readside.projection

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
  private var last10Matches = List[MatchScoreWithUUID]()

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

        // Add to the list of the last 10 matches with peoples names and the match score..
        val play1 = e.teamOne._1
        val play2 = e.teamOne._2
        val play3 = e.teamTwo._1
        val play4 = e.teamTwo._2
        last10Matches = MatchScoreWithUUID(play1, play2, play3, play4, e.teamOneScore, e.teamTwoScore) :: last10Matches

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

  def getLast10MatchScores = {
    last10Matches.map(m => MatchScore(names(m.user1), names(m.user2), names(m.user3), names(m.user4), m.teamOneScore, m.teamTwoScore))

  }
}

case class MatchScoreWithUUID(user1: UUID, user2: UUID, user3: UUID, user4: UUID, teamOneScore: Int, teamTwoScore: Int)
case class MatchScore(user1: String, user2: String, user3: String, user4: String, teamOneScore: Int, teamTwoScore: Int)

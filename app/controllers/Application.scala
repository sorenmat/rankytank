package controllers

import play.api.mvc._
import readside.projection.{LeagueProjection, ProfileProjection, MatchInfoProjection}
import com.scalaprog.engine.Server
import commands.{RegisterMatchScore, CreateUserProfile}
import models._
import play.api.data._
import play.api.data.Forms._
import java.util.UUID


object Application extends Controller with Secured   {


  val registerNewMatchForm = Form(
    tuple(
      "teamOne" -> list(text) ,
      "teamTwo" -> list(text),
      "scoreone" -> number(min =0, max = 10),
      "scoretwo" -> number(min =0, max = 10)
    )
  )


  def index = IsAuthenticated { _ => implicit request =>
    // Ok("index")
    println("index called by "+session.get("user"))


    println(MatchInfoProjection.getScores.toList)
    println("-------------------")
    println(ProfileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList)
    println("leagues...")
    println(LeagueProjection.leagues.map(l => (l._1.toString, l._2)).toSeq)
    Ok(views.html.index("", MatchInfoProjection.getScores.toList, ProfileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList, LeagueProjection.leagues.toList, registerNewMatchForm))
    //Ok(views.html.index("Your new application is ready.", null)  )
  }



  def addNewMatch = IsAuthenticated { _ => implicit request =>
    val urlFormEncoded = request.body.asFormUrlEncoded.getOrElse(Map())

    registerNewMatchForm.bindFromRequest.fold(
      formWithErrors => {// binding failure, you retrieve the form containing errors,
        println("-------SCORE FORM ----------")
        println(formWithErrors)
        println("-----------------")
        BadRequest(views.html.index("", MatchInfoProjection.getScores.toList, ProfileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList, LeagueProjection.leagues.toList,  formWithErrors))
      },
      value => {// binding success, you get the actual value
        println("yay it worked")
        val league = UUID.fromString(urlFormEncoded("league")(0))
        val teamOne = (UUID.fromString(urlFormEncoded("teamOne")(0)), UUID.fromString(urlFormEncoded("teamOne")(1)) )
        val teamTwo = (UUID.fromString(urlFormEncoded("teamTwo")(0)), UUID.fromString(urlFormEncoded("teamTwo")(1)) )
        val scoreOne = urlFormEncoded("scoreone")(0).toInt
        val scoreTwo = urlFormEncoded("scoretwo")(0).toInt
        println("TeamOne "+teamOne+": "+scoreOne)
        println("TeamTwo "+teamTwo+": "+scoreTwo)


        Server.execute(RegisterMatchScore(league, teamOne, teamTwo, scoreOne, scoreTwo))
        Ok(views.html.index("", MatchInfoProjection.getScores.toList, ProfileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList,LeagueProjection.leagues.toList,  registerNewMatchForm))
      }
    )
  }


  def createNewLeague = IsAuthenticated { _ => implicit request =>
    Ok(views.html.createNewLeague(null))
  }

}
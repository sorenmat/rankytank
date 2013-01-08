package controllers

import play.api.mvc._
import play.api.data.validation.Constraints._
import projection.{LeagueProjection, ProfileProjection, MatchInfoProjection}
import com.scalaprog.engine.Server
import commands.{RegisterMatchScore, CreateUserProfile}
import models._
import play.api.data._
import play.api.data.Forms._
import java.util.UUID
import scala.collection.JavaConversions._


object Application extends Controller {

  val profileForm = Form(
    mapping(
      "profileName" -> nonEmptyText.verifying("Username not unique" , user => isUserNameUnique(user)) ,
      "email" -> nonEmptyText.verifying("Not a valid email address", e => e.contains("@") && e.contains(".")),
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )

  val registerNewMatchForm = Form(
    tuple(
      "teamOne" -> list(text) ,
      "teamTwo" -> list(text),
      "scoreone" -> number(min =0, max = 10),
      "scoretwo" -> number(min =0, max = 10)
    )
  )

  def isUserNameUnique(name: String) : Boolean = {
    println("profile projections name")
    println(ProfileProjection.names.mkString("\n"))
    println("unique name "+(!ProfileProjection.names.contains(name)))
    !ProfileProjection.names.contains(name.trim)
  }

  def index = Action {
    // Ok("index")
    println("index called")


    println(MatchInfoProjection.getScores.toList)
    println("-------------------")
    println(ProfileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList)
    println("leagues...")
    println(LeagueProjection.leagues.map(l => (l._1.toString, l._2)).toSeq)
    Ok(views.html.index("", MatchInfoProjection.getScores.toList, ProfileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList, LeagueProjection.leagues.toList, profileForm, registerNewMatchForm))
    //Ok(views.html.index("Your new application is ready.", null)  )
  }





  def registerProfile = Action { implicit request =>
    println("registred profile called")

    profileForm.bindFromRequest.fold(
      formWithErrors => {// binding failure, you retrieve the form containing errors,
        println("-----------------")
        println(formWithErrors)
        println("-----------------")
        BadRequest(views.html.index("", MatchInfoProjection.getScores.toList, ProfileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList, LeagueProjection.leagues.toList,formWithErrors, registerNewMatchForm))
      },
      value => {// binding success, you get the actual value
        Server.execute(CreateUserProfile(UUID.randomUUID(), value.profileName, value.password, value.email))
        Ok(views.html.index("", MatchInfoProjection.getScores.toList, ProfileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList, LeagueProjection.leagues.toList, profileForm, registerNewMatchForm))
      }
    )
  }

  def addNewMatch = Action { implicit request =>
    var newData = Map[String, String]()
    val urlFormEncoded = request.body.asFormUrlEncoded.getOrElse(Map())

    registerNewMatchForm.bindFromRequest.fold(
      formWithErrors => {// binding failure, you retrieve the form containing errors,
        println("-------SCORE FORM ----------")
        println(formWithErrors)
        println("-----------------")
        BadRequest(views.html.index("", MatchInfoProjection.getScores.toList, ProfileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList, LeagueProjection.leagues.toList, profileForm, formWithErrors))
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
        Ok(views.html.index("", MatchInfoProjection.getScores.toList, ProfileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList,LeagueProjection.leagues.toList, profileForm, registerNewMatchForm))
      }
    )
  }


  def createNewLeague = Action {
    Ok(views.html.createNewLeague(null))
  }
}
package controllers

import play.api.mvc._
import play.api.data.validation.Constraints._
import projection.{ProfileProjection, MatchInfoProjection}
import com.scalaprog.engine.Server
import commands.CreateUserProfile
import models._
import play.api.data._
import play.api.data.Forms._
import java.util.UUID


object Application extends Controller {

  val profileForm = Form(
    mapping(
      "profileName" -> nonEmptyText.verifying("Username not unique" , user => !profileProjection.names.contains(user)) ,
      "email" -> nonEmptyText.verifying("Not a valid email address", e => e.contains("@") && e.contains(".")),
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )

  val projection = new MatchInfoProjection()
  val profileProjection = new ProfileProjection()
  for (e <- Server.eventStore.getEventLog) {
    projection.eventPublished(e)
    profileProjection.eventPublished(e)
  }

  def index = Action {
    // Ok("index")
    println("index called")


    println(projection.getScores.toList)
    println("-------------------")
    println(profileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList)
    Ok(views.html.index("", projection.getScores.toList, profileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList, profileForm))
    //Ok(views.html.index("Your new application is ready.", null)  )
  }





  def registerProfile = Action { implicit request =>
    println("registred profile called")

    profileForm.bindFromRequest.fold(
      formWithErrors => {// binding failure, you retrieve the form containing errors,
        println("-----------------")
        println(formWithErrors)
        println("-----------------")
        BadRequest(views.html.index("", projection.getScores.toList, profileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList, formWithErrors))
      },
      value => {// binding success, you get the actual value
        Server.execute(CreateUserProfile(UUID.randomUUID(), value.profileName, value.password, value.email))
        Ok(views.html.index("", projection.getScores.toList, profileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList, profileForm))
      }
    )
  }
}
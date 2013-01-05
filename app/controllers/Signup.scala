package controllers

import play.api.mvc._
import projection.{ProfileProjection, MatchInfoProjection}
import com.scalaprog.engine.Server
import commands.CreateUserProfile
import models._
import play.api.data._
import play.api.data.Forms._
import java.util.UUID


object Signup extends Controller {

  val profileForm = Form[User](
    mapping(
      "profileName" -> nonEmptyText,
      "email" -> nonEmptyText.verifying("Not a valid email address", e => e.contains("@") && e.contains(".")),
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )

  def index = Action {
    // Ok("index")
    println("index called")

    Ok(views.html.signup(profileForm))
    //Ok(views.html.index("Your new application is ready.", null)  )
  }





  def registerProfile = Action { implicit request =>
    println("signup called")

    profileForm.bindFromRequest.fold(
      formWithErrors => {// binding failure, you retrieve the form containing errors,
        println("-----------------")
        println(formWithErrors)
        println("-----------------")
        BadRequest(views.html.signup(formWithErrors))
      },
      value => {// binding success, you get the actual value
        Ok(views.html.signup(profileForm))
      }
    )
  }
}
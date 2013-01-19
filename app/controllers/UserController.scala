package controllers


import play.api.mvc._
import readside.projection.ProfileProjection
import com.scalaprog.engine.Server
import commands.CreateUserProfile
import models._
import play.api.data._
import play.api.data.Forms._
import java.util.UUID

object UserController extends Controller {

  val loginForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    ) verifying ("Invalid email or password", result => result match {
      case (username, password) => ProfileProjection.names.contains(username)
    })
  )

  val profileForm = Form(
    mapping(
      "profileName" -> nonEmptyText.verifying("Username not unique" , user => isUserNameUnique(user)) ,
      "email" -> nonEmptyText.verifying("Not a valid email address", e => e.contains("@") && e.contains(".")),
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )

  def isUserNameUnique(name: String) : Boolean = {
    println("profile projections name")
    println(ProfileProjection.names.mkString("\n"))
    println("unique name "+(!ProfileProjection.names.contains(name)))
    !ProfileProjection.names.contains(name.trim)
  }

  def login = Action { implicit request =>
    println("registred profile called")

    loginForm.bindFromRequest.fold(
      formWithErrors => {// binding failure, you retrieve the form containing errors,
        println("-----------------")
        println(formWithErrors)
        println("-----------------")
        BadRequest(views.html.login(formWithErrors))
      },
      value => {// binding success, you get the actual value
        if(!ProfileProjection.names.contains(value._1))
          BadRequest(views.html.login(loginForm))
        //Server.execute(CreateUserProfile(UUID.randomUUID(), value.profileName, value.password, value.email))
        Redirect(routes.Application.index()).withSession("user" -> value._1)
        //Ok(views.html.index("", MatchInfoProjection.getScores.toList, ProfileProjection.namesAndUUIDS.map(d => (d._1, d._2)).toList, LeagueProjection.leagues.toList,  null)
      }
    )
  }



  def registerProfile = Action { implicit request =>
    println("registred profile called")
    profileForm.bindFromRequest.fold(
      formWithErrors => {// binding failure, you retrieve the form containing errors,
        println("-----------------")
        println(formWithErrors)
        println("-----------------")
        BadRequest(views.html.registerProfile(formWithErrors))
      },
      value => {// binding success, you get the actual value
        println("saving user")
        Server.execute(CreateUserProfile(UUID.randomUUID(), value.profileName, value.password, value.email))
        Redirect(routes.UserController.login())
        //Ok(views.html.index())
      }
    )
  }

}
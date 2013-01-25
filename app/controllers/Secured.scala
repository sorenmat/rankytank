package controllers

import play.api.mvc._

/**
 * User: soren
 */
trait Secured {

  /**
   * Provide security features
   */

  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get("user")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.UserController.showlogin)

  // --

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) {
    user =>
      Action(request => f(user)(request))
  }

  /**
   * Check if the connected user is a member of this project.
   */
  def IsMemberOf(project: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated {
    user => request =>
    /*
    if(Project.isMember(project, user)) {
      f(user)(request)
    } else {
      Results.Forbidden
    }
    */
      f(user)(request)
  }

  /**
   * Check if the connected user is a owner of this task.
   */
  def IsOwnerOf(task: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated {
    user => request =>
    /*if(Task.isOwner(task, user)) {
      f(user)(request)
    } else {
      Results.Forbidden
    } */
      f(user)(request)
  }
}

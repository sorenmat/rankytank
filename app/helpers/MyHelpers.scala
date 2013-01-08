package helpers

import views.html.helper.FieldConstructor
import views._
import html.myTwitterBootstrapFieldConstructor

/**
 * User: soren
 */
object MyHelpers {
  implicit val myFields = FieldConstructor(myTwitterBootstrapFieldConstructor.f)
}
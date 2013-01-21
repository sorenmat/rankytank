import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "rankytank"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.mongodb" % "mongo-java-driver" % "2.10.1",
    "org.scalatest" %% "scalatest" % "1.8" % "test",
    "com.google.code.gson" % "gson" % "2.2.2",
    "jp.t2v" %% "play20.auth" % "0.5",
    "com.google.protobuf" % "protobuf-java" % "2.4.1",

    "org.webjars" % "flot" % "0.7",
    "com.github.twitter" % "bootstrap" % "2.0.2"

  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    // Add your own project settings here
    //sourceDirectory in Compile <<= baseDirectory / "CQRS_Core/src",
    scalaSource in Compile <<= baseDirectory / "CQRS_Core/src",
    resolvers += "webjars" at "http://webjars.github.com/m2",
    testOptions in Test := Nil

  )

}

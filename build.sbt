organization in ThisBuild := "com.airtonjal"

name := "centrality-nubank"

version := "0.1"

scalaVersion  := "2.10.5"

scalacOptions := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "spray repo" at "http://repo.spray.io"

val sprayVersion = "1.3.2"
val akkaVersion = "2.3.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "io.spray" %% "spray-can" % sprayVersion,
  "io.spray" %% "spray-routing" % sprayVersion,
  "org.json4s" %% "json4s-native" % "3.2.4",
  "org.scalaz" %% "scalaz-core" % "7.1.2"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "io.spray" %% "spray-testkit" % sprayVersion % "test",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test"
)

Revolver.settings

mainClass in Revolver.reStart := Some("com.airtonjal.Bootstrap")

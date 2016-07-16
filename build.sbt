name := """sbt QA tools"""

version := "1.0"

scalaVersion := "2.11.8"

enablePlugins(GatlingPlugin)

scalacOptions := Seq("-deprecation", "-feature", "-unchecked")

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.2" % "test",
  "io.gatling" % "gatling-test-framework" % "2.2.2" % "test",
  "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.typesafe.play" %% "play-json" % "2.5.4",
  "com.typesafe.play" %% "play-functional" % "2.5.4",
  "org.scalacheck" %% "scalacheck" % "1.13.0",
  "com.scalawilliam" %% "xs4s" % "0.2"
)

coverageMinimum := 80.0
coverageFailOnMinimum := true

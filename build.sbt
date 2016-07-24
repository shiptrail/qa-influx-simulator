name := """sbt QA tools"""

version := "1.0"

scalaVersion := "2.11.8"

enablePlugins(GatlingPlugin)

scalacOptions := Seq(
  "-encoding", "UTF-8", "-target:jvm-1.8", "-deprecation",
  "-feature", "-unchecked", "-language:implicitConversions", "-language:postfixOps")

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.2" % "test"
libraryDependencies += "io.gatling"            % "gatling-test-framework"    % "2.2.2" % "test"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.5.4"
libraryDependencies += "com.typesafe.play" %% "play-functional" % "2.5.4"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.0"

coverageMinimum := 80.0
coverageFailOnMinimum := true
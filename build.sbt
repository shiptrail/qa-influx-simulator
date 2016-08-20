name := """insi"""

version := {
  val jenkinsBuild = for {
    minor <- sys.env.get("BUILD_NUMBER")
    changenum <- sys.env.get("GERRIT_CHANGE_NUMBER")
    patchset <- sys.env.get("GERRIT_PATCHSET_NUMBER")
  } yield s"1.$minor.$changenum.$patchset"

  val user = sys.env.getOrElse("USER", "nouser")

  jenkinsBuild.getOrElse(s"1.${git.gitHeadCommit.value.get}.$user")
}

maintainer := "SWP DV Team <dbslehre@inf.fu-berlin.de>"

scalaVersion := "2.11.8"

enablePlugins(GatlingPlugin)

enablePlugins(BuildInfoPlugin)

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, maintainer)
buildInfoPackage := "clientupdates"

scalacOptions := Seq("-deprecation", "-feature", "-unchecked")

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.2",
  "io.gatling" % "gatling-test-framework" % "2.2.2" % "test",
  "io.gatling" % "gatling-core" % "2.2.2",
  "io.gatling" % "gatling-app" % "2.2.2",
  "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.typesafe.play" %% "play-json" % "2.5.4",
  "com.typesafe.play" %% "play-functional" % "2.5.4",
  "org.scalacheck" %% "scalacheck" % "1.13.0",
  "com.scalawilliam" %% "xs4s" % "0.2",
  "org.rogach" %% "scallop" % "2.0.1"
)

parallelExecution in Test := false

coverageMinimum := 80.0
coverageFailOnMinimum := true

coverageExcludedPackages := "simulations.*"

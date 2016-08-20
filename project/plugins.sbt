addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "3.0.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.5")

// deployment

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0-M5")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.7.1")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.1")
libraryDependencies += "org.vafer" % "jdeb" % "1.3" artifacts (Artifact("jdeb", "jar", "jar"))

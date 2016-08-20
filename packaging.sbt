enablePlugins(JavaAppPackaging)


// ====== General settings ======

packageSummary := "influx simulator command line client"

packageDescription := """This command line utility can send pre-recorded or (pseudo)randomly generated gps data to a backend-server instance.""".stripMargin

// ====== Linux specific settings ======

// Where to install package data
defaultLinuxInstallLocation := "/opt"

// ====== Debian specific ======

// dpkg-deb in java
enablePlugins(JDebPackaging)
// dependencies for debian based distributions
debianPackageDependencies in Debian ++= Seq("openjdk-8-jre", "gpsbabel")

// ====== RPM specific ======

// rpm specific fields
rpmVendor := "SWP DV Team"
rpmLicense := Some("Apache License 2.0")
rpmRequirements := List("java-1.8.0-openjdk", "gpsbabel")

lazy val root = (project in file("."))
  .enablePlugins(io.gatling.sbt.GatlingPlugin)
  .settings(
    name := "GatlingDemo",
    version := "0.1.0",
    scalaVersion := "2.13.12",
    libraryDependencies ++= Seq(
      "io.gatling" % "gatling-core" % "3.11.5" % Test,
      "io.gatling" % "gatling-http" % "3.11.5" % Test
    )
  )

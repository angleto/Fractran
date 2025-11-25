ThisBuild / organization := "io.github.angleto"
ThisBuild / version      := "0.2.0"
ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "fractran",
    description := "A FRACTRAN interpreter implemented in Scala",

    // Main class for running the application
    Compile / mainClass := Some("io.github.angleto.fractran.Main"),

    // Dependencies
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.19" % Test
    ),

    // Compiler options
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xlint:unused",
      "-Xlint:adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard"
    ),

    // Assembly settings for creating fat JAR
    assembly / mainClass := Some("io.github.angleto.fractran.Main"),
    assembly / assemblyJarName := "fractran.jar"
  )
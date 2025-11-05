name := "Fractran"

version := "1.0.0"

scalaVersion := "3.3.1"

// Compiler options for Scala 3
scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-Xfatal-warnings"
)

// Main class for running
Compile / mainClass := Some("FractranCL")

// Assembly settings (optional, for creating fat JAR)
assembly / assemblyJarName := "fractran.jar"
assembly / mainClass := Some("FractranCL")

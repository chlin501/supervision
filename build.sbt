name := "supervision"

scalaVersion := "2.13.2"

libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "28.1-jre",
  "com.typesafe" % "config" % "1.4.0",
  "dev.zio" %% "zio" % "1.0.0-RC18-2",
  "org.slf4j" % "slf4j-api" % "1.7.29",
  "org.slf4j" % "slf4j-simple" % "1.7.29",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)

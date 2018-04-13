name := "Prototype"

version := "1.0"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.google.protobuf" % "protobuf-java-util" % "3.5.1",
  "com.google.protobuf" % "protobuf-java" % "3.5.1",
  "org.postgresql" % "postgresql" % "42.2.2",
  "org.apache.kafka" %% "kafka" % "1.1.0",
  "org.apache.kafka" % "kafka-streams" % "1.1.0",
  "com.typesafe.akka" %% "akka-actor" % "2.5.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.11" % Test,
  "com.typesafe.akka" %% "akka-persistence" % "2.5.11",
  "com.typesafe.akka" %% "akka-remote" % "2.5.11",
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "com.typesafe.akka" %% "akka-stream" % "2.5.11",
  "net.liftweb" %% "lift-json" % "3.2.0",
  "net.debasishg" %% "redisclient" % "3.5"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test


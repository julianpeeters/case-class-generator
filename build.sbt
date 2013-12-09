name := "case-class-generator"

version := "0.3-SNAPSHOT"

organization := "com.julianpeeters"

scalaVersion := "2.10.3"

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= Seq( 
  "org.ow2.asm" % "asm-util" % "4.1",
  "com.julianpeeters" %% "artisinal-pickle-maker" % "0.5",
  "com.banno.salat.avro" %% "salat-avro" % "0.0.10-3",
  "org.apache.avro" % "avro" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5", 
  "org.specs2" %% "specs2" % "2.2" % "test",
  "com.novus" %% "salat" % "1.9.4" % "test"
)

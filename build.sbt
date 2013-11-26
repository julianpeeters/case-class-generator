name := "case-class-generator"

version := "0.3-SNAPSHOT"

organization := "com.julianpeeters"

scalaVersion := "2.10.3"

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq( 
  "com.novus" %% "salat" % "1.9.3", //exclude("org.scala-lang", "scalap"),
  "org.ow2.asm" % "asm-util" % "4.1",
  "com.julianpeeters" %% "artisinal-pickle-maker" % "0.3",
  "org.slf4j" % "slf4j-simple" % "1.7.5", 
  "org.specs2" %% "specs2" % "2.2" % "test",
  "org.apache.avro" % "avro" % "1.7.5"
)

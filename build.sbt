name := "asm-salat-example"

version := "0.1-SNAPSHOT"

organization := "com.julianpeeters"

//scalaVersion := "2.9.1"
scalaVersion := "2.10.2"

//resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += Resolver.sonatypeRepo("snapshots")

resolvers += Resolver.file("Local Ivy Repository", file("/home/julianpeeters/.ivy2/local/"))(Resolver.ivyStylePatterns)

libraryDependencies ++= Seq( 
 //  "com.novus" %% "salat" % "1.9.1", 
   "com.novus" %% "salat" % "1.9.3",
   "com.julianpeeters" %% "artisinal-pickle-maker" % "0.2-SNAPSHOT",
   "org.slf4j" % "slf4j-simple" % "1.7.5", 
   "org.apache.avro" % "avro" % "1.7.5"
  // "org.scala-lang" %% "scala-pickling" % "0.8.0-SNAPSHOT"
)

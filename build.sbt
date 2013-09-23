name := "scalavro-example"

version := "0.0.1-SNAPSHOT"

organization := "com.julianpeeters"

scalaVersion := "2.10.2"

resolvers += "spray" at "http://repo.spray.io/"
//resolvers += Resolver.file("Local Ivy Repository", file("/home/julianpeeters/.ivy2/local/"))(Resolver.ivyStylePatterns)

libraryDependencies ++= Seq( 
  "io.spray" %%  "spray-json" % "1.2.5",
  "com.gensler" %% "scalavro" % "0.4.0"
 //"org.ow2.asm" % "asm-util" % "4.1", 
 // "org.apache.avro" % "avro" % "1.7.4",
)

name := "readable-string-interpolation"

version := "1.0"

scalaVersion := "2.10.2"

scalacOptions += "-feature"

// For StringConcatenation (TODO: implement a similar class and remove this dependency)
libraryDependencies += "org.eclipse.xtext" % "org.eclipse.xtext.xbase.lib" % "2.4.2"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test"

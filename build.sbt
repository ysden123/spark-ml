import sbt.Keys.libraryDependencies

lazy val scalaTestVersion = "3.0.4"
lazy val typeSafeConfVersion = "1.3.1"
lazy val sparkVersion = "2.2.0"
lazy val jFreeChartVersion = "1.0.19"
lazy val commonMath3Version = "3.6.1"

lazy val commonSettings = Seq(
  organization := "com.stulsoft",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq(
    "-feature",
    "-language:implicitConversions",
    "-language:postfixOps"),
  libraryDependencies ++= Seq(
    "com.typesafe" % "config" % typeSafeConfVersion,
    "org.apache.commons" % "commons-math3" % commonMath3Version,
    "org.jfree" % "jfreechart" % jFreeChartVersion,
      "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-mllib" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion,
    "org.apache.spark" %% "spark-streaming" % sparkVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  )
)

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

lazy val sparkMl = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "sparkMl"
  )
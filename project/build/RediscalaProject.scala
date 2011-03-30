import sbt._
import reaktor.scct.ScctProject

class RediscalaProject(info: ProjectInfo) extends DefaultProject(info) with IdeaProject with ScctProject {
  lazy val scalaToolsSnapshots = "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"

  lazy val scalazCore = "org.scalaz" %% "scalaz-core" % "6.0-SNAPSHOT"
  lazy val specs = "org.scala-tools.testing" %% "specs" % "1.6.7" % "test"
  lazy val junit = "junit" % "junit" % "4.8.2" % "test"
  lazy val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test"
}

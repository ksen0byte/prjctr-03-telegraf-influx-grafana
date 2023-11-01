import mill._, scalalib._

import $ivy.`com.lihaoyi::mill-contrib-docker:$MILL_VERSION`
import contrib.docker.DockerModule

object app extends ScalaModule with DockerModule {
  object docker extends DockerConfig {
    override def tags = List("scala-cask-app")
    override def exposedPorts = Seq(8081)
  }

  def scalaVersion = "3.3.1"

  override def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.9.1",
    ivy"io.micrometer:micrometer-registry-statsd:1.11.5",
    ivy"org.mongodb:mongodb-driver-sync:4.11.0"
  )
}

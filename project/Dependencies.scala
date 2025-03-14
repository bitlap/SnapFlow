import sbt.*

object Dependencies {

  object Version {
    val scala       = "3.6.3"
    val circe       = "0.14.10"
    val scalikejdbc = "4.0.0"
    val logback     = "1.5.17"
    val sttpApispec = "1.11.18"
    val http4s      = "0.23.30"
    val config      = "1.4.3"
    val pureconfig  = "0.17.8"
    val catsEffect  = "3.5.7"
    val slf4jApi    = "2.0.16"
  }

  lazy val dependencies: Seq[ModuleID] = Seq(
    "com.typesafe"           % "config"          % Version.config withSources (),
    "org.typelevel"         %% "cats-effect"     % Version.catsEffect withSources (),
    "com.github.pureconfig" %% "pureconfig-core" % Version.pureconfig withSources (),
    "org.http4s"            %% "http4s-dsl"      % Version.http4s withSources (),
    "org.http4s"            %% "http4s-circe"    % Version.http4s withSources (),
    "io.circe"              %% "circe-parser"    % Version.circe withSources (),
    "io.circe"              %% "circe-generic"   % Version.circe withSources (),
    "io.circe"              %% "circe-core"      % Version.circe withSources (),
    "org.slf4j"              % "slf4j-api"       % Version.slf4jApi withSources (),
    "org.slf4j"              % "slf4j-simple"    % Version.slf4jApi withSources (),
    "ch.qos.logback"         % "logback-classic" % Version.logback withSources ()
  )
}

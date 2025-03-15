import sbt.*

object Dependencies {

  object Versions {
    val scala       = "3.6.3"
    val circe       = "0.14.10"
    val scalikejdbc = "4.0.0"
    val logback     = "1.5.17"
    val sttp        = "1.11.18"
    val http4s      = "0.23.30"
    val config      = "1.4.3"
    val pureconfig  = "0.17.8"
    val catsEffect  = "3.5.7"
    val slf4jApi    = "2.0.16"
    val distage     = "1.2.16"
    val doobie      = "1.0.0-RC8"
    val log4cats    = "2.7.0"
  }

  lazy val dependencies: Seq[ModuleID] = Seq(
    "com.typesafe"           % "config"              % Versions.config withSources (),
    "org.typelevel"         %% "cats-effect"         % Versions.catsEffect withSources (),
    "com.github.pureconfig" %% "pureconfig-core"     % Versions.pureconfig withSources (),
    "org.http4s"            %% "http4s-dsl"          % Versions.http4s withSources (),
    "org.http4s"            %% "http4s-ember-server" % Versions.http4s withSources (),
    "org.http4s"            %% "http4s-circe"        % Versions.http4s withSources (),
    "io.circe"              %% "circe-parser"        % Versions.circe withSources (),
    "io.circe"              %% "circe-generic"       % Versions.circe withSources (),
    "io.circe"              %% "circe-core"          % Versions.circe withSources (),
    "org.slf4j"              % "slf4j-api"           % Versions.slf4jApi withSources (),
    "ch.qos.logback"         % "logback-classic"     % Versions.logback withSources (),
//    ("com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % Versions.sttp)
//      .exclude("org.slf4j", "slf4j-api") withSources (),
    "io.7mind.izumi" %% "distage-core"  % Versions.distage withSources (),
    "io.7mind.izumi" %% "logstage-core" % Versions.distage withSources (),
//    "io.7mind.izumi" %% "distage-extension-plugins" % Versions.distage withSources (),
//    "io.7mind.izumi" %% "distage-framework" % Versions.distage withSources (),
    "org.tpolecat"  %% "doobie-core"     % Versions.doobie withSources (),
    "org.tpolecat"  %% "doobie-postgres" % Versions.doobie withSources (),
    "org.tpolecat"  %% "doobie-hikari"   % Versions.doobie withSources (),
    "org.typelevel" %% "log4cats-core"   % Versions.log4cats withSources (),
    "org.typelevel" %% "log4cats-slf4j"  % Versions.log4cats withSources ()
  )
}

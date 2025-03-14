import scala.io.AnsiColor.*

import Dependencies.Version
import de.heikoseeberger.sbtheader.HeaderPlugin

Global / onLoad := {
  println(s"""$GREEN
             |$GREEN
             |$GREEN  ________  _____  ___        __         _______        _______  ___        ______    __   __  ___ 
             |$GREEN /"       )(\"   \\|"  \\      /""\\       |   __ "\\      /"     "||"  |      /    " \\  |"  |/  \\|  "|
             |$GREEN(:   \\___/ |.\\   \\    |    /    \\      (. |__) :)    (: ______)||  |     // ____  \\ |'  /    \\:  |
             |$GREEN \\___  \\   |: \\.   \\  |   /' /\\  \\     |:  ____/      \\/    |  |:  |    /  /    ) :)|: /'        |
             |$GREEN  __/  \\  |.  \\    \\. |  //  __'  \\    (|  /          // ___)   \\  |___(: (____/ //  \\//  /\'    |
             |$GREEN /" \\   :) |    \\    \\ | /   /  \\  \\  /|__/ \\        (:  (     ( \\_|:  \\        /   /   /  \\   |
             |$GREEN(_______/   \\___|\\____\\)(___/    \\___)(_______)        \\__/      \\_______)\"_____/   |___/    \\___|
             |$GREEN                                                                                                   
             |$GREEN
             |$RESET        v.${version.value}
             |""".stripMargin)
  (Global / onLoad).value
}

ThisBuild / resolvers ++= Seq(
  "New snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots/"
) ++ Resolver.sonatypeOssRepos("snapshots")
ThisBuild / scalaVersion := Version.scala

lazy val commonConfiguration: Project => Project =
  _.settings(Information.value)
    .settings(ProjectSetting.value)
    .settings(ProjectSetting.noPublish)
    .settings(commands ++= Commands.value)

lazy val SnapFlow = (project in file("."))
  .settings(name := "SnapFlow")
  .configure(commonConfiguration)
  .settings(libraryDependencies ++= Dependencies.dependencies)
  .enablePlugins(ScalafmtPlugin, HeaderPlugin)

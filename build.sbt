import scala.io.AnsiColor.*

import Dependencies.Versions
import de.heikoseeberger.sbtheader.HeaderPlugin

Global / onLoad := {
  println(raw"""$GREEN
             |$GREEN
             |$GREEN                                   ,-.----.            .--., ,--.'|                           
             |$GREEN                 ,---,             \    /  \         ,--.'  \|  | :     ,---.           .---. 
             |$GREEN  .--.--.    ,-+-. /  |            |   :    |        |  | /\/:  : '    '   ,'\         /. ./| 
             |$GREEN /  /    '  ,--.'|'   |  ,--.--.   |   | .\ :        :  : :  |  ' |   /   /   |     .-'-. ' | 
             |$GREEN|  :  /`./ |   |  ,"' | /       \  .   : |: |        :  | |-,'  | |  .   ; ,. :    /___/ \: | 
             |$GREEN|  :  ;_   |   | /  | |.--.  .-. | |   |  \ :        |  : :/||  | :  '   | |: : .-'.. '   ' . 
             |$GREEN \  \    `.|   | |  | | \__\/: . . |   : .  |        |  |  .''  : |__'   | .; :/___/ \:     ' 
             |$GREEN  `----.   \   | |  |/  ," .--.; | :     |`-'        '  : '  |  | '.'|   :    |.   \  ' .\    
             |$GREEN /  /`--'  /   | |--'  /  /  ,.  | :   : :           |  | |  ;  :    ;\   \  /  \   \   ' \ | 
             |$GREEN'--'.     /|   |/     ;  :   .'   \|   | :           |  : \  |  ,   /  `----'    \   \  |--"  
             |$GREEN  `--'---' '---'      |  ,     .-./`---'.|           |  |,'   ---`-'              \   \ |     
             |$GREEN                       `--`---'      `---`           `--'                          '---"      
             |$GREEN                                                                                                   
             |$GREEN
             |$RESET        v.${version.value}
             |""".stripMargin)
  (Global / onLoad).value
}

ThisBuild / resolvers ++= Seq(
  "New snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots/"
) ++ Resolver.sonatypeOssRepos("snapshots")
ThisBuild / scalaVersion := Versions.scala

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

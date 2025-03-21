import com.typesafe.sbt.GitPlugin.autoImport.git

import sbt.{ Compile, Def, SettingKey }
import sbt.Keys.*
import sbtbuildinfo.BuildInfoKeys.buildInfoKeys
import sbtbuildinfo.BuildInfoPlugin.autoImport.*

object BuildInfoSettings {

  private val gitCommitString = SettingKey[String]("gitCommit")

  val value: Seq[Def.Setting[?]] = Seq(
    buildInfoObject := "SnapFlowBuildInfo",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, gitCommitString) ++ Seq[BuildInfoKey](
      Compile / libraryDependencies
    ),
    buildInfoPackage := s"${organization.value}.SnapFlow",
    buildInfoOptions ++= Seq(BuildInfoOption.ToJson, BuildInfoOption.BuildTime),
    gitCommitString := git.gitHeadCommit.value.getOrElse("Not Set")
  )

}

import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.headerCreateAll
import sbt.*
import sbt.Keys.*

object Information {

  val value: Seq[Def.Setting[?]] = Seq(
    Compile / compile := (Compile / compile).dependsOn(Compile / headerCreateAll).value,
    organization      := "cn.dreamylost", // move to com.dreamylost
    organizationName  := "SnapFlow",
    startYear         := Some(2025),
    description       := "SnapFlow",
    homepage          := Some(url(s"https://github.com/bitlap/SnapFlow")),
    licenses += License.GPL3_or_later,
    developers := List(
      Developer(
        "jxnu-liguobin",
        "梦境迷离",
        "dreamylost@outlook.com",
        url("https://github.com/jxnu-liguobin")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/bitlap/SnapFlow"),
        "scm:git@github.com:bitlap/SnapFlow.git"
      )
    )
  )

}

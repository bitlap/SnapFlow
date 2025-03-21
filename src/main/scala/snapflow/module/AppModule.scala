/*
 * Copyright (C) 2025  SnapFlow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package snapflow.module

import scala.concurrent.duration.*

import org.http4s.dsl.Http4sDsl
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import cats.*
import cats.effect.{ Async, Concurrent, IO, Sync }
import distage.{ ModuleDef, Scene, TagK }
import distage.StandardAxis.Repo
import doobie.Transactor
import doobie.util.log.LogHandler
import izumi.fundamentals.platform.integration.PortCheck
import pureconfig.*
import pureconfig.{ ConfigObjectSource, ConfigSource }
import snapflow.{ service, HttpServer }
import snapflow.api.{ HttpApi, UserApi }
import snapflow.config.*
import snapflow.repo.UserRepository
import snapflow.service.*
import snapflow.sql.{ SQL, TransactorResource }

object AppModule {

  def apply[F[_]: {TagK, Async}]: ModuleDef = new ModuleDef {
    include(modules.repos[F])
    include(modules.apis[F])
    include(modules.configs)
    include(modules.utils)
  }

  object modules {

    def apis[F[_]: {TagK, Async}]: ModuleDef = new ModuleDef {
      make[HomeService[F]].from[HomeService.Impl[F]]
      make[UserService[F]].from[UserService.Impl[F]]
      make[PhotoService[F]].from[PhotoService.Impl[F]]
      make[WechatMiniService[F]].from[WechatMiniService.Impl[F]]
      make[UserApi[F]]
      many[HttpApi[F]].weak[UserApi[F]]
      makeTrait[Http4sDsl[F]]

      addImplicit[Async[F]]
      make[HttpServer].fromResource[HttpServer.Impl[F]]
    }

    def repos[F[_]: {TagK, Concurrent}]: ModuleDef = new ModuleDef {
      tag(Repo.Dummy)
      make[UserRepository[F]].fromResource[UserRepository.Dummy[F]]
      addImplicit[Concurrent[F]]

      make[LogHandler[F]].from[SQLLogHandler.Impl[F]]
//      make[SQL[F]].from[SQL.Impl[F]]
//      make[Transactor[F]].fromResource[TransactorResource[F]]
//      make[PortCheck].from(new PortCheck(3.seconds))
    }

    def configs[F[_]: TagK]: ModuleDef = new ModuleDef {
      make[PostgresCfg].fromValue {
        ConfigSource.default.at("postgres").loadOrThrow[PostgresCfg]
      }
    }

    def utils[F[_]: {TagK, Sync}]: ModuleDef = new ModuleDef {
      make[Logger[F]].from(Slf4jLogger.getLogger[F])
    }
  }
}

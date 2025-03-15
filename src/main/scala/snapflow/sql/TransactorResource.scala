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

package snapflow.sql

import scala.concurrent.ExecutionContext

import cats.effect.{ Async, Sync }
import distage.{ Id, Lifecycle }
import doobie.hikari.HikariTransactor
import doobie.util.log.LogHandler
import izumi.distage.model.provisioning.IntegrationCheck
import izumi.fundamentals.platform.integration.{ PortCheck, ResourceCheck }
import snapflow.config.*

final class TransactorResource[F[_]: Async](
  cfg: PostgresCfg,
  portCheck: PortCheck,
  blockingExecutionContext: ExecutionContext @Id("io"),
  logHandler: LogHandler[F]
) extends Lifecycle.OfCats(
      HikariTransactor.newHikariTransactor(
        driverClassName = cfg.jdbcDriver,
        url = cfg.substitute(cfg.url),
        user = cfg.user,
        pass = cfg.password,
        connectEC = blockingExecutionContext,
        logHandler = Some(logHandler)
      )
    )
    with IntegrationCheck[F] {

  override def resourcesAvailable(): F[ResourceCheck] = Sync[F].delay {
    portCheck.checkPort(
      cfg.host,
      cfg.port,
      s"Couldn't connect to postgres at host=${cfg.host} port=${cfg.port}"
    )
  }
}

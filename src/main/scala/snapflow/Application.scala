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

package snapflow

import cats.effect.{ ExitCode, IO, IOApp }
import distage.{ Activation, Injector, Roots }
import izumi.distage.model.definition.Module
import izumi.functional.lifecycle.Lifecycle
import snapflow.module.AppModule
import snapflow.service.WxMiniMaService

object Application extends IOApp.Simple {
  private val injector       = Injector[IO]()
  private val module: Module = AppModule.apply[IO]
  private val plan           = injector.plan(module, Roots.Everything, Activation.empty).getOrThrow()

  override val run: IO[Unit] = {
    val locatorLifeCycle = for {
      locator <- injector.produce(plan)
    } yield locator

    locatorLifeCycle.use { locator =>
      val server    = locator.get[HttpServer]
      val wxService = locator.get[WxMiniMaService]
      IO.println(s"Server Has Started at ${server.server.address}") *>
        IO.println(f"Start server with WxConfig:{wxService.underlying.getWxMaConfig.toString}")
        *> IO.never
    }.as(ExitCode.Success)
  }

}

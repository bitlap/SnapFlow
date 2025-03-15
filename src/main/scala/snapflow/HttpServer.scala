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

import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

import com.comcast.ip4s.Port

import cats.effect.*
import cats.syntax.all.*
import fs2.io.net.Network
import izumi.distage.model.definition.Lifecycle
import snapflow.api.HttpApi

final case class HttpServer(
  server: Server
)

object HttpServer {

  final class Impl[F[_]](
    allHttpApis: Set[HttpApi[F]]
  )(implicit
    async: Async[F]
  ) extends Lifecycle.Of[F, HttpServer](
        Lifecycle.fromCats {
          val combinedApis = allHttpApis.map(_.http).toList.foldK

          EmberServerBuilder
            .default(using async, Network.forAsync)
            .withHttpApp(combinedApis.orNotFound)
            .withPort(Port.fromInt(8080).get)
            .build
            .map(HttpServer(_))
        }
      )

}

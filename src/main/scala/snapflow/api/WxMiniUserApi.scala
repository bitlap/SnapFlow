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

package snapflow.api

import org.http4s.HttpRoutes
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl

import cats.effect.Concurrent
import cats.syntax.all.*
import io.circe.syntax.*
import snapflow.model.UserProfile
import snapflow.service.WxMiniUserService

final class WxMiniUserApi[F[_]: Concurrent](
  dsl: Http4sDsl[F[_]],
  userService: WxMiniUserService[F]
) extends HttpApi[F] {

  import dsl.*

  override def http: HttpRoutes[F[_]] = {
    HttpRoutes.of { case GET -> Root / "profile" / userId =>
      Ok(userService.getUserById(userId).map(_.asJson))
    }
  }
}

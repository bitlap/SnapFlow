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

package snapflow.service

import org.typelevel.log4cats.Logger

import cats.effect.Async
import cats.syntax.all.*
import snapflow.config.PostgresCfg
import snapflow.model.UserProfile
import snapflow.repo.UserRepository

trait UserService[F[_]]:
  def getUserById(userId: String): F[Option[UserProfile]]

object UserService {

  final class Impl[F[_]: Async](
    wechatService: WechatService[F],
    userRepository: UserRepository[F],
    cfg: PostgresCfg,
    log: Logger[F]
  ) extends UserService[F] {

    override def getUserById(userId: String): F[Option[UserProfile]] =
      log.info(userId + "config:" cfg) *> userRepository.getUserById(userId)

  }
}

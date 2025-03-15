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

import cats.effect.Async
import cats.syntax.applicativeError.*
import doobie.free.connection.ConnectionIO
import doobie.util.transactor.Transactor
import snapflow.model.QueryFailure

trait SQL[F[_]] {
  def execute[A](queryName: String)(conn: ConnectionIO[A]): F[A]
}

object SQL {

  final class Impl[F[_]: Async](
    transactor: Transactor[F]
  ) extends SQL[F] {

    override def execute[A](queryName: String)(conn: ConnectionIO[A]): F[A] = {
      transactor.trans
        .apply(conn)
        .handleErrorWith(ex => Async[F].raiseError(QueryFailure(queryName, ex)))
    }
  }
}

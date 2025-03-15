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

package snapflow.repo

import scala.util.Random

import org.typelevel.log4cats.Logger

import cats.Monad
import cats.effect.{ Concurrent, Ref }
import cats.syntax.all.*
import distage.Lifecycle
import doobie.generic.auto.*
import doobie.postgres.implicits.*
import doobie.syntax.string.*
import snapflow.model.*
import snapflow.sql.SQL

trait UserRepository[F[_]] {
  def getUserById(userId: String): F[Option[UserProfile]]

  def updateUser(userId: String, userProfile: UserProfile): F[Unit]
}

object UserRepository {

  final class Dummy[F[_]: Concurrent]
      extends Lifecycle.LiftF[F, UserRepository[F]](for {
        state <- Ref.of(Map("1" -> UserProfile(Random.nextInt(100).toString, Random.nextInt(100).toString)))
      } yield {
        new UserRepository[F] {
          override def getUserById(userId: String): F[Option[UserProfile]] =
            state.get.map(_.get(userId))

          override def updateUser(userId: String, userProfile: UserProfile): F[Unit] =
            state.update(m => m ++ Map(userId -> userProfile))
        }
      })

  final class Postgres[F[_]: Monad](
    sql: SQL[F],
    log: Logger[F]
  ) extends Lifecycle.LiftF[F, UserRepository[F]](for {
        _ <- log.info("Creating Profile table")
        _ <- sql.execute("ddl-profiles") {
          sql"""create table if not exists user_profiles (
           |  user_id uuid not null,
           |  name text not null,
           |  description text not null,
           |  primary key (user_id)
           |) without oids
           |""".stripMargin.update.run
        }
      } yield new UserRepository[F] {
        override def updateUser(userId: String, profile: UserProfile): F[Unit] = {
          sql
            .execute("update-user-profile") {
              sql"""insert into use_profiles (user_id, name, description)
               |values ($userId, ${profile.name}, ${profile.description})
               |on conflict (user_id) do update set
               |  name = excluded.name,
               |  description = excluded.description
               |""".stripMargin.update.run
            }
            .void
        }

        override def getUserById(userId: String): F[Option[UserProfile]] = {
          sql.execute("get-user-profile") {
            sql"""select name, description from user_profiles
             |where user_id = $userId
             |""".stripMargin.query[UserProfile].option
          }
        }
      })
}

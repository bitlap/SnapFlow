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

import java.io.File

import cats.effect.*
import cats.syntax.all.*
import cn.binarywang.wx.miniapp.api.WxMaService
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder
import snapflow.model.wxmini.*

trait WxMiniService[F[_]]:

  def wxUserSafelyOps[A](wxCall: () => A): F[A]

  def createQrCodeUnlimt(scene: String, page: String): F[File]

  def sessionInfo(appid: String, code: String): F[SessionResult]

  def switchover(appid: String): F[Unit]

  def checkUserInfo(sessionKey: String, rawData: String, signature: String): F[Unit]

  def phoneNoInfo(appid: String, code: String): F[PhoneNumberInfo]

  def userInfo(
    appid: String,
    sessionKey: String,
    signature: String,
    rawData: String,
    encryptedData: String,
    iv: String
  ): F[UserInfo]

object WxMiniService {

  final class Impl[F[_]](wxService: WxMiniMaService)(implicit F: Sync[F]) extends WxMiniService[F] {
    private final lazy val wxMaService = wxService.underlying

    override def wxUserSafelyOps[A](wxCall: () => A): F[A] = {
      F.blocking {
        val values_ = wxCall()
        WxMaConfigHolder.remove()
        values_
      }
    }

    override def switchover(appid: String): F[Unit] =
      for {
        isOver <- wxUserSafelyOps(() => wxMaService.switchover(appid))
        _      <- F.raiseWhen(!isOver)(new IllegalArgumentException(s"未找到对应appid=[$appid]的配置，请核实！"))
      } yield ()

    override def checkUserInfo(sessionKey: String, rawData: String, signature: String): F[Unit] =
      for {
        check <- wxUserSafelyOps(() => wxMaService.getUserService.checkUserInfo(sessionKey, rawData, signature))
        _     <- F.raiseWhen(!check)(new IllegalArgumentException(s"用户信息检查失败！"))
      } yield ()

    override def createQrCodeUnlimt(scene: String, page: String): F[File] = {
      for {
        file <- F.delay(wxMaService.getQrcodeService.createWxaCodeUnlimit(scene, page))
      } yield file
    }

    override def sessionInfo(appid: String, code: String): F[SessionResult] = {
      for {
        _             <- switchover(appid)
        sessionResult <- wxUserSafelyOps(() => wxMaService.getUserService.getSessionInfo(code))
      } yield SessionResult(sessionResult)
    }

    override def userInfo(
      appid: String,
      sessionKey: String,
      signature: String,
      rawData: String,
      encryptedData: String,
      iv: String
    ): F[UserInfo] = {
      for {
        _        <- switchover(appid)
        _        <- checkUserInfo(sessionKey, rawData, signature)
        userInfo <- wxUserSafelyOps(() => wxMaService.getUserService.getUserInfo(sessionKey, encryptedData, iv))
      } yield UserInfo(userInfo)
    }

    override def phoneNoInfo(appid: String, code: String): F[PhoneNumberInfo] =
      for {
        _               <- switchover(appid)
        phoneNumberInfo <- wxUserSafelyOps(() => wxMaService.getUserService.getPhoneNumber(code))

      } yield PhoneNumberInfo(phoneNumberInfo)

  }
}

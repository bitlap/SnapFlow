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

import scala.jdk.CollectionConverters.*

import cats.effect.*
import cats.syntax.all.*
import cn.binarywang.wx.miniapp.api.WxMaService
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl
import izumi.distage.model.definition.Lifecycle
import snapflow.HttpServer
import snapflow.config.WxMiniCfg

final case class WxMiniMaService(underlying: WxMaService)

object WxMiniMaService {

  private def createWxServiceImpl(wxConfig: WxMiniCfg): WxMaServiceImpl = {
    val maService = new WxMaServiceImpl
    maService.setMultiConfigs(
      wxConfig.configs
        .map(a =>
          val config = new WxMaDefaultConfigImpl
          config.setAppid(a.appid)
          config.setSecret(a.secret)
          config.setToken(a.token)
          config.setAesKey(a.aesKey)
          config.setMsgDataFormat(a.msgDataFormat)
          config
        )
        .map(c => c.getAppid -> c)
        .toMap
        .asJava
    )
    maService
  }

  final class Impl[F[_]](wxConfig: WxMiniCfg)(implicit F: Sync[F])
      extends Lifecycle.Of[F, WxMiniMaService](
        Lifecycle.fromCats {
          Resource.eval(F.blocking(WxMiniMaService(createWxServiceImpl(wxConfig))))
        }
      )

}

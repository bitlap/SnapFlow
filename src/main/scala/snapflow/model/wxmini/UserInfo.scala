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

package snapflow.model.wxmini

import cn.binarywang.wx.miniapp.bean.Watermark
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo

final case class UserInfo(
  nickName: String,
  gender: String,
  language: String,
  city: String,
  province: String,
  country: String,
  avatarUrl: String,
  unionId: Option[String],
  watermark: Option[WaterMark]
)

object UserInfo:

  def apply(jxObject: WxMaUserInfo): UserInfo =
    new UserInfo(
      jxObject.getNickName,
      jxObject.getGender,
      jxObject.getLanguage,
      jxObject.getCity,
      jxObject.getProvince,
      jxObject.getCountry,
      jxObject.getAvatarUrl,
      Option(jxObject.getUnionId),
      WaterMark(jxObject.getWatermark)
    )

package com.advertisement.cashcow.common.network.bean.wx

import com.advertisement.cashcow.common.base.BaseApiBean

/**
 * 作者：Administrator on 2018/7/10 0010
 * 描述：
 * 接口：
 */
class WxUserInfoApiBean : BaseApiBean() {


    /**
     * openid : 普通用户的标识，对当前开发者帐号唯一
     * nickname : 普通用户昵称
     * sex : 普通用户性别，1为男性，2为女性
     * province : 普通用户个人资料填写的省份
     * city : 普通用户个人资料填写的城市
     * country : 国家，如中国为CN
     * headimgurl : 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
     * privilege : 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
     * unionid :  用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     *
     */

    var openid: String? = null
    var nickname: String? = null
    var sex: Int = 0
    var province: String? = null
    var city: String? = null
    var country: String? = null
    var headimgurl: String? = null
    var unionid: String? = null
    var privilege: List<String>? = null
}

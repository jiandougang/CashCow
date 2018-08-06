package com.advertisement.cashcow.common.network.bean.wx

import com.advertisement.cashcow.common.base.BaseApiBean

/**
 * 作者：吴国洪 on 2018/7/10
 * 描述：微信授权登陆返回access_token数据实体
 */
class WxAccessTokenApiBean : BaseApiBean() {


    /**
     * access_token : 接口调用凭证
     * expires_in : access_token接口调用凭证超时时间，单位（秒）
     * refresh_token : 用户刷新access_token
     * openid : 授权用户唯一标识
     * scope : 用户授权的作用域，使用逗号（,）分隔
     * unionid : 当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
     */

    var access_token: String? = null
    var expires_in: Int = 0
    var refresh_token: String? = null
    var openid: String? = null
    var scope: String? = null
    var unionid: String? = null
}

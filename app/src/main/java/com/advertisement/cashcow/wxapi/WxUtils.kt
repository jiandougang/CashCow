package com.advertisement.cashcow.wxapi

import android.content.Context
import android.content.Intent
import com.advertisement.cashcow.common.broadcast.WxBroadcastReceiver
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.LoginByPasswordApi
import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import com.advertisement.cashcow.config.Constant
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import rx.schedulers.Schedulers


/**
 * Author     wildma
 * DATE       2017/07/16
 */
class WxUtils {

    companion object {
        var iwxapi: IWXAPI? = null
        var WEIXIN_APP_ID = "wx34cf01600caaa4c3"
        var WEIXIN_APP_SECRET = "8396e0a61c8d78eb5e46a25929c0b5af"
        var WEIXIN_APP_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token"
        var WEIXIN_APP_REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token"
        var WEIXIN_APP_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo"

        fun getIWxApiInstance(context: Context): IWXAPI {
            if (iwxapi == null) {
                iwxapi = WXAPIFactory.createWXAPI(context, WEIXIN_APP_ID, true)
                iwxapi?.registerApp(WEIXIN_APP_ID)
            }
            return iwxapi!!
        }


        /**
         * 第三方微信登录处理
         *
         * @param code 用户或取access_token的code，仅在ErrCode为0时有效
         * @description
         * 1、获取临时票据code
         * 2、获取access_token & openid
         * 3、检查access_token是否有效
         * 4、刷新或续期access_token
         * 5、获取微信用户详细信息
         *
         * 3、4步暂时不做处理
         */
        fun requestRegister(context: Context, code: String) {
            val intent = Intent()
            intent.action = WxBroadcastReceiver.BroadcastName


            val parameters: MutableMap<String, String> = java.util.HashMap()

            parameters["appid"] = WEIXIN_APP_ID
            parameters["secret"] = WEIXIN_APP_SECRET
            parameters["code"] = code
            parameters["grant_type"] = "authorization_code"


            val novate = NetworkConfig.getInstance(context)

            val wxApi = novate.create(WxEntryApi::class.java)
            val loginApi = novate.create(LoginByPasswordApi::class.java)

            novate.call(wxApi.requestWxAccessToken(WEIXIN_APP_ACCESS_TOKEN_URL, parameters)
                    .subscribeOn(Schedulers.io())
//                    .flatMap { t ->
//                        LogUtils.d(t.toString())
//                        parameters.clear()
//                        parameters["appid"] = WEIXIN_APP_ID
//                        parameters["grant_type"] = "refresh_token"
//                        parameters["refresh_token"] = t.refresh_token.toString()
//
//                        wxApi.requestWxRefreshToken(WEIXIN_APP_REFRESH_TOKEN_URL, parameters)
//
//                    }
                    .flatMap { t ->
                        LogUtils.d(t.toString())
                        parameters.clear()
                        parameters["access_token"] = t.access_token.toString()
                        parameters["openid"] = t.openid.toString()

                        wxApi.requestWxUserInfo(WEIXIN_APP_USER_INFO_URL, parameters)

                    }
                    .flatMap { t ->
                        LogUtils.d(t.toString())
                        parameters.clear()
                        parameters["openid"] = t.openid.toString()
                        val sex = if (t.sex == 0)
                            "男"
                        else
                            "女"
                        parameters["sex"] = sex
                        parameters["nickname"] = t.nickname.toString()
                        parameters["avatar"] = t.headimgurl.toString()

                        loginApi.requestRegister(parameters)

                    }

                    , object : BaseSubscriber<LoginByPasswordApiBean>(context) {

                override fun onStart() {
                    intent.putExtra(WXEntryActivity.WeiXin_Broadcast_Callback_Type, WXEntryActivity.WeiXin_Loading)
                    intent.putExtra(WXEntryActivity.WeiXin_Loading, code)
                    context.sendBroadcast(intent)
                }

                override fun onNext(t: LoginByPasswordApiBean) {
                    LogUtils.d(t.toString())
                    //0代表未注册，2代表已注册
                    if ("0" == t.resultCode ||
                            "2" == t.resultCode) {
                        CacheConfigUtils.getConfigCacheInstance(context).put(Constant.cache_user_info, t)
                        intent.putExtra(WXEntryActivity.WeiXin_Broadcast_Callback_Type, WXEntryActivity.WeiXin_Callback_Success)
                        intent.putExtra(WXEntryActivity.WeiXin_Callback_Success, t.resultMsg)

                    }else{
                        intent.putExtra(WXEntryActivity.WeiXin_Broadcast_Callback_Type, WXEntryActivity.WeiXin_Callback_Fail)
                        intent.putExtra(WXEntryActivity.WeiXin_Callback_Fail, t.resultMsg)
                    }

                    context.sendBroadcast(intent)
                }

                override fun onError(e: Throwable?) {
                    LogUtils.e("onError:${e.toString()}")
                    intent.putExtra(WXEntryActivity.WeiXin_Broadcast_Callback_Type, WXEntryActivity.WeiXin_Callback_Fail)
                    intent.putExtra(WXEntryActivity.WeiXin_Callback_Fail, e.toString())
                    context.sendBroadcast(intent)
                }

                override fun onCompleted() {
                    intent.putExtra(WXEntryActivity.WeiXin_Broadcast_Callback_Type, WXEntryActivity.WeiXin_Loading_Complete)
                    intent.putExtra(WXEntryActivity.WeiXin_Loading_Complete, "")

                    context.sendBroadcast(intent)
                }

            })
        }

    }


}

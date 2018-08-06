package com.advertisement.cashcow.wxapi


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.broadcast.WxBroadcastReceiver
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * 作者：吴国洪 on 2018/7/04
 * 描述：微信授权回调页面
 */
class WXEntryActivity : Activity(), IWXAPIEventHandler {

    companion object {
        const val WeiXin_Authorization = "WeiXin_Authorization"//授权
        const val WeiXin_Loading = "WeiXin_Loading"
        const val WeiXin_Loading_Complete = "WeiXin_Loading_Complete"
        const val WeiXin_Callback_Success = "WeiXin_Callback_Success"
        const val WeiXin_Callback_Fail = "WeiXin_Callback_Fail"

        const val WeiXin_Broadcast_Callback_Type = "WeiXin_Broadcast_Callback_Type"//广播回调种类
    }

    private var wxAPI: IWXAPI? = null
    private val RETURN_MSG_TYPE_LOGIN = 1
    private val RETURN_MSG_TYPE_SHARE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_empty)
        wxAPI = WxUtils.getIWxApiInstance(this)
        //如果没回调onResp，八成是这句没有写
        wxAPI!!.handleIntent(intent, this)
        LogUtils.d("WXEntryActivity onCreate")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        wxAPI!!.handleIntent(getIntent(), this)
        LogUtils.d("WXEntryActivity onNewIntent")
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    override fun onReq(arg0: BaseReq) {
        LogUtils.d("WXEntryActivity onReq:$arg0")
    }


    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    override fun onResp(resp: BaseResp) {
        LogUtils.d("onResp:------>")
        LogUtils.d("error_code:---->" + resp.errCode)

        when (resp.errCode) {
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                ToastUtils.showShort("拒绝授权微信登录")
            }

            BaseResp.ErrCode.ERR_USER_CANCEL -> {

                when (resp.type) {
                    RETURN_MSG_TYPE_SHARE -> {
                        ToastUtils.showShort("取消了微信分享")
                    }
                    RETURN_MSG_TYPE_LOGIN -> {
                        ToastUtils.showShort("取消了微信登录")
                    }
                }
            }

            BaseResp.ErrCode.ERR_OK -> {
                when (resp.type) {
                    ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> {
                        LogUtils.d("微信分享操作.....")
                    }

                    ConstantsAPI.COMMAND_SENDAUTH -> {
                        LogUtils.d("微信登录操作.....")

                        //拿到了微信返回的code,立马再去请求access_token
                        val code = (resp as SendAuth.Resp).code
                        LogUtils.d("code:------>" + code)

                        //这里拿到了这个code，去做2次网络请求获取access_token和用户个人信息
                        val intent = Intent()
                        intent.action = WxBroadcastReceiver.BroadcastName
                        intent.putExtra(WXEntryActivity.WeiXin_Broadcast_Callback_Type, WXEntryActivity.WeiXin_Authorization)
                        intent.putExtra(WeiXin_Authorization, code)
                        sendBroadcast(intent)

                    }
                }
            }
        }

        finish()
    }
}


package com.advertisement.cashcow.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.advertisement.cashcow.wxapi.WXEntryActivity

/**
 * 作者：吴国洪 on 2018/7/9
 * 描述：接收微信授权回调发送的信息
 */
class WxBroadcastReceiver(private val receiverCallBack: ReceiverCallBack) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        receiverCallBack.callBack(intent.getStringExtra(WXEntryActivity.WeiXin_Broadcast_Callback_Type), intent)
    }

    interface ReceiverCallBack {

        /**
         * 根据不同Wx返回的回调类型进行判断处理
         */
        fun callBack(callbackType: String, intent: Intent)
    }

    companion object {

        var BroadcastName = WxBroadcastReceiver::class.java.name!!
    }
}

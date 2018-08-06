package com.advertisement.cashcow.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * 作者：吴国洪 on 2018/7/24
 * 描述：用于跨页面收发通知用
 */
class NormalBroadcastReceiver(private val receiverCallBack: ReceiverCallBack) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        receiverCallBack.callBack(intent)
    }

    interface ReceiverCallBack {


        fun callBack(intent: Intent)
    }

    companion object {
        var BroadcastName = NormalBroadcastReceiver::class.java.name!!
    }
}

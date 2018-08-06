package com.advertisement.cashcow.common.network

import android.content.Context

import com.tamic.novate.Novate

/**
 * 作者：吴国洪 on 2018/6/19
 * 描述：接口公共配置
 */
object NetworkConfig {

    var Base_Url = "http://192.168.5.100:8080/moneyTree/"
    /**
     * 分享邀请好友链接
     */
    var Invite_Friends_Url = "http://192.168.5.100:8080/moneyTree/pages/system/h5/login.html?feedbackcode="


    fun getInstance(context: Context): Novate {
        return Novate.Builder(context)
                .baseUrl(Base_Url)
                .addLog(true)
                .addCache(false)
                //                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //                .addConverterFactory(GsonConverterFactory.create())
                .build()

    }
}

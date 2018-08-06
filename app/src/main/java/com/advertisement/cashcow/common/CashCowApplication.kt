package com.advertisement.cashcow.common

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.advertisement.cashcow.config.UtilsConfig


/**
 * 作者：吴国洪 on 2018/5/30
 * 描述：应用入口
 */
class CashCowApplication : Application() {



    override fun onCreate() {
        super.onCreate()

        UtilsConfig.start(this)

    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }


}

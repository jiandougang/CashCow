package com.advertisement.cashcow.util

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

/**
 * 作者：Administrator on 2018/6/27 0027
 * 描述：防止InputMethodManager内存泄漏
 */
class DummyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Handler().postDelayed({
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
        }, 500)
    }

}

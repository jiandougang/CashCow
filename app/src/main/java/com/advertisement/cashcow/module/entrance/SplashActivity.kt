package com.advertisement.cashcow.module.entrance

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.advertisement.cashcow.R
import com.advertisement.cashcow.module.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*


/**
 * 作者：吴国洪 on 2018/7/21
 * 描述：APP广告入口
 */
class SplashActivity : AppCompatActivity() ,Runnable{

    override fun run() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        iv_bg.postDelayed(this,1500)

    }
}

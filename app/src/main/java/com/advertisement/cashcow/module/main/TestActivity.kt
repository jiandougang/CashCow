package com.advertisement.cashcow.module.main


import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.advertisement.cashcow.R
import com.advertisement.cashcow.thirdLibs.rxretrofitlibrary.retrofit_rx.download.DownInfo
import com.advertisement.cashcow.thirdLibs.rxretrofitlibrary.retrofit_rx.download.DownState
import com.advertisement.cashcow.thirdLibs.rxretrofitlibrary.retrofit_rx.download.HttpDownManager
import com.advertisement.cashcow.thirdLibs.rxretrofitlibrary.retrofit_rx.listener.HttpDownOnNextListener
import com.advertisement.cashcow.thirdLibs.rxretrofitlibrary.retrofit_rx.utils.DbDownUtil
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import kotlinx.android.synthetic.main.activity_simple_main.*
import java.io.File

/**
 * @author   www.yaoxiaowen.com
 * time:  2017/12/20 20:23
 * @since 1.0.0
 */
class TestActivity : AppCompatActivity(), View.OnClickListener {
    private var apkApi: DownInfo? = null
    private var manager: HttpDownManager? = null
    private var dbUtil: DbDownUtil? = null
    private var listData: List<DownInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_main)

        deleteAllBtn.setOnClickListener(this)

        try {
            //检测是否有写的权限
            val permission = ActivityCompat.checkSelfPermission(this,
                    PermissionConstants.getPermissions(PermissionConstants.STORAGE)[1])
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PermissionConstants.getPermissions(PermissionConstants.STORAGE),1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        dbUtil = DbDownUtil.getInstance()
        listData = dbUtil!!.queryDownAll()

        manager = HttpDownManager.getInstance()

            val outputFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "test.mp4")
            apkApi = DownInfo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
            apkApi!!.id = 1
            apkApi!!.isUpdateProgress = true
            apkApi!!.savePath = outputFile.absolutePath
            apkApi!!.listener = object : HttpDownOnNextListener<Any?>() {
                override fun onComplete() {
                    Toast.makeText(this@TestActivity, "提示：下载结束", Toast.LENGTH_SHORT).show()

                }

                override fun updateProgress(readLength: Long, countLength: Long) {
                    LogUtils.d(readLength)
                    firstTitle.text = readLength.toInt().toString()
                    firstProgressBar.max = countLength.toInt()
                    firstProgressBar.progress = readLength.toInt()
                }

                override fun onNext(t: Any?) {
                }

                override fun onStart() {
                    LogUtils.d(1111)

                }
            }

    }


    override fun onDestroy() {
        super.onDestroy()
        /*记录退出时下载任务的状态-复原用*/
        for (downInfo in listData!!) {
            dbUtil!!.update(downInfo)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.deleteAllBtn -> {
                if (apkApi!!.state !== DownState.FINISH) {
                    manager!!.startDown(apkApi)
                }
            }

        }

    }


    companion object {


    }
}

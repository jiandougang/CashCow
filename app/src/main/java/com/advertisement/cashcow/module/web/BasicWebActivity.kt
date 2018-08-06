package com.advertisement.cashcow.module.web

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseActivity
import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import com.advertisement.cashcow.module.main.detail.DetailsContract
import com.advertisement.cashcow.module.main.detail.DetailsPresenter
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.NetworkUtils
import kotlinx.android.synthetic.main.activity_webview.*

/**
 * 作者：吴国洪 on 2018/6/15
 * 描述：第三方网页 activity
 */
class BasicWebActivity : BaseActivity(), DetailsContract.View {

    private var userInfo: LoginByPasswordApiBean? = null

    private val mPresenter by lazy { DetailsPresenter(BasicWebActivity.javaClass.name) }

    private var isContinue = false
    private var url: String = ""
    private var advertisementId: String = ""
    private var advertisementDuration:Int = 0

    override fun initData() {
        mPresenter.attachView(this)
        url = intent.getStringExtra(LoadURL)
        advertisementId = intent.getStringExtra(AdvertisementId)
        advertisementDuration = intent.getIntExtra(AdvertisementDuration,0)

        userInfo = CacheConfigUtils.parseUserInfo(this)

    }

    /**
     * 初始化 View
     */
    override fun initView() {
        initWebSetting()
        nv_bar.setOnBackClickListener { onBackPressed() }
        //点击重新连接网络
        tv_center_badnet.setOnClickListener {
            tv_center_badnet.visibility = View.INVISIBLE
            //重新加载网页
            webView.reload()
        }
    }

    /**
     *  加载布局
     */
    override fun layoutId(): Int {
        return R.layout.activity_webview
    }

    override fun onBackPressedSupport() {
        if (webView.canGoBack()) {
            webView.goBack()
            top_progress.stopAllAnimation()
        } else super.onBackPressedSupport()

    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebSetting() {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = false
        //正常网络流程
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                //如果没有网络直接跳出方法
                if (!NetworkUtils.isConnected()) {
                    return
                }
                //如果进度条隐藏则让它显示
                if (View.INVISIBLE == top_progress.visibility) {
                    top_progress!!.visibility = View.VISIBLE
                }
                //大于80的进度的时候,放慢速度加载,否则交给自己加载
                if (newProgress >= 80) {
                    //拦截webView自己的处理方式
                    if (isContinue) {
                        return
                    }
                    top_progress!!.setCurProgress(100, 3000) {
                        finishOperation(true)
                        isContinue = false
                    }

                    isContinue = true
                } else {
                    top_progress.setNormalProgress(newProgress)
                }

            }

            //获取Web页中的标题
            override fun onReceivedTitle(view: WebView?, title: String?) {
//                if (title != null) {
//                    nv_bar.setTitle(title)
//                }
            }
        }
        webView!!.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                top_progress.clearAnimation()

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                mPresenter.requestViewEvent(this@BasicWebActivity, userInfo?.resultData?.id.toString(), advertisementId, advertisementDuration)

                top_progress.clearAnimation()
            }

            //https的处理方式
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }

            //错误页面的逻辑处理
            override fun onReceivedError(view: WebView, errorCode: Int,
                                         description: String, failingUrl: String) {
                errorOperation()
            }
        }
        //开始加载
        webView.loadUrl(url)
    }


    /**
     * 错误的时候进行的操作
     */
    private fun errorOperation() {
        //隐藏webview
        webView.visibility = View.INVISIBLE

        if (View.INVISIBLE == top_progress.visibility) {
            top_progress.visibility = View.VISIBLE
        }
        //3.5s 加载 0->80 进度的加载 为了实现,特意调节长了事件
        top_progress!!.setCurProgress(80, 3500) {
            //3.5s 加载 80->100 进度的加载
            top_progress!!.setCurProgress(100, 3500) {
                finishOperation(false)
            }
        }
    }

    /**
     * 结束进行的操作
     */
    private fun finishOperation(flag: Boolean) {
        //最后加载设置100进度
        top_progress!!.setNormalProgress(100)
        //显示网络异常布局
        tv_center_badnet!!.visibility = if (flag) View.INVISIBLE else View.VISIBLE

        top_progress.visibility = View.INVISIBLE
    }

    override fun handleSuccess(type: String, obj: Any) {
    }


    override fun preLoad(type: String, obj: Any) {
    }


    override fun afterLoad(type: String, obj: Any) {
    }


    override fun handleError(type: String, obj: Any) {
    }

    companion object {
        const val LoadURL = "LoadURL"
        const val AdvertisementId = "AdvertisementId"
        const val AdvertisementDuration = "AdvertisementDuration"
    }
}
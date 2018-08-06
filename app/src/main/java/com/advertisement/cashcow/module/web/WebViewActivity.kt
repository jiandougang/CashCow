//package com.advertisement.cashcow.module.web
//
//import android.graphics.Bitmap
//import android.view.ViewGroup
//import android.webkit.WebChromeClient
//import android.webkit.WebView
//import android.webkit.WebViewClient
//import com.advertisement.cashcow.R
//import com.advertisement.cashcow.common.base.BaseActivity
//import kotlinx.android.synthetic.main.activity_webview.*
//import android.view.animation.Animation
//import com.advertisement.cashcow.widget.progressbar.HProgressBarLoading
//
//
///**
// * 作者：吴国洪 on 2018/6/15
// * 描述：第三方网页 activity
// * 接口：
// */
//class WebViewActivity : BaseActivity() {
//
//    private var animation: Animation? = null
//    /**
//     * activity配置
//     */
//    override fun configSetting() {
//    }
//
//
//    override fun layoutId(): Int {
//        return R.layout.activity_webview
//    }
//
//    override fun initData() {
//
//    }
//
//    override fun initView() {
////        //声明WebSettings子类
////        val webSettings = cwv_content.settings
////        //设置自适应屏幕，两者合用
////        webSettings.useWideViewPort = true //将图片调整到适合webView的大小
////        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
////
////        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
////        webSettings.javaScriptEnabled = true
////
////        //缩放操作
////        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
////        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
////        webSettings.displayZoomControls = false //隐藏原生的缩放控件
////
////        webSettings.loadsImagesAutomatically = true//支持自动加载图片
////        webSettings.defaultTextEncodingName = "utf-8"//设置编码格式
////
////        webSettings.setAppCacheMaxSize(8 * 1024 * 1024)   //缓存最多可以有8M
////        webSettings.allowFileAccess = true   // 可以读取文件缓存(manifest生效)
////
////        if (NetworkUtils.isConnected()) {
////            webSettings.cacheMode = WebSettings.LOAD_DEFAULT//根据cache-control决定是否从网络上取数据。
////        } else {
////            webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK//没网，则从本地获取，即离线加载
////        }
////
////        webSettings.domStorageEnabled = true // 开启 DOM storage API 功能
////        webSettings.databaseEnabled = true   //开启 database storage API 功能
////        webSettings.setAppCacheEnabled(true)//开启 Application Caches 功能
////
////        val appCacheDir = this.applicationContext.getDir("webCache", Context.MODE_PRIVATE).path
////        webSettings.setAppCachePath(appCacheDir) //设置  Application Caches 缓存目录
////
////        cwv_content.loadUrl("http://www.baidu.com/")
////        // 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
////        cwv_content.webViewClient = object : WebViewClient() {
////            //设置加载前的函数
////            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
////                println("开始加载了")
////
////            }
////
////            //设置结束加载函数
////            override fun onPageFinished(view: WebView, url: String) {
////                println("结束加载了")
////            }
////
////            //设置不用系统浏览器打开,直接显示在当前WebView
////            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
////                view.loadUrl(url)
////                return true
////            }
////
////
////        }
////
////        cwv_content.webChromeClient = object : WebChromeClient() {
////            //            //获得网页的加载进度并显示
////            override fun onProgressChanged(view: WebView?, newProgress: Int) {
//////                if (newProgress < 100) {
////////                    String progress = newProgress + "%";
////////                    progress.setText(progress);
//////                    np_progress.progress = newProgress
////                    Log.i("1111", "progress:$newProgress")
////                if (newProgress < 100) {
////                    if (np_progress.visibility == View.GONE)
////                        np_progress.visibility = View.VISIBLE
////                    np_progress.progress = newProgress
////                } else {
////                    np_progress.progress = 100
////                    animation = AnimationUtils.loadAnimation(this@WebViewActivity, R.anim.web_view_progress_bar)
//////                    // 运行动画
////                    np_progress.startAnimation(animation)
////                    // 将 spinner 的可见性设置为不可见状态
////                    np_progress.visibility = View.INVISIBLE
////                }
////                super.onProgressChanged(view, newProgress)
////            }
////
////            //获取Web页中的标题
////            override fun onReceivedTitle(view: WebView?, title: String?) {
////
////            }
////        }
//
//    }
//
//    override fun onDestroy() {
//
//        cwv_content.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
//        cwv_content.clearHistory()
//
//        (cwv_content.parent as ViewGroup).removeView(cwv_content)
//        cwv_content.destroy()
//        super.onDestroy()
//
//    }
//
//    override fun onBackPressedSupport() {
//        if (cwv_content.canGoBack()) {
//            cwv_content.goBack()
//        } else super.onBackPressedSupport()
//    }
//}
//
//
//
//
//

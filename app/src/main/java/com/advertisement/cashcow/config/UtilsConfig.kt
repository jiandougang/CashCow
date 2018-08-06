package com.advertisement.cashcow.config

import android.app.Application
import android.os.StrictMode
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.facebook.drawee.backends.pipeline.Fresco
import com.kk.taurus.playerbase.config.PlayerConfig
import com.kk.taurus.playerbase.config.PlayerLibrary
import me.yokeyword.fragmentation.Fragmentation


/**
 * 作者：吴国洪 on 2018/5/31
 * 描述：Android第三方库配置
 */
object UtilsConfig {


    fun start(context: Application) {

//        if (LeakCanary.isInAnalyzerProcess(context)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return
//        }
//        LeakCanary.install(context)

        // android 7.0系统解决拍照的问题
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()

        PlayerConfig.setUseDefaultNetworkEventProducer(true)
//        val defaultPlanId = 1
//        PlayerConfig.addDecoderPlan(DecoderPlan(defaultPlanId, IjkPlayer::class.simpleName, "IjkPlayer"))
//        PlayerConfig.setDefaultPlanId(defaultPlanId)
        PlayerLibrary.init(context)


        Fresco.initialize(context)


        Utils.init(context)

        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(AppUtils.isAppDebug())
                // 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                .handleException {
                    // 建议在该回调处上传至我们的Crash监测服务器
                    // 以Bugtags为例子: 手动把捕获到的 Exception 传到 Bugtags 后台。
                    // Bugtags.sendException(e);
                }
                .install()


        val logConfig = LogUtils.getConfig()
        logConfig.setLogSwitch(true)   //设置 log 总开关
        logConfig.setConsoleSwitch(true) // 设置 log 控制台开关
        logConfig.setGlobalTag("1111111")
    }
}

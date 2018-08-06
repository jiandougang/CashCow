package com.advertisement.cashcow.module.main.information.messagesCenter

import android.content.Context
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.InformationApi
import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.advertisement.cashcow.common.network.bean.information.MessagesCenterApiBean
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import java.util.*

/**
 * 作者：吴国洪 on 2018/7/18
 * 描述：MessagesCenterFragment逻辑处理类
 */
class MessagesCenterPresenter : BasePresenter<MessagesCenterContract.View>(), MessagesCenterContract.Presenter {
    /**
     * 消息“标识已读”事件
     */
    override fun requestReadedNews(userId: String, newsId: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["newsid"] = newsId
        parameters["userid"] = userId

        val context: Context = (mRootView as MessagesCenterFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(InformationApi::class.java)

        novate.call(api.requestReadedNews(parameters), object : BaseSubscriber<TextApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                (mRootView as MessagesCenterFragment).handleError(InformationApi.requestReadedNews, e.toString())
            }

            override fun onStart() {
                (mRootView as MessagesCenterFragment).preLoad(InformationApi.requestReadedNews, "")
            }

            override fun onNext(t: TextApiBean) {
                LogUtils.d(t.toString())
                if (t.resultCode == "0"){
                    (mRootView as MessagesCenterFragment).handleSuccess(InformationApi.requestReadedNews, t)
                }else{
                    (mRootView as MessagesCenterFragment).handleError(InformationApi.requestReadedNews, t.resultMsg.toString())
                }
            }

            override fun onCompleted() {
                (mRootView as MessagesCenterFragment).afterLoad(InformationApi.requestReadedNews, "")
            }
        })
    }

    override fun requestQueryNewsByUserID(userId: String,page:String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["page"] = page
        parameters["rows"] = InformationApi.REQUEST_COUNT.toString()
        parameters["userid"] = userId

        val context: Context = (mRootView as MessagesCenterFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(InformationApi::class.java)

        novate.call(api.requestQueryNewsByUserID(parameters), object : BaseSubscriber<MessagesCenterApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                (mRootView as MessagesCenterFragment).handleError(InformationApi.requestQueryNewsByUserID, e.toString())
            }

            override fun onStart() {
                (mRootView as MessagesCenterFragment).preLoad(InformationApi.requestQueryNewsByUserID, "")
            }

            override fun onNext(t: MessagesCenterApiBean) {
                LogUtils.d(t.toString())
                (mRootView as MessagesCenterFragment).handleSuccess(InformationApi.requestQueryNewsByUserID, t)
            }

            override fun onCompleted() {
                (mRootView as MessagesCenterFragment).afterLoad(InformationApi.requestQueryNewsByUserID, "")
            }
        })
    }


}

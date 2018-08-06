package com.advertisement.cashcow.module.main.mine.myCollection

import android.content.Context
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.InformationApi
import com.advertisement.cashcow.common.network.bean.information.InformationAdApiBean
import com.advertisement.cashcow.module.main.information.InformationPresenter
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import java.util.*

/**
 * 作者：吴国洪 on 2018/5/28
 * 描述：InformationFragment逻辑处理类
 * 接口：
 */
open class MyCollectionPresenter(type:String) : InformationPresenter(type) {


    fun requestGetStoreAds(userId: String, page: String) {
        val parameters: MutableMap<String, String> = HashMap()
        val context: Context = (mRootView as MyCollectionFragment).context!!

        parameters["page"] = page
        parameters["rows"] = InformationApi.REQUEST_COUNT.toString()
        parameters["userid"] = userId


        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(InformationApi::class.java)

        novate.call(api.requestGetStoreAds(parameters), object : BaseSubscriber<InformationAdApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                (mRootView as MyCollectionFragment).handleError(InformationApi.requestGetStoreAds, e.toString())
            }

            override fun onStart() {
                (mRootView as MyCollectionFragment).preLoad(InformationApi.requestGetStoreAds, "")

            }

            override fun onNext(t: InformationAdApiBean) {
                LogUtils.d(t.toString())
                (mRootView as MyCollectionFragment).handleSuccess(InformationApi.requestGetStoreAds, t)
            }

            override fun onCompleted() {
                (mRootView as MyCollectionFragment).afterLoad(InformationApi.requestGetStoreAds, "")

            }
        })
    }


}

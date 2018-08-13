package com.advertisement.cashcow.module.main.mine.goldCoinsDetail

import android.content.Context
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.bean.mine.GoldCoinsDetailApiBean
import com.advertisement.cashcow.common.network.bean.mine.GoldCoinsDetailStatisticsApiBean
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable

/**
 * 作者：吴国洪 on 2018/8/8
 * 描述：GoldCoinsDetail
 */
open class GoldCoinsDetailPresenter : BasePresenter<GoldCoinsDetailContract.View>(), GoldCoinsDetailContract.Presenter {

    override fun requestCountPktByUserId(context: Context, userId: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["userid"] = userId

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(Api::class.java)

        mRootView as GoldCoinsDetailFragment

        novate.call(api.requestCountPktByUserId(parameters), object : BaseSubscriber<GoldCoinsDetailStatisticsApiBean>(context) {

            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                mRootView!!.handleError(Api.requestCountPktByUserId, e.toString())
            }

            override fun onStart() {
            }

            override fun onNext(t: GoldCoinsDetailStatisticsApiBean) {
                LogUtils.d(t.toString())
                if (t.resultCode == "0") {
                    mRootView!!.handleSuccess(Api.requestCountPktByUserId, t)
                }else{
                    mRootView!!.handleError(Api.requestCountPktByUserId, t.resultMsg.toString())
                }
            }

            override fun onCompleted() {

            }
        })
    }

    override fun requestGetRedPkts(context: Context, userId: String, currentPage: Int, status: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["userid"] = userId
        parameters["page"] = currentPage.toString()
        parameters["rows"] = "10"
        parameters["status"] = status


        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(Api::class.java)

        mRootView as GoldCoinsDetailListFragment

        novate.call(api.requestGetRedPkts(parameters), object : BaseSubscriber<GoldCoinsDetailApiBean>(context) {

            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                mRootView!!.handleError("", e.toString())
            }

            override fun onStart() {
                mRootView!!.preLoad(Api.requestGetRedPkts, "")
            }

            override fun onNext(t: GoldCoinsDetailApiBean) {
                LogUtils.d(t.toString())
                mRootView!!.handleSuccess(Api.requestGetRedPkts, t)
            }

            override fun onCompleted() {
                mRootView!!.afterLoad(Api.requestGetRedPkts, "")

            }
        })
    }

    override fun initAllData() {
    }


}

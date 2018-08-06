package com.advertisement.cashcow.module.main.mine.myBankCard

import android.content.Context
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.api.MyBankCardApi
import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.advertisement.cashcow.common.network.bean.mine.MyBankCardApiBean
import com.advertisement.cashcow.common.network.bean.mine.MyBankCardBindingApiBean
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable

/**
 * 作者：吴国洪 on 2018/7/14
 * 描述：MyBankCardFragment逻辑处理类
 * 接口：
 */
open class MyBankCardsPresenter : BasePresenter<MyBankCardsContract.View>(), MyBankCardsContract.Presenter {

    override fun initBankCardList(): ArrayList<SelectBankCardBean> {
        return arrayListOf(SelectBankCardBean("建设银行", false),
                SelectBankCardBean("招商银行", false),
                SelectBankCardBean("农业银行", false),
                SelectBankCardBean("农商银行", false),
                SelectBankCardBean("民生银行", false),
                SelectBankCardBean("光大银行", false),
                SelectBankCardBean("交通银行", false),
                SelectBankCardBean("中国银行", false),
                SelectBankCardBean("工商银行", false)
        )
    }

    override fun requestUnbindBankCard(bindId: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["bindid"] = bindId

        val context: Context = (mRootView as MyBankCardsFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(MyBankCardApi::class.java)

        novate.call(api.requestUnbindBankCard(parameters), object : BaseSubscriber<TextApiBean>(context) {

            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                (mRootView as MyBankCardsFragment).handleError("", e.toString())
            }

            override fun onStart() {
                (mRootView as MyBankCardsFragment).preLoad(MyBankCardApi.requestUnbindBankCard, "")
            }

            override fun onNext(t: TextApiBean) {
                LogUtils.d(t.toString())

                if (t.resultCode == "0") {
                    (mRootView as MyBankCardsFragment).handleSuccess(MyBankCardApi.requestUnbindBankCard, t)
                } else {
                    (mRootView as MyBankCardsFragment).handleError(MyBankCardApi.requestUnbindBankCard, t.resultMsg.toString())
                }
            }

            override fun onCompleted() {
                (mRootView as MyBankCardsFragment).afterLoad(MyBankCardApi.requestUnbindBankCard, "")

            }

        })
    }

    override fun requestGetBankCards(userId: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["userid"] = userId

        val context: Context = (mRootView as MyBankCardsFragment).context

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(MyBankCardApi::class.java)

        novate.call(api.requestGetBankCards(parameters), object : BaseSubscriber<MyBankCardApiBean>(context) {

            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                (mRootView as MyBankCardsFragment).handleError("", e.toString())
            }

            override fun onStart() {
                (mRootView as MyBankCardsFragment).preLoad(MyBankCardApi.requestGetBankCards, "")
            }

            override fun onNext(t: MyBankCardApiBean) {
                LogUtils.d(t.toString())

                if (t.resultCode == "0") {
                    (mRootView as MyBankCardsFragment).handleSuccess(MyBankCardApi.requestGetBankCards, t)
                } else {
                    (mRootView as MyBankCardsFragment).handleError(MyBankCardApi.requestGetBankCards, t.resultMsg.toString())
                }
            }

            override fun onCompleted() {
                (mRootView as MyBankCardsFragment).afterLoad(MyBankCardApi.requestGetBankCards, "")

            }

        })
    }

    override fun requestBindBandCard(userId: String, bandName: String, account: String, name: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["userid"] = userId
        parameters["bandname"] = bandName
        parameters["account"] = account
        parameters["name"] = name


        val context: Context = (mRootView as MyBankCardsBindingFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val mineApi = novate.create(Api::class.java)

        novate.call(mineApi.requestBindBandCard(parameters), object : BaseSubscriber<MyBankCardBindingApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")

                (mRootView as MyBankCardsBindingFragment).handleError(Api.requestBindBandCard, e.toString())
            }

            override fun onStart() {
                (mRootView as MyBankCardsBindingFragment).preLoad(Api.requestBindBandCard, "")

            }

            override fun onNext(t: MyBankCardBindingApiBean) {
                LogUtils.i(t.toString())
                if ("0" == t.resultCode) {
                    (mRootView as MyBankCardsBindingFragment).handleSuccess(Api.requestBindBandCard, t)
                } else {
                    (mRootView as MyBankCardsBindingFragment).handleError(Api.requestBindBandCard, t.resultMsg.toString())

                }
            }

            override fun onCompleted() {
                (mRootView as MyBankCardsBindingFragment).afterLoad(Api.requestBindBandCard, "")

            }

        })

    }


}

package com.advertisement.cashcow.module.login.register.initPassword

import android.content.Context
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.InitPasswordApi
import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import rx.Observable
import rx.schedulers.Schedulers

/**
 * 作者：吴国洪 on 2018/5/28
 * 描述：InitPasswordFragment逻辑处理类
 */
class InitPasswordPresenter : BasePresenter<InitPasswordContract.View>(), InitPasswordContract.Presenter {


    /**
     * 初请求注册接口
     */
    override fun requestRegister(context: Context, phoneNum: String, password: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["phone"] = phoneNum
        parameters["password"] = password

        mRootView as InitPasswordFragment


        val novate = NetworkConfig.getInstance(context)

        val initPasswordApi = novate.create(InitPasswordApi::class.java)

        novate.call(initPasswordApi.requestIsRegister(parameters)
                .subscribeOn(Schedulers.io())
                .flatMap { t ->
                    LogUtils.i(t.toString())
                    if ("0" != t?.resultCode) {
                        mRootView!!.handleError("", t.resultMsg.toString())
                        Observable.empty()
                    } else {
                        initPasswordApi.requestRegister(parameters)
                    }
                }

                , object : BaseSubscriber<TextApiBean>(context) {

            override fun onStart() {
                mRootView!!.afterLoad(InitPasswordApi.requestRegister, "")

            }

            override fun onNext(t: TextApiBean) {
                LogUtils.i(t.toString())
                if ("0" != t.resultCode) {
                    mRootView!!.handleError(InitPasswordApi.requestRegister, t.resultMsg.toString())
                }else{
                    mRootView!! .handleSuccess(InitPasswordApi.requestRegister, t.resultMsg!!)

                }
            }

            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")
                mRootView!!.handleError("", e.toString())
            }

            override fun onCompleted() {
                mRootView!!.afterLoad(InitPasswordApi.requestRegister, "")

            }

        })
    }


}

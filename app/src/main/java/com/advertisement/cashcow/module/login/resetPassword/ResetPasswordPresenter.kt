package com.advertisement.cashcow.module.login.resetPassword

import android.content.Context
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.api.LoginByVerificationApi
import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*

/**
 * 作者：吴国洪 on 2018/6/26
 * 描述：ResetPasswordFragment逻辑处理类
 */
class ResetPasswordPresenter : BasePresenter<ResetPasswordContract.View>(), ResetPasswordContract.Presenter {

    override fun requestIsOkAndResetPassword(phoneNum: String, password: String, verificationCode:String,vCodeType:String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["phone"] = phoneNum
        parameters["password"] = password
        parameters["vcode"] = verificationCode
        parameters["vcodetype"] = vCodeType


        val context: Context = (mRootView as ResetPasswordFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(Api::class.java)

        novate.call(api.requestIsOk(parameters)
                .subscribeOn(Schedulers.io())
                .flatMap { t ->
                    LogUtils.i(t.toString())
                    if ("0" == t?.resultCode) {
                        api.requestResetPassword(parameters)
                    } else {
                        (mRootView as ResetPasswordFragment).handleError(Api.requestIsOk, t.resultMsg.toString())
                        Observable.empty()
                    }
                }
                , object : BaseSubscriber<TextApiBean>(context) {

            override fun onStart() {
                (mRootView as ResetPasswordFragment).preLoad(Api.requestResetPassword, "")
            }

            override fun onNext(t: TextApiBean) {
                LogUtils.i(t.toString())
                if ("0" != t.resultCode) {
                    (mRootView as ResetPasswordFragment).handleError(Api.requestResetPassword, t.resultMsg.toString())
                } else {
                    (mRootView as ResetPasswordFragment).handleSuccess(Api.requestResetPassword, t.resultMsg.toString())
                }
            }

            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")
                (mRootView as ResetPasswordFragment).handleError(Api.requestResetPassword, e.toString())
            }

            override fun onCompleted() {
                (mRootView as ResetPasswordFragment).afterLoad(Api.requestResetPassword, "")
            }

        })
    }

    override fun requestIsRegisterAndGetVerificationCode(type:String,phoneNum: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["phone"] = phoneNum

        val context: Context = (mRootView as ResetPasswordFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(Api::class.java)

        novate.call(api.requestIsRegister(parameters)
                .subscribeOn(Schedulers.io())
                .flatMap { t ->
                    LogUtils.i(t.toString())
                    if ("0" == t?.resultCode) {
                        (mRootView as ResetPasswordFragment).handleError(LoginByVerificationApi.requestIsRegister, t.resultMsg.toString())
                        Observable.empty()
                    } else {
                        parameters["vcodetype"] = type

                        api.requestGetVerificationCode(parameters)
                    }
                }
                , object : BaseSubscriber<TextApiBean>(context) {

            override fun onStart() {

            }

            override fun onNext(t: TextApiBean) {
                LogUtils.i(t.toString())
                if ("0" != t.resultCode) {
                    (mRootView as ResetPasswordFragment).handleError(Api.requestGetVerificationCode, t.resultMsg.toString())
                } else {
                    (mRootView as ResetPasswordFragment).handleSuccess(Api.requestGetVerificationCode, t.resultMsg.toString())
                }
            }

            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")
                (mRootView as ResetPasswordFragment).handleError("", e.toString())
            }

            override fun onCompleted() {

            }

        })

    }
}

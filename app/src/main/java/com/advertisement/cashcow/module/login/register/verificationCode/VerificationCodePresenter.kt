package com.advertisement.cashcow.module.login.register.verificationCode

import android.content.Context
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.VerificationApi
import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import java.util.*

/**
 * 作者：吴国洪 on 2018/6/20
 * 描述：VerificationFragment逻辑处理类
 */
class VerificationCodePresenter : BasePresenter<VerificationCodeContract.View>(), VerificationCodeContract.Presenter {

    override fun requestGetVerificationCode(phoneNum: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["phone"] = phoneNum

        val context: Context = (mRootView as VerificationCodeFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val verificationApi = novate.create(VerificationApi::class.java)

        novate.call(verificationApi.requestGetVerificationCode(parameters), object : BaseSubscriber<TextApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")
                (mRootView as VerificationCodeFragment).handleError(VerificationApi.requestGetVerificationCode, e.toString())
            }

            override fun onStart() {

            }

            override fun onNext(t: TextApiBean) {
                LogUtils.i(t.toString())
                if ("0" != t.resultCode) {
                    (mRootView as VerificationCodeFragment).handleError(VerificationApi.requestGetVerificationCode, context.getString(R.string.verification_code_sent_unsuccessfully))
                } else {
                    (mRootView as VerificationCodeFragment).handleSuccess(VerificationApi.requestGetVerificationCode, VerificationApi.requestGetVerificationCode)
                }
            }

            override fun onCompleted() {
            }
        })
    }


    override fun requestCheckVerificationCode(phoneNum: String, verificationCode: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["phone"] = phoneNum
        parameters["vcode"] = verificationCode


        val context: Context = (mRootView as VerificationCodeFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val verificationdApi = novate.create(VerificationApi::class.java)

        novate.call(verificationdApi!!.requestCheckVerificationCode(parameters), object : BaseSubscriber<TextApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")

                (mRootView as VerificationCodeFragment).handleError(VerificationApi.requestCheckVerificationCode, e.toString())
            }

            override fun onStart() {
                (mRootView as VerificationCodeFragment).preLoad(VerificationApi.requestCheckVerificationCode, "")

            }

            override fun onNext(t: TextApiBean) {
                LogUtils.i(t.toString())

                if ("0" != t.resultCode) {
                    (mRootView as VerificationCodeFragment).handleError(VerificationApi.requestCheckVerificationCode, t.resultMsg.toString())
                } else {
                    (mRootView as VerificationCodeFragment).handleSuccess(VerificationApi.requestCheckVerificationCode, t.resultMsg.toString())
                }

            }


            override fun onCompleted() {
                (mRootView as VerificationCodeFragment).afterLoad(VerificationApi.requestCheckVerificationCode, "")

            }

        })
    }

}

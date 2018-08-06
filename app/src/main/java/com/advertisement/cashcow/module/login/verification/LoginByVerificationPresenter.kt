package com.advertisement.cashcow.module.login.verification

import android.content.Context
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.LoginByPasswordApi
import com.advertisement.cashcow.common.network.api.LoginByVerificationApi
import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.advertisement.cashcow.config.Constant
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*

/**
 * 作者：吴国洪 on 2018/5/28 0028
 * 描述：LoginByVerificationFragment逻辑处理类
 */
class LoginByVerificationPresenter : BasePresenter<LoginByVerificationContract.View>(), LoginByVerificationContract.Presenter {


    /**
     * 请求发送验证码
     */
    override fun requestIsRegisterAndGetVerificationCode(phoneNum: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["phone"] = phoneNum

        val context: Context = (mRootView as LoginByVerificationFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(LoginByVerificationApi::class.java)

        novate.call(api.requestIsRegister(parameters)
                .subscribeOn(Schedulers.io())
                .flatMap { t ->
                    LogUtils.i(t.toString())
                    if ("1" == t?.resultCode) {
                        api.requestGetVerificationCode(parameters)
                    } else {
                        (mRootView as LoginByVerificationFragment).handleError(LoginByVerificationApi.requestIsRegister, t.resultMsg.toString())
                        Observable.empty()
                    }
                }

                , object : BaseSubscriber<TextApiBean>(context) {

            override fun onStart() {

            }

            override fun onNext(t: TextApiBean) {
                LogUtils.i(t.toString())
                if ("0" != t.resultCode) {
                    (mRootView as LoginByVerificationFragment).handleError(LoginByVerificationApi.requestGetVerificationCode, t.resultMsg.toString())
                } else {
                    (mRootView as LoginByVerificationFragment).handleSuccess(LoginByVerificationApi.requestGetVerificationCode, t.resultMsg.toString())
                }
            }

            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")
                (mRootView as LoginByVerificationFragment).handleError("", e.toString())
            }

            override fun onCompleted() {

            }

        })
    }

    /**
     * 请求登录
     */
    override fun requestLogin(phoneNum: String, verificationCode: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["phone"] = phoneNum
        parameters["vcode"] = verificationCode

        val context: Context = (mRootView as LoginByVerificationFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(LoginByVerificationApi::class.java)

        novate.call(api.requestLogin(parameters), object : BaseSubscriber<LoginByPasswordApiBean>(context) {

            override fun onStart() {
                (mRootView as LoginByVerificationFragment).preLoad(LoginByPasswordApi.requestLogin, "")
            }

            override fun onNext(t: LoginByPasswordApiBean) {
                LogUtils.i(t.toString())
                if ("0" != t.resultCode) {
                    (mRootView as LoginByVerificationFragment).handleError(LoginByVerificationApi.requestLogin, t.resultMsg.toString())
                }else{
                    CacheConfigUtils.getConfigCacheInstance(context).put(Constant.cache_user_info, t)
                    (mRootView as LoginByVerificationFragment).handleSuccess(LoginByVerificationApi.requestLogin, t.resultMsg.toString())
                }
            }

            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                (mRootView as LoginByVerificationFragment).handleError(LoginByVerificationApi.requestLogin, e.toString())
            }

            override fun onCompleted() {
                (mRootView as LoginByVerificationFragment).afterLoad(LoginByVerificationApi.requestLogin, "")

            }

        })
    }
}

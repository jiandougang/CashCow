package com.advertisement.cashcow.module.login.password

import android.content.Context
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.LoginByPasswordApi
import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import com.advertisement.cashcow.config.Constant
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import java.util.*

/**
 * 作者：吴国洪 on 2018/6/20
 * 描述：LoginByPasswordFragment逻辑处理类
 */
class LoginByPasswordPresenter : BasePresenter<LoginByPasswordContract.View>(), LoginByPasswordContract.Presenter {

    /**
     * 请求登录
     */
    override fun requestLogin(phoneNum: String, password: String) {


        val parameters: MutableMap<String, String> = HashMap()

        parameters["phone"] = phoneNum
        parameters["password"] = password


        val context: Context = (mRootView as LoginByPasswordFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val loginByPasswordApi = novate.create(LoginByPasswordApi::class.java)

        novate.call(loginByPasswordApi.requestLogin(parameters), object : BaseSubscriber<LoginByPasswordApiBean>(context) {

            override fun onStart() {
                (mRootView as LoginByPasswordFragment).preLoad(LoginByPasswordApi.requestLogin, "")
            }

            override fun onNext(t: LoginByPasswordApiBean) {
                LogUtils.d(t.toString())
                if ("0" != t.resultCode) {
                    (mRootView as LoginByPasswordFragment).handleError(LoginByPasswordApi.requestLogin, t.resultMsg.toString())
                } else {
                    CacheConfigUtils.getConfigCacheInstance(context).put(Constant.cache_user_info, t)
                    (mRootView as LoginByPasswordFragment).handleSuccess(LoginByPasswordApi.requestLogin, t.resultCode!!)
                }
            }

            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                (mRootView as LoginByPasswordFragment).handleError("", e.toString())
            }

            override fun onCompleted() {
                (mRootView as LoginByPasswordFragment).afterLoad(LoginByPasswordApi.requestLogin, "")

            }

        })
    }

}

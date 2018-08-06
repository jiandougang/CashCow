package com.advertisement.cashcow.module.login.register.phone

import android.content.Context
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import java.util.*

/**
 * 作者：吴国洪 on 2018/6/26
 * 描述：PhoneNumFragment逻辑处理类
 */
class PhonePresenter : BasePresenter<PhoneNumContract.View>(), PhoneNumContract.Presenter {


    override fun requestIsRegister(phoneNum: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["phone"] = phoneNum

        val context: Context = (mRootView as PhoneNumFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(Api::class.java)

        novate.call(api.requestIsRegister(parameters), object : BaseSubscriber<TextApiBean>(context) {

            override fun onStart() {
                (mRootView as PhoneNumFragment).preLoad(Api.requestIsRegister,"")
            }

            override fun onNext(t: TextApiBean) {
                LogUtils.i(t.toString())
                if ("0" != t.resultCode) {
                    (mRootView as PhoneNumFragment).handleError(Api.requestIsRegister, t.resultMsg.toString())
                } else {
                    (mRootView as PhoneNumFragment).handleSuccess(Api.requestIsRegister, t.resultCode!!)
                }
            }

            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                (mRootView as PhoneNumFragment).handleError(Api.requestIsRegister, e.toString())
            }

            override fun onCompleted() {
                (mRootView as PhoneNumFragment).afterLoad(Api.requestIsRegister,"")
            }

        })

    }

}

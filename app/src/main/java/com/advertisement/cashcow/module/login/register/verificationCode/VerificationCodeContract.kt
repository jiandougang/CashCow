package com.advertisement.cashcow.module.login.register.verificationCode

import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter


/**
 * Created by 吴国洪 on 2018/06/20
 * InitPassword 契约类
 */

interface VerificationCodeContract {

    interface View : IBaseView {
        /**
         * 请求失败或者注册失败时回调
         */

    }

    interface Presenter : IPresenter<View> {

        /**
         * 请求短信验证码接口
         */
        fun requestGetVerificationCode(phoneNum: String)


        /**

         * 校验验证码
         *
         * @param phoneNum  (手机号码)
         * @param verificationCode   (手机验证码)
         */
        fun requestCheckVerificationCode(phoneNum: String, verificationCode: String)

    }


}
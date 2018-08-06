package com.advertisement.cashcow.module.login.verification

import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter


/**
 * Created by 吴国洪 on 2018/05/28.
 * Information 契约类
 */

interface LoginByVerificationContract {

    interface View : IBaseView

    interface Presenter : IPresenter<View>{

        /**
         * 请求判断是否注册和短信验证码接口
         */
        fun requestIsRegisterAndGetVerificationCode(phoneNum: String)

        /**
         * 请求登录接口
         */
        fun requestLogin(phoneNum: String, verificationCode: String)
    }

}
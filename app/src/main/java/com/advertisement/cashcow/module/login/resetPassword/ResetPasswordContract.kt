package com.advertisement.cashcow.module.login.resetPassword

import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter


/**
 * Created by 吴国洪 on 2018/06/26
 * ResetPassword 契约类
 */

interface ResetPasswordContract {

    interface View : IBaseView

    interface Presenter : IPresenter<View> {

        /**
         * 请求判断是否注册和短信验证码接口
         *
         * @param type type 验证码模板类型
         * 1:表示申请注册
         * 2:表示忘记登录密码时重置
         * 3:表示忘记交易密码时重置
         * 4:表示验证码登录
         * @param phoneNum phoneNum
         */
        fun requestIsRegisterAndGetVerificationCode(type: String, phoneNum: String)

        /**
         * 请求校验验证码和重置密码接口
         */
        fun requestIsOkAndResetPassword(phoneNum: String, password: String, verificationCode: String,vCodeType:String)
    }


}
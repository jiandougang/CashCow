package com.advertisement.cashcow.module.login.register.phone

import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter


/**
 * Created by 吴国洪 on 2018/05/28.
 * LoginByPassword 契约类
 */

interface PhoneNumContract {

    interface View : IBaseView {


    }

    interface Presenter : IPresenter<View> {
        /**
         * 校验当前手机号码是否已被注册接口
         */
        fun requestIsRegister(phoneNum: String)
    }


}
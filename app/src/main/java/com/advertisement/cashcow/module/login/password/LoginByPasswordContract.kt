package com.advertisement.cashcow.module.login.password

import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter


/**
 * Created by 吴国洪 on 2018/05/28.
 * LoginByPassword 契约类
 */

interface LoginByPasswordContract {

    interface View : IBaseView

    interface Presenter : IPresenter<View> {
        /**
         * 请求登录
         */
        fun requestLogin(phoneNum:String,password:String)


    }


}
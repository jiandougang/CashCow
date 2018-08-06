package com.advertisement.cashcow.module.login.register.initPassword

import android.content.Context
import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter


/**
 * Created by 吴国洪 on 2018/06/20
 * InitPassword 契约类
 */

interface InitPasswordContract {

    interface View : IBaseView

    interface Presenter : IPresenter<View> {

        /**
         * 请求注册接口
         */
        fun requestRegister(context: Context, phoneNum: String, password: String)



    }


}
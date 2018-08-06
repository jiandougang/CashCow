package com.advertisement.cashcow.module.main.mine.personalInformation

import android.content.Context
import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter
import java.io.File


/**
 * Created by 吴国洪 on 2018/05/28.
 * Information 契约类
 */

interface PersonalInformationContract {

    interface View : IBaseView

    interface Presenter : IPresenter<View> {

        /**
         * 获取file的时候如果没有路径就重新创建
         * @return
         */
         fun getFile(): File

        /**
         * 获取用户信息
         *
         * @param id 通过登录接口返回的用户id
         */

        /**
         * 上传用户头像
         *
         * @param id 通过登录接口获取的用户id
         * @param picFile 文件对象
         *
         */
        fun requestUploadAvatar(id: String,picFile:File)

        /**
         * 绑定银行卡
         *
         * @param userid 用户id
         * @param nickname 昵称，可选
         * @param phone 手机号,可选
         * @param wx 微信,可选
         * @param qq qq(qq, 可选)
         */
        fun requestSaveBasicInfo(context: Context, userId: String, nickname: String,
                                 phone: String,password:String)
    }

}
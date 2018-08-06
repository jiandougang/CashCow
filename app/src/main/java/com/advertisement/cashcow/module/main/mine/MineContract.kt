package com.advertisement.cashcow.module.main.mine

import android.content.Context
import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter
import java.io.File


/**
 * Created by 吴国洪 on 2018/05/28.
 * Information 契约类
 */

interface MineContract {

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
        fun requestUserById(id: String)

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
        fun requestUploadAvatar(id: String, picFile: File)

        /**
         * 微信分享
         *
         * @param context context
         * @param scene
         * 发送到聊天界面——WXSceneSession
         * 发送到朋友圈——WXSceneTimeline
         * 添加到微信收藏——WXSceneFavorite
         *
         */
        fun wechatShare(context: Context, scene: Int, userId: String)


        /**
         * 查询用户累计收益
         *
         * @param userId userId
         * @param page page
         *
         */
        fun requestGetBenefits(userId: String, page: String)


        /**
         * 修改登录密码接口
         *
         * @param userId userId
         * @param oldPassword oldPassword
         * @param newPassword newPassword
         *
         */
        fun requestUpdateDealPass(userId: String, oldPassword: String, newPassword: String)

        /**
         * 修改交易密码接口
         *
         * @param userId userId
         * @param oldPassword oldPassword
         * @param newPassword newPassword
         *
         */
        fun requestUpdateLoginPassword(userId: String, oldPassword: String, newPassword: String)

        /**
         * 校验当前手机号码是否已被注册
         *
         * @param phoneNum 手机号码
         * @param vCode 验证码
         * @param vCodeType type 验证码模板类型
         * 1:表示申请注册
         * 2:表示忘记登录密码时重置
         * 3:表示忘记交易密码时重置
         * 4:表示验证码登录
         * @param phoneNum phoneNum
         */
        fun requestIsOk(context: Context, phoneNum: String, vCode: String, vCodeType: String, className: String)


        /**
         * 请求短信验证码接口
         *
         * @param type type 验证码模板类型
         * 1:表示申请注册
         * 2:表示忘记登录密码时重置
         * 3:表示忘记交易密码时重置
         * 4:表示验证码登录
         * @param phoneNum phoneNum
         */
        fun requestGetVerificationCode(context: Context, type: String, phoneNum: String, className: String)

        /**
         * 重置交易密码
         *
         * @param phoneNum 手机号码
         * @param dealPassword 交易密码
         */
        fun requestResetDealPwd(phoneNum: String, dealPassword: String)


    }


}
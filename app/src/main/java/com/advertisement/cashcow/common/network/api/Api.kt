package com.advertisement.cashcow.common.network.api

import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.advertisement.cashcow.common.network.bean.mine.CumulativeApiBean
import com.advertisement.cashcow.common.network.bean.mine.MineApiBean
import com.advertisement.cashcow.common.network.bean.mine.ModifyPasswordApiBean
import com.advertisement.cashcow.common.network.bean.mine.MyBankCardBindingApiBean
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

/**
 * 作者：吴国洪 on 2018/7/76
 * 描述：接口方法
 */
interface Api {

    companion object {
        var requestIsRegister = "requestIsRegister"
        var requestUserById = "requestUserById"
        var requestGetBenefits = "requestGetBenefits"
        var requestUpdateLoginPass = "requestUpdateLoginPass"
        var requestSaveBasicInfo = "requestSaveBasicInfo"

        var requestGetVerificationCode = "requestIsRegisterAndGetVerificationCode"
        var requestResetPassword = "requestIsOkAndResetPassword"
        var requestIsOk = "requestIsOk"
        var requestResetDealPwd = "requestResetDealPwd"
        var requestBindBandCard = "requestBindBandCard"


        /**
         * 1:表示申请注册
         * 2:表示忘记登录密码时重置
         * 3:表示忘记交易密码时重置
         * 4:表示验证码登录
         */
        class VerificationType(val type: Int) {
            companion object {
                val REGISTER = "1"
                val FORGET_LOGIN_PASSWORD = "2"
                val FORGET_TRADING_PASSWORD = "3"
                val VERIFICATION_CODE_LOGIN = "4"
                val BINDING_PHONE_NUMBER = "5"

            }
        }
    }


    /**
     * 请求获取验证码
     *

     * @param type type 验证码模板类型
     * 1:表示申请注册
     * 2:表示忘记登录密码时重置
     * 3:表示忘记交易密码时重置
     * 4:表示验证码登录
     *
     * @param phone phone
     */
    @GET("member/getVerifyCode.do")
    fun requestGetVerificationCode(@QueryMap maps: Map<String, String>): Observable<TextApiBean>


    /**
     * 校验当前手验证码是否正确接口
     */
    @GET("member/isOk.do")
    fun requestIsOk(@QueryMap maps: Map<String, String>): Observable<TextApiBean>

    /**
     * 请求重置密码接口
     */
    @POST("member/resetPwd.do")
    fun requestResetPassword(@QueryMap maps: Map<String, String>): Observable<TextApiBean>

    /**
     * 检查验证码是否正确
     *
     * @param id 通过登录接口获取的用户id
     *
     */
    @GET("member/getUserById.do")
    fun requestUserById(@QueryMap maps: Map<String, String>): Observable<MineApiBean>


    /**
     * 上传用户头像
     *
     * @param id 通过登录接口获取的用户id
     * @param uploadFile 文件参数名称
     *
     */
    @POST("member/uploadAvatar.do")
    fun requestUploadAvatar(@Body body: RequestBody): Observable<TextApiBean>

    /**
     * 查询累计收益
     *
     * @param userid userid
     * @param rows 页大小
     * @param page 当前页
     */
    @GET("member/getBenefits.do")
    fun requestGetBenefits(@QueryMap maps: Map<String, String>): Observable<CumulativeApiBean>

    /**
     * 修改登录密码
     *
     * @param userid userid
     * @param newloginpass 新登录密码
     * @param oldloginpass 旧登录密码
     *
     */
    @POST("member/updateLoginPass.do")
    fun requestUpdateLoginPass(@QueryMap maps: Map<String, String>): Observable<ModifyPasswordApiBean>

    /**
     * 修改交易密码
     *
     * @param userid userid
     * @param newloginpass 新登录密码
     * @param oldloginpass 旧登录密码
     *
     */
    @POST("member/updateDealPass.do")
    fun requestUpdateDealPass(@QueryMap maps: Map<String, String>): Observable<ModifyPasswordApiBean>

    /**
     * 校验当前手机号码是否已被注册接口
     * 0代表未注册，1代表已注册
     *
     * @param phone 手机号码
     */
    @GET("member/isRegister.do")
    fun requestIsRegister(@QueryMap maps: Map<String, String>): Observable<TextApiBean>


    /**
     * 重置交易密码
     * 0代表未注册，1代表已注册
     *
     * @param phone 手机号码
     */
    @POST("member/resetDealPwd.do")
    fun requestResetDealPwd(@QueryMap maps: Map<String, String>): Observable<TextApiBean>

    /**
     * 绑定银行卡
     *
     * @param userid 用户id
     * @param bandname 银行名称
     * @param account 卡号
     * @param name 真实姓名
     */
    @GET("member/bindBandCard.do")
    fun requestBindBandCard(@QueryMap maps: Map<String, String>): Observable<MyBankCardBindingApiBean>


    /**
     * 绑定银行卡
     *
     * @param userid 用户id
     * @param nickname 昵称，可选
     * @param phone 手机号,可选
     * @param wx 微信,可选
     * @param qq qq(qq, 可选)
     */
    @POST("member/saveBasicInfo.do")
    fun requestSaveBasicInfo(@QueryMap maps: Map<String, String>): Observable<LoginByPasswordApiBean>

}

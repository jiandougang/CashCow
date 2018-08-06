package com.advertisement.cashcow.common.network.api

import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import com.advertisement.cashcow.common.network.bean.TextApiBean
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

/**
 * 作者：吴国洪 on 2018/6/27
 * 描述：LoginByVerification接口
 */
interface LoginByVerificationApi {

    companion object {
        var requestIsRegister = "requestIsRegister"
        var requestGetVerificationCode = "requestIsRegisterAndGetVerificationCode"
        var requestLogin = "requestLogin"
    }

    /**
     * 请求获取验证码
     *
     * * @param phone(手机号码)
     */
    @GET("member/getVerifyCode.do")
    fun requestGetVerificationCode(@QueryMap maps: Map<String, String>): Observable<TextApiBean>

    /**
     * 校验当前手机号码是否已被注册接口
     * 0代表未注册，1代表已注册
     */
    @GET("member/isRegister.do")
    fun requestIsRegister(@QueryMap maps: Map<String, String>): Observable<TextApiBean>

    /**
     * 请求注册接口
     */
    @POST("member/login.do")
    fun requestLogin(@QueryMap maps: Map<String, String>): Observable<LoginByPasswordApiBean>


}

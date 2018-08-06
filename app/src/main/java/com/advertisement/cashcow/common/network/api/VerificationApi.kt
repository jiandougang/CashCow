package com.advertisement.cashcow.common.network.api

import com.advertisement.cashcow.common.network.bean.TextApiBean
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

/**
 * 作者：吴国洪 on 2018/6/20
 * 描述：Verification接口
 */
interface VerificationApi {

    companion object {
        var requestGetVerificationCode = "requestIsRegisterAndGetVerificationCode"
        var requestCheckVerificationCode = "requestCheckVerificationCode"
    }

    /**
     * 请求获取验证码
     *
     * * @param phone(手机号码)
     */
    @GET("member/getVerifyCode.do")
    fun requestGetVerificationCode(@QueryMap maps: Map<String, String>): Observable<TextApiBean>

    /**
     * 检查验证码是否正确
     * * @param phone(手机号码)
     * *@param vcode(手机验证码)
     */
    @GET("member/isOk.do")
    fun requestCheckVerificationCode(@QueryMap maps: Map<String, String>): Observable<TextApiBean>

}

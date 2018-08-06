package com.advertisement.cashcow.common.network.api

import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

/**
 * 作者：吴国洪 on 2018/6/25
 * 描述：LoginByPassword接口
 */
interface LoginByPasswordApi {


    companion object {
        var requestLogin = "requestLogin"
    }

    /**
     * 请求登录接口
     */
    @POST("member/login.do")
    fun requestLogin(@QueryMap maps: Map<String, String>): Observable<LoginByPasswordApiBean>

    /**
     * 用户注册接口
     */
    @POST("member/register.do")
    fun requestRegister(@QueryMap maps: Map<String, String>): Observable<LoginByPasswordApiBean>

}

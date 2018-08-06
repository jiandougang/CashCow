package com.advertisement.cashcow.common.network.api

import com.advertisement.cashcow.common.network.bean.TextApiBean
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

/**
 * 作者：吴国洪 on 2018/6/20
 * 描述：InitPassword接口
 */
interface InitPasswordApi {

    companion object {
        var requestRegister = "requestRegister"
        var requestIsRegister = "requestIsRegister"

    }

    /**
     * 请求注册接口
     */
    @POST("member/register.do")
    fun requestRegister(@QueryMap maps: Map<String, String>): Observable<TextApiBean>

    /**
     * 请求是否已注册
     */
    @GET("member/isRegister.do")
    fun requestIsRegister(@QueryMap maps: Map<String, String>): Observable<TextApiBean>
}

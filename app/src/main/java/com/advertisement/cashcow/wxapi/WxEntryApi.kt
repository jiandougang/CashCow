package com.advertisement.cashcow.wxapi

import com.advertisement.cashcow.common.network.bean.wx.WxAccessTokenApiBean
import com.advertisement.cashcow.common.network.bean.wx.WxRefreshTokenApiBean
import com.advertisement.cashcow.common.network.bean.wx.WxUserInfoApiBean
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url
import rx.Observable

/**
 * 作者：吴国洪 on 2018/6/25
 * 描述：WxEntryActivity接口
 */
interface WxEntryApi {


    companion object {
        var requestWxAccessToken = "requestWxAccessToken"
        var requestWxRefreshToken = "requestWxRefreshToken"
        var requestWxUserInfo = "requestWxUserInfo"
    }


    /**
     * 通过code获取微信access_token
     */
    @GET
    fun requestWxAccessToken(@Url url: String, @QueryMap maps: Map<String, String>): Observable<WxAccessTokenApiBean>

    /**
     * 刷新或续期access_token使用
     */
    @GET
    fun requestWxRefreshToken(@Url url: String, @QueryMap maps: Map<String, String>): Observable<WxRefreshTokenApiBean>

    /**
     * 通过code获取微信access_token
     */
    @GET
    fun requestWxUserInfo(@Url url: String, @QueryMap maps: Map<String, String>): Observable<WxUserInfoApiBean>
}

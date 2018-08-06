package com.advertisement.cashcow.common.network.api

import com.advertisement.cashcow.common.network.bean.DetailsApiBean
import com.advertisement.cashcow.common.network.bean.TextApiBean
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

/**
 * 作者：吴国洪 on 2018/07/05
 * 描述：Details接口
 */
interface DetailsApi {

    companion object {
        val requestGetContentById = "requestGetContentById"
        val requestStoreAd = "requestStoreAd"
        val requestCancelStore = "requestCancelStore"
        val requestViewEvent = "requestViewEvent"
        val requestAddwxEvent = "requestAddwxEvent"


    }

    /**
     * 获取广告的详情信息
     */
    @GET("ad/getContentById.do")
    fun requestGetContentById(@QueryMap maps: Map<String, String>): Observable<DetailsApiBean>


    /**
     * 收藏广告
     * @param userid(用户id)
     * @param  adid(广告id)
     */
    @GET("store/storeAd.do")
    fun requestStoreAd(@QueryMap maps: Map<String, String>): Observable<TextApiBean>

    /**
     * 取消广告收藏接口
     * @param userid(用户id)
     * @param  adid(广告id)
     */
    @GET("store/cancelStore.do")
    fun requestCancelStore(@QueryMap maps: Map<String, String>): Observable<TextApiBean>


    /**
     * 广告详情：观看时长超过广告设定的时间
     * @param userid(用户id)
     * @param  adid(广告id)
     */
    @GET("uop/viewEvent.do")
    fun requestViewEvent(@QueryMap maps: Map<String, String>): Observable<TextApiBean>

    /**
     * 广告详情：添加微信号
     * @param userid(用户id)
     * @param  adid(广告id)
     */
    @GET("uop/addwxEvent.do")
    fun requestAddwxEvent(@QueryMap maps: Map<String, String>): Observable<TextApiBean>

}

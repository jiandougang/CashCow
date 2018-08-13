package com.advertisement.cashcow.common.network.api

import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.advertisement.cashcow.common.network.bean.information.*
import com.advertisement.cashcow.module.main.information.AppVersionUpdateApiBean
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

/**
 * 作者：吴国洪 on 2018/6/29
 * 描述：Information接口
 */
interface InformationApi {

    companion object {
        val requestGetTopAd = "requestGetTopAd"
        val requestQueryAds = "requestQueryAds"
        val requestGetStoreAds = "requestGetStoreAds"
        val requestHotSearch = "requestHotSearch"
        val requestQueryNewsByUserID = "requestQueryNewsByUserID"
        val requestReadedNews = "requestReadedNews"
        val requestStatisUnreadNews = "requestStatisUnreadNews"
        val requestClickEvent = "requestClickEvent"
        val requestGetLastedVersion = "requestGetLastedVersion"


        /**每一页展示多少条数据 */
        val REQUEST_COUNT = 10
    }

    /**
     * 获取3张滚动播放图片
     */
    @GET("ad/getTopAd.do")
    fun requestGetTopAd(@QueryMap maps: Map<String, String>): Observable<InformationBannerAdApiBean>

    @GET("ad/queryAds.do")
    fun requestQueryAds(@QueryMap maps: Map<String, String>): Observable<InformationAdApiBean>

    /**
     * 个人广告收藏列表
     *
     * @param userid (用户id)
     * @param page(当前页)
     * @param rows(页大小)
     */
    @GET("store/getStoreAds.do")
    fun requestGetStoreAds(@QueryMap maps: Map<String, String>): Observable<InformationAdApiBean>


    /**
     * 检索热门的最近10条广告信息
     *
     */
    @GET("fun/hotSearchs.do")
    fun requestHotSearch(@QueryMap maps: Map<String, String>): Observable<SearchHotTagApiBean>


    /**
     * 获取消息列表
     *
     * @param userid (用户id)
     * @param page(当前页)
     * @param rows(页大小)
     */
    @GET("news/queryNewsByUserID.do")
    fun requestQueryNewsByUserID(@QueryMap maps: Map<String, String>): Observable<MessagesCenterApiBean>

    /**
     * 获取消息列表
     *
     * @param userid (用户id)
     * @param newsid(消息唯一标识)
     * @param rows(页大小)
     */
    @GET("news/readedNews.do")
    fun requestReadedNews(@QueryMap maps: Map<String, String>): Observable<TextApiBean>

    /**
     * 统计消息的未读条数
     *
     * @param userid (用户id)
     */
    @GET("news/statisUnreadNews.do")
    fun requestStatisUnreadNews(@QueryMap maps: Map<String, String>): Observable<StatisticsUnreadMessageApiBean>


    /**
     * 统计消息的未读条数
     *
     * @param userid (用户id)
     * @param adid adid(广告id)
     */
    @GET("uop/clickEvent.do")
    fun requestClickEvent(@QueryMap maps: Map<String, String>): Observable<TextApiBean>


    /**
     * 安卓app版本更新
     *
    */
    @GET("version/getLastedVersion.do")
    fun requestGetLastedVersion(@QueryMap maps: Map<String, String>): Observable<AppVersionUpdateApiBean>

}

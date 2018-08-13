package com.advertisement.cashcow.module.main.detail

import android.content.Context
import android.support.v4.app.FragmentManager
import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter
import com.advertisement.cashcow.common.network.bean.DetailsApiBean


/**
 * Created by 吴国洪 on 2018/06/23
 * DetailFragment契约类
 */

interface DetailsContract {

    interface View : IBaseView {


    }

    interface Presenter : IPresenter<View> {
        fun initAllData(): ArrayList<DetailsBean>

        fun initTitleTypeData(title: String): DetailsBean

        fun initNameTypeData(name: String, time: String): DetailsBean

        fun initContentTypeData(content: String): DetailsBean

        fun initPictureTypeData(url: String): DetailsBean



        /**
         * 解析封装返回的广告详情数据
         *
         * @param dataList 请求广告实体
         */
        fun parseAdData(detailsApiBean: DetailsApiBean?): ArrayList<DetailsBean>

        /**
         * 获取广告的详情信息
         *
         * @param id 广告id
         * @param userId 用户id,用于显示广告收藏状态
         */
        fun requestGetContentById(id: String, userId: String)

        /**
         * 收藏广告
         *
         * @param userId userid(用户id)
         * @param adId adid(广告id)
         */
        fun requestStoreAd(userId: String, adId: String)

        /**
         * 取消收藏广告
         *
         * @param userId userid(用户id)
         * @param adId adid(广告id)
         */
        fun requestCancelStore(userId: String, adId: String)

        /**
         * 广告详情：观看时长超过广告设定的时间
         * @param userid(用户id)
         * @param  adid(广告id)
         */
        fun requestViewEvent(context: Context,userId: String, adId: String, delayTime: Int)

        /**
         * 广告详情：添加微信号
         * @param userid(用户id)
         * @param  adid(广告id)
         */
        fun requestAddwxEvent(context: Context,userId: String, adId: String)

        /**
         * 红包(金币)是否已领取
         * @param userid(用户id)
         * @param  adid(广告id)
         */
        fun requestIsReceived(context: Context, userId: String?, advertisementId: String,coins:Int,
                              manager: FragmentManager, loginCallback: () -> Unit)

        /**
         * 领取(金币)红包
         * @param userid(用户id)
         * @param  adid(广告id)
         */
        fun requestReceiveRedPckt(context: Context, userId: String, adId: String,
                                  manager: FragmentManager)


    }
}
package com.advertisement.cashcow.module.main.information

import android.content.Context
import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter
import com.advertisement.cashcow.common.network.bean.information.InformationAdApiBean
import com.advertisement.cashcow.common.network.bean.information.InformationBannerAdApiBean


/**
 * Created by 吴国洪 on 2018/05/28.
 * Information 契约类
 */

interface InformationContract {

    interface View : IBaseView {
        fun startActivity(entity: InformationBean.VideoEntity)
    }

    interface Presenter : IPresenter<View> {

        /**
         * 解析封装返回的广告数据
         *
         * @param page 请求广告实体
         */
        fun initAllData(context: Context, page: String, title: String): ArrayList<InformationBean>

        /**
         * 初始化空banner数据
         */
        fun initEmptyBanner(): ArrayList<InformationBean>

        /**
         * 初始化种类选择按钮
         */
        fun initFilterTypeData(context: Context, title: String): ArrayList<InformationBean>

        /**
         * 获取3张滚动播放图片
         */
        fun requestGetTopAd(context: Context, title: String)

        /**
         * 查询广告接口
         *
         * @param page 第几页
         */
        fun requestQueryAds(context: Context, title: String, page: String)

        /**
         * 解析封装返回的广告数据
         *
         * @param dataList 请求广告实体
         */
        fun parseAdData(dataList: List<InformationAdApiBean.RowsBean>?): ArrayList<InformationBean>

        /**
         * bannerAd转换为普通的List Ad
         *
         *@param data 请求banner广告实体
         */
        fun parseBannerAdToQueryAd(data: InformationBannerAdApiBean.ResultDataBean): InformationBean

        /**
         * 统计消息的未读条数
         *
         */
        fun requestStatisUnreadNews(context: Context, userId: String)


        /**
         * 点击列表广告信息
         *
         */
        fun requestClickEvent(context: Context, userId: String, adId: String)


        /**
         * 解析标题子段，用于后台访问接口
         **/
        fun parseTitle(title: String, context: Context): String
    }


}
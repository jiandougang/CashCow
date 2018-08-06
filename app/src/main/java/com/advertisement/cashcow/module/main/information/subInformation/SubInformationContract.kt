package com.advertisement.cashcow.module.main.information.subInformation

import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter
import com.advertisement.cashcow.common.network.bean.information.InformationAdApiBean
import com.advertisement.cashcow.module.main.information.InformationBean


/**
 * Created by 吴国洪 on 2018/05/28.
 * Information 契约类
 */

interface SubInformationContract {

    interface View : IBaseView {
        fun startActivity(entity: InformationBean.VideoEntity)
    }

    interface Presenter : IPresenter<View> {

        /**
         * 解析封装返回的广告数据
         *
         * @param page 请求广告实体
         */
        fun initAllData(page:String): ArrayList<InformationBean>
        fun initAllSubData(title:String,page:String): ArrayList<InformationBean>
        /**
         * 加载更多数据
         */
        fun loadMoreData()


        /**
         * 查询广告接口
         *
         * @param page 第几页
         */
        fun requestQueryAds(page: String)

        /**
         * 解析封装返回的广告数据
         *
         * @param dataList 请求广告实体
         */
        fun parseAdData(dataList: List<InformationAdApiBean.RowsBean>?): ArrayList<InformationBean>
    }


}
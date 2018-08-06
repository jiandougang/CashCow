package com.advertisement.cashcow.module.main.search

import android.content.Context
import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter


/**
 * Created by 吴国洪 on 2018/05/28.
 * Information 契约类
 */

interface SearchContract {

    interface View : IBaseView

    interface Presenter : IPresenter<View> {
        fun initAllData(context: Context): ArrayList<SearchBean>

        /**
         * 初始化会话Item
         */
        fun initSessionTypeData(sessionText: String): SearchBean

        /**
         * 初始化历史Item
         */
        fun initHistoryTypeData(context: Context): ArrayList<SearchBean>

        fun requestHotSearchs()

        /**
         * 保存搜索历史记录
         */
        fun saveSearchTag(context: Context, key: String, callback: () -> Unit): List<SearchBean>

        fun requestqueryAds(context: Context, key: String, page: String)
    }

}
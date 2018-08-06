package com.advertisement.cashcow.module.main.search

import android.content.Context
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.api.InformationApi
import com.advertisement.cashcow.common.network.bean.information.InformationAdApiBean
import com.advertisement.cashcow.common.network.bean.information.SearchHotTagApiBean
import com.advertisement.cashcow.config.Constant
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable

/**
 * 作者：吴国洪 on 2018/6/21
 * 描述：SearchFragment逻辑处理类
 * 接口：
 */
class SearchPresenter : BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    override fun requestqueryAds(context: Context, key: String, page: String) {
        val parameters: MutableMap<String, String> = java.util.HashMap()

        parameters["mhcx"] = key
        parameters["page"] = page
        parameters["rows"] = InformationApi.REQUEST_COUNT.toString()


        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(InformationApi::class.java)

        novate.call(api.requestQueryAds(parameters), object : BaseSubscriber<InformationAdApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                (mRootView as SearchFragment).handleError(InformationApi.requestQueryAds, e.toString())
            }

            override fun onStart() {
                (mRootView as SearchFragment).preLoad(Api.requestIsRegister, "")
            }

            override fun onNext(t: InformationAdApiBean) {
                LogUtils.d(t.toString())
                (mRootView as SearchFragment).handleSuccess(InformationApi.requestQueryAds, t)
            }

            override fun onCompleted() {
                (mRootView as SearchFragment).afterLoad(Api.requestIsRegister, "")
            }
        })
    }

    override fun saveSearchTag(context: Context, key: String, callback: () -> Unit): List<SearchBean> {
        val searchHistoryTag = CacheConfigUtils.parseSearchTag(context)
        val tempMutableHistoryTextList = searchHistoryTag.historyTextList?.toMutableList()

        //最多保存5条历史记录
        if (tempMutableHistoryTextList?.size!! == SearchHistoryTag.HistoryCount) {
            tempMutableHistoryTextList.removeAt(0)
            callback()
        }

        tempMutableHistoryTextList.add(key)
        searchHistoryTag.historyTextList = tempMutableHistoryTextList

        CacheConfigUtils.getConfigCacheInstance(context).put(Constant.cache_search_tag, searchHistoryTag)
        val searchBean = SearchBean(SearchBean.historyType)
        searchBean.historyTypeEntity = SearchBean.HistoryTypeItem(key)
        return listOf(searchBean)
    }

    override fun requestHotSearchs() {
        val parameters: MutableMap<String, String> = HashMap()

        val context: Context = (mRootView as SearchFragment).context

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(InformationApi::class.java)

        novate.call(api.requestHotSearch(parameters), object : BaseSubscriber<SearchHotTagApiBean>(context) {

            override fun onError(e: com.tamic.novate.Throwable?) {
                LogUtils.i(e.toString())
                (mRootView as SearchFragment).handleError(InformationApi.requestHotSearch, e.toString())
            }

            override fun onStart() {

            }

            override fun onNext(t: SearchHotTagApiBean) {
                LogUtils.d(t.toString())
                (mRootView as SearchFragment).handleSuccess(InformationApi.requestHotSearch, t)
            }

            override fun onCompleted() {
            }
        })
    }


    override fun initAllData(context: Context): ArrayList<SearchBean> {
        val data = ArrayList<SearchBean>()
        data.add(initSessionTypeData("热门搜索"))
        data.add(SearchBean(SearchBean.tagType))
        data.add(initSessionTypeData("历史搜索"))

        data.addAll(initHistoryTypeData(context))
        return data
    }

    /**
     * 初始化会话Item
     */
    override fun initSessionTypeData(sessionText: String): SearchBean {
        val searchBean = SearchBean(SearchBean.sessionType)
        searchBean.sessionTypeEntity = SearchBean.SessionTypeItem(sessionText)
        return searchBean

    }

    /**
     * 初始化搜索历史Item
     */
    override fun initHistoryTypeData(context: Context): ArrayList<SearchBean> {

        val searchBeanArrayList = ArrayList<SearchBean>()

        LogUtils.d(CacheConfigUtils.parseSearchTag(context).historyTextList?.size)
        if (CacheConfigUtils.parseSearchTag(context).historyTextList != null) {
            for (historyTag in CacheConfigUtils.parseSearchTag(context).historyTextList!!) {
                val searchBean = SearchBean(SearchBean.historyType)
                searchBean.historyTypeEntity = SearchBean.HistoryTypeItem(historyTag)

                searchBeanArrayList.add(searchBean)
            }
        }

        return searchBeanArrayList

    }


}

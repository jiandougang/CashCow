package com.advertisement.cashcow.util

import android.content.Context
import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import com.advertisement.cashcow.config.Constant
import com.advertisement.cashcow.module.main.search.SearchHistoryTag
import com.blankj.utilcode.util.CacheDiskUtils
import com.blankj.utilcode.util.CacheDoubleUtils
import com.blankj.utilcode.util.CacheMemoryUtils

/**
 * 作者：吴国洪 on 2018/7/12
 * 描述：关于配置缓存信息工具
 */
object CacheConfigUtils {


    /**
     * 获取配置缓存实例
     * 该实例应该用于配置信息持久化，不能作为保存大文件
     *
     * @param context context
     * @return CacheDoubleUtils
     */
    fun getConfigCacheInstance(context: Context): CacheDoubleUtils {

        return CacheDoubleUtils.getInstance(CacheMemoryUtils.getInstance(),
                CacheDiskUtils.getInstance(context.cacheDir))
    }

    fun parseUserInfo(context: Context): LoginByPasswordApiBean {
        val tempUserInfo = CacheConfigUtils.getConfigCacheInstance(context).getSerializable(Constant.cache_user_info)
        return if (tempUserInfo != null) {
            tempUserInfo as LoginByPasswordApiBean
        } else {
            LoginByPasswordApiBean()
        }
    }



    fun parseSearchTag(context: Context): SearchHistoryTag {
        val tempSearchTag = CacheConfigUtils.getConfigCacheInstance(context).getSerializable(Constant.cache_search_tag)

        return if (tempSearchTag != null) {
            tempSearchTag as SearchHistoryTag
        } else {
            val searchHistoryTag = SearchHistoryTag()
            searchHistoryTag.historyTextList = listOf()
            return searchHistoryTag
        }
    }

}

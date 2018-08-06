package com.advertisement.cashcow.module.main.search

import com.advertisement.cashcow.common.network.bean.information.SearchHotTagApiBean

/**
 * 作者：吴国洪 on 2018/6/19
 * 描述：搜索模块Item实体
 * 一个列表包含搜索输入框，标签，搜索历史
 */
class SearchBean(var type: String) {

    companion object {
        val tagType = "tagType"
        val sessionType = "sessionType"
        val historyType = "historyType"
    }

    //////////////////////////////////数据集////////////////////////////////////////////////
    /***
     * session条项
     */
    var sessionTypeEntity: SessionTypeItem? = null
    /***
     *  history条项
     */
    var historyTypeEntity: HistoryTypeItem? = null

    var tagTypeEntity: TagTypeItem? = null
    //////////////////////////////////子数据集////////////////////////////////////////////////

    /**
     * HotTag数据
     * @param hotTagList keyWord
     */
    data class TagTypeItem(val hotTagList:List<SearchHotTagApiBean.ResultDataBean>)

    /**
     * Session数据
     * @param sessionText 文本
     */
    data class SessionTypeItem(val sessionText:String)
    /**
     * History数据
     * @param historyText 文本
     */
    data class HistoryTypeItem(val historyText:String)

}

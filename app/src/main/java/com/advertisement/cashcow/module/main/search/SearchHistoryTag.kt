package com.advertisement.cashcow.module.main.search

import java.io.Serializable

/**
 * 作者：吴国洪 on 2018/7/17
 * 描述：历史搜索文本
 */
class SearchHistoryTag : Serializable {
    companion object {
        private const val serialVersionUID = 1L

        const val HistoryCount = 5
    }

    var historyTextList: List<String>? = null

}

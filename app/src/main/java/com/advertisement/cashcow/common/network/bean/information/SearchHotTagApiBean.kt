package com.advertisement.cashcow.common.network.bean.information

import com.advertisement.cashcow.common.base.BaseApiBean

/**
 * 作者：吴国洪 on 2018/7/17
 * 描述：热门搜索tag实体类
 */
class SearchHotTagApiBean : BaseApiBean() {

    var resultData: List<ResultDataBean>? = null

    class ResultDataBean {
        /**
         * num : 4
         * keyword : 水分子
         */

        var num: Int = 0
        var keyword: String? = null
    }
}

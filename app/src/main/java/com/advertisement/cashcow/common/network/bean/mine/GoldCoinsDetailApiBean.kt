package com.advertisement.cashcow.common.network.bean.mine

import com.advertisement.cashcow.common.base.BaseApiBean

/**
 * 作者：吴国洪 on 2018/8/8
 * 描述：金币详情实体
 */
class GoldCoinsDetailApiBean : BaseApiBean() {

    /**
     * rows : [{"gold":30,"redpkt":"吃鸡游戏注册领红包","receivetime":"2018-08-07 17:10:55","status":"审核中"},{"gold":30,"redpkt":"王者荣耀注册领红包","receivetime":"2018-08-07 17:11:27","status":"审核中"}]
     * pageNo : 1
     * pageSize : 5
     * total : 2
     * pages : 1
     * flag : null
     */

    var pageNo: Int = 0
    var pageSize: Int = 0
    var total: Int = 0
    var pages: Int = 0
    var flag: Any? = null
    var rows: List<RowsBean>? = null

    class RowsBean {
        /**
         * gold : 30
         * redpkt : 吃鸡游戏注册领红包
         * receivetime : 2018-08-07 17:10:55
         * status : 审核中
         */

        var gold: Int = 0
        var redpkt: String? = null
        var receivetime: String? = null
        var status: String? = null
    }
}

package com.advertisement.cashcow.common.network.bean.mine

import com.advertisement.cashcow.common.base.BaseApiBean

/**
 * 作者：吴国洪 on 2018/8/8
 * 描述：金币详情实体
 */
class GoldCoinsDetailStatisticsApiBean : BaseApiBean() {

    /**
     * resultData : [{"gold":140,"status":"审核中"},{"gold":40,"status":"已通过"},{"gold":40,"status":"未通过"}]
     * resultCode : 0
     * resultMsg :
     */

    var resultCode: String? = null
    var resultMsg: String? = null
    var resultData: List<ResultDataBean>? = null

    class ResultDataBean {
        /**
         * gold : 140
         * status : 审核中
         */

        var gold: Int = 0
        var status: String? = null
    }
}

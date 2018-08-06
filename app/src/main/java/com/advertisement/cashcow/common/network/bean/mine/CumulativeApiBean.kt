package com.advertisement.cashcow.common.network.bean.mine

import com.advertisement.cashcow.common.base.BaseApiBean

/**
 * 作者：吴国洪 on 2018/7/12
 * 描述：累计收益返回数据实体
 */
class CumulativeApiBean :BaseApiBean(){
    /**
     * rows : [{"recordtime":"2018-07-04 11:36:40","benefit":22},{"recordtime":"2018-07-03 11:37:02","benefit":23},{"recordtime":"2018-07-02 11:37:17","benefit":333},{"recordtime":"2018-07-01 11:37:33","benefit":26},{"recordtime":"2018-06-30 11:37:46","benefit":333},{"recordtime":"2018-07-10 11:32:56","benefit":3},{"recordtime":"2018-07-09 11:35:28","benefit":4.5},{"recordtime":"2018-07-08 11:35:46","benefit":32},{"recordtime":"2018-07-07 11:36:04","benefit":1},{"recordtime":"2018-07-06 11:36:11","benefit":3}]
     * pageNo : 1
     * pageSize : 10
     * total : 12
     * pages : 2
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
         * recordtime : 2018-07-04 11:36:40
         * benefit : 22
         */

        var recordtime: String? = null
        var benefit: String? = null
    }
}

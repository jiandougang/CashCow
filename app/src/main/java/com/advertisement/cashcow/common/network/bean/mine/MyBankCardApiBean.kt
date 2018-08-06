package com.advertisement.cashcow.common.network.bean.mine

import com.advertisement.cashcow.common.base.BaseApiBean

/**
 * 作者：Administrator on 2018/7/14 0014
 * 描述：
 * 接口：
 */
class MyBankCardApiBean : BaseApiBean() {

    /**
     * resultData : [{"bindtime":"2018-07-12 10:18:06","name":"李云龙","bandname":"招商银行","bindid":"018b7b9b-a155-4ab2-81ab-6e75a3a94bbd4","account":"6226090251914772"},{"bindtime":"2018-07-12 10:18:06","name":"李云龙","bandname":"建设银行","bindid":"018b7b9b-a155-4ab2-81ab-6e75a3a94bbd5","account":"6226090251914772"},{"bindtime":"2018-07-12 10:18:06","name":"李云龙","bandname":"中国银行","bindid":"018b7b9b-a155-4ab2-81ab-6e75a3a94bbd6","account":"6226090251914772"},{"bindtime":"2018-07-12 10:18:06","name":"李云龙","bandname":"农业银行","bindid":"018b7b9b-a155-4ab2-81ab-6e75a3a94bbd7","account":"6226090251914772"},{"bindtime":"2018-07-12 10:18:06","name":"李云龙","bandname":"工商银行","bindid":"018b7b9b-a155-4ab2-81ab-6e75a3a94bbd8","account":"6226090251914772"}]
     * resultCode : 0
     * resultMsg :
     */

    var resultCode: String? = null
    var resultMsg: String? = null
    var resultData: List<ResultDataBean>? = null

    class ResultDataBean {
        /**
         * bindtime : 2018-07-12 10:18:06
         * name : 李云龙
         * bandname : 招商银行
         * bindid : 018b7b9b-a155-4ab2-81ab-6e75a3a94bbd4
         * account : 6226090251914772
         */

        var bindtime: String? = null
        var name: String? = null
        var bandname: String? = null
        var bindid: String? = null
        var account: String? = null
        var userid: String? = null

    }
}

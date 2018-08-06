package com.advertisement.cashcow.common.network.bean.mine

import com.advertisement.cashcow.common.base.BaseApiBean

/**
 * 作者：Administrator on 2018/7/17 0017
 * 描述：
 * 接口：
 */
class MyBankCardBindingApiBean:BaseApiBean() {

    companion object {
        val bindId = "bindId"
    }

    /**
     * resultData : {"bindtime":"2018-07-17 10:17:28","name":"2","bandname":"2","userid":"1","account":"2","bindid":"cba26d99-135e-4b59-ae55-2322404d223f"}
     * resultCode : 0
     * resultMsg : 绑定银行卡成功
     */

    var resultData: ResultDataBean? = null
    var resultCode: String? = null
    var resultMsg: String? = null

    class ResultDataBean {
        /**
         * bindtime : 2018-07-17 10:17:28
         * name : 2
         * bandname : 2
         * userid : 1
         * account : 2
         * bindid : cba26d99-135e-4b59-ae55-2322404d223f
         */

        var bindtime: String? = null
        var name: String? = null
        var bandname: String? = null
        var userid: String? = null
        var account: String? = null
        var bindid: String? = null
    }
}

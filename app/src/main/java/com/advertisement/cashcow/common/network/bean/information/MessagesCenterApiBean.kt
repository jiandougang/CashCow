package com.advertisement.cashcow.common.network.bean.information

import com.advertisement.cashcow.common.base.BaseApiBean

/**
 * 作者：吴国洪 on 2018/7/18
 * 描述：消息通知实体
 */
class MessagesCenterApiBean:BaseApiBean() {


    /**
     * rows : [{"news":"【广州银行】申请信用卡时，请上传申请凭证","unreadflag":"1","recordtime":"2018-07-17 17:18:40","id":"bd0293a9-1ed9-43d4-b18b-8e1cf74ce47b"},{"news":"【浦发银行】申请信用卡时，请上传申请凭证","unreadflag":"1","recordtime":"2018-07-17 17:18:30","id":"ead1ca5a-3201-4119-92f5-98c080bfb67f"},{"news":"【信用卡返现】申请信用卡时，请上传申请凭证","unreadflag":"1","recordtime":"2018-07-16 16:48:34","id":"aaa5dab6-34a6-4d09-9cf9-5543d3de4f2d"},{"news":"摇钱树APP计划于2018年7月底上线","unreadflag":"1","recordtime":"2018-07-16 11:04:30","id":"409bc526-73ec-4244-b4d1-ac111c00838b"}]
     * pageNo : 1
     * pageSize : 10
     * total : 4
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
         * news : 【广州银行】申请信用卡时，请上传申请凭证
         * unreadflag : 1
         * recordtime : 2018-07-17 17:18:40
         * id : bd0293a9-1ed9-43d4-b18b-8e1cf74ce47b
         */

        var news: String? = null
        var unreadflag: String? = null
        var recordtime: String? = null
        var id: String? = null
    }
}

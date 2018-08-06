package com.advertisement.cashcow.common.network.bean.information

import com.advertisement.cashcow.common.base.BaseApiBean

/**
 * 作者：Administrator on 2018/7/15 0015
 * 描述：
 * 接口：
 */
class InformationBannerAdApiBean : BaseApiBean() {

    var resultData: List<ResultDataBean>? = null

    class ResultDataBean {
        /**
         * appaddr :
         * filesrc : http://192.168.5.100:8080/moneyTree//resources/images/6bf9ceb0-d78b-4568-b846-b93a012c12c9/IMG_7539.JPG
         * linkaddr : http://www.df55538.com
         * id : 79150fe2-b67e-4d20-8cc1-01dfcf586d651
         * showtype : 4
         * adsc : 广告时长
         */

        var appaddr: String? = null
        var filesrc: String? = null
        var linkaddr: String? = null
        var id: String? = null
        var showtype: Int = 0
        var adsc: Int = 0

    }
}

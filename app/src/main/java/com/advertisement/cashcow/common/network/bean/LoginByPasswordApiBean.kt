package com.advertisement.cashcow.common.network.bean

import com.advertisement.cashcow.common.base.BaseApiBean
import java.io.Serializable

/**
 * 作者：吴国洪 on 2018/6/25
 */
class LoginByPasswordApiBean : BaseApiBean(), Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }


    /**
     * jsessionid : 64B300C8BBBF8198A8779CF27FBF4AE3
     * resultData : {"level":1,"openid":"12","sex":"男","opendate":1531736876000,"dealpass":"b05034654830577525631c5a9c366a4b","avatar":"http://192.168.5.100:8080/moneyTree/resources/images/avatar/clip_temp.jpg","remaining":100.06,"password":"f1ffe3555d3f9F1e14e1859b301bdd55","earnings":0.06,"phone":"18665756471","name":"吴国洪","nickname":"fuck card","id":"018b7b9b-a155-4ab2-81ab-6e75a3a94bbd","hisearnings":100.06,"isauth":"否"}
     * resultCode : 0
     * resultMsg : 用户登录成功
     */

    var jsessionid: String? = null
    var resultData: ResultDataBean? = null
    var resultCode: String? = null
    var resultMsg: String? = null

    class ResultDataBean: Serializable {

        companion object {
            private const val serialVersionUID = 1L
        }
        /**
         * level : 1
         * openid : 12
         * sex : 男
         * opendate : 1531736876000
         * dealpass : b05034654830577525631c5a9c366a4b
         * avatar : http://192.168.5.100:8080/moneyTree/resources/images/avatar/clip_temp.jpg
         * remaining : 100.06
         * password : f1ffe3555d3f9F1e14e1859b301bdd55
         * earnings : 0.06
         * phone : 18665756471
         * name : 吴国洪
         * nickname : fuck card
         * id : 018b7b9b-a155-4ab2-81ab-6e75a3a94bbd
         * hisearnings : 100.06
         * isauth : 否
         */

        var level: Int = 0
        var openid: String? = null
        var sex: String? = null
        var opendate: Long = 0
        var dealpass: String? = null
        var avatar: String? = null
        var remaining: Double = 0.toDouble()
        var password: String? = null
        var earnings: Double = 0.toDouble()
        var phone: String? = null
        var name: String? = null
        var nickname: String? = null
        var id: String? = null
        var hisearnings: Double = 0.toDouble()
        var isauth: String? = null
    }
}


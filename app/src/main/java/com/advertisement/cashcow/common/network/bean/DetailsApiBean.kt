package com.advertisement.cashcow.common.network.bean

import com.advertisement.cashcow.common.base.BaseApiBean

/**
 * 作者： 吴国洪 on 2018/7/5
 * 描述： 广告详情接口实体
 */

class DetailsApiBean : BaseApiBean() {

    /**
     * qq :
     * wx :
     * appaddr : http://www.df55538.com
     * isReceived : 0
     * video :
     * storestatus : 0
     * title : 六合彩特码48.5码，北京赛车9.9倍
     * addedtime : 2018-06-26 16:46:18
     * content : ["六合彩特码48.5码，北京赛车9.9倍，重庆时时彩9.85倍，分分彩9.85倍<","http://192.168.5.100:8080/moneyTree/resources/videos/周建华公司小视频.mp4",">","http://192.168.5.100:8080/moneyTree//resources/images/6bf9ceb0-d78b-4568-b846-b93a012c12c9/彩票素材.gif"]
     * gold : 50
     * appname : 水分子
     * phone :
     * linkaddr :
     * company : 金钻彩票
     * id : 7f738e33-b7f3-4c8a-9263-8be045116372
     * storenum : 3
     * adsc : 3
     */

    var qq: String? = null
    var wx: String? = null
    var appaddr: String? = null
    var isReceived: String? = null
    var video: String? = null
    var storestatus: String? = null
    var title: String? = null
    var addedtime: String? = null
    var gold: Int = 0
    var appname: String? = null
    var phone: String? = null
    var linkaddr: String? = null
    var company: String? = null
    var id: String? = null
    var storenum: Int = 0
    var adsc: Int = 0
    var content: List<String>? = null
}

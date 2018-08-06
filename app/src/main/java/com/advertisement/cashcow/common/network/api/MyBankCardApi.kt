package com.advertisement.cashcow.common.network.api

import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.advertisement.cashcow.common.network.bean.mine.MyBankCardApiBean
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

/**
 * 作者：吴国洪 on 2018/7/14
 * 描述：我的银行卡接口
 */
interface MyBankCardApi {

    companion object {
        var requestGetBankCards = "requestGetBankCards"
        var requestUnbindBankCard = "requestUnbindBankCard"


    }


    /**
     * 获取绑定银行卡
     *
     * @param userid 用户id
     *
     */
    @GET("member/getBandCards.do")
    fun requestGetBankCards(@QueryMap maps: Map<String, String>): Observable<MyBankCardApiBean>

    /**
     * 解绑银行卡
     *
     * @param bindid 银行卡绑定id
     *
     */
    @GET("member/unbindBandCard.do")
    fun requestUnbindBankCard(@QueryMap maps: Map<String, String>): Observable<TextApiBean>
}

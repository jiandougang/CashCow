package com.advertisement.cashcow.module.main.mine.myBankCard

import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter


/**
 * Created by 吴国洪 on 2018/05/28.
 * Information 契约类
 */

interface MyBankCardsContract {

    interface View : IBaseView

    interface Presenter : IPresenter<View> {

        /**
         * 获取绑定银行卡
         * @param userId 用户id)
         */
        fun requestGetBankCards(userId: String)

        /**
         * 解绑银行卡
         *
         * @param bindid 银行卡绑定id
         *
         */
        fun requestUnbindBankCard(bindId: String)

        /**
         * 重置交易密码
         *
         * @param userid 用户id
         * @param bandname 银行名称
         * @param account 卡号
         * @param name 真实姓名
         */
        fun requestBindBandCard(userId: String, bandName: String, account: String, name: String)


        fun initBankCardList(): ArrayList<SelectBankCardBean>
    }


}
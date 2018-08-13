package com.advertisement.cashcow.module.main.mine.goldCoinsDetail

import android.content.Context
import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter



interface GoldCoinsDetailContract {

    interface View : IBaseView

    interface Presenter : IPresenter<View> {

        fun initAllData()

        /**
         * 领取红包（金币）列表
         *
         * @param context context
         * @param userId 用户Id
         * @param currentPage 当前页
         * @param status 状态 1、2、3分别对应审核中、已通过、未通过3个状态数据
         */
        fun requestGetRedPkts(context: Context, userId: String, currentPage:Int, status: String)

        /**
         * 领取红包（金币）列表
         *
         * @param userId 用户Id
         */
        fun requestCountPktByUserId(context: Context, userId: String)
    }


}
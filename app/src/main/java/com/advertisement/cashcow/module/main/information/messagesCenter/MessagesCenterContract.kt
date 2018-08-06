package com.advertisement.cashcow.module.main.information.messagesCenter

import com.advertisement.cashcow.common.base.IBaseView
import com.advertisement.cashcow.common.base.IPresenter


/**
 * Created by 吴国洪 on 2018/07/78.
 * MessagesCenter 契约类
 */

interface MessagesCenterContract {

    interface View : IBaseView

    interface Presenter : IPresenter<View> {

        /**
         * 获取消息列表
         */
        fun requestQueryNewsByUserID(userId: String, page: String)

        /**
         * 消息“标识已读”事件
         */
        fun requestReadedNews(userId: String, newsId: String)

    }


}
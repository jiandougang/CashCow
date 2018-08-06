package com.advertisement.cashcow.module.main.information.messagesCenter

import android.os.Bundle
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_notification_message.*

/**
 * 作者：吴国洪 on 2018/7/18
 * 描述：通知消息模块
 */

open class NoticMessageFragment : BaseFragment() {

    private var time: String? = null
    private var message: String? = null

    override fun lazyLoad() {
    }


    override fun getLayoutId(): Int = R.layout.fragment_notification_message


    companion object {
        fun getInstance(time: String, message: String): NoticMessageFragment {
            val fragment = NoticMessageFragment()
            val bundle = Bundle()
            fragment.time = time
            fragment.message = message
            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * 初始化 ViewI
     */
    override fun initView() {
        tv_message.text = this.message
        tv_time.text = this.time
    }


}

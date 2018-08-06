package com.advertisement.cashcow.module.main.information.messagesCenter

import android.content.Context
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.network.bean.information.MessagesCenterApiBean
import com.advertisement.cashcow.widget.recycleview.ViewHolder
import com.advertisement.cashcow.widget.recycleview.adapter.CommonAdapter
import com.hazz.kotlinmvp.view.recyclerview.MultipleType
import java.util.*


/**
 * 作者：吴国洪 on 2018/7/18
 * 描述：消息中心模块列表 Adapter
 */

open class MessagesCenterAdapter(mContext: Context, data: ArrayList<MessagesCenterApiBean.RowsBean>) :
        CommonAdapter<MessagesCenterApiBean.RowsBean>(mContext, data, object : MultipleType<MessagesCenterApiBean.RowsBean> {
            override fun getLayoutId(item: MessagesCenterApiBean.RowsBean, position: Int): Int {
                return R.layout.item_messages_center
            }
        }) {


    fun clear(startFormIndex: Int) {
        data.removeAll(data.drop(startFormIndex))
        notifyDataSetChanged()
    }


    /**
     * 绑定数据
     */
    override fun bindData(holder: ViewHolder, data: MessagesCenterApiBean.RowsBean, position: Int) {
        if (data.unreadflag != "1"){
            holder.getView<View>(R.id.v_reading_status).visibility = View.GONE
        }
    }
}



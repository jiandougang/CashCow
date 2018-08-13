package com.advertisement.cashcow.module.main.mine.goldCoinsDetail

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.network.bean.mine.GoldCoinsDetailApiBean
import com.advertisement.cashcow.widget.recycleview.ViewHolder
import com.advertisement.cashcow.widget.recycleview.adapter.CommonAdapter
import java.util.*


/**
 * 作者：吴国洪 on 2018/8/08
 * 描述：金币详情列表 Adapter
 */

class GoldCoinsDetailAdapter(context: Context, data: ArrayList<GoldCoinsDetailApiBean.RowsBean>, layoutId: Int)
    : CommonAdapter<GoldCoinsDetailApiBean.RowsBean>(context, data, layoutId) {


    @SuppressLint("SetTextI18n")
    override fun bindData(holder: ViewHolder, data: GoldCoinsDetailApiBean.RowsBean, position: Int) {
        holder.getView<TextView>(R.id.tv_title).text = data.redpkt
        holder.getView<TextView>(R.id.tv_time).text = data.receivetime
        holder.getView<TextView>(R.id.tv_coins).text = "+" + data.gold.toString()
    }


    /**
     *  得到 RecyclerView Item 数量（Banner 作为一个 item）
     */
    override fun getItemCount(): Int {
        return data.size
    }

}
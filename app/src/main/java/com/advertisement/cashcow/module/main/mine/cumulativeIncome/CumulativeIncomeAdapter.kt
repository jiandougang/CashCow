package com.advertisement.cashcow.module.main.mine.cumulativeIncome

import android.content.Context
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.network.bean.mine.CumulativeApiBean
import com.advertisement.cashcow.widget.recycleview.ViewHolder
import com.advertisement.cashcow.widget.recycleview.adapter.CommonAdapter
import com.hazz.kotlinmvp.view.recyclerview.MultipleType
import java.util.*


/**
 * 作者：吴国洪 on 2018/5/29
 * 描述：详情模块列表 Adapter
 */

class CumulativeIncomeAdapter(mContext: Context, data: ArrayList<CumulativeApiBean.RowsBean>) :
        CommonAdapter<CumulativeApiBean.RowsBean>(mContext, data, object : MultipleType<CumulativeApiBean.RowsBean> {
            override fun getLayoutId(item: CumulativeApiBean.RowsBean, position: Int): Int {
                return R.layout.item_cumulative_income
            }
        }) {

    init {

    }

    /**
     * 添加视频的详细信息
     */
    fun addData(item: CumulativeApiBean.RowsBean) {
        data.clear()
        notifyDataSetChanged()
        data.add(item)
        notifyItemInserted(0)

    }

    override fun bindData(holder: ViewHolder, data: CumulativeApiBean.RowsBean, position: Int) {
        holder.getView<TextView>(R.id.tv_record_time).text = data.recordtime
        holder.getView<TextView>(R.id.tv_benefit).text = data.benefit
    }

}



package com.advertisement.cashcow.module.main.information.subInformation

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.widget.ImageView
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseActivity
import com.advertisement.cashcow.module.main.information.InformationBean
import com.advertisement.cashcow.widget.recycleview.ViewHolder
import com.advertisement.cashcow.widget.recycleview.adapter.CommonAdapter
import java.util.*


/**
 * 作者：吴国洪 on 2018/6/07
 * 描述：资讯模块列表 Adapter
 */

class SubInformationFilterSelectAdapter(context: Context, data: ArrayList<InformationBean.FilterEntity>, layoutId: Int)
    : CommonAdapter<InformationBean.FilterEntity>(context, data, layoutId) {

    private var category = ""
    private var type = ""

    /**
     * 将必要参数传递出去
     *
     * @param holder
     * @param data
     * @param position
     */
    override fun bindData(holder: ViewHolder, data: InformationBean.FilterEntity, position: Int) {
        holder.getView<ImageView>(R.id.iv_filter_type).setImageResource(data.resId)
        holder.getView<TextView>(R.id.tv_filter_type).text = data.text

        holder.getView<ConstraintLayout>(R.id.cl_filter_type).setOnClickListener {
            (mContext as BaseActivity).start(SubInformationFragment.getInstance(data.text,this.category,this.type, data.text))
        }
    }

    fun setParameter(category: String, type: String) {
        this.category = category
        this.type = type
    }

    /**
     *  得到 RecyclerView Item 数量（Banner 作为一个 item）
     */
    override fun getItemCount(): Int {
        return data.size
    }


}
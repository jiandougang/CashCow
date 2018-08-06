package com.advertisement.cashcow.module.main.information

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.widget.ImageView
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseActivity
import com.advertisement.cashcow.common.manager.EmptyActivity
import com.advertisement.cashcow.module.main.information.subInformation.SubInformationFragment
import com.advertisement.cashcow.widget.recycleview.ViewHolder
import com.advertisement.cashcow.widget.recycleview.adapter.CommonAdapter
import java.util.*


/**
 * 作者：吴国洪 on 2018/6/07
 * 描述：资讯模块列表 Adapter
 */

class InformationFilterSelectAdapter(context: Context, data: ArrayList<InformationBean.FilterEntity>, layoutId: Int)
    : CommonAdapter<InformationBean.FilterEntity>(context, data, layoutId) {

    private var category = ""
    private var onClickListener: OnClickListener? = null
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
           if (onClickListener!=null){
               onClickListener!!.callBack(data,position)
           }

            val intent = Intent(mContext, EmptyActivity::class.java)
            intent.putExtra(EmptyActivity.Activity_Key, SubInformationFragment.javaClass.name)
            intent.putExtra(SubInformationFragment.Category, this.category)
            intent.putExtra(SubInformationFragment.Type, data.text)
            (mContext as BaseActivity).startActivity(intent)
            (mContext as BaseActivity).overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
        }
    }


    /**
     *  得到 RecyclerView Item 数量（Banner 作为一个 item）
     */
    override fun getItemCount(): Int {
        return data.size
    }


    fun setParameter(category: String) {
        this.category = category
    }

    fun setOnClickListener(onClickListener:OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun callBack(obj: InformationBean.FilterEntity, position: Int)
    }

}
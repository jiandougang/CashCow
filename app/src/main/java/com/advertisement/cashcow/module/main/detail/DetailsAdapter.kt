package com.advertisement.cashcow.module.main.detail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.widget.recycleview.ViewHolder
import com.advertisement.cashcow.widget.recycleview.adapter.CommonAdapter
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.StringUtils
import com.facebook.drawee.view.SimpleDraweeView
import com.hazz.kotlinmvp.view.recyclerview.MultipleType
import java.util.*


/**
 * 作者：吴国洪 on 2018/5/29
 * 描述：详情模块列表 Adapter
 */

class DetailsAdapter(mContext: Context, data: ArrayList<DetailsBean>) :
        CommonAdapter<DetailsBean>(mContext, data, object : MultipleType<DetailsBean> {
            override fun getLayoutId(item: DetailsBean, position: Int): Int {
                return when (data[position].type) {

                    DetailsBean.titleType ->
                        R.layout.item_details_title

                    DetailsBean.userType ->
                        R.layout.item_details_name

                    DetailsBean.contentType ->
                        R.layout.item_details_text

                    DetailsBean.pictureType ->
                        R.layout.item_details_picture

                    DetailsBean.contactType ->
                        R.layout.item_details_contact
                    else ->0
                }
            }
        }) {

    private var onClickListener: OnClickListener? = null

    /**
     * 添加视频的详细信息
     */
    fun addData(item: DetailsBean) {
        data.clear()
        notifyDataSetChanged()
        data.add(item)
        notifyItemInserted(0)

    }

    fun setOnWidgetClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    /**
     * 绑定数据
     */
    @SuppressLint("SetTextI18n")
    override fun bindData(holder: ViewHolder, data: DetailsBean, position: Int) {
        when (data.type) {
            DetailsBean.titleType -> {
                holder.getView<TextView>(R.id.tv_title).let {
                    it.text = data.titleTypeEntity?.text
                }
            }

            DetailsBean.userType -> {
                holder.getView<TextView>(R.id.tv_name).text = data.userTypeEntity?.name
                holder.getView<TextView>(R.id.tv_time).text = data.userTypeEntity?.time
            }

            DetailsBean.contentType -> {

                holder.getView<TextView>(R.id.tv_content).let {
                    it.text = data.contentTypeEntity?.text
                    it.setTextColor(Color.parseColor("#333333"))
                    it.textSize = ConvertUtils.sp2px(7f).toFloat()
                }
            }

            DetailsBean.pictureType -> {
                holder.getView<SimpleDraweeView>(R.id.sdv_pic).setImageURI(data.pictureTypeEntity?.url)
            }

            DetailsBean.contactType -> {


                holder.getView<TextView>(R.id.tv_qq).let {
                    it.visibility = if (StringUtils.isEmpty(data.contactTypeEntity?.qq))
                        View.GONE
                    else
                        View.VISIBLE
                    it.text = "QQ："+data.contactTypeEntity?.qq
                    it.setOnClickListener {
                        if (onClickListener != null) {
                            this.onClickListener!!.callBack("QQ："+data.contactTypeEntity?.qq!!)
                        }
                    }
                }


                holder.getView<TextView>(R.id.tv_wechat).let {
                    it.visibility = if (StringUtils.isEmpty(data.contactTypeEntity?.wechat))
                        View.GONE
                    else
                        View.VISIBLE
                    it.text = "微信："+data.contactTypeEntity?.wechat
                    it.setOnLongClickListener {
                        if (onClickListener != null) {
                            onClickListener!!.callBack("微信："+data.contactTypeEntity?.wechat!!)
                        }

                        true
                    }
                }



                holder.getView<TextView>(R.id.tv_phone).let {
                    it.visibility = if (StringUtils.isEmpty(data.contactTypeEntity?.phone))
                        View.GONE
                    else
                        View.VISIBLE
                    it.text = "电话："+data.contactTypeEntity?.phone
                    it.setOnClickListener {
                        if (onClickListener != null) {
                            this.onClickListener!!.callBack("电话："+data.contactTypeEntity?.phone!!)
                        }
                    }
                }

                if (holder.getView<TextView>(R.id.tv_phone).visibility == View.GONE
                        && holder.getView<TextView>(R.id.tv_wechat).visibility == View.GONE
                        && holder.getView<TextView>(R.id.tv_qq).visibility == View.GONE) {
                    holder.getView<ConstraintLayout>(R.id.cl_contact).visibility = View.GONE
                }
            }

            else -> {}
        }
    }


    interface OnClickListener {
        fun callBack(text: String)
    }
}



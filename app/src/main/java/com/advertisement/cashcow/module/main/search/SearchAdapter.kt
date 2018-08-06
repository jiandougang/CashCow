package com.advertisement.cashcow.module.main.search

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.widget.recycleview.ViewHolder
import com.advertisement.cashcow.widget.recycleview.adapter.CommonAdapter
import com.blankj.utilcode.util.ConvertUtils
import com.google.android.flexbox.FlexboxLayout
import com.hazz.kotlinmvp.view.recyclerview.MultipleType
import java.util.*


/**
 * 作者：吴国洪 on 2018/5/29
 * 描述：搜索模块列表 Adapter
 */

class SearchAdapter(mContext: Context, data: ArrayList<SearchBean>) :
        CommonAdapter<SearchBean>(mContext, data, object : MultipleType<SearchBean> {
            override fun getLayoutId(item: SearchBean, position: Int): Int {
                return when (data[position].type) {

                    SearchBean.tagType ->
                        R.layout.item_search_tag

                    SearchBean.sessionType ->
                        R.layout.item_search_session

                    SearchBean.historyType ->
                        R.layout.item_search_history
                    else ->0
                }
            }
        }) {

    var onClickListener: OnClickListener? = null


    //局部刷新关键：带payload的这个onBindViewHolder方法必须实现
    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            //注意：payloads的size总是1
            val payload = payloads[0] as String
            when (payload) {
                SearchBean.tagType -> {

                    val fl = holder.getView<FlexboxLayout>(R.id.fl_tag)

                    val params = FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT)
                    params.leftMargin = ConvertUtils.dp2px(12f)
                    params.rightMargin = ConvertUtils.dp2px(12f)
                    params.topMargin = ConvertUtils.dp2px(12f)
                    params.bottomMargin = ConvertUtils.dp2px(12f)
                    val tempData = data[1]
                    if (tempData.tagTypeEntity != null) {
                        for (i in tempData.tagTypeEntity?.hotTagList!!.indices) {
                            val obj = tempData.tagTypeEntity?.hotTagList!![i]
                            val textView = LayoutInflater.from(mContext).inflate(R.layout.item_search_tag_item, null, false) as TextView

                            textView.text = obj.keyword
                            textView.layoutParams = params
                            textView.setOnClickListener {
                                if (onClickListener != null) {
                                    onClickListener!!.callBack(position, obj.keyword.toString())
                                }
                            }
                            fl.addView(textView)
                        }
                    }

                }
            }
        }
    }

    /**
     * 绑定数据
     */
    @SuppressLint("InflateParams")
    override fun bindData(holder: ViewHolder, data: SearchBean, position: Int) {
        when (data.type) {
            SearchBean.tagType -> {

            }

            SearchBean.sessionType -> {
                val tvSession = holder.getView<TextView>(R.id.tv_session)
                tvSession.text = data.sessionTypeEntity!!.sessionText
            }

            SearchBean.historyType -> {

                val tvHistory = holder.getView<TextView>(R.id.tv_history)
                tvHistory.let {
                    it.text = data.historyTypeEntity!!.historyText
                    it.setOnClickListener {
                        if (onClickListener != null) {
                            onClickListener!!.callBack(position, data.historyTypeEntity!!.historyText)
                        }
                    }
                }
            }
        }

    }


    fun setClickListener(clickListener: OnClickListener) {
        this.onClickListener = clickListener
    }

    interface OnClickListener {
        fun callBack(position: Int, text: String)
    }
}



package com.advertisement.cashcow.module.main.mine.myBankCard

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.widget.recycleview.ViewHolder
import com.advertisement.cashcow.widget.recycleview.adapter.CommonAdapter
import com.hazz.kotlinmvp.view.recyclerview.MultipleType
import java.util.*


/**
 * 作者：吴国洪 on 2018/7/18
 * 描述：底部选择银行卡弹窗
 */

class DialogSelectBankCardsAdapter(mContext: Context, data: ArrayList<SelectBankCardBean>) :
        CommonAdapter<SelectBankCardBean>(mContext, data, object : MultipleType<SelectBankCardBean> {
            override fun getLayoutId(item: SelectBankCardBean, position: Int): Int {
                return R.layout.item_select_bank_card
            }
        }) {
    private var lastSelectPosition = -1

    fun setSelectState(position: Int) {
        if (lastSelectPosition != -1
                && lastSelectPosition != position) {
            getDataList()[lastSelectPosition].isSelected = false
        }
        getDataList()[position].isSelected = !getDataList()[position].isSelected
        lastSelectPosition = position
    }

    fun getLastSelectPosition(): Int {
        return this.lastSelectPosition
    }

    fun getSelectItem(): SelectBankCardBean? {
        return if (lastSelectPosition != -1) {
            getDataList()[lastSelectPosition]
        } else {
            null
        }
    }

    //局部刷新关键：带payload的这个onBindViewHolder方法必须实现
    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            //注意：payloads的size总是1
            val payload = payloads[0] as String
            when (payload) {
                SelectBankCardBean.payloads -> {
                    val iv_select_bank_card = holder.getView<ImageView>(R.id.iv_select_bank_card)
                    if (getDataList()[position].isSelected) {
                        iv_select_bank_card.visibility = View.VISIBLE
                    } else {
                        iv_select_bank_card.visibility = View.GONE
                    }
                }
            }
        }
    }


    override fun bindData(holder: ViewHolder, data: SelectBankCardBean, position: Int) {
        val iv_select_bank_card = holder.getView<ImageView>(R.id.iv_select_bank_card)
        holder.getView<TextView>(R.id.tv_select_bank_card).text = getDataList()[position].text

        holder.getView<ConstraintLayout>(R.id.cl_select_bank_card).setOnClickListener {
            data.isSelected = !data.isSelected

            if (data.isSelected) {
                iv_select_bank_card.visibility = View.VISIBLE
            } else {
                iv_select_bank_card.visibility = View.GONE
            }

        }
    }


}



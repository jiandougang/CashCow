package com.advertisement.cashcow.module.main.mine.myBankCard

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.network.bean.mine.MyBankCardApiBean
import com.advertisement.cashcow.widget.recycleview.ViewHolder
import com.advertisement.cashcow.widget.recycleview.adapter.CommonAdapter
import com.hazz.kotlinmvp.view.recyclerview.MultipleType


/**
 * 作者：吴国洪 on 2018/5/29
 * 描述：详情模块列表 Adapter
 */

class MyBankCardsAdapter(mContext: Context, data: ArrayList<MyBankCardApiBean.ResultDataBean>) :
        CommonAdapter<MyBankCardApiBean.ResultDataBean>(mContext, data, object : MultipleType<MyBankCardApiBean.ResultDataBean> {
            override fun getLayoutId(item: MyBankCardApiBean.ResultDataBean, position: Int): Int {
                return R.layout.item_my_bank_card
            }
        }) {
    private var onWidgetClickListener: OnWidgetClickListener? = null

    fun setOnDelListener(onWidgetClickListener: OnWidgetClickListener) {
        this.onWidgetClickListener = onWidgetClickListener
    }

    override fun bindData(holder: ViewHolder, data: MyBankCardApiBean.ResultDataBean, position: Int) {
        holder.getView<TextView>(R.id.tv_bank).text = data.bandname
        holder.getView<TextView>(R.id.tv_bind_user_name).text = data.name
        holder.getView<TextView>(R.id.tv_bank_card_num).text = data.account
        holder.getView<ImageView>(R.id.iv_bank_card_icon).setImageResource(parseImageIcon(data.bandname!!))

        holder.getView<TextView>(R.id.tv_unbind_bank_card).setOnClickListener {
            onWidgetClickListener?.onClick(position)
        }
    }

    private fun parseImageIcon(bankName: String): Int {
        when (bankName) {
            "工商银行" ->
                return R.mipmap.bank_card_icon_icbc

            "光大银行" ->
                return R.mipmap.bank_card_icon_everbright_bank

            "建设银行" ->
                return R.mipmap.bank_card_icon_construction_bank

            "交通银行" ->
                return R.mipmap.bank_card_icon_bank_of_communications

            "民生银行" ->
                return R.mipmap.bank_card_icon_minsheng_bank

            "农商银行" ->
                return R.mipmap.bank_card_icon_agricultural_bank

            "农业银行" ->
                return R.mipmap.bank_card_icon_abc

            "招商银行" ->
                return R.mipmap.bank_card_icon_china_merchants_bank

            "中国银行" ->
                return R.mipmap.bank_card_icon_bank_of_china

            else ->
                return 0

        }
    }


    interface OnWidgetClickListener {
        fun onClick(pos: Int)
    }

}



package com.advertisement.cashcow.module.main.mine.myBankCard

import android.support.v7.util.DiffUtil
import com.advertisement.cashcow.common.network.bean.mine.MyBankCardBindingApiBean


/**
 * 作者：吴国洪 on 2018/7/24
 * 描述：判断 新旧Item是否相等
 */

class MyBankCardsDiffCallBack(private val mOldData: List<MyBankCardBindingApiBean.ResultDataBean>?, private val mNewData: List<MyBankCardBindingApiBean.ResultDataBean>?//看名字
) : DiffUtil.Callback() {

    //老数据集size
    override fun getOldListSize(): Int {
        return mOldData?.size ?: 0
    }

    //新数据集size
    override fun getNewListSize(): Int {
        return mNewData?.size ?: 0
    }

    /**
     * Called by the DiffUtil to decide whether two object represent the same Item.
     * 被DiffUtil调用，用来判断 两个对象是否是相同的Item。
     * For example, if your items have unique ids, this method should check their id equality.
     * 例如，如果你的Item有唯一的id字段，这个方法就 判断id是否相等。
     * 本例判断name字段是否一致
     *
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list
     * @return True if the two items represent the same object or false if they are different.
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldData!![oldItemPosition].bindid == mNewData!![newItemPosition].bindid
    }

    /**
     * Called by the DiffUtil when it wants to check whether two items have the same data.
     * 被DiffUtil调用，用来检查 两个item是否含有相同的数据
     * DiffUtil uses this information to detect if the contents of an item has changed.
     * DiffUtil用返回的信息（true false）来检测当前item的内容是否发生了变化
     * DiffUtil uses this method to check equality instead of [Object.equals]
     * DiffUtil 用这个方法替代equals方法去检查是否相等。
     * so that you can change its behavior depending on your UI.
     * 所以你可以根据你的UI去改变它的返回值
     * For example, if you are using DiffUtil with a
     * [RecyclerView.Adapter][android.support.v7.widget.RecyclerView.Adapter], you should
     * return whether the items' visual representations are the same.
     * 例如，如果你用RecyclerView.Adapter 配合DiffUtil使用，你需要返回Item的视觉表现是否相同。
     * This method is called only if [.areItemsTheSame] returns
     * `true` for these items.
     * 这个方法仅仅在areItemsTheSame()返回true时，才调用。
     *
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list which replaces the
     * oldItem
     * @return True if the contents of the items are the same or false if they are different.
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val beanOld = mOldData?.get(oldItemPosition)
        val beanNew = mNewData?.get(newItemPosition)
        return beanOld?.bindid.equals(beanNew?.bindid)
    }
}

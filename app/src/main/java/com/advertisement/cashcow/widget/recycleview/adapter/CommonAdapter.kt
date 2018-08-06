package com.advertisement.cashcow.widget.recycleview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.advertisement.cashcow.widget.recycleview.ViewHolder
import com.hazz.kotlinmvp.view.recyclerview.MultipleType
import com.hazz.kotlinmvp.view.recyclerview.adapter.OnItemClickListener

/**
 * 作者：吴国洪 on 2018/5/28
 * 描述：通用的 Adapter
 */
abstract class CommonAdapter<T>(var mContext: Context, var data: ArrayList<T>, //条目布局
                                private var mLayoutId: Int) : RecyclerView.Adapter<ViewHolder>() {

    private var mInflater: LayoutInflater? = null
    private var mTypeSupport: MultipleType<T>? = null

    //使用接口回调点击事件
    protected var mItemClickListener: OnItemClickListener? = null

    //使用接口回调点击事件
    private var mItemLongClickListener: OnItemLongClickListener? = null

    init {
        mInflater = LayoutInflater.from(mContext)
    }


    //需要多布局
    constructor(context: Context, data: ArrayList<T>, typeSupport: MultipleType<T>) : this(context, data, -1) {
        this.mTypeSupport = typeSupport
    }

    override fun getItemViewType(position: Int): Int {
        //多布局问题
        return mTypeSupport?.getLayoutId(data[position], position)
                ?: super.getItemViewType(position)
    }


    //指定布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (mTypeSupport != null) {
            //需要多布局
            mLayoutId = viewType
        }
        //创建view
        val view = mInflater?.inflate(mLayoutId, parent, false)
        return ViewHolder(view!!)
    }

    //更新布局
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //绑定数据
        bindData(holder, data[position], position)

        //条目点击事件
        mItemClickListener?.let {
            holder.itemView.setOnClickListener { mItemClickListener!!.onItemClick(data[position], position) }
        }

        //长按点击事件
        mItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener { mItemLongClickListener!!.onItemLongClick(data[position], position) }
        }
    }

    fun onBindItemHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {

    }


    //局部刷新关键：带payload的这个onBindViewHolder方法必须实现
    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            onBindItemHolder(holder, position, payloads)
        }

    }


    /**
     * 将必要参数传递出去
     *
     * @param holder
     * @param data
     * @param position
     */
    protected abstract fun bindData(holder: ViewHolder, data: T, position: Int)

    //返回数据数
    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.mItemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(itemLongClickListener: OnItemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener
    }


    fun remove(position: Int) {
        this.data.removeAt(position)
        notifyItemRemoved(position)

        if (position != data.size) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, this.data.size - position)
        }
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    open fun addAll(list: Collection<T>) {
        val lastIndex = this.data.size
        if (this.data.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size)
        }
    }

    open fun insert(position: Int, obj: T) {
        if (position < data.size) {
            this.data.add(position, obj)
            notifyItemInserted(position)
            notifyItemRangeChanged(position, this.data.size - position)
        }

    }

    fun setDataList(list: Collection<T>) {
        this.data.clear()
        this.data.addAll(list)
    }

    fun getDataList(): List<T> {
        return data
    }
}

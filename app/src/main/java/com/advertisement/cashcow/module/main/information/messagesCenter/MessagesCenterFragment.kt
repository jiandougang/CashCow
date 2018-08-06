package com.advertisement.cashcow.module.main.information.messagesCenter

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.InformationApi
import com.advertisement.cashcow.common.network.bean.information.MessagesCenterApiBean
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import com.hazz.kotlinmvp.view.recyclerview.adapter.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_sub_information.*

/**
 * 作者：吴国洪 on 2018/5/28
 * 描述：消息中心模块
 */

open class MessagesCenterFragment : BaseFragment(), MessagesCenterContract.View, OnItemClickListener {


    open val mPresenter by lazy { MessagesCenterPresenter() }

    private var messagesCenterAdapter: MessagesCenterAdapter? = null
    private var lRecyclerViewAdapter: LRecyclerViewAdapter? = null
    private var title: String = ""
    lateinit var arrayData: ArrayList<MessagesCenterApiBean.RowsBean>
    private var currentPage: Int = 1

    override fun lazyLoad() {
        mPresenter.attachView(this)
        arrayData = ArrayList()
        mPresenter.requestQueryNewsByUserID(CacheConfigUtils.parseUserInfo(context!!).resultData?.id.toString(), currentPage.toString())
    }


    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun getLayoutId(): Int = R.layout.fragment_sub_information


    companion object {
        fun getInstance(title: String): MessagesCenterFragment {
            val fragment = MessagesCenterFragment()
            val bundle = Bundle()
            fragment.title = title
            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * 初始化 ViewI
     */
    override fun initView() {
        messagesCenterAdapter = MessagesCenterAdapter(activity!!, arrayData)
        messagesCenterAdapter!!.setOnItemClickListener(this)
        lRecyclerViewAdapter = LRecyclerViewAdapter(messagesCenterAdapter)

        nv_bar.setTitle(this.title)
        nv_bar.let {
            it.setTitle(this.title)
            it.setOnBackClickListener { activity!!.onBackPressed() }
        }

        rv_information.let {
            it.layoutManager = linearLayoutManager
            it.adapter = lRecyclerViewAdapter
            it.itemAnimator = DefaultItemAnimator()
            it.setPullRefreshEnabled(false)
            it.setFooterViewHint("玩命加载中", "没有更多了", "")

            it.setOnLoadMoreListener {
                currentPage++
                mPresenter.requestQueryNewsByUserID(CacheConfigUtils.parseUserInfo(context!!).resultData?.id.toString(), currentPage.toString())

            }
        }
    }

    override fun onItemClick(obj: Any?, position: Int) {
        obj as MessagesCenterApiBean.RowsBean
        mPresenter.requestReadedNews(CacheConfigUtils.parseUserInfo(context!!).resultData?.id.toString(), obj.id.toString())
        start(NoticMessageFragment.getInstance(obj.recordtime.toString(),
                obj.news.toString()))
    }

    override fun preLoad(type: String, obj: Any) {
        ic_mask.visibility = View.VISIBLE
    }

    override fun afterLoad(type: String, obj: Any) {
        ic_mask.visibility = View.GONE

        if (messagesCenterAdapter?.data?.size ?: 0 > 0) {
            ic_no_content.visibility = View.GONE
            rv_information.visibility = View.VISIBLE
        }
    }

    override fun handleError(type: String, obj: Any) {
        ToastUtils.showShort(obj.toString())
    }

    override fun handleSuccess(type: String, obj: Any) {
        when (type) {
            InformationApi.requestQueryNewsByUserID -> {
                val messagesCenterApiBean = obj as MessagesCenterApiBean
                messagesCenterAdapter?.addAll(messagesCenterApiBean.rows!!)


                if (messagesCenterApiBean.pageNo == messagesCenterApiBean.pages)
                    rv_information.setNoMore(true)
            }

        }
    }
}

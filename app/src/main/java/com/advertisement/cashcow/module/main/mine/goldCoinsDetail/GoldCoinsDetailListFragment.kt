package com.advertisement.cashcow.module.main.mine.goldCoinsDetail

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.bean.mine.GoldCoinsDetailApiBean
import com.advertisement.cashcow.util.CacheConfigUtils
import com.flyco.tablayout.listener.OnTabSelectListener
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_gold_coins_detail_list.*

/**
 * 作者：吴国洪 on 2018/8/07
 * 描述：金币详情列表模块
 */

open class GoldCoinsDetailListFragment : BaseFragment(), GoldCoinsDetailContract.View, OnTabSelectListener {

    open val mGoldCoinsDetailPresenter by lazy { GoldCoinsDetailPresenter() }

    private var goldCoinsDetailAdapter: GoldCoinsDetailAdapter? = null
    private var lRecyclerViewAdapter: LRecyclerViewAdapter? = null
    private var status: String = ""
    lateinit var arrayData: ArrayList<GoldCoinsDetailApiBean.RowsBean>
    var currentPage: Int = 1

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun lazyLoad() {
        mGoldCoinsDetailPresenter.attachView(this)
        mGoldCoinsDetailPresenter.requestGetRedPkts(context, CacheConfigUtils.parseUserInfo(context).resultData?.id!!, currentPage, status)

    }

    override fun getLayoutId(): Int = R.layout.fragment_gold_coins_detail_list


    companion object {
        fun getInstance(status: String): GoldCoinsDetailListFragment {
            val fragment = GoldCoinsDetailListFragment()
            val bundle = Bundle()
            fragment.status = status
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        arrayData = ArrayList()
        goldCoinsDetailAdapter = GoldCoinsDetailAdapter(activity, arrayData, R.layout.item_coins_details)
        lRecyclerViewAdapter = LRecyclerViewAdapter(goldCoinsDetailAdapter)

        rv_content.let {
            it.layoutManager = linearLayoutManager
            it.adapter = lRecyclerViewAdapter
            it.itemAnimator = DefaultItemAnimator()
            it.setPullRefreshEnabled(false)
            it.setFooterViewHint("玩命加载中", "没有更多了", "")

            it.setOnLoadMoreListener {
                currentPage++
                mGoldCoinsDetailPresenter.requestGetRedPkts(context, CacheConfigUtils.parseUserInfo(context).resultData?.id!!, currentPage, status)
            }
        }
    }

    override fun onTabSelect(position: Int) {
    }

    override fun onTabReselect(position: Int) {
    }

    override fun handleError(type: String, obj: Any) {
    }


    override fun handleSuccess(type: String, obj: Any) {
        when (type) {
            Api.requestGetRedPkts -> {
                rv_content.refreshComplete(Api.REQUEST_COUNT)
                val coinsDetailApiBean = obj as GoldCoinsDetailApiBean
                goldCoinsDetailAdapter?.addAll(coinsDetailApiBean.rows!!)

                if (coinsDetailApiBean.pageNo == coinsDetailApiBean.pages) {
                    rv_content.setLoadMoreEnabled(false)
                    rv_content.setNoMore(true)
                }
            }
        }
    }

    override fun preLoad(type: String, obj: Any) {
    }


    override fun afterLoad(type: String, obj: Any) {
    }
}

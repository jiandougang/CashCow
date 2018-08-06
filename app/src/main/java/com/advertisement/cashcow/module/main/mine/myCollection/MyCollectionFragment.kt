package com.advertisement.cashcow.module.main.mine.myCollection

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.network.api.InformationApi
import com.advertisement.cashcow.common.network.bean.information.InformationAdApiBean
import com.advertisement.cashcow.module.main.information.*
import com.advertisement.cashcow.util.CacheConfigUtils
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import com.hazz.kotlinmvp.view.recyclerview.adapter.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_cumulative_income.*

/**
 * 作者：吴国洪 on 2018/5/28
 * 描述：资讯字模块
 */

open class MyCollectionFragment : InformationFragment(), InformationContract.View, OnItemClickListener {


    open val mCollectionPresenter by lazy { MyCollectionPresenter(MyCollectionFragment.javaClass.name) }

    private var informationAdapter: InformationAdapter? = null
    private var lRecyclerViewAdapter: LRecyclerViewAdapter? = null
    lateinit var arrayData: ArrayList<InformationBean>
    var currentPage: Int = 1

    override fun lazyLoad() {
        mPresenter = InformationPresenter(MyCollectionFragment.javaClass.name)
        mPresenter!!.attachView(this)
        mCollectionPresenter.attachView(this)
        arrayData = ArrayList()
        mCollectionPresenter.requestGetStoreAds(CacheConfigUtils.parseUserInfo(context).resultData?.id!!, currentPage.toString())
    }


    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun getLayoutId(): Int = R.layout.fragment_cumulative_income


    companion object {
        fun getInstance(title: String): MyCollectionFragment {
            val fragment = MyCollectionFragment()
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
        informationAdapter = InformationAdapter(activity, arrayData)
        informationAdapter!!.setOnItemClickListener(this)
        lRecyclerViewAdapter = LRecyclerViewAdapter(informationAdapter)

        nv_bar.let {
            it.setOnBackClickListener {
                activity.onBackPressed()
                it.setTitle(this.title)
            }
        }
        rv_data.let {
            it.layoutManager = linearLayoutManager
            it.adapter = lRecyclerViewAdapter
            it.itemAnimator = DefaultItemAnimator()
            it.setPullRefreshEnabled(false)
            it.setFooterViewHint("玩命加载中", "没有更多了", "")

            it.setOnLoadMoreListener {
                currentPage++
                mCollectionPresenter.requestGetStoreAds(CacheConfigUtils.parseUserInfo(context).resultData?.id!!, currentPage.toString())
            }
            it.setOnRefreshListener {
                currentPage = 0
                rv_data.setLoadMoreEnabled(true)
                informationAdapter?.clear(2)
                lRecyclerViewAdapter?.notifyDataSetChanged()
                mCollectionPresenter.requestGetStoreAds(CacheConfigUtils.parseUserInfo(context).resultData?.id!!, currentPage.toString())
            }
        }
    }


    override fun afterLoad(type: String, obj: Any) {
        ic_mask.visibility = View.GONE
        if (informationAdapter?.data?.size ?: 0 > 0) {
            ic_no_content.visibility = View.GONE
            rv_data.visibility = View.VISIBLE
        }
    }


    override fun handleSuccess(type: String, obj: Any) {
        when (type) {

            InformationApi.requestGetStoreAds -> {

                rv_data.refreshComplete(InformationApi.REQUEST_COUNT)
                val informationApi = obj as InformationAdApiBean
                informationAdapter?.addAll(mPresenter!!.parseAdData(informationApi.rows))

                if (informationApi.pageNo == informationApi.pages){
                    rv_data.setLoadMoreEnabled(false)
                    rv_data.setNoMore(true)
                }

            }
        }
    }
}

package com.advertisement.cashcow.module.main.information.subInformation

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.network.api.InformationApi
import com.advertisement.cashcow.common.network.bean.information.InformationAdApiBean
import com.advertisement.cashcow.module.main.information.*
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import com.hazz.kotlinmvp.view.recyclerview.adapter.OnItemClickListener
import com.othershe.nicedialog.BaseNiceDialog
import com.othershe.nicedialog.NiceDialog
import com.othershe.nicedialog.ViewConvertListener
import kotlinx.android.synthetic.main.fragment_sub_information.*

/**
 * 作者：吴国洪 on 2018/5/28
 * 描述：资讯字模块
 */

open class SubInformationFragment : InformationFragment(), InformationContract.View, OnItemClickListener {


    open val mSubInformationPresenter by lazy { SubInformationPresenter(SubInformationFragment.javaClass.name) }

    private var informationAdapter: InformationAdapter? = null
    private var lRecyclerViewAdapter: LRecyclerViewAdapter? = null
    private var category: String = ""
    private var type: String = ""
    private var zType: String = ""

    lateinit var arrayData: ArrayList<InformationBean>
    var currentPage: Int = 0

    override fun lazyLoad() {
        mPresenter = InformationPresenter(SubInformationFragment.javaClass.name)
        mPresenter!!.attachView(this)
        mSubInformationPresenter.attachView(this)
        arrayData = mSubInformationPresenter.initAllSubData(this.title, type, zType, currentPage.toString())
    }


    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun getLayoutId(): Int = R.layout.fragment_sub_information


    companion object {
        const val Category = "Category"
        const val Type = "Type"


        fun getInstance(title: String, category: String, type: String, zType: String): SubInformationFragment {
            val fragment = SubInformationFragment()
            val bundle = Bundle()
            fragment.title = title
            fragment.category = category
            fragment.type = type
            fragment.zType = zType
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        informationAdapter = InformationAdapter(activity!!, arrayData)
        informationAdapter!!.setOnItemClickListener(this)
        lRecyclerViewAdapter = LRecyclerViewAdapter(informationAdapter)

        nv_bar.let {
            it.setTitle(this.title)
            it.setOnBackClickListener { activity!!.onBackPressed() }
        }

        rv_information.let {
            it.layoutManager = linearLayoutManager
            it.adapter = lRecyclerViewAdapter!!
            it.itemAnimator = DefaultItemAnimator()
            it.setPullRefreshEnabled(true)
            it.setFooterViewHint("玩命加载中", "没有更多了", "")

            it.setOnLoadMoreListener {
                currentPage++
                mSubInformationPresenter.initAllSubData(this.title, type, zType, currentPage.toString())

            }
            it.setOnRefreshListener {
                rv_information.setLoadMoreEnabled(true)
                currentPage = 0
                informationAdapter?.clear(mSubInformationPresenter.getRecycleViewClearIndex(context!!, type))
                lRecyclerViewAdapter?.notifyDataSetChanged()
                mSubInformationPresenter.initAllSubData(this.title, type, zType, currentPage.toString())
            }
        }


    }


    override fun afterLoad(type: String, obj: Any) {
        if (informationAdapter?.data?.size ?: 0 > 0) {
            ic_no_content.visibility = View.GONE
            rv_information.visibility = View.VISIBLE
        }

    }

    override fun handleSuccess(type: String, obj: Any) {
        when (type) {

            InformationApi.requestQueryAds -> {

                rv_information.refreshComplete(InformationApi.REQUEST_COUNT)
                val informationApi = obj as InformationAdApiBean
                informationAdapter?.addAll(mSubInformationPresenter.parseAdData(informationApi.rows))



                if (informationApi.pageNo == informationApi.pages) {
                    rv_information.setLoadMoreEnabled(false)
                    rv_information.setNoMore(true)
                }
            }
        }
    }
}

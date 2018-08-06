package com.advertisement.cashcow.module.main.mine.cumulativeIncome

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.api.InformationApi
import com.advertisement.cashcow.common.network.bean.mine.CumulativeApiBean
import com.advertisement.cashcow.module.main.mine.MineContract
import com.advertisement.cashcow.module.main.mine.MinePresenter
import com.blankj.utilcode.util.ToastUtils
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_cumulative_income.*


/**
 * 作者：吴国洪 on 2018/7/12
 * 描述：累计收益页面
 */

class CumulativeIncomeFragment : BaseFragment(), View.OnClickListener, MineContract.View {


    private val mPresenter by lazy { MinePresenter() }

    private var userId: String? = null

    private var cumulativeIncomeAdapter: CumulativeIncomeAdapter? = null

    private var lRecyclerViewAdapter: LRecyclerViewAdapter? = null

    private lateinit var arrayData: ArrayList<CumulativeApiBean.RowsBean>

    private var currentPage: Int = 1


    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun getLayoutId(): Int = R.layout.fragment_cumulative_income

    companion object {
        const val User_Id = "User_Id"


        fun getInstance(userId: String): CumulativeIncomeFragment {
            val fragment = CumulativeIncomeFragment()
            val bundle = Bundle()
            fragment.userId = userId
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        cumulativeIncomeAdapter = CumulativeIncomeAdapter(activity!!, arrayData)
        lRecyclerViewAdapter = LRecyclerViewAdapter(cumulativeIncomeAdapter)

        rv_data.let {
            it.layoutManager = linearLayoutManager
            it.adapter = lRecyclerViewAdapter
            it.itemAnimator = DefaultItemAnimator()
            it.setFooterViewHint("玩命加载中", "没有更多了", "")

            it.setOnLoadMoreListener {
                currentPage++
                mPresenter.requestGetBenefits(userId!!, currentPage.toString())
            }
            it.setOnRefreshListener {
                currentPage = 0
                cumulativeIncomeAdapter?.clear()
                lRecyclerViewAdapter?.notifyDataSetChanged()
                mPresenter.requestGetBenefits(userId!!, currentPage.toString())
            }
        }
        nv_bar.setOnBackClickListener {
            activity!!.finish()
        }

        ic_no_content.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.ic_no_content -> {
                mPresenter.requestGetBenefits(userId!!, currentPage.toString())
            }
        }
    }

    override fun handleError(type: String, obj: Any) {
        ToastUtils.showShort(obj.toString())

    }


    override fun handleSuccess(type: String, obj: Any) {
        when (type) {
            Api.requestGetBenefits -> {
                rv_data.refreshComplete(InformationApi.REQUEST_COUNT)

                val cumulativeApiBean = obj as CumulativeApiBean
                cumulativeIncomeAdapter?.addAll(cumulativeApiBean.rows!!)
                if (cumulativeApiBean.pageNo == cumulativeApiBean.pages)
                    rv_data.setNoMore(true)
            }
        }

    }

    override fun preLoad(type: String, obj: Any) {
        ic_mask.visibility = View.VISIBLE

    }


    override fun afterLoad(type: String, obj: Any) {
        ic_mask.visibility = View.GONE
        if (cumulativeIncomeAdapter?.data?.size ?: 0 > 0) {
            ic_no_content.visibility = View.GONE
            rv_data.visibility = View.VISIBLE
        }
    }


    override fun lazyLoad() {
        arrayData = ArrayList()
        mPresenter.attachView(this)
        mPresenter.requestGetBenefits(userId!!, currentPage.toString())
    }
}

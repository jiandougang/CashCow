package com.advertisement.cashcow.module.main.mine.myBankCard

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.MyBankCardApi
import com.advertisement.cashcow.common.network.bean.mine.MyBankCardApiBean
import com.advertisement.cashcow.util.CacheConfigUtils
import com.advertisement.cashcow.widget.recycleview.DiffCallBack
import com.blankj.utilcode.util.ToastUtils
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import com.hazz.kotlinmvp.view.recyclerview.adapter.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_cumulative_income.*


/**
 * 作者：吴国洪 on 2018/5/28
 * 描述：资讯字模块
 */

open class MyBankCardsFragment : BaseFragment(), MyBankCardsContract.View, OnItemClickListener, MyBankCardsAdapter.OnWidgetClickListener {


    open val mPresenter by lazy { MyBankCardsPresenter() }

    private var myBankCardsAdapter: MyBankCardsAdapter? = null
    private var lRecyclerViewAdapter: LRecyclerViewAdapter? = null
    private var title: String = ""
    private lateinit var arrayData: ArrayList<MyBankCardApiBean.ResultDataBean>

    private var currentPosition = 0

    override fun lazyLoad() {
        mPresenter.attachView(this)
        arrayData = ArrayList()
        mPresenter.requestGetBankCards(CacheConfigUtils.parseUserInfo(context).resultData?.id!!)
    }


    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun getLayoutId(): Int = R.layout.fragment_cumulative_income


    companion object {
        const val Title = "Category"
        const val REQ_CODE = 666

        fun getInstance(title: String): MyBankCardsFragment {
            val fragment = MyBankCardsFragment()
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
        myBankCardsAdapter = MyBankCardsAdapter(activity!!, arrayData)
        myBankCardsAdapter!!.let {
            it.setOnDelListener(this)
            it.setOnItemClickListener(this)
        }
        lRecyclerViewAdapter = LRecyclerViewAdapter(myBankCardsAdapter)

        rv_data.let {
            it.layoutManager = linearLayoutManager
            it.adapter = lRecyclerViewAdapter
            it.itemAnimator = DefaultItemAnimator()
            it.setPullRefreshEnabled(false)
        }

        nv_bar.let {
            it.setOnRightClickListener {
                startForResult(MyBankCardsBindingFragment.getInstance(), REQ_CODE)
            }

            it.setOnBackClickListener {
                activity.onBackPressed()
            }

            it.setTitle(this.title)

            it.setRightTextAndImage("", R.mipmap.my_bank_cards_icon_add_card)

        }
    }

    override fun onClick(pos: Int) {
        if (myBankCardsAdapter?.getDataList()?.isNotEmpty()!!) {
            currentPosition = pos
            mPresenter.requestUnbindBankCard(myBankCardsAdapter?.getDataList()?.get(pos)?.bindid.toString())
        }
    }

    override fun onItemClick(obj: Any?, position: Int) {

    }

    override fun preLoad(type: String, obj: Any) {
        ic_mask.visibility = View.VISIBLE

    }

    override fun afterLoad(type: String, obj: Any) {
        ic_mask.visibility = View.GONE
        if (myBankCardsAdapter?.data?.size ?: 0 > 0) {
            ic_no_content.visibility = View.GONE
            rv_data.visibility = View.VISIBLE
        }
    }

    override fun handleError(type: String, obj: Any) {
        ToastUtils.showShort(obj.toString())

    }

    override fun handleSuccess(type: String, obj: Any) {
        when (type) {

            MyBankCardApi.requestGetBankCards -> {
                val myBankCardApiBean = obj as MyBankCardApiBean

                val diffResult = DiffUtil.calculateDiff(DiffCallBack(arrayData, myBankCardApiBean.resultData!!), true)


                diffResult.dispatchUpdatesTo(myBankCardsAdapter)

                myBankCardsAdapter?.setDataList(myBankCardApiBean.resultData!!)
//                myBankCardsAdapter?.addAll(myBankCardApiBean.resultData!!)
            }

            MyBankCardApi.requestUnbindBankCard -> {
                myBankCardsAdapter?.remove(currentPosition)
            }

        }
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle) {
        super.onFragmentResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            mPresenter.requestGetBankCards(CacheConfigUtils.parseUserInfo(context).resultData?.id!!)

        }
    }
}

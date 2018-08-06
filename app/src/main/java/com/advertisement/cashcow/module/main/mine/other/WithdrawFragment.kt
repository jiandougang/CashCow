package com.advertisement.cashcow.module.main.mine.other

import android.os.Bundle
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.bean.mine.MineApiBean
import com.advertisement.cashcow.module.main.mine.MineContract
import com.advertisement.cashcow.module.main.mine.MinePresenter
import com.blankj.utilcode.util.LogUtils
import kotlinx.android.synthetic.main.fragment_withdraw.*


/**
 * 作者：吴国洪 on 2018/7/15
 * 描述：提取现金页面
 */

class WithdrawFragment : BaseFragment(), View.OnClickListener, MineContract.View {

    private val mPresenter by lazy { MinePresenter() }

    override fun getLayoutId(): Int = R.layout.fragment_withdraw

    companion object {
        fun getInstance(): WithdrawFragment {
            val fragment = WithdrawFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        nv_bar1.setOnBackClickListener { activity.onBackPressed() }

    }

    override fun onClick(v: View?) {

        when {


        }
    }


    override fun handleError(type: String, obj: Any) {
    }


    override fun handleSuccess(type: String, obj: Any) {
        LogUtils.i((obj as MineApiBean).toString())
    }

    override fun preLoad(type: String, obj: Any) {
    }


    override fun afterLoad(type: String, obj: Any) {
    }


    override fun lazyLoad() {
        mPresenter.attachView(this)
    }
}

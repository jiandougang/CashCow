package com.advertisement.cashcow.module.main.mine.other

import android.os.Bundle
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.bean.mine.MineApiBean
import com.advertisement.cashcow.module.main.mine.MineContract
import com.advertisement.cashcow.module.main.mine.MinePresenter
import com.blankj.utilcode.util.LogUtils
import kotlinx.android.synthetic.main.fragment_password_management.*


/**
 * 作者：吴国洪 on 2018/7/08
 * 描述：密码管理面
 */

class PasswordManagementFragment : BaseFragment(), View.OnClickListener, MineContract.View {


    private val mPresenter by lazy { MinePresenter() }

    override fun getLayoutId(): Int = R.layout.fragment_password_management

    companion object {
        fun getInstance(): PasswordManagementFragment {
            val fragment = PasswordManagementFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        cl_modify_transaction_password.setOnClickListener(this)
        cl_modify_login_password.setOnClickListener(this)
        nv_bar.setOnBackClickListener { activity.onBackPressed() }

    }

    override fun onClick(v: View?) {

        when {
            v?.id == R.id.cl_modify_transaction_password -> {
                start(ModifyTradingPasswordFragment.getInstance())
            }

            v?.id == R.id.cl_modify_login_password -> {
                start(ModifyLoginPasswordFragment.getInstance())
            }
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

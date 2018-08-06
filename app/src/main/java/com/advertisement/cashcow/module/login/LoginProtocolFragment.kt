package com.advertisement.cashcow.module.login

import android.os.Bundle
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login_protocolt.*

/**
 * 作者：吴国洪 on 2018/8/02
 * 描述：登录协议页面
 */

class LoginProtocolFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_login_protocolt

    companion object {
        fun getInstance(): LoginProtocolFragment {
            val fragment = LoginProtocolFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        nv_bar1.let {
            it.setOnBackClickListener {
                activity.onBackPressed()
            }
        }
    }

    override fun lazyLoad() {
    }

}



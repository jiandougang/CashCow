package com.advertisement.cashcow.module.login.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.module.main.MainActivity
import com.advertisement.cashcow.module.main.mine.MinePresenter
import kotlinx.android.synthetic.main.fragment_register_verification_code.*

@Suppress("DEPRECATION")
/**
 * 作者：吴国洪 on 2018/6/05
 * 描述：实名认证页面
 * 接口：
 */

class AuthenticationFragment : BaseFragment(), View.OnClickListener {

    private val mPresenter by lazy { MinePresenter() }

    private var mTitle: String? = null

    override fun getLayoutId(): Int = R.layout.fragment_register_real_name_authentication

    companion object {
        fun getInstance(title: String): AuthenticationFragment {
            val fragment = AuthenticationFragment()
            val bundle = Bundle()
            fragment.mTitle = title
            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * 初始化 ViewI
     */
    override fun initView() {
        tv_confirm.setOnClickListener(this)

    }

    override fun lazyLoad() {
    }

    override fun onClick(v: View?) {
            when {
                v?.id == R.id.tv_confirm -> {
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity!!.finish()
                }
            }
    }

}

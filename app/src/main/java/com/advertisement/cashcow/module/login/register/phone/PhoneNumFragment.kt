package com.advertisement.cashcow.module.login.register.phone

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.module.login.register.verificationCode.VerificationCodeFragment
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_register_phone_num.*

/**
 * 作者：吴国洪 on 2018/6/05
 * 描述：注册-输入手机号页面
 * 接口：
 */

class PhoneNumFragment : BaseFragment(), View.OnClickListener, PhoneNumContract.View {


    private val mPresenter by lazy { PhonePresenter() }

    private var mTitle: String? = null

    override fun getLayoutId(): Int = R.layout.fragment_register_phone_num

    companion object {
        fun getInstance(title: String): PhoneNumFragment {
            val fragment = PhoneNumFragment()
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
        tv_next_step.setOnClickListener(this)
        iv_delete.setOnClickListener(this)

        nv_bar1.let {
            it.setOnBackClickListener {
               activity.onBackPressed()
            }

            it.setOnRightClickListener {
                activity.onBackPressed()
            }
        }

        et_phone.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_next_step.isEnabled = 11 == s?.length
            }

        })
    }

    override fun lazyLoad() {
        mPresenter.attachView(this)
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.tv_next_step -> {

                if (RegexUtils.isMobileExact(et_phone.text.toString()))
                    mPresenter.requestIsRegister(et_phone.text.toString())
                else
                    ToastUtils.showShort(getString(R.string.please_input_correct_format_mobile_number))
            }


            v?.id == R.id.iv_delete -> {
                et_phone.text.clear()
            }
        }
    }


    override fun handleError(type: String, obj: Any) {
        ToastUtils.showShort(obj.toString())
    }

    override fun handleSuccess(type: String, obj: Any) {
        start(VerificationCodeFragment.getInstance(et_phone.text.toString()))
    }


    override fun afterLoad(type: String, obj: Any) {
        ic_mask.visibility = View.GONE
    }

    override fun preLoad(type: String, obj: Any) {
        ic_mask.visibility = View.VISIBLE
    }

}

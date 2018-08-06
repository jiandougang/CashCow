package com.advertisement.cashcow.module.login.register.initPassword

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.InitPasswordApi
import com.advertisement.cashcow.module.login.password.LoginByPasswordFragment
import com.advertisement.cashcow.util.PwdCheckUtil
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_register_init_password.*
import me.yokeyword.fragmentation.SupportFragment

/**
 * 作者：吴国洪 on 2018/6/04
 * 描述：注册-输入密码码页面
 *
 * 接口：用户注册接口：member/register.do
 */

class InitPasswordFragment : BaseFragment(), View.OnClickListener, InitPasswordContract.View {


    private val mPresenter by lazy { InitPasswordPresenter() }

    private var phoneNum: String? = null
    private var showPassword: Boolean = false

    override fun getLayoutId(): Int = R.layout.fragment_register_init_password

    companion object {
        fun getInstance(phoneNum: String): InitPasswordFragment {
            val fragment = InitPasswordFragment()
            fragment.phoneNum = phoneNum
            return fragment
        }
    }

    override fun initView() {
        tv_confirm_register.setOnClickListener(this)
        iv_delete.setOnClickListener(this)
        iv_show1.setOnClickListener(this)
        tv_confirm_register.setOnClickListener(this)
        cl_terms_of_service.setOnClickListener(this)
        cl_privacy_policy.setOnClickListener(this)
    }

    override fun lazyLoad() {
        mPresenter.attachView(this)
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.tv_confirm_register -> {
                if (et_password.text.length < 6) {
                    ToastUtils.showShort(getString(R.string.input_fit_length_password))
                    return
                }

                if (!PwdCheckUtil.isLetterDigit(et_password.text.toString().trim())) {
                    ToastUtils.showShort(getString(R.string.input_fit_type_password))
                    return
                }
                mPresenter.requestRegister(context, phoneNum!!, et_password.text.toString())
//                start(RegisterSuccessFragment.getInstance(getString(R.string.register)))
            }

            v?.id == R.id.iv_delete -> {
                et_password.text.clear()
            }

            v?.id == R.id.iv_show1 -> {
                if (showPassword) {// 显示密码
                    iv_show1.setImageDrawable(resources.getDrawable(R.mipmap.register_input_show))
                    et_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    et_password.setSelection(et_password.text.toString().length)
                } else {// 隐藏密码
                    iv_show1.setImageDrawable(resources.getDrawable(R.mipmap.register_input_hide))
                    et_password.transformationMethod = PasswordTransformationMethod.getInstance()
                    et_password.setSelection(et_password.text.toString().length)
                }
                showPassword = !showPassword
            }

            v?.id == R.id.cl_terms_of_service -> {
                ToastUtils.showShort(getString(R.string.terms_of_service))
            }

            v?.id == R.id.cl_privacy_policy -> {
                ToastUtils.showShort(getString(R.string.privacy_policy))
            }

        }
    }

    override fun preLoad(type: String, obj: Any) {
        ic_mask.visibility = View.VISIBLE

    }


    override fun afterLoad(type: String, obj: Any) {
        ic_mask.visibility = View.GONE

    }

    override fun handleError(type: String, obj: Any) {
        ToastUtils.showShort(obj.toString())
    }

    override fun handleSuccess(type: String, obj: Any) {

        when (type) {
            InitPasswordApi.requestRegister -> {
                start(LoginByPasswordFragment.getInstance(""), SupportFragment.SINGLETASK)
                ToastUtils.showShort(obj.toString())
            }
        }

    }
}

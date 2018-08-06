package com.advertisement.cashcow.module.login.resetPassword

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.module.login.password.LoginByPasswordFragment
import com.advertisement.cashcow.util.LocalCommonUtils
import com.advertisement.cashcow.util.PwdCheckUtil
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_reset_password.*
import me.yokeyword.fragmentation.SupportFragment
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * 作者：吴国洪 on 2018/6/26
 * 描述：重置密码码页面
 *
 * 接口：用户注册接口：member/register.do
 */

class ResetPasswordFragment : BaseFragment(), View.OnClickListener, ResetPasswordContract.View {


    private val mPresenter by lazy { ResetPasswordPresenter() }

    private var subscription: Subscription? = null


    override fun getLayoutId(): Int = R.layout.fragment_reset_password

    companion object {
        fun getInstance(): ResetPasswordFragment {
            return ResetPasswordFragment()
        }
    }

    /**
     * 重置倒计时时间
     *
     * 只有时间间隔为59秒才请求验证码接口
     */
    private fun resetTime(seconds: Int) {
        if (seconds == 59) {
            mPresenter.requestIsRegisterAndGetVerificationCode("2", et_count.text.toString())
        }

        subscription = Observable.interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
                .take(seconds) //设置循环seconds次
                .map { aLong ->
                    seconds - aLong!! //
                }.observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(object : Observer<Long> {
                    override fun onError(e: Throwable?) {
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onNext(t: Long?) {
                        tv_verification_code.let {
                            it.isEnabled = false
                            it.setTextColor(Color.parseColor("#C4C4C4"))
                            it.text = t.toString() + getString(R.string.s_will_resend)
                        }
                    }

                    override fun onCompleted() {
                        tv_verification_code.let {
                            it.text = getString(R.string.resend)
                            it.setTextColor(resources.getColor(R.color.white))
                            it.isEnabled = true
                        }
                    }
                })
    }

    override fun initView() {
        et_count.setText("18665756471")
        tv_verification_code.setOnClickListener(this)
        tv_confirm.setOnClickListener(this)
        if (LocalCommonUtils.getCalculateRemainingTime(context!!, this.javaClass.name) in 1..58) {
            resetTime(LocalCommonUtils.calculateRemainingTime(context, this.javaClass.name, false))
        }

        nv_bar.setOnBackClickListener { activity!!.onBackPressed() }
    }

    override fun lazyLoad() {
        mPresenter.attachView(this)
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.tv_verification_code -> {
                if (RegexUtils.isMobileExact(et_count.text.toString()))
                    resetTime(LocalCommonUtils.calculateRemainingTime(context, this.javaClass.name, true))
                else
                    ToastUtils.showShort("请输入正确格式的手机号码")

            }

            v?.id == R.id.tv_confirm -> {

                if (et_verification_code.text.length < 4) {
                    ToastUtils.showShort(getString(R.string.input_fit_length_verification_code))
                    return
                }

                if (et_new_password.text.length < 6) {
                    ToastUtils.showShort(getString(R.string.input_fit_length_password))
                    return
                }

                if (et_new_password.text.toString() != et_confirm_password.text.toString()) {
                    ToastUtils.showShort(getString(R.string.password_not_match))
                    return
                }

                if (!PwdCheckUtil.isLetterDigit(et_new_password.text.toString().trim())) {
                    ToastUtils.showShort(getString(R.string.input_fit_type_password))
                    return
                }

                mPresenter.requestIsOkAndResetPassword(
                        et_count.text.toString(),
                        et_new_password.text.toString(),
                        et_verification_code.text.toString(),
                        Api.Companion.VerificationType.FORGET_LOGIN_PASSWORD
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.unsubscribe()
    }

    override fun handleError(type: String, obj: Any) {
        when (type) {
            Api.requestIsRegister -> {
                ToastUtils.showShort(obj.toString())
                subscription!!.unsubscribe()
                resetTime(0)
            }

            Api.requestGetVerificationCode -> {
                ToastUtils.showShort(getString(R.string.verification_code_sent_unsuccessfully))
                subscription!!.unsubscribe()
                resetTime(0)
            }

            Api.requestIsOk -> {
                ToastUtils.showShort(obj.toString())
            }

            Api.requestResetPassword -> {
                ToastUtils.showShort(obj.toString())
            }
        }

    }

    override fun handleSuccess(type: String, obj: Any) {
        when (type) {
            Api.requestResetPassword -> {
                start(LoginByPasswordFragment.getInstance(""), SupportFragment.SINGLETASK)
            }
        }
    }

    override fun preLoad(type: String, obj: Any) {
        ic_mask?.visibility = View.VISIBLE
    }

    override fun afterLoad(type: String, obj: Any) {
        ic_mask?.visibility = View.GONE
    }
}

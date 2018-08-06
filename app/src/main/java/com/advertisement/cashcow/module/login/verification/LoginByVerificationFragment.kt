package com.advertisement.cashcow.module.login.verification

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.broadcast.NormalBroadcastReceiver
import com.advertisement.cashcow.common.network.api.LoginByVerificationApi
import com.advertisement.cashcow.module.login.LoginProtocolFragment
import com.advertisement.cashcow.module.login.register.phone.PhoneNumFragment
import com.advertisement.cashcow.module.login.resetPassword.ResetPasswordFragment
import com.advertisement.cashcow.util.LocalCommonUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_login_by_verification_code.*
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * 作者：吴国洪 on 2018/6/04
 * 描述：验证码登录页面
 * 接口：
 */

class LoginByVerificationFragment : BaseFragment(), View.OnClickListener, LoginByVerificationContract.View {


    private val mPresenter by lazy { LoginByVerificationPresenter() }

    private var mTitle: String? = null

    private var subscription: Subscription? = null

    override fun getLayoutId(): Int = R.layout.fragment_login_by_verification_code

    companion object {
        fun getInstance(title: String): LoginByVerificationFragment {
            val fragment = LoginByVerificationFragment()
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
        tv_login.setOnClickListener(this)
        tv_verification_code.setOnClickListener(this)
        tv_password_login.setOnClickListener(this)
        tv_forget_password.setOnClickListener(this)
        iv_delete.setOnClickListener(this)
        tv_login_protocol.setOnClickListener(this)

        if (LocalCommonUtils.getCalculateRemainingTime(context!!, this.javaClass.name) in 1..58) {
            resetTime(LocalCommonUtils.calculateRemainingTime(context!!, this.javaClass.name, false))
        }

        nv_bar.let {
            it.setOnRightClickListener {
                startWithPop(PhoneNumFragment.getInstance(getString(R.string.register)))

            }
            it.setOnBackClickListener {
                activity.onBackPressed()
            }
        }
    }

    /**
     * 重置倒计时时间
     *
     * 只有时间间隔为59秒才请求验证码接口
     */
    private fun resetTime(seconds: Int) {
        if (seconds == 59) {
            mPresenter.requestIsRegisterAndGetVerificationCode(et_count.text.toString())
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

    override fun lazyLoad() {
        mPresenter.attachView(this)
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.tv_verification_code -> {
                if (RegexUtils.isMobileExact(et_count.text.toString()))
                    resetTime(LocalCommonUtils.calculateRemainingTime(context!!,this.javaClass.name, true))
                else
                    ToastUtils.showShort("请输入正确格式的手机号码")
            }

            v?.id == R.id.tv_login -> {

                if (!RegexUtils.isMobileExact(et_count.text.toString())) {
                    ToastUtils.showShort("请输入正确格式的手机号码")
                    return
                }
                if (et_verification_code.text.length < 4) {
                    ToastUtils.showShort("请输入正确格式的验证码")
                    return
                }

                mPresenter.requestLogin(et_count.text.toString(), et_verification_code.text.toString())
            }
            v?.id == R.id.iv_delete -> {
                et_count.text.clear()
            }

            v?.id == R.id.tv_password_login -> {
               pop()
            }

            v?.id == R.id.tv_forget_password -> {
                startWithPop(ResetPasswordFragment.getInstance())
            }

            v?.id == R.id.tv_login_protocol -> {
                start(LoginProtocolFragment.getInstance())

            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        subscription?.unsubscribe()
    }

    override fun handleError(type: String, obj: Any) {
        when (type) {
            LoginByVerificationApi.requestIsRegister -> {
                ToastUtils.showShort(obj.toString())
                subscription!!.unsubscribe()
                resetTime(0)
            }

            LoginByVerificationApi.requestGetVerificationCode -> {
                ToastUtils.showShort(getString(R.string.verification_code_sent_unsuccessfully))
                subscription!!.unsubscribe()
                resetTime(0)
            }

            LoginByVerificationApi.requestLogin -> {
                ToastUtils.showShort(obj.toString())
            }
        }

    }

    override fun handleSuccess(type: String, obj: Any) {
        when (type) {
            LoginByVerificationApi.requestGetVerificationCode ->
                ToastUtils.showShort(getString(R.string.verification_code_sent_successfully))

            LoginByVerificationApi.requestLogin -> {
                val intent = Intent()
                intent.action = NormalBroadcastReceiver.BroadcastName
                context.sendBroadcast(intent)
                activity.finish()
            }
        }
    }

    override fun preLoad(type: String, obj: Any) {
        ic_mask.visibility = View.VISIBLE
    }

    override fun afterLoad(type: String, obj: Any) {
        ic_mask.visibility = View.GONE
    }
}

package com.advertisement.cashcow.module.login.register.verificationCode

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.VerificationApi
import com.advertisement.cashcow.module.login.register.initPassword.InitPasswordFragment
import com.advertisement.cashcow.util.LocalCommonUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_register_verification_code.*
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * 作者：吴国洪 on 2018/6/04
 * 描述：注册-输入验证码页面
 * 接口：获取短信验证码接口 : getVerifyCode.do
 */

class VerificationCodeFragment : BaseFragment(), View.OnClickListener, VerificationCodeContract.View {



    var subscription: Subscription? = null


    private val mPresenter by lazy { VerificationCodePresenter() }

    private var phoneNum: String? = null

    override fun getLayoutId(): Int = R.layout.fragment_register_verification_code

    companion object {
        fun getInstance(phoneNum: String): VerificationCodeFragment {
            val fragment = VerificationCodeFragment()
            fragment.phoneNum = phoneNum
            return fragment
        }
    }

    /**
     * 初始化 ViewI
     */
    override fun initView() {
        tv_confirm.setOnClickListener(this)
        tv_phone.text = phoneNum
        tv_verification_code.setOnClickListener(this)

        nv_bar.setOnBackClickListener { onBackPressedSupport() }

        vce_input.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                tv_confirm.isEnabled = 4 == s?.length
            }
        })

        if (LocalCommonUtils.getCalculateRemainingTime(context!!, this.javaClass.name) in 1..58) {
            resetTime(LocalCommonUtils.calculateRemainingTime(context!!, this.javaClass.name, false))
        }
    }


    /**
     * 重置倒计时时间
     *
     * 只有时间间隔为59秒才请求验证码接口
     */
    private fun resetTime(seconds: Int) {
        if (seconds == 59) {
            mPresenter.requestGetVerificationCode(phoneNum!!)
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
            v?.id == R.id.tv_confirm -> {
                mPresenter.requestCheckVerificationCode(phoneNum!!, vce_input.text.toString())

            }

            v?.id == R.id.tv_verification_code -> {
                resetTime(LocalCommonUtils.calculateRemainingTime(context!!,this.javaClass.name, true))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.unsubscribe()
    }

    override fun preLoad(type: String, obj: Any) {
        ic_mask?.visibility = View.VISIBLE
    }

    override fun afterLoad(type: String, obj: Any) {
        ic_mask?.visibility = View.GONE
    }

    override fun handleSuccess(type: String, obj: Any) {
        when (type) {
            VerificationApi.requestGetVerificationCode ->
                ToastUtils.showShort(getString(R.string.verification_code_sent_successfully))

            VerificationApi.requestCheckVerificationCode -> {
                start(InitPasswordFragment.getInstance(phoneNum!!))
            }
        }
    }

    override fun handleError(type: String, obj: Any) {
        if (VerificationApi.requestGetVerificationCode == type) {
            subscription!!.unsubscribe()
            resetTime(0)
        }

        ToastUtils.showShort(obj.toString())
    }
}

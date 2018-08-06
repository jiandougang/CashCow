package com.advertisement.cashcow.module.main.mine.other

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.module.main.mine.MineContract
import com.advertisement.cashcow.module.main.mine.MinePresenter
import com.advertisement.cashcow.util.CacheConfigUtils
import com.advertisement.cashcow.util.LocalCommonUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_modify_phone_number.*
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


/**
 * 作者：吴国洪 on 2018/7/16
 * 描述：忘记交易密码管理面
 */

class ModifyPhoneNumberFragment : BaseFragment(), View.OnClickListener, MineContract.View {

    private var subscription: Subscription? = null

    private val mPresenter by lazy { MinePresenter() }

    override fun getLayoutId(): Int = R.layout.fragment_modify_phone_number

    companion object {
        fun getInstance(): ModifyPhoneNumberFragment {
            val fragment = ModifyPhoneNumberFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        tv_verification_code.setOnClickListener(this)
        tv_confirm.setOnClickListener(this)

        nv_bar.setOnBackClickListener { activity!!.onBackPressed() }

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
            mPresenter.requestGetVerificationCode(context,Api.Companion.VerificationType.BINDING_PHONE_NUMBER,
                    et_phone_number.text.toString(),
                    ModifyPhoneNumberFragment.javaClass.name)
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

    override fun onDestroy() {
        super.onDestroy()
        subscription?.unsubscribe()
    }


    override fun onClick(v: View?) {

        when {
            v?.id == R.id.tv_confirm -> {
//                checkInput({
//                    mPresenter.requestIsOk(context,et_phone_number.text.toString(),
//                            et_input_verification_code.text.toString(),
//                            Api.Companion.VerificationType.BINDING_PHONE_NUMBER,
//                            ModifyPhoneNumberFragment.javaClass.name)
//                }, false)
                start(InitBindingPhoneNumberPasswordFragment.getInstance(et_phone_number.text.toString()))

            }

            v?.id == R.id.tv_verification_code -> {
                checkInput({
                    resetTime(LocalCommonUtils.calculateRemainingTime(context, ModifyPhoneNumberFragment.javaClass.name, true))
                }, true)

            }
        }
    }

    private fun checkInput(callBack: () -> Unit, ignoreVerification: Boolean) {
        if (RegexUtils.isMobileExact(et_phone_number.text.toString())) {
            if (CacheConfigUtils.parseUserInfo(context!!).resultData?.phone.toString() != et_phone_number.text.toString()) {

                if (et_input_verification_code.text.length == 4 ||
                        ignoreVerification)
                    callBack()
                else {
                    ToastUtils.showShort(getString(R.string.please_input_correct_format_verification_code))
                }

            } else
                ToastUtils.showShort(getString(R.string.same_phone_number))

        } else
            ToastUtils.showShort(getString(R.string.please_input_correct_format_mobile_number))
    }

    override fun handleError(type: String, obj: Any) {
        when (type) {
            Api.requestIsOk -> {
                ToastUtils.showShort(obj.toString())
                subscription?.unsubscribe()
                resetTime(0)
            }

            Api.requestGetVerificationCode -> {
                ToastUtils.showShort(getString(R.string.verification_code_sent_unsuccessfully))
                subscription?.unsubscribe()
                resetTime(0)
            }

        }
    }


    override fun handleSuccess(type: String, obj: Any) {

        when (type) {
            Api.requestUpdateLoginPass -> {
                activity.onBackPressed()
            }

            Api.requestIsOk -> {
                start(InitBindingPhoneNumberPasswordFragment.getInstance(et_phone_number.text.toString()))

            }

        }
    }

    override fun afterLoad(type: String, obj: Any) {
        ic_mask.visibility = View.GONE
    }


    override fun preLoad(type: String, obj: Any) {
        ic_mask.visibility = View.VISIBLE
    }


    override fun lazyLoad() {
        mPresenter.attachView(this)
    }
}


package com.advertisement.cashcow.module.login.password

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.broadcast.NormalBroadcastReceiver
import com.advertisement.cashcow.common.broadcast.WxBroadcastReceiver
import com.advertisement.cashcow.common.broadcast.WxBroadcastReceiver.ReceiverCallBack
import com.advertisement.cashcow.module.login.LoginProtocolFragment
import com.advertisement.cashcow.module.login.register.phone.PhoneNumFragment
import com.advertisement.cashcow.module.login.resetPassword.ResetPasswordFragment
import com.advertisement.cashcow.module.login.verification.LoginByVerificationFragment
import com.advertisement.cashcow.util.PwdCheckUtil
import com.advertisement.cashcow.wxapi.WXEntryActivity
import com.advertisement.cashcow.wxapi.WxUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.mm.opensdk.modelmsg.SendAuth
import kotlinx.android.synthetic.main.fragment_login_by_password.*


/**
 * 作者：吴国洪 on 2018/5/28
 * 描述：密码登录页面
 * 接口：
 */

class LoginByPasswordFragment : BaseFragment(), View.OnClickListener, LoginByPasswordContract.View {


    private var showPassword: Boolean = false

    private val mPresenter by lazy { LoginByPasswordPresenter() }

    private var wxBroadcastReceiver: WxBroadcastReceiver? = null


    private var finishActivityForResult = ""

    override fun getLayoutId(): Int = R.layout.fragment_login_by_password

    companion object {
        const val Finish_Activity_For_Result = "Finish_Activity_For_Result"

        fun getInstance(finishActivityForResult: String?): LoginByPasswordFragment {
            val fragment = LoginByPasswordFragment()
            val bundle = Bundle()
            fragment.finishActivityForResult = finishActivityForResult.toString()
            fragment.arguments = bundle
            return fragment
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        tv_login.setOnClickListener(this)
        tv_SMS_login.setOnClickListener(this)
        tv_reset_password.setOnClickListener(this)
        iv_show2.setOnClickListener(this)
        iv_delete.setOnClickListener(this)
        iv_delete_account.setOnClickListener(this)
        iv_wechat.setOnClickListener(this)
        tv_login_protocol.setOnClickListener(this)

        nv_bar1.let {
            it.setOnRightClickListener {
                start(PhoneNumFragment.getInstance(getString(R.string.register)))
            }
            it.setOnBackClickListener {
                activity.onBackPressed()
            }
        }

        et_password1.setText("111qqq")
        et_count1.setText("18665756471")
    }

    override fun lazyLoad() {
        mPresenter.attachView(this)

        wxBroadcastReceiver = WxBroadcastReceiver(object : ReceiverCallBack {
            /**
             * 根据不同Wx返回的回调类型进行判断处理
             */
            override fun callBack(callbackType: String, intent: Intent) {

                when (callbackType) {
                    WXEntryActivity.WeiXin_Authorization -> {
                        WxUtils.requestRegister(activity!!, intent.getStringExtra(WXEntryActivity.WeiXin_Authorization))
                    }

                    WXEntryActivity.WeiXin_Loading -> {
                        preLoad("", "")
                    }

                    WXEntryActivity.WeiXin_Loading_Complete -> {
                        afterLoad("", "")
                    }

                    WXEntryActivity.WeiXin_Callback_Success -> {

                        startOtherPage()
                    }

                    WXEntryActivity.WeiXin_Callback_Fail -> {
                        ToastUtils.showShort(intent.getStringExtra(WXEntryActivity.WeiXin_Callback_Success))
                    }
                }
            }


        })

        val filter = IntentFilter()
        filter.addAction(WxBroadcastReceiver.BroadcastName)
        activity!!.registerReceiver(wxBroadcastReceiver, filter)
    }

    private fun startOtherPage() {
        if (finishActivityForResult == Finish_Activity_For_Result) {
            val intent = Intent()
            intent.action = NormalBroadcastReceiver.BroadcastName
            context.sendBroadcast(intent)
            activity!!.finish()
        } else {
            val bundle = Bundle()
            setFragmentResult(RESULT_OK, bundle)
            activity!!.onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.iv_show2 -> {
                if (showPassword) {// 显示密码
                    iv_show2.setImageDrawable(resources.getDrawable(R.mipmap.register_input_show))
                    et_password1.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    et_password1.setSelection(et_password1.text.toString().length)
                } else {// 隐藏密码
                    iv_show2.setImageDrawable(resources.getDrawable(R.mipmap.register_input_hide))
                    et_password1.transformationMethod = PasswordTransformationMethod.getInstance()
                    et_password1.setSelection(et_password1.text.toString().length)
                }
                showPassword = !showPassword
            }

            v?.id == R.id.iv_wechat -> {
                val req = SendAuth.Req()
                req.scope = "snsapi_userinfo"
                req.state = System.currentTimeMillis().toString()
                WxUtils.getIWxApiInstance(context!!).sendReq(req)
            }

            v?.id == R.id.iv_delete -> {
                et_password1.text.clear()

            }

            v?.id == R.id.iv_delete_account -> {
                et_count1.text.clear()
            }

            v?.id == R.id.tv_login -> {
                if (!checkInputFormat()) {
                    mPresenter.requestLogin(et_count1.text.toString(), et_password1.text.toString())
                }

            }

            v?.id == R.id.tv_SMS_login -> {
                start(LoginByVerificationFragment.getInstance(getString(R.string.login)))
            }

            v?.id == R.id.tv_reset_password -> {
                start(ResetPasswordFragment.getInstance())

            }

            v?.id == R.id.tv_login_protocol -> {
                start(LoginProtocolFragment.getInstance())
            }

        }
    }


    private fun checkInputFormat(): Boolean {
        if (!RegexUtils.isMobileExact(et_count1.text.toString())) {
            ToastUtils.showShort("请输入正确格式的手机号码")
            return true
        }

        if (et_password1.text.length < 6) {
            ToastUtils.showShort(getString(R.string.input_fit_length_password))
            return true
        }

        if (!PwdCheckUtil.isLetterDigit(et_password1.text.toString().trim())) {
            ToastUtils.showShort(getString(R.string.input_fit_type_password))
            return true
        }
        return false
    }


    override fun handleError(type: String, obj: Any) {
        ToastUtils.showShort(obj.toString())
    }

    override fun handleSuccess(type: String, obj: Any) {
        startOtherPage()
    }

    override fun afterLoad(type: String, obj: Any) {
        ic_mask.visibility = View.GONE
    }


    override fun preLoad(type: String, obj: Any) {
        ic_mask.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        if (wxBroadcastReceiver != null) {
            activity!!.unregisterReceiver(wxBroadcastReceiver)
            wxBroadcastReceiver = null
        }
    }

}



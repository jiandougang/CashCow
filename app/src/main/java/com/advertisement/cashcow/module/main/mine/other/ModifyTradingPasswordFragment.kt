package com.advertisement.cashcow.module.main.mine.other

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.bean.mine.ModifyPasswordApiBean
import com.advertisement.cashcow.module.main.mine.MineContract
import com.advertisement.cashcow.module.main.mine.MinePresenter
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_modify_trading_password.*
import rx.Observable


/**
 * 作者：吴国洪 on 2018/7/16
 * 描述：修改交易密码管理面
 */

class ModifyTradingPasswordFragment : BaseFragment(), View.OnClickListener, MineContract.View {


    private val mPresenter by lazy { MinePresenter() }

    override fun getLayoutId(): Int = R.layout.fragment_modify_trading_password

    companion object {
        fun getInstance(): ModifyTradingPasswordFragment {
            val fragment = ModifyTradingPasswordFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        nv_bar.setOnBackClickListener { activity!!.onBackPressed() }
        tv_confirm.setOnClickListener(this)
        tv_forget_password.setOnClickListener(this)
        setConfirmState()
    }

    private fun setConfirmState() {
        val observableOldPassword = Observable.create(Observable.OnSubscribe<String> { subscriber ->
            et_old_password.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    subscriber.onNext(s.toString())
                }
            })
        })

        val observableNewPassword = Observable.create(Observable.OnSubscribe<String> { subscriber ->
            et_new_password.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    subscriber.onNext(s.toString())
                }
            })
        })

        val observableNewPasswordAgain = Observable.create(Observable.OnSubscribe<String> { subscriber ->
            et_new_password_again.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    subscriber.onNext(s.toString())
                }
            })
        })


        Observable.combineLatest(observableOldPassword, observableNewPassword, observableNewPasswordAgain) { oldPassword, newPassword, newPasswordAgain ->

            val isOldPasswordValid = oldPassword.length == 6
            val isNewPasswordValid = newPassword.length == 6
            val isNewPasswordAgainValid = newPassword == newPasswordAgain


            isOldPasswordValid && isNewPasswordValid && isNewPasswordAgainValid
        }.subscribe {
            tv_confirm.isEnabled = it
        }
    }


    override fun onClick(v: View?) {

        when {
            v?.id == R.id.tv_confirm -> {
                mPresenter.requestUpdateDealPass(
                        CacheConfigUtils.parseUserInfo(context!!).resultData?.id.toString(),
                        et_old_password.text.toString(),
                        et_new_password.text.toString())
            }
            v?.id == R.id.tv_forget_password -> {
                start(ForgetTradingPasswordFragment.getInstance())

            }


        }
    }

    override fun handleError(type: String, obj: Any) {
        ToastUtils.showShort(obj.toString())

    }


    override fun handleSuccess(type: String, obj: Any) {
        ToastUtils.showShort((obj as ModifyPasswordApiBean).resultMsg)
        activity!!.onBackPressed()
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


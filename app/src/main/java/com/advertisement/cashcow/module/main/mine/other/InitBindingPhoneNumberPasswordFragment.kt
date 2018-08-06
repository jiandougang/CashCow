package com.advertisement.cashcow.module.main.mine.other

import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.broadcast.NormalBroadcastReceiver
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import com.advertisement.cashcow.module.main.mine.personalInformation.PersonalInformationContract
import com.advertisement.cashcow.module.main.mine.personalInformation.PersonalInformationFragment
import com.advertisement.cashcow.module.main.mine.personalInformation.PersonalInformationPresenter
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_modify_phone_number_init_password.*
import me.yokeyword.fragmentation.SupportFragment


/**
 * 作者：吴国洪 on 2018/6/04
 * 描述：设置初始密码,用于绑定手机号页面
 *
 * 接口：用户注册接口：member/register.do
 */

class InitBindingPhoneNumberPasswordFragment : BaseFragment(), View.OnClickListener, PersonalInformationContract.View {


    private val mPresenter by lazy { PersonalInformationPresenter(InitBindingPhoneNumberPasswordFragment.javaClass.name) }

    private var phoneNum: String? = null
    private var showPassword: Boolean = false

    override fun getLayoutId(): Int = R.layout.fragment_modify_phone_number_init_password

    companion object {
        fun getInstance(phoneNum: String): InitBindingPhoneNumberPasswordFragment {
            val fragment = InitBindingPhoneNumberPasswordFragment()
            fragment.phoneNum = phoneNum
            return fragment
        }
    }

    override fun initView() {
        tv_confirm_register.setOnClickListener(this)
        iv_delete.setOnClickListener(this)
        iv_show1.setOnClickListener(this)
        tv_confirm_register.setOnClickListener(this)
        nv_bar.setOnBackClickListener { activity.onBackPressed() }
    }

    override fun lazyLoad() {
        mPresenter.attachView(this)
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.tv_confirm_register -> {

                mPresenter.requestSaveBasicInfo(context,
                        CacheConfigUtils.parseUserInfo(context!!).resultData?.id!!,
                        "",
                        phoneNum!!,
                        et_password.text.toString())
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
            Api.requestSaveBasicInfo -> {
                obj as LoginByPasswordApiBean
                CacheConfigUtils.parseUserInfo(context!!).resultData?.phone = obj.resultData?.phone
                val intent = Intent()
                intent.action = NormalBroadcastReceiver.BroadcastName
                intent.putExtra(PersonalInformationFragment.PHONE_KEY, obj.resultData?.phone)
                context.sendBroadcast(intent)
                start(PersonalInformationFragment.getInstance(""), SupportFragment.SINGLETASK)
            }
        }
    }
}

package com.advertisement.cashcow.module.main.mine.other

import android.os.Bundle
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import com.advertisement.cashcow.module.main.mine.personalInformation.PersonalInformationContract
import com.advertisement.cashcow.module.main.mine.personalInformation.PersonalInformationPresenter
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_modify_nick_name.*


/**
 * 作者：吴国洪 on 2018/7/23
 * 描述：修改昵称页面
 */

class ModifyNickNameFragment : BaseFragment(), View.OnClickListener, PersonalInformationContract.View {

    private val mPresenter by lazy { PersonalInformationPresenter(ModifyNickNameFragment.javaClass.name) }

    override fun getLayoutId(): Int = R.layout.fragment_modify_nick_name

    companion object {
        fun getInstance(): ModifyNickNameFragment {
            val fragment = ModifyNickNameFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {

        nv_bar.let {
            it.setOnBackClickListener {
                activity.onBackPressed()
            }

            it.setOnRightClickListener {
                mPresenter.requestSaveBasicInfo(context!!,
                        CacheConfigUtils.parseUserInfo(context!!).resultData?.id!!,
                        et_nick_name.text.toString(),
                        "",
                        "")
            }
        }

    }

    override fun onBackPressedSupport(): Boolean {
        val bundle = Bundle()
        setFragmentResult(RESULT_OK, bundle)

        return super.onBackPressedSupport()
    }

    override fun onClick(v: View?) {

        when {

        }
    }


    override fun handleError(type: String, obj: Any) {
        ToastUtils.showShort(obj.toString())
    }


    override fun handleSuccess(type: String, obj: Any) {
        when (type) {
            Api.requestSaveBasicInfo -> {
                CacheConfigUtils.parseUserInfo(context).resultData?.nickname = (obj as LoginByPasswordApiBean).resultData?.nickname
                activity.onBackPressed()
            }
        }
    }

    override fun preLoad(type: String, obj: Any) {
        ic_mask.visibility = View.VISIBLE

    }


    override fun afterLoad(type: String, obj: Any) {
        ic_mask.visibility = View.GONE
    }


    override fun lazyLoad() {
        mPresenter.attachView(this)
    }
}

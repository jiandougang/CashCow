package com.advertisement.cashcow.module.main.mine

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.broadcast.NormalBroadcastReceiver
import com.advertisement.cashcow.common.manager.EmptyActivity
import com.advertisement.cashcow.module.login.password.LoginByPasswordFragment
import com.advertisement.cashcow.module.main.information.subInformation.SubInformationFragment
import com.advertisement.cashcow.module.main.mine.cumulativeIncome.CumulativeIncomeFragment
import com.advertisement.cashcow.module.main.mine.goldCoinsDetail.GoldCoinsDetailFragment
import com.advertisement.cashcow.module.main.mine.myBankCard.MyBankCardsFragment
import com.advertisement.cashcow.module.main.mine.myCollection.MyCollectionFragment
import com.advertisement.cashcow.module.main.mine.other.CustomerServiceFragment
import com.advertisement.cashcow.module.main.mine.other.InviteFriendsFragment
import com.advertisement.cashcow.module.main.mine.other.WithdrawFragment
import com.advertisement.cashcow.module.main.mine.personalInformation.PersonalInformationFragment
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.StringUtils
import com.othershe.nicedialog.BaseNiceDialog
import com.othershe.nicedialog.NiceDialog
import com.othershe.nicedialog.ViewConvertListener
import com.othershe.nicedialog.ViewHolder
import kotlinx.android.synthetic.main.fragment_mine.*


/**
 * 作者：吴国洪 on 2018/6/04
 * 描述：我的页面
 * 接口：获取用户信息 : member/getUserById.do
 */

class MineFragment : BaseFragment(), View.OnClickListener, MineContract.View {

    private val id = "018b7b9b-a155-4ab2-81ab-6e75a3a94bbd"
    private val mPresenter by lazy { MinePresenter() }
    private var normalBroadcastReceiver: NormalBroadcastReceiver? = null


    override fun getLayoutId(): Int = R.layout.fragment_mine

    companion object {
        //        const val requestCode = 100
        fun getInstance(): MineFragment {
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        val tempPhone = if (StringUtils.isEmpty(CacheConfigUtils.parseUserInfo(context!!).resultData?.phone)) {
            getString(R.string.phone_with_colon) +  "未绑定"
        } else {
            getString(R.string.phone_with_colon) + CacheConfigUtils.parseUserInfo(context!!).resultData?.phone
        }
        if (CacheConfigUtils.parseUserInfo(context!!).resultData != null) {
            sd_avatar.setImageURI((CacheConfigUtils.parseUserInfo(context!!).resultData?.avatar.toString()))
            tv_user_name.text = CacheConfigUtils.parseUserInfo(context!!).resultData?.nickname ?: "匿名用户"

            tv_cumulative_income.text = CacheConfigUtils.parseUserInfo(context!!).resultData?.hisearnings.toString()
            tv_today_income.text = CacheConfigUtils.parseUserInfo(context!!).resultData?.earnings.toString()
            tv_balance.text = "￥${CacheConfigUtils.parseUserInfo(context!!).resultData?.remaining.toString()}"
        } else {
            sd_avatar.setImageURI("")
            tv_user_name.text = getString(R.string.not_logged_in)
            tv_cumulative_income.text = "0.00"
            tv_today_income.text = "0.00"
            tv_balance.text = "￥0.00"

        }
        tv_phone.text = tempPhone
        cl_header.setOnClickListener(this)
        cl_invite_friend.setOnClickListener(this)
        cl_cumulative_income.setOnClickListener(this)
        cl_my_collection.setOnClickListener(this)
        cl_customer_service.setOnClickListener(this)
        cl_my_band_cards.setOnClickListener(this)
        cl_coins_count.setOnClickListener(this)
        cl_withdraw.setOnClickListener(this)
        cl_business_cooperation.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when {
            v?.id == R.id.cl_header -> {
                val intent = Intent(activity, EmptyActivity::class.java)

                if (CacheConfigUtils.parseUserInfo(context!!).resultData != null) {
                    intent.putExtra(PersonalInformationFragment.Finish_Activity_For_Result, PersonalInformationFragment.Finish_Activity_For_Result)
                    intent.putExtra(EmptyActivity.Activity_Key, PersonalInformationFragment.javaClass.name)
                } else {
                    intent.putExtra(LoginByPasswordFragment.Finish_Activity_For_Result, LoginByPasswordFragment.Finish_Activity_For_Result)
                    intent.putExtra(EmptyActivity.Activity_Key, LoginByPasswordFragment.javaClass.name)
                }

                activity!!.startActivity(intent)
                activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
            }

            v?.id == R.id.cl_invite_friend -> {

                val intent = Intent(activity, EmptyActivity::class.java)
                if (CacheConfigUtils.parseUserInfo(context!!).resultData != null) {

                    intent.putExtra(EmptyActivity.Activity_Key, InviteFriendsFragment.javaClass.name)
                } else {
                    intent.putExtra(LoginByPasswordFragment.Finish_Activity_For_Result, LoginByPasswordFragment.Finish_Activity_For_Result)
                    intent.putExtra(EmptyActivity.Activity_Key, LoginByPasswordFragment.javaClass.name)

                }
                activity!!.startActivity(intent)
                activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
            }

            v?.id == R.id.cl_cumulative_income -> {
                val intent = Intent(activity, EmptyActivity::class.java)

                if (CacheConfigUtils.parseUserInfo(context!!).resultData != null) {
                    intent.putExtra(EmptyActivity.Activity_Key, CumulativeIncomeFragment.javaClass.name)
                    intent.putExtra(CumulativeIncomeFragment.User_Id, (CacheConfigUtils.parseUserInfo(context!!).resultData?.id))

                } else {
                    intent.putExtra(LoginByPasswordFragment.Finish_Activity_For_Result, LoginByPasswordFragment.Finish_Activity_For_Result)
                    intent.putExtra(EmptyActivity.Activity_Key, LoginByPasswordFragment.javaClass.name)
                }
                activity!!.startActivity(intent)
                activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
            }

            v?.id == R.id.cl_my_collection -> {
                val intent = Intent(activity, EmptyActivity::class.java)
                if (CacheConfigUtils.parseUserInfo(context!!).resultData != null) {
                    intent.putExtra(EmptyActivity.Activity_Key, MyCollectionFragment.javaClass.name)
                    intent.putExtra(SubInformationFragment.Category, context!!.resources.getString(R.string.my_collection))

                } else {
                    intent.putExtra(EmptyActivity.Activity_Key, LoginByPasswordFragment.javaClass.name)
                    intent.putExtra(LoginByPasswordFragment.Finish_Activity_For_Result, LoginByPasswordFragment.Finish_Activity_For_Result)
                }

                activity!!.startActivity(intent)
                activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
            }

            v?.id == R.id.cl_customer_service -> {
                val intent = Intent(activity, EmptyActivity::class.java)
                intent.putExtra(EmptyActivity.Activity_Key, CustomerServiceFragment.javaClass.name)
                activity!!.startActivity(intent)
                activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
            }

            v?.id == R.id.cl_my_band_cards -> {
                val intent = Intent(activity, EmptyActivity::class.java)
                if (CacheConfigUtils.parseUserInfo(context).resultData != null) {
                    intent.putExtra(EmptyActivity.Activity_Key, MyBankCardsFragment.javaClass.name)
                    intent.putExtra(MyBankCardsFragment.Title, context!!.resources.getString(R.string.my_bank_card))

                } else {
                    intent.putExtra(EmptyActivity.Activity_Key, LoginByPasswordFragment.javaClass.name)
                    intent.putExtra(LoginByPasswordFragment.Finish_Activity_For_Result, LoginByPasswordFragment.Finish_Activity_For_Result)
                }

                activity!!.startActivity(intent)
                activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)

            }

            v?.id == R.id.cl_withdraw -> {
                val intent = Intent(activity, EmptyActivity::class.java)
                if (CacheConfigUtils.parseUserInfo(context!!).resultData != null) {
                    intent.putExtra(EmptyActivity.Activity_Key, WithdrawFragment.javaClass.name)

                } else {
                    intent.putExtra(EmptyActivity.Activity_Key, LoginByPasswordFragment.javaClass.name)
                    intent.putExtra(LoginByPasswordFragment.Finish_Activity_For_Result, LoginByPasswordFragment.Finish_Activity_For_Result)
                }

                activity!!.startActivity(intent)
                activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
            }

            v?.id == R.id.cl_business_cooperation -> {
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_confirm_text)
                        .setConvertListener(object : ViewConvertListener() {
                            public override fun convertView(holder: ViewHolder, dialog: BaseNiceDialog) {
                                val tel = "020-84387781"
                                holder.setOnClickListener(R.id.tv_cancel) {
                                    dialog.dismiss()
                                }
                                holder.getView<TextView>(R.id.tv_confirm).let {
                                    it.text = getString(R.string.Call)
                                    it.setOnClickListener {
                                        dialog.dismiss()
                                        val intent = Intent(Intent.ACTION_CALL)
                                        val data = Uri.parse("tel:$tel")
                                        intent.data = data
                                        startActivity(intent)
                                    }
                                }
                                holder.getView<TextView>(R.id.tv_content).text = tel
                            }
                        })
                        .setMargin(52)
                        .show(fragmentManager)
            }

            v?.id == R.id.cl_coins_count -> {
                val intent = Intent(activity, EmptyActivity::class.java)

                if (CacheConfigUtils.parseUserInfo(context!!).resultData != null) {
                    intent.putExtra(EmptyActivity.Activity_Key, GoldCoinsDetailFragment.javaClass.name)
                } else {
                    intent.putExtra(LoginByPasswordFragment.Finish_Activity_For_Result, LoginByPasswordFragment.Finish_Activity_For_Result)
                    intent.putExtra(EmptyActivity.Activity_Key, LoginByPasswordFragment.javaClass.name)
                }

                activity!!.startActivity(intent)
                activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (normalBroadcastReceiver != null) {
            activity!!.unregisterReceiver(normalBroadcastReceiver)
            normalBroadcastReceiver = null
        }
    }

    override fun handleError(type: String, obj: Any) {
    }


    override fun handleSuccess(type: String, obj: Any) {

    }

    override fun preLoad(type: String, obj: Any) {
    }


    override fun afterLoad(type: String, obj: Any) {
    }


    override fun lazyLoad() {
        mPresenter.attachView(this)
        normalBroadcastReceiver = NormalBroadcastReceiver(object : NormalBroadcastReceiver.ReceiverCallBack {
            override fun callBack(intent: Intent) {
                initView()
            }
        })

        val filter = IntentFilter()
        filter.addAction(NormalBroadcastReceiver.BroadcastName)
        activity!!.registerReceiver(normalBroadcastReceiver, filter)
    }


}

package com.advertisement.cashcow.module.main.mine.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.bean.mine.MineApiBean
import com.advertisement.cashcow.module.main.mine.MineContract
import com.advertisement.cashcow.module.main.mine.MinePresenter
import com.advertisement.cashcow.thirdLibs.bottomDialog.BottomDialog
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.LogUtils
import com.othershe.nicedialog.BaseNiceDialog
import com.othershe.nicedialog.NiceDialog
import com.othershe.nicedialog.ViewConvertListener
import com.othershe.nicedialog.ViewHolder
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import kotlinx.android.synthetic.main.fragment_mine_invite_friends.*


/**
 * 作者：吴国洪 on 2018/7/08
 * 描述：邀请好友页面
 */

class InviteFriendsFragment : BaseFragment(), View.OnClickListener, MineContract.View {

    private var bottomDialog: BottomDialog? = null

    private val mPresenter by lazy { MinePresenter() }

    override fun getLayoutId(): Int = R.layout.fragment_mine_invite_friends

    companion object {
        fun getInstance(): InviteFriendsFragment {
            val fragment = InviteFriendsFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {

        bottomDialog = BottomDialog.Builder(context!!).setContentView(setDialogContent()).build()
        tv_invite_friends.setOnClickListener(this)
        tv_reward_rule.setOnClickListener(this)
        nv_bar.setOnBackClickListener { activity.onBackPressed() }
    }

    override fun onClick(v: View?) {

        when {
            v?.id == R.id.tv_invite_friends -> {
                bottomDialog?.show()
            }

            v?.id == R.id.ll_wechat_friends -> {
                mPresenter.wechatShare(activity!!, SendMessageToWX.Req.WXSceneSession, CacheConfigUtils.parseUserInfo(context!!).resultData?.id!!)
                bottomDialog?.dismiss()
            }

            v?.id == R.id.ll_moment -> {
                mPresenter.wechatShare(activity!!, SendMessageToWX.Req.WXSceneTimeline,CacheConfigUtils.parseUserInfo(context!!).resultData?.id!!)
                bottomDialog?.dismiss()
            }

            v?.id == R.id.tv_cancel -> {
                bottomDialog?.dismiss()
            }

            v?.id == R.id.tv_reward_rule -> {

                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_invite_friends_protocol)
                        .setConvertListener(object : ViewConvertListener() {
                            public override fun convertView(holder: ViewHolder, dialog: BaseNiceDialog) {
                                holder.setOnClickListener(R.id.iv_cancel) {
                                    dialog.dismiss()
                                }
                            }
                        })
                        .setHeight(440)
                        .setMargin(24)
                        .show(fragmentManager)
            }

        }
    }


    private fun setDialogContent(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_select_share_type, null)
        view.findViewById<LinearLayout>(R.id.ll_wechat_friends).setOnClickListener(this)
        view.findViewById<LinearLayout>(R.id.ll_moment).setOnClickListener(this)
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener(this)



        return view
    }

    override fun handleError(type: String, obj: Any) {
    }


    override fun handleSuccess(type: String, obj: Any) {
        LogUtils.i((obj as MineApiBean).toString())
    }

    override fun preLoad(type: String, obj: Any) {
    }


    override fun afterLoad(type: String, obj: Any) {
    }


    override fun lazyLoad() {
        mPresenter.attachView(this)
    }
}

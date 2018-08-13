package com.advertisement.cashcow.module.main.mine.goldCoinsDetail

import android.os.Bundle
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.bean.mine.GoldCoinsDetailStatisticsApiBean
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.ToastUtils
import com.othershe.nicedialog.BaseNiceDialog
import com.othershe.nicedialog.NiceDialog
import com.othershe.nicedialog.ViewConvertListener
import com.othershe.nicedialog.ViewHolder
import kotlinx.android.synthetic.main.fragment_gold_coins_detail.*
import java.util.*

/**
 * 作者：吴国洪 on 2018/8/07
 * 描述：金币详情模块
 */

open class GoldCoinsDetailFragment : BaseFragment(), GoldCoinsDetailContract.View {

    open val mGoldCoinsDetailPresenter by lazy { GoldCoinsDetailPresenter() }

    private val mTitles = arrayOf("审核中", "已通过", "未通过")
    private val mFragments = ArrayList<BaseFragment>()
    private var pageAdapter: TabPageAdapter? = null

    override fun lazyLoad() {
        mGoldCoinsDetailPresenter.attachView(this)
        mGoldCoinsDetailPresenter.requestCountPktByUserId(context, CacheConfigUtils.parseUserInfo(context!!).resultData?.id.toString())
        mFragments.add(GoldCoinsDetailListFragment.getInstance("1"))
        mFragments.add(GoldCoinsDetailListFragment.getInstance("2"))
        mFragments.add(GoldCoinsDetailListFragment.getInstance("3"))
    }

    override fun getLayoutId(): Int = R.layout.fragment_gold_coins_detail


    companion object {
        fun getInstance(): GoldCoinsDetailFragment {
            val fragment = GoldCoinsDetailFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        pageAdapter = TabPageAdapter(fragmentManager, mTitles, mFragments)
        vp_coins_state.adapter = pageAdapter
        vp_coins_state.offscreenPageLimit = 3
        ctl_coins_state.setViewPager(vp_coins_state)

        nv_bar.let {
            it.setOnBackClickListener {
                activity.onBackPressed()
            }

            it.setOnRightClickListener {
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_gold_coins_detail_hint)
                        .setConvertListener(object : ViewConvertListener() {
                            public override fun convertView(holder: ViewHolder, dialog: BaseNiceDialog) {
                                holder.setOnClickListener(R.id.tv_got_it) {
                                    dialog.dismiss()
                                }

                            }
                        })
                        .setMargin(56)
                        .show(fragmentManager)
            }
        }
    }


    override fun handleError(type: String, obj: Any) {
    }


    override fun handleSuccess(type: String, obj: Any) {
        when (type) {
            Api.requestCountPktByUserId -> {
                obj as GoldCoinsDetailStatisticsApiBean
                for (item in obj.resultData!!) {
                    when (item.status) {
                        "审核中" ->
                            mTitles[0] = "${item.status}(${item.gold})"
                        "已通过" ->
                            mTitles[1] = "${item.status}(${item.gold})"
                        "未通过" ->
                            mTitles[2] = "${item.status}(${item.gold})"
                    }
                    ctl_coins_state.notifyDataSetChanged()
                }
            }
        }
    }

    override fun preLoad(type: String, obj: Any) {
    }

    override fun afterLoad(type: String, obj: Any) {
    }
}

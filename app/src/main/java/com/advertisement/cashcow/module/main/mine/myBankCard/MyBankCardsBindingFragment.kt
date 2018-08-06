package com.advertisement.cashcow.module.main.mine.myBankCard

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.bean.mine.MyBankCardBindingApiBean
import com.advertisement.cashcow.config.Constant
import com.advertisement.cashcow.util.CacheConfigUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.jdsjlzx.recyclerview.LRecyclerView
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import com.hazz.kotlinmvp.view.recyclerview.adapter.OnItemClickListener
import com.othershe.nicedialog.BaseNiceDialog
import com.othershe.nicedialog.NiceDialog
import com.othershe.nicedialog.ViewConvertListener
import com.othershe.nicedialog.ViewHolder
import kotlinx.android.synthetic.main.fragment_my_bank_card_binding.*


/**
 * 作者：吴国洪 on 2018/7/16
 * 描述：绑定银行卡模块
 */

open class MyBankCardsBindingFragment : BaseFragment(), MyBankCardsContract.View, View.OnClickListener {


    open val mPresenter by lazy { MyBankCardsPresenter() }


    override fun lazyLoad() {
        mPresenter.attachView(this)
    }


    override fun getLayoutId(): Int = R.layout.fragment_my_bank_card_binding


    companion object {

        fun getInstance(): MyBankCardsBindingFragment {
            val fragment = MyBankCardsBindingFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun initView() {
        if (!StringUtils.isEmpty(CacheConfigUtils.getConfigCacheInstance(context!!).getString(Constant.cache_band_bank_card_hint))) {
            cl_bind_bank_card_hint.visibility = View.GONE
        } else {
            iv_delete_show_hint.setOnClickListener(this)
        }
        nv_bar.setOnBackClickListener {
            activity.onBackPressed()
        }
        tv_confirm.setOnClickListener(this)
        cl_bank_name.setOnClickListener(this)
    }

    fun checkInfoFormat(): Boolean {
        if (et_account.text.isEmpty()) {
            ToastUtils.showShort(getString(R.string.input_fit_length_account))
            return false
        }

        if (tv_bank_name.text == context.getString(R.string.enter_bank_type)) {
            ToastUtils.showShort(getString(R.string.enter_bank_type))
            return false
        }

        if (et_bank_card_number.text.length != 23) {
            ToastUtils.showShort(getString(R.string.enter_bank_error_card_number))
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.iv_delete_show_hint -> {
                CacheConfigUtils.getConfigCacheInstance(context!!).put(Constant.cache_band_bank_card_hint, Constant.cache_band_bank_card_hint)
                cl_bind_bank_card_hint.visibility = View.GONE
            }

            v?.id == R.id.tv_confirm -> {
                if (checkInfoFormat()) {
                    mPresenter.requestBindBandCard(CacheConfigUtils.parseUserInfo(context).resultData?.id.toString(), tv_bank_name.text.toString(),
                            et_bank_card_number.text.toString().replace(" ", ""), et_account.text.toString())
                }

            }

            v?.id == R.id.cl_bank_name -> {
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_select_bank_card)
                        .setConvertListener(object : ViewConvertListener() {
                            public override fun convertView(holder: ViewHolder, dialog: BaseNiceDialog) {
                                val dialogSelectBankCardsAdapter = DialogSelectBankCardsAdapter(context!!, mPresenter.initBankCardList())

                                val lRecyclerViewAdapter = LRecyclerViewAdapter(dialogSelectBankCardsAdapter)
                                lRecyclerViewAdapter.removeFooterView()
                                lRecyclerViewAdapter.removeHeaderView()

                                dialogSelectBankCardsAdapter.setOnItemClickListener(object : OnItemClickListener {
                                    override fun onItemClick(obj: Any?, position: Int) {

                                        val lastPosition = dialogSelectBankCardsAdapter.getLastSelectPosition()

                                        dialogSelectBankCardsAdapter.setSelectState(position)

                                        lRecyclerViewAdapter.notifyItemChanged(lRecyclerViewAdapter.getAdapterPosition(false, lastPosition),
                                                SelectBankCardBean.payloads)
                                        lRecyclerViewAdapter.notifyItemChanged(lRecyclerViewAdapter.getAdapterPosition(false, position), SelectBankCardBean.payloads)

                                    }
                                })
                                holder.getView<LRecyclerView>(R.id.rv_select_bank_card).let {
                                    it.adapter = lRecyclerViewAdapter
                                    it.setPullRefreshEnabled(false)
                                    it.setLoadMoreEnabled(false)
                                    it.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                                }
                                holder.getView<TextView>(R.id.tv_finish).setOnClickListener {
                                    if (!StringUtils.isEmpty(dialogSelectBankCardsAdapter.getSelectItem()?.text)) {
                                        tv_bank_name.text = dialogSelectBankCardsAdapter.getSelectItem()?.text
                                    }
                                    dialog.dismiss()
                                }

                                holder.getView<TextView>(R.id.tv_cancel).setOnClickListener {
                                    dialog.dismiss()
                                }
                            }
                        })
                        .setShowBottom(true)
                        .setHeight(300)
                        .show(fragmentManager)
            }

        }
    }

    override fun onBackPressedSupport(): Boolean {
        val bundle = Bundle()
        setFragmentResult(RESULT_OK, bundle)
        return super.onBackPressedSupport()
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
            Api.requestBindBandCard -> {
                val bundle = Bundle()
                bundle.putString(MyBankCardBindingApiBean.bindId, (obj as MyBankCardBindingApiBean).resultData?.bindid)
                setFragmentResult(RESULT_OK, bundle)
                activity.onBackPressed()
                hideSoftInput()
            }

        }
    }
}

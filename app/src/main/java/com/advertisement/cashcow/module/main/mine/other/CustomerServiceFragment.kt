package com.advertisement.cashcow.module.main.mine.other

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.bean.mine.MineApiBean
import com.advertisement.cashcow.module.main.mine.MineContract
import com.advertisement.cashcow.module.main.mine.MinePresenter
import com.advertisement.cashcow.util.ClipboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.othershe.nicedialog.BaseNiceDialog
import com.othershe.nicedialog.NiceDialog
import com.othershe.nicedialog.ViewConvertListener
import com.othershe.nicedialog.ViewHolder
import kotlinx.android.synthetic.main.fragment_customer_service.*


/**
 * 作者：吴国洪 on 2018/7/13
 * 描述：客服热线页面
 */

class CustomerServiceFragment : BaseFragment(), View.OnClickListener, MineContract.View {

    private val mPresenter by lazy { MinePresenter() }

    override fun getLayoutId(): Int = R.layout.fragment_customer_service

    companion object {
        fun getInstance(): CustomerServiceFragment {
            val fragment = CustomerServiceFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        nv_bar.setOnBackClickListener {
            activity.onBackPressed()
        }

        cl_hot_line.setOnClickListener(this)
        cl_wechat.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when {
            v?.id == R.id.cl_hot_line -> {
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_confirm_text)
                        .setConvertListener(object : ViewConvertListener() {
                            public override fun convertView(holder: ViewHolder, dialog: BaseNiceDialog) {
                                val tel = tv_hot_line.text
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

            v?.id == R.id.cl_wechat ->{
                ClipboardUtils.copyText(tv_wechat.text.toString())
                ToastUtils.showShort(getString(R.string.copied_to_the_cut_version))
            }
        }
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

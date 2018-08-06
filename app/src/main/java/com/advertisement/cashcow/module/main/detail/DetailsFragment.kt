package com.advertisement.cashcow.module.main.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.DetailsApi
import com.advertisement.cashcow.common.network.bean.DetailsApiBean
import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import com.advertisement.cashcow.module.login.password.LoginByPasswordFragment
import com.advertisement.cashcow.module.main.information.InformationBean
import com.advertisement.cashcow.util.CacheConfigUtils
import com.advertisement.cashcow.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.othershe.nicedialog.BaseNiceDialog
import com.othershe.nicedialog.NiceDialog
import com.othershe.nicedialog.ViewConvertListener
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * 作者：吴国洪 on 2018/6/23
 * 描述：广告详情页面
 */

class DetailsFragment : BaseFragment(), View.OnClickListener, DetailsContract.View {


    private var type: String? = null
    private var advertisementId: String? = null

    private val mPresenter by lazy { DetailsPresenter(DetailsFragment.javaClass.name) }

    private var detailsAdapter: DetailsAdapter? = null

    private var userInfo: LoginByPasswordApiBean? = null

    private var collectionStars = 0

    private var collectionStatus = ""

    private lateinit var arrayData: ArrayList<DetailsBean>

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun getLayoutId(): Int = R.layout.fragment_detail

    companion object {
        const val REQ_COLLECTION_CODE = 666

        fun getInstance(type: String, advertisementId: String): DetailsFragment {
            val fragment = DetailsFragment()
            val bundle = Bundle()
            fragment.type = type
            fragment.advertisementId = advertisementId

            fragment.arguments = bundle
            return fragment
        }
    }


    override fun initView() {
        detailsAdapter = DetailsAdapter(activity!!, arrayData)
        detailsAdapter!!.setOnWidgetClickListener(object : DetailsAdapter.OnClickListener {
            override fun callBack(text: String) {
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_confirm_text_single_item)
                        .setConvertListener(object : ViewConvertListener() {
                            @SuppressLint("SetTextI18n")
                            public override fun convertView(holder: com.othershe.nicedialog.ViewHolder, dialog: BaseNiceDialog) {
                                val fileSrcArray = text.split("：")

                                holder.setOnClickListener(R.id.tv_confirm) {
                                    ClipboardUtils.copyText(fileSrcArray[1])
                                    ToastUtils.showShort(getString(R.string.copied_to_the_cut_version))
                                    if (userInfo?.resultData != null) {
                                        mPresenter.requestAddwxEvent(context, userInfo?.resultData?.id!!, advertisementId.toString())
                                    }
                                    dialog.dismiss()
                                }
                                holder.getView<TextView>(R.id.tv_content).text = text
                                holder.getView<TextView>(R.id.tv_confirm).text = "复制${fileSrcArray[0]}到剪切版"
                            }
                        })
                        .setMargin(52)
                        .show(fragmentManager)
            }
        })

        rv_detail.layoutManager = linearLayoutManager
        rv_detail.adapter = detailsAdapter
        rv_detail.itemAnimator = DefaultItemAnimator()

        if (type == InformationBean.appWithPicType) {
            cl_app_type.visibility = View.VISIBLE
        }

        nv_bar.let {
            it.setOnBackClickListener {
                activity!!.finish()
            }

            it.setOnRightClickListener {

                if (collectionStatus == "0") {//已收藏
                    mPresenter.requestCancelStore((userInfo as LoginByPasswordApiBean).resultData?.id!!, advertisementId!!)

                } else {//未收藏
                    if (userInfo?.resultData != null) {
                        mPresenter.requestStoreAd((userInfo as LoginByPasswordApiBean).resultData?.id!!, advertisementId!!)
                    } else {
                        startForResult(LoginByPasswordFragment.getInstance(""), REQ_COLLECTION_CODE)
                    }
                }
            }
        }
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle) {
        super.onFragmentResult(requestCode, resultCode, data)
        userInfo = CacheConfigUtils.parseUserInfo(context!!)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQ_COLLECTION_CODE -> {
                    mPresenter.requestStoreAd((userInfo as LoginByPasswordApiBean).resultData?.id!!, advertisementId!!)
                }
            }
        }
    }

    override fun lazyLoad() {
        arrayData = ArrayList()
        mPresenter.attachView(this)

        userInfo = CacheConfigUtils.parseUserInfo(context!!)
        mPresenter.requestGetContentById(this.advertisementId.toString(), userInfo?.resultData?.id.toString())

    }

    override fun onClick(v: View?) {
        when {

        }
    }

    override fun preLoad(type: String, obj: Any) {
    }


    override fun afterLoad(type: String, obj: Any) {
    }

    override fun handleError(type: String, obj: Any) {
        when (type) {
            DetailsApi.requestStoreAd -> {
                if (obj.toString() == "此广告已收藏") {
                    collectionStatus = "0"
                    nv_bar.setRightTextAndImage((collectionStars).toString(), R.mipmap.details_collection_star)
                }
            }
        }
    }

    override fun handleSuccess(type: String, obj: Any) {
        when (type) {
            DetailsApi.requestGetContentById -> {
                val detailsBean = obj as DetailsApiBean
                tv_app_name.text = detailsBean.appname
                detailsAdapter?.addAll(mPresenter.parseAdData(detailsBean))

                if (detailsBean.storestatus == "0") {
                    nv_bar.setRightTextAndImage(detailsBean.storenum.toString(), R.mipmap.details_collection_star)
                } else {
                    nv_bar.setRightTextAndImage(detailsBean.storenum.toString(), R.mipmap.detail_dis_collection_star)
                }

                if (userInfo?.resultData != null) {
                    mPresenter.requestViewEvent(context, userInfo?.resultData?.id.toString(), advertisementId!!, detailsBean.adsc)
                }

                collectionStars = detailsBean.storenum
                collectionStatus = detailsBean.storestatus.toString()
            }

            DetailsApi.requestStoreAd -> {
                collectionStars += 1
                collectionStatus = "0"
                nv_bar.setRightTextAndImage((collectionStars).toString(), R.mipmap.details_collection_star)
                ToastUtils.showShort("收藏成功")
            }

            DetailsApi.requestCancelStore -> {
                collectionStars -= 1
                collectionStatus = "1"
                nv_bar.setRightTextAndImage((collectionStars).toString(), R.mipmap.detail_dis_collection_star)
                ToastUtils.showShort("取消收藏")

            }
        }
    }

}

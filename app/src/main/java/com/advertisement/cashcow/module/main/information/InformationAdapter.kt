package com.advertisement.cashcow.module.main.information

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseActivity
import com.advertisement.cashcow.module.main.information.subInformation.SubInformationFilterSelectAdapter
import com.advertisement.cashcow.module.main.information.subInformation.SubInformationFragment
import com.advertisement.cashcow.module.web.BasicWebActivity
import com.advertisement.cashcow.widget.recycleview.ViewHolder
import com.advertisement.cashcow.widget.recycleview.adapter.CommonAdapter
import com.blankj.utilcode.util.FileUtils
import com.donkingliang.banner.CustomBanner
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.hazz.kotlinmvp.view.recyclerview.MultipleType
import java.util.*


/**
 * 作者：吴国洪 on 2018/5/29
 * 描述：资讯模块列表 Adapter
 */

open class InformationAdapter(mContext: Context, data: ArrayList<InformationBean>) :
        CommonAdapter<InformationBean>(mContext, data, object : MultipleType<InformationBean> {
            override fun getLayoutId(item: InformationBean, position: Int): Int {
                return when (data[position].type) {
                    InformationBean.bannerType ->
                        R.layout.item_information_banner

                    InformationBean.filterType,
                    InformationBean.subFilterType ->
                        R.layout.item_information_recycleview_filter_type

                    InformationBean.advTypeSinglePic ->
                        R.layout.item_information_single_picture

                    InformationBean.advTypeMulPic ->
                        R.layout.item_information_multi_picture

                    InformationBean.videoType,
                    InformationBean.appWithPicType,
                    InformationBean.appWithVideoType ->
                        R.layout.item_information_apk

                    InformationBean.financialHeaderType ->
                        R.layout.item_sub_information_financial


                    else ->0
                }
            }
        }) {

    private var category = ""
    private var type = ""


    constructor(mContext: Context, data: ArrayList<InformationBean>, typeSupport: MultipleType<InformationBean>) : this(mContext, data)


    fun clear(startFormIndex: Int) {
        data.removeAll(data.drop(startFormIndex))
        notifyDataSetChanged()
    }


    //局部刷新关键：带payload的这个onBindViewHolder方法必须实现
    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            //注意：payloads的size总是1
            val payload = payloads[0] as String
            when (payload) {
                InformationBean.advTypeSinglePic -> {
                    initBannerView(holder)
                }
            }
        }
    }

    private fun initBannerView(holder: ViewHolder) {
        when (data[0].bannerTypeEntity!!.bannerList?.size!!) {
        //没数据更新时
            0 -> return

        //只有一条数据时，显示单张图片
            1 -> {
                val path = data[0].bannerTypeEntity!!.bannerList?.get(0)?.imgPath
                val uri = Uri.parse(path)

                if ("gif" == FileUtils.getFileExtension(path)) {
                    val controller = Fresco.newDraweeControllerBuilder()
                            .setUri(uri).build()
                    holder.getView<SimpleDraweeView>(R.id.sd_single_banner_view).controller = controller

                } else {
                    holder.getView<SimpleDraweeView>(R.id.sd_single_banner_view).setImageURI(uri)
                }
            }

        //有多条数据时显示bannerView
            else -> {
                holder.getView<SimpleDraweeView>(R.id.sd_single_banner_view).visibility = View.GONE
                holder.getView<CustomBanner<BannerEntity>>(R.id.cb_banner).let {
                    it.visibility = View.VISIBLE
                    it.setPages(object : CustomBanner.ViewCreator<BannerEntity?> {
                        override fun updateUI(context: Context?, view: View?, position: Int, bannerEntity: BannerEntity?) {

                            val mImageView = view!!.findViewById<SimpleDraweeView>(R.id.sdv_pic)
                            val path = bannerEntity!!.imgPath

                            val uri = Uri.parse(path)

                            if ("gif" == FileUtils.getFileExtension(path)) {
                                val controller = Fresco.newDraweeControllerBuilder()
                                        .setUri(uri).build()
                                mImageView.controller = controller

                            } else {
                                mImageView.setImageURI(uri)
                            }

                        }

                        override fun createView(context: Context?, position: Int): View {
                            return LayoutInflater.from(context).inflate(R.layout.item_details_picture, null)

                        }
                    }, data[0].bannerTypeEntity?.bannerList)
                            .setOnPageClickListener { position, p1 ->
                                mItemClickListener!!.onItemClick(data[0], position)

                            }
                            .startTurning(3000)
                }
            }
        }
    }

    /**
     * 绑定数据
     */
    override fun bindData(holder: ViewHolder, data: InformationBean, position: Int) {
        when (data.type) {
            InformationBean.bannerType -> {
                initBannerView(holder)
            }

            InformationBean.filterType -> {

                val recyclerView = holder.getView<RecyclerView>(R.id.rv_select_type)
                val size = data.filterTypeEntity!!.filterList!!.size.div(2)
                recyclerView.layoutManager = GridLayoutManager(mContext, size)
                val informationFilterSelectAdapter = InformationFilterSelectAdapter(mContext, data.filterTypeEntity!!.filterList!!, R.layout.item_information_item_filter_type)
                informationFilterSelectAdapter.setParameter(category)
                informationFilterSelectAdapter.setOnClickListener(object : InformationFilterSelectAdapter.OnClickListener {
                    override fun callBack(obj: InformationBean.FilterEntity, position: Int) {
                        type = obj.text
                    }
                })
                recyclerView.adapter = informationFilterSelectAdapter
            }

            InformationBean.advTypeSinglePic -> {
                holder.getView<SimpleDraweeView>(R.id.sdv_pic1).setImageURI(data.advertisementTypeSinglePicEntity?.picPath)
                holder.getView<TextView>(R.id.tv_title1).text = data.advertisementTypeSinglePicEntity?.text ?: ""
                holder.getView<TextView>(R.id.tv_publisher1).text = data.advertisementTypeSinglePicEntity?.publisher ?: ""
                holder.getView<TextView>(R.id.tv_release_time1).text = data.advertisementTypeSinglePicEntity?.releaseTime ?: ""
            }

            InformationBean.advTypeMulPic -> {
                holder.getView<SimpleDraweeView>(R.id.sdv_pic1).setImageURI(data.advertisementTypeMultiPicEntity?.picPathList?.get(0))
                holder.getView<SimpleDraweeView>(R.id.sdv_pic2).setImageURI(data.advertisementTypeMultiPicEntity?.picPathList?.get(1))
                holder.getView<SimpleDraweeView>(R.id.sdv_pic3).setImageURI(data.advertisementTypeMultiPicEntity?.picPathList?.get(2))
                holder.getView<TextView>(R.id.tv_title).text = data.advertisementTypeMultiPicEntity?.text ?: ""
                holder.getView<TextView>(R.id.tv_publisher).text = data.advertisementTypeMultiPicEntity?.publisher ?: ""
                holder.getView<TextView>(R.id.tv_release_time).text = data.advertisementTypeMultiPicEntity?.releaseTime ?: ""
            }

            InformationBean.videoType,
            InformationBean.appWithPicType,
            InformationBean.appWithVideoType -> {
                holder.getView<SimpleDraweeView>(R.id.sdv_pic).setImageURI(data.appEntity?.thumbPic)
                holder.getView<TextView>(R.id.tv_title).text = data.appEntity?.text ?: ""
                holder.getView<TextView>(R.id.tv_publisher).text = data.appEntity?.publisher ?: ""
                holder.getView<TextView>(R.id.tv_release_time).text = data.appEntity?.releaseTime ?: ""
                val download = holder.getView<ImageView>(R.id.iv_down_load)
                val play = holder.getView<ImageView>(R.id.iv_play)
                download.visibility =  View.GONE
                play.visibility =  View.GONE


                if (data.type == InformationBean.appWithVideoType) {
                    play.visibility = View.VISIBLE
                    download.visibility = View.VISIBLE
                }

                if (data.type == InformationBean.appWithPicType) {
                    download.visibility = View.VISIBLE
                }
                download.setOnClickListener {
                    val intent = Intent(mContext, BasicWebActivity::class.java)
                    intent.putExtra(BasicWebActivity.LoadURL, data.appEntity?.linkUrl)
                    intent.putExtra(BasicWebActivity.AdvertisementId, data.appEntity?.id)

                    mContext.startActivity(intent)
                    (mContext as BaseActivity).overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
                }

            }

            InformationBean.financialHeaderType -> {
                holder.getView<ImageView>(R.id.iv_loan).setOnClickListener {
                    (mContext as BaseActivity).start(SubInformationFragment.getInstance("贷款","资讯","金融", "贷款"))
                }

                holder.getView<ImageView>(R.id.iv_share).setOnClickListener {
                    (mContext as BaseActivity).start(SubInformationFragment.getInstance("股票","资讯","金融", "股票"))

                }

            }

            InformationBean.subFilterType -> {
                val recyclerView = holder.getView<RecyclerView>(R.id.rv_select_type)
                recyclerView.layoutManager = GridLayoutManager(mContext, 5)
                val informationFilterSelectAdapter = SubInformationFilterSelectAdapter(mContext, data.filterTypeEntity!!.filterList!!, R.layout.item_information_item_filter_type)
                informationFilterSelectAdapter.setParameter(this.category, type)
                recyclerView.adapter = informationFilterSelectAdapter
            }

            else -> throw IllegalAccessException("Api 解析出错了，出现其他类型")
        }
    }

    fun setParameter(category: String) {
        this.category = category
    }
}



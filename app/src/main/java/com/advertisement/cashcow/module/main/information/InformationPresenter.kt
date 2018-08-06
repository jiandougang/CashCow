package com.advertisement.cashcow.module.main.information

import android.content.Context
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.InformationApi
import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.advertisement.cashcow.common.network.bean.information.InformationAdApiBean
import com.advertisement.cashcow.common.network.bean.information.InformationBannerAdApiBean
import com.advertisement.cashcow.common.network.bean.information.StatisticsUnreadMessageApiBean
import com.advertisement.cashcow.module.main.information.subInformation.SubInformationFragment
import com.advertisement.cashcow.module.main.mine.myCollection.MyCollectionFragment
import com.advertisement.cashcow.util.CacheConfigUtils
import com.advertisement.cashcow.util.LocalCommonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import java.util.*
import kotlin.collections.ArrayList

/**
 * 作者：吴国洪 on 2018/5/28
 * 描述：InformationFragment逻辑处理类
 * 接口：
 */
open class InformationPresenter(type: String) : BasePresenter<InformationContract.View>(), InformationContract.Presenter {

    var type: String? = null

    init {
        this.type = type
    }

    override fun requestClickEvent(context: Context, userId: String, adId: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["userid"] = userId
        parameters["adid"] = adId

        when (type) {
            MyCollectionFragment.javaClass.name ->
                mRootView as MyCollectionFragment

            SubInformationFragment.javaClass.name ->
                mRootView as InformationFragment
            else ->
                mRootView as InformationFragment
        }


        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(InformationApi::class.java)

        novate.call(api.requestClickEvent(parameters), object : BaseSubscriber<TextApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                mRootView!!.handleError(InformationApi.requestClickEvent, e.toString())
            }

            override fun onStart() {

            }

            override fun onNext(t: TextApiBean) {
                LogUtils.d(t.toString())
                mRootView!!.handleSuccess(InformationApi.requestClickEvent, t)
            }

            override fun onCompleted() {
            }
        })
    }

    override fun requestStatisUnreadNews(context: Context, userId: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["userid"] = userId

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(InformationApi::class.java)

        novate.call(api.requestStatisUnreadNews(parameters), object : BaseSubscriber<StatisticsUnreadMessageApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.i(e.toString())
                (mRootView as InformationFragment).handleError(InformationApi.requestStatisUnreadNews, e.toString())
            }

            override fun onStart() {

            }

            override fun onNext(t: StatisticsUnreadMessageApiBean) {
                LogUtils.d(t.toString())
                if (t.resultCode == "0") {
                    (mRootView as InformationFragment).handleSuccess(InformationApi.requestStatisUnreadNews, t)
                } else {
                    (mRootView as InformationFragment).handleError(InformationApi.requestStatisUnreadNews, t.resultMsg.toString())
                }
            }

            override fun onCompleted() {
            }
        })
    }

    override fun initAllData(context: Context, page: String, title: String): ArrayList<InformationBean> {
        if (CacheConfigUtils.parseUserInfo(context).resultData != null) {
            requestStatisUnreadNews(context,
                    CacheConfigUtils.parseUserInfo(context).resultData?.id.toString())
        }
        requestGetTopAd(context, title)
        requestQueryAds(context, title, page)
        val data = ArrayList<InformationBean>()
        data.addAll(initEmptyBanner())
        data.addAll(initFilterTypeData(context, title))
        return data
    }


    override fun requestGetTopAd(context: Context, title: String) {
        val parameters: MutableMap<String, String> = HashMap()

        var category = ""
        when (title) {
            context.getString(R.string.information) ->
                category = "资讯"
            context.getString(R.string.feature) ->
                category = "特色"
            context.getString(R.string.special_price) ->
                category = "特价"
            context.getString(R.string.investment) ->
                category = "招商"
        }
        parameters["category"] = category


        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(InformationApi::class.java)

        novate.call(api.requestGetTopAd(parameters), object : BaseSubscriber<InformationBannerAdApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.i(e.toString())
                (mRootView as InformationFragment).handleError(InformationApi.requestGetTopAd, e.toString())
            }

            override fun onStart() {

            }

            override fun onNext(t: InformationBannerAdApiBean) {
                LogUtils.d(t.toString())
                (mRootView as InformationFragment).handleSuccess(InformationApi.requestGetTopAd, t)
            }

            override fun onCompleted() {
            }
        })
    }

    override fun requestQueryAds(context: Context, title: String, page: String) {
        val parameters: MutableMap<String, String> = HashMap()
        val category = parseTitle(title, context)

        parameters["category"] = category
        parameters["page"] = page
        parameters["rows"] = "10"


        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(InformationApi::class.java)

        novate.call(api.requestQueryAds(parameters), object : BaseSubscriber<InformationAdApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.i(e.toString())
                (mRootView as InformationFragment).handleError(InformationApi.requestQueryAds, e.toString())
            }

            override fun onStart() {

            }

            override fun onNext(t: InformationAdApiBean) {
                LogUtils.d(t.toString())
                (mRootView as InformationFragment).handleSuccess(InformationApi.requestQueryAds, t)
            }

            override fun onCompleted() {
            }
        })
    }

    override fun parseBannerAdToQueryAd(data: InformationBannerAdApiBean.ResultDataBean): InformationBean {
        val adApiBeanList: ArrayList<InformationAdApiBean.RowsBean> = ArrayList()
        val tempObj = InformationAdApiBean.RowsBean()
        tempObj.linkaddr = data.linkaddr
        tempObj.showtype = data.showtype
        tempObj.appaddr = data.appaddr
        tempObj.title = ""
        tempObj.company = ""
        tempObj.addedtime = ""

        //banner返回的只有一张图片地址
        when (data.showtype) {
            3 -> tempObj.filesrc = ",${data.filesrc}"
            5 -> tempObj.filesrc = "${data.filesrc},"
            else -> tempObj.filesrc = data.filesrc
        }

        tempObj.id = data.id
        adApiBeanList.add(tempObj)
        return parseAdData(adApiBeanList)[0]
    }

    override fun parseAdData(dataList: List<InformationAdApiBean.RowsBean>?): ArrayList<InformationBean> {
        val informationBeanArray: ArrayList<InformationBean> = ArrayList()

        if (dataList != null) {
            for (item in dataList) {
                val informationBean: InformationBean
                when (item.showtype) {

                    3 -> {//返回数据含有一张视频+一张图片
                        //第一条是视频链接，第二条是图片链接
                        //(包含有没下载链接和有下载链接)
                        val fileSrcArray = item.filesrc?.split(",")
                        val thumbPic = fileSrcArray?.get(1)

                        informationBean = if (!StringUtils.isEmpty(item.appaddr)) {//下载APP链接
                            InformationBean(InformationBean.appWithVideoType)
                        } else {//普通视频
                            InformationBean(InformationBean.videoType)
                        }

                        val videoEntity = InformationBean.VideoEntity(item.id,
                                item.title, LocalCommonUtils.encodePathWithUtf8(thumbPic),
                                item.company, LocalCommonUtils.getTimeFormatText(TimeUtils.string2Date(item.addedtime)),
                                item.linkaddr, item.appaddr,item.adsc)
                        informationBean.appEntity = videoEntity
                    }

                    4 -> {//返回数据含有一张图片(包含小图和大图)
                        if (!StringUtils.isEmpty(item.appaddr)) {//下载APP链接

                            informationBean = InformationBean(InformationBean.appWithPicType)

                            val appEntity = InformationBean.VideoEntity(item.id,
                                    item.title, LocalCommonUtils.encodePathWithUtf8(item.filesrc),
                                    item.company, item.addedtime,
                                    item.linkaddr, item.appaddr,item.adsc)
                            informationBean.appEntity = appEntity

                        } else {

                            informationBean = InformationBean(InformationBean.advTypeSinglePic)
                            val advertisementSinglePicEntity = InformationBean.AdvertisementSinglePicEntity(item.id,
                                    item.title, item.company, LocalCommonUtils.getTimeFormatText(TimeUtils.string2Date(item.addedtime)),
                                    LocalCommonUtils.encodePathWithUtf8(item.filesrc), item.linkaddr,item.adsc)
                            informationBean.advertisementTypeSinglePicEntity = advertisementSinglePicEntity

                        }
                    }

                    5 -> {//返回数据含有三张图片
                        val fileSrcArray = item.filesrc?.split(",") as ArrayList
                        // 遍历数组下标
                        for (indices in fileSrcArray.indices) {
                            fileSrcArray[indices] = LocalCommonUtils.encodePathWithUtf8(fileSrcArray[indices]).toString()
                        }

                        informationBean = InformationBean(InformationBean.advTypeMulPic)
                        val advertisementMultiPicEntity = InformationBean.AdvertisementMultiPicEntity(
                                item.id, item.title, item.company, LocalCommonUtils.getTimeFormatText(TimeUtils.string2Date(item.addedtime)),
                                fileSrcArray, item.linkaddr,item.adsc)
                        informationBean.advertisementTypeMultiPicEntity = advertisementMultiPicEntity
                    }

                    else -> {
                        informationBean = InformationBean(InformationBean.advTypeMulPic)
                    }
                }
                informationBeanArray.add(informationBean)
            }
        }

        return informationBeanArray
    }

    override fun initEmptyBanner(): ArrayList<InformationBean> {
        val informationBeanArray: ArrayList<InformationBean> = ArrayList()

        val bannerEntityList = ArrayList<BannerEntity>()


        val informationBean = InformationBean(InformationBean.bannerType)
        informationBean.bannerTypeEntity = InformationBean.BannerTypeItem(bannerEntityList)
        informationBeanArray.add(informationBean)
        return informationBeanArray
    }

    override fun initFilterTypeData(context: Context, title: String): ArrayList<InformationBean> {
        val informationBeanArray: ArrayList<InformationBean> = ArrayList()

        val filterEntityList = ArrayList<InformationBean.FilterEntity>()
        val textArr: Array<String>?
        val resArr: Array<Int>?

        when (title) {
            context.getString(R.string.feature),
            context.getString(R.string.special_price) -> {
                textArr = arrayOf(context.getString(R.string.brand_giant), context.getString(R.string.clothes),
                        context.getString(R.string.cate), context.getString(R.string.food),
                        context.getString(R.string.entertainment),
                        context.getString(R.string.travel), context.getString(R.string.accommodation), context.getString(R.string.traffic)
                        , context.getString(R.string.digital_appliance), context.getString(R.string.synthesis))

                resArr = arrayOf(R.mipmap.icon_filter_type_brand_giant, R.mipmap.icon_filter_type_clothes,
                        R.mipmap.icon_filter_type_cate, R.mipmap.icon_filter_type_food,
                        R.mipmap.icon_filter_type_entertainment, R.mipmap.icon_filter_type_travel,
                        R.mipmap.icon_filter_type_accmmodation, R.mipmap.icon_filter_type_traffic,
                        R.mipmap.icon_filter_type_digital_appliance, R.mipmap.icon_filter_type_synthesis)
            }

            context.getString(R.string.investment) -> {
                textArr = arrayOf(context.getString(R.string.brand_giant), context.getString(R.string.clothes),
                        context.getString(R.string.cate), context.getString(R.string.milk_tea),
                        context.getString(R.string.beauty),
                        context.getString(R.string.education_training), context.getString(R.string.car_maintenance), context.getString(R.string.building_shop)
                        , context.getString(R.string.digital_appliance), context.getString(R.string.synthesis))

                resArr = arrayOf(R.mipmap.icon_filter_type_brand_giant, R.mipmap.icon_filter_type_clothes,
                        R.mipmap.icon_filter_type_cate, R.mipmap.icon_filter_type_milk_tea,
                        R.mipmap.icon_filter_type_beauty, R.mipmap.icon_filter_type_education_training,
                        R.mipmap.icon_filter_type_car_maintenance, R.mipmap.icon_filter_type_buildind_shop,
                        R.mipmap.icon_filter_type_digital_appliance, R.mipmap.icon_filter_type_synthesis)
            }

            else -> {
                textArr = arrayOf(context.getString(R.string.brand_giant), context.getString(R.string.finance),
                        context.getString(R.string.game), context.getString(R.string.education),
                        context.getString(R.string.health_beauty),
                        context.getString(R.string.estate), context.getString(R.string.immigration), context.getString(R.string.all))

                resArr = arrayOf(R.mipmap.icon_filter_type_brand_giant, R.mipmap.icon_filter_type_finance,
                        R.mipmap.icon_filter_type_game, R.mipmap.icon_filter_type_education,
                        R.mipmap.icon_filter_type_health_beauty, R.mipmap.icon_filter_type_estate,
                        R.mipmap.icon_filter_type_immigration, R.mipmap.icon_filter_type_all)
            }

        }

        for (i in 0 until textArr.size) {
            val resId = resArr[i]
            val filterEntity = InformationBean.FilterEntity(textArr[i], resId)
            filterEntityList.add(filterEntity)
        }

        val informationBean = InformationBean(InformationBean.filterType)






        informationBean.filterTypeEntity = InformationBean.FilterTypeItem(filterEntityList)
        informationBeanArray.add(informationBean)

        return informationBeanArray

    }


    override fun parseTitle(title: String, context: Context): String {
        var category1 = ""
        when (title) {
            context.getString(R.string.information) ->
                category1 = "资讯"
            context.getString(R.string.feature) ->
                category1 = "特色"
            context.getString(R.string.special_price) ->
                category1 = "特价"
            context.getString(R.string.investment) ->
                category1 = "招商"
        }
        return category1
    }
}

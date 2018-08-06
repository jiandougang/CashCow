package com.advertisement.cashcow.module.main.information.subInformation

import android.content.Context
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.InformationApi
import com.advertisement.cashcow.common.network.bean.information.InformationAdApiBean
import com.advertisement.cashcow.module.main.information.InformationBean
import com.advertisement.cashcow.module.main.information.InformationPresenter
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import java.util.*
import kotlin.collections.ArrayList

/**
 * 作者：吴国洪 on 2018/5/28
 * 描述：InformationFragment逻辑处理类
 * 接口：
 */
open class SubInformationPresenter(type: String) : InformationPresenter(type) {

    fun initAllSubData(title: String, type: String, zType: String, page: String): ArrayList<InformationBean> {
        val informationBeanArray: ArrayList<InformationBean> = ArrayList()
        val context: Context = (mRootView as SubInformationFragment).context!!

        when (title) {
            context.getString(R.string.finance) -> {
                informationBeanArray.add(InformationBean(InformationBean.financialHeaderType))
            }
            context.getString(R.string.health_beauty),
            context.getString(R.string.clothes),
            context.getString(R.string.digital_appliance),
            context.getString(R.string.traffic),
            context.getString(R.string.travel),
            context.getString(R.string.synthesis) -> {
                informationBeanArray.addAll(initFilterTypeData(context, type))
            }

        }

        requestSubQueryAds(context, title, type, zType, page)
        return informationBeanArray
    }

    /**
     * 返回刷新时需要从哪个下标删除数据
     */
    fun getRecycleViewClearIndex(context: Context, type: String): Int {
        return when (type) {
            context.getString(R.string.finance),
            context.getString(R.string.health_beauty),
            context.getString(R.string.clothes),
            context.getString(R.string.digital_appliance),
            context.getString(R.string.traffic),
            context.getString(R.string.travel),
            context.getString(R.string.synthesis) -> {
                1
            }
            else -> {
                0
            }
        }
    }


    override fun initFilterTypeData(context: Context, type: String): ArrayList<InformationBean> {
        val informationBeanArray: ArrayList<InformationBean> = ArrayList()

        val filterEntityList = ArrayList<InformationBean.FilterEntity>()
        val textArr: Array<String>?
        val resArr: Array<Int>?

        when (type) {
            context.getString(R.string.health_beauty) -> {
                textArr = arrayOf(context.getString(R.string.skin_care), context.getString(R.string.breast_enhancement),
                        context.getString(R.string.speckle), context.getString(R.string.aphrodisiac),
                        context.getString(R.string.health_care_products))

                resArr = arrayOf(R.mipmap.sub_information_icon_skin, R.mipmap.sub_information_icon_breast,
                        R.mipmap.sub_information_icon_remove, R.mipmap.sub_information_icon_pill,
                        R.mipmap.sub_information_icon_health)
            }

            context.getString(R.string.clothes) -> {

                textArr = arrayOf(context.getString(R.string.women_clothing), context.getString(R.string.women_shoes),
                        context.getString(R.string.men_clothing), context.getString(R.string.men_shoes),
                        context.getString(R.string.underwear),
                        context.getString(R.string.mother_infant), context.getString(R.string.luggage), context.getString(R.string.sport)
                        , context.getString(R.string.outdoor), context.getString(R.string.other))

                resArr = arrayOf(R.mipmap.sub_information_icon_women_clothing, R.mipmap.sub_information_icon_women_shoes,
                        R.mipmap.sub_information_icon_men_clothing, R.mipmap.sub_information_icon_men_shoes,
                        R.mipmap.sub_information_icon_underwear, R.mipmap.sub_information_icon_mother_infant,
                        R.mipmap.sub_information_icon_luggage, R.mipmap.sub_information_icon_sport,
                        R.mipmap.sub_information_icon_outdoor, R.mipmap.sub_information_icon_other)
            }

            context.getString(R.string.digital_appliance) -> {
                textArr = arrayOf(context.getString(R.string.phone), context.getString(R.string.refrigerator),
                        context.getString(R.string.television), context.getString(R.string.computer),
                        context.getString(R.string.air_conditioner), context.getString(R.string.microwave_oven),
                        context.getString(R.string.sterilized_cupboard), context.getString(R.string.electric_fan),
                        context.getString(R.string.washing_machine), context.getString(R.string.other))

                resArr = arrayOf(R.mipmap.sub_information_icon_phone, R.mipmap.sub_information_icon_refrigerator,
                        R.mipmap.sub_information_icon_television, R.mipmap.sub_information_icon_computer,
                        R.mipmap.sub_information_icon_air_conditioner, R.mipmap.sub_information_icon_microwave_oven,
                        R.mipmap.sub_information_icon_sterilized_cupboard, R.mipmap.sub_information_icon_electric_fan,
                        R.mipmap.sub_information_icon_wash_machine, R.mipmap.sub_information_icon_other)
            }

            context.getString(R.string.traffic) -> {
                textArr = arrayOf(context.getString(R.string.plane), context.getString(R.string.train),
                        context.getString(R.string.car), context.getString(R.string.steamer),
                        context.getString(R.string.bike))

                resArr = arrayOf(R.mipmap.sub_information_icon_plane, R.mipmap.sub_information_icon_train,
                        R.mipmap.sub_information_icon_car, R.mipmap.sub_information_icon_steamer,
                        R.mipmap.sub_information_icon_bike)
            }

            context.getString(R.string.travel) -> {
                textArr = arrayOf(context.getString(R.string.sight_seeing_tour), context.getString(R.string.theme_tour),
                        context.getString(R.string.farmhouse), context.getString(R.string.parent_child_tour),
                        context.getString(R.string.comprehensive_tour))

                resArr = arrayOf(R.mipmap.sub_information_icon_sight_seeing, R.mipmap.sub_information_icon_theme,
                        R.mipmap.sub_information_icon_farmhouse, R.mipmap.sub_information_icon_parent_child,
                        R.mipmap.sub_information_icon_comprehensive)
            }

            context.getString(R.string.synthesis) -> {
                textArr = arrayOf(context.getString(R.string.tea_wine), context.getString(R.string.office_furniture),
                        context.getString(R.string.toy), context.getString(R.string.books),
                        context.getString(R.string.makeups), context.getString(R.string.flower_gardening),
                        context.getString(R.string.car_motorcycle), context.getString(R.string.medicine),
                        context.getString(R.string.instrument), context.getString(R.string.other))

                resArr = arrayOf(R.mipmap.sub_information_icon_tea_wine, R.mipmap.sub_information_icon_office_furniture,
                        R.mipmap.sub_information_icon_toy, R.mipmap.sub_information_icon_books,
                        R.mipmap.sub_information_icon_makeups, R.mipmap.sub_information_icon_flower_gardening,
                        R.mipmap.sub_information_icon_car_motocycle, R.mipmap.sub_information_icon_medicine,
                        R.mipmap.sub_information_icon_instrument, R.mipmap.sub_information_icon_other)
            }

            else -> {
                textArr = arrayOf(context.getString(R.string.tea_wine), context.getString(R.string.office_furniture),
                        context.getString(R.string.toy), context.getString(R.string.books),
                        context.getString(R.string.makeups), context.getString(R.string.flower_gardening),
                        context.getString(R.string.car_motorcycle), context.getString(R.string.medicine),
                        context.getString(R.string.instrument), context.getString(R.string.other))

                resArr = arrayOf(R.mipmap.sub_information_icon_tea_wine, R.mipmap.sub_information_icon_office_furniture,
                        R.mipmap.sub_information_icon_toy, R.mipmap.sub_information_icon_books,
                        R.mipmap.sub_information_icon_makeups, R.mipmap.sub_information_icon_flower_gardening,
                        R.mipmap.sub_information_icon_car_motocycle, R.mipmap.sub_information_icon_medicine,
                        R.mipmap.sub_information_icon_instrument, R.mipmap.sub_information_icon_other)
            }

        }

        for (i in 0 until textArr.size) {
            val resId = resArr[i]
            val filterEntity = InformationBean.FilterEntity(textArr[i], resId)
            filterEntityList.add(filterEntity)
        }

        val informationBean = InformationBean(InformationBean.subFilterType)

        informationBean.filterTypeEntity = InformationBean.FilterTypeItem(filterEntityList)
        informationBeanArray.add(informationBean)

        return informationBeanArray

    }


    private fun requestSubQueryAds(context: Context, title: String,
                                   type: String,
                                   zType: String,
                                   page: String) {
        val parameters: MutableMap<String, String> = HashMap()

        val category = parseTitle(title, context)

        parameters["page"] = page
        parameters["rows"] = InformationApi.REQUEST_COUNT.toString()
        parameters["category"] = category
        parameters["type"] = type
        parameters["ztype"] = zType

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(InformationApi::class.java)

        novate.call(api.requestQueryAds(parameters), object : BaseSubscriber<InformationAdApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                (mRootView as SubInformationFragment).handleError(InformationApi.requestQueryAds, e.toString())
            }

            override fun onStart() {

            }

            override fun onNext(t: InformationAdApiBean) {
                LogUtils.d(t.toString())
                (mRootView as SubInformationFragment).handleSuccess(InformationApi.requestQueryAds, t)
            }

            override fun onCompleted() {
                (mRootView as SubInformationFragment).afterLoad(InformationApi.requestQueryAds, "")
            }
        })
    }


}

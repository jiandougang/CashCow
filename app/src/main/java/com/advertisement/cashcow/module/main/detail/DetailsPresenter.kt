package com.advertisement.cashcow.module.main.detail

import android.content.Context
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.DetailsApi
import com.advertisement.cashcow.common.network.bean.DetailsApiBean
import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.advertisement.cashcow.module.video.VideoDetailActivity
import com.advertisement.cashcow.module.web.BasicWebActivity
import com.advertisement.cashcow.util.LocalCommonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import rx.Observable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.set

/**
 * 作者：吴国洪 on 2018/6/23
 * 描述：DetailFragment逻辑处理类
 */
class DetailsPresenter(type: String) : BasePresenter<DetailsContract.View>(), DetailsContract.Presenter {


    var type: String? = null

    init {
        this.type = type
    }


    override fun requestAddwxEvent(context: Context, userId: String, adId: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["adid"] = adId
        parameters["userid"] = userId

        val novate = NetworkConfig.getInstance(context!!)

        val api = novate.create(DetailsApi::class.java)

        novate.call(api.requestAddwxEvent(parameters), object : BaseSubscriber<TextApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                if (type == DetailsFragment.javaClass.name) {
                    (mRootView as DetailsFragment).handleError(DetailsApi.requestAddwxEvent, e.toString())
                } else {
                    (mRootView as VideoDetailActivity).handleError(DetailsApi.requestAddwxEvent, e.toString())
                }
            }

            override fun onStart() {

            }

            override fun onNext(t: TextApiBean) {
                LogUtils.d(t.toString())
                if (t.resultCode == "0") {
                    if (type == DetailsFragment.javaClass.name) {
                        (mRootView as DetailsFragment).handleSuccess(DetailsApi.requestAddwxEvent, t)
                    } else {
                        (mRootView as VideoDetailActivity).handleSuccess(DetailsApi.requestAddwxEvent, t)
                    }
                } else {
                    if (type == DetailsFragment.javaClass.name) {
                        (mRootView as DetailsFragment).handleError(DetailsApi.requestAddwxEvent, t.resultMsg.toString())
                    } else {
                        (mRootView as VideoDetailActivity).handleError(DetailsApi.requestAddwxEvent, t.resultMsg.toString())
                    }
                }
            }

            override fun onCompleted() {
            }
        })
    }


    override fun requestViewEvent(context: Context, userId: String, adId: String, delayTime: Int) {

        Observable.timer(delayTime.toLong(), TimeUnit.SECONDS).subscribe {

            val parameters: MutableMap<String, String> = HashMap()

            parameters["adid"] = adId
            parameters["userid"] = userId

            when (type) {
                BasicWebActivity.javaClass.name ->
                    mRootView as BasicWebActivity

                VideoDetailActivity.javaClass.name ->
                    mRootView as VideoDetailActivity
                else ->
                    mRootView as DetailsFragment
            }

            val novate = NetworkConfig.getInstance(context)

            val api = novate.create(DetailsApi::class.java)

            novate.call(api.requestViewEvent(parameters), object : BaseSubscriber<TextApiBean>(context) {
                override fun onError(e: Throwable?) {
                    LogUtils.e(e.toString())
                    mRootView!!.handleError(DetailsApi.requestViewEvent, e.toString())
                }

                override fun onStart() {

                }

                override fun onNext(t: TextApiBean) {
                    LogUtils.d(t.toString())
                    if (t.resultCode == "0") {
                        mRootView!!.handleSuccess(DetailsApi.requestViewEvent, t)
                    } else {
                        mRootView!!.handleError(DetailsApi.requestViewEvent, t.resultMsg.toString())
                    }
                }

                override fun onCompleted() {
                }
            })
        }
    }


    override fun requestCancelStore(userId: String, adId: String) {

        val parameters: MutableMap<String, String> = HashMap()

        parameters["adid"] = adId
        parameters["userid"] = userId

        LogUtils.d(type)
        val context = if (type == DetailsFragment.javaClass.name) {

            (mRootView as DetailsFragment).context
        } else {

            (mRootView as VideoDetailActivity)
        }

        val novate = NetworkConfig.getInstance(context!!)

        val api = novate.create(DetailsApi::class.java)

        novate.call(api.requestCancelStore(parameters), object : BaseSubscriber<TextApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                if (type == DetailsFragment.javaClass.name) {
                    (mRootView as DetailsFragment).handleError(DetailsApi.requestCancelStore, e.toString())
                } else {
                    (mRootView as VideoDetailActivity).handleError(DetailsApi.requestCancelStore, e.toString())
                }
            }

            override fun onStart() {

            }

            override fun onNext(t: TextApiBean) {
                LogUtils.d(t.toString())
                if (t.resultCode == "0") {
                    if (type == DetailsFragment.javaClass.name) {
                        (mRootView as DetailsFragment).handleSuccess(DetailsApi.requestCancelStore, t)
                    } else {
                        (mRootView as VideoDetailActivity).handleSuccess(DetailsApi.requestCancelStore, t)
                    }
                } else {
                    if (type == DetailsFragment.javaClass.name) {
                        (mRootView as DetailsFragment).handleError(DetailsApi.requestCancelStore, t.resultMsg.toString())
                    } else {
                        (mRootView as VideoDetailActivity).handleError(DetailsApi.requestCancelStore, t.resultMsg.toString())
                    }
                }
            }

            override fun onCompleted() {
            }
        })
    }

    override fun requestStoreAd(userId: String, adId: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["adid"] = adId
        parameters["userid"] = userId

        LogUtils.d(type)
        val context = if (type == DetailsFragment.javaClass.name) {

            (mRootView as DetailsFragment).context
        } else {

            (mRootView as VideoDetailActivity)
        }

        val novate = NetworkConfig.getInstance(context!!)

        val api = novate.create(DetailsApi::class.java)

        novate.call(api.requestStoreAd(parameters), object : BaseSubscriber<TextApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.d(e.toString())
                if (type == DetailsFragment.javaClass.name) {
                    (mRootView as DetailsFragment).handleError(DetailsApi.requestStoreAd, e.toString())
                } else {
                    (mRootView as VideoDetailActivity).handleError(DetailsApi.requestStoreAd, e.toString())
                }
            }

            override fun onStart() {

            }

            override fun onNext(t: TextApiBean) {
                LogUtils.d(t.toString())
                if (t.resultCode == "0") {
                    if (type == DetailsFragment.javaClass.name) {
                        (mRootView as DetailsFragment).handleSuccess(DetailsApi.requestStoreAd, t)
                    } else {
                        (mRootView as VideoDetailActivity).handleSuccess(DetailsApi.requestStoreAd, t)
                    }
                } else {
                    if (type == DetailsFragment.javaClass.name) {
                        (mRootView as DetailsFragment).handleError(DetailsApi.requestStoreAd, t.resultMsg.toString())
                    } else {
                        (mRootView as VideoDetailActivity).handleError(DetailsApi.requestStoreAd, t.resultMsg.toString())
                    }
                }
            }

            override fun onCompleted() {
            }
        })
    }

    override fun parseAdData(detailsApiBean: DetailsApiBean?): ArrayList<DetailsBean> {
        val detailsBeanArray: ArrayList<DetailsBean> = ArrayList()

        val title = DetailsBean(DetailsBean.titleType)
        val textEntity = DetailsBean.TextEntity(StringUtils.toSBC(detailsApiBean?.title))
        title.titleTypeEntity = textEntity
        detailsBeanArray.add(title)
        val user = DetailsBean(DetailsBean.userType)
        val userEntity = DetailsBean.UserEntity(detailsApiBean?.company.toString(),
                LocalCommonUtils.getTimeFormatText(TimeUtils.string2Date(detailsApiBean?.addedtime.toString()))!!
        )
        user.userTypeEntity = userEntity
        detailsBeanArray.add(user)

        if (detailsApiBean?.content != null) {
            for (item in detailsApiBean.content!!) {
                if (item.contains("/moneyTree//resources/images/")) {
                    val pic = DetailsBean(DetailsBean.pictureType)
                    val picEntity = DetailsBean.PictureEntity(item)
                    pic.pictureTypeEntity = picEntity
                    detailsBeanArray.add(pic)
                } else {
                    val content = DetailsBean(DetailsBean.contentType)
                    val contentEntity = DetailsBean.TextEntity(StringUtils.toSBC(item))
                    content.contentTypeEntity = contentEntity
                    detailsBeanArray.add(content)
                }
            }
        }

        val contact = DetailsBean(DetailsBean.contactType)
        val contactEntity = DetailsBean.ContactEntity(detailsApiBean?.qq.toString(), detailsApiBean?.wx.toString(), detailsApiBean?.phone.toString())
        contact.contactTypeEntity = contactEntity
        detailsBeanArray.add(contact)

        return detailsBeanArray
    }


    override fun requestGetContentById(id: String, userId: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["id"] = id
        parameters["userid"] = userId


        val context = if (type == DetailsFragment.javaClass.name) {

            (mRootView as DetailsFragment).context
        } else {

            (mRootView as VideoDetailActivity)
        }

        val novate = NetworkConfig.getInstance(context!!)

        val api = novate.create(DetailsApi::class.java)

        novate.call(api.requestGetContentById(parameters), object : BaseSubscriber<DetailsApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.d(e.toString())
                if (type == DetailsFragment.javaClass.name) {
                    (mRootView as DetailsFragment).handleError(DetailsApi.requestGetContentById, e.toString())
                } else {
                    (mRootView as VideoDetailActivity).handleError(DetailsApi.requestGetContentById, e.toString())
                }
            }

            override fun onStart() {

            }

            override fun onNext(t: DetailsApiBean) {
                LogUtils.d(t.toString())
                if (type == DetailsFragment.javaClass.name) {
                    (mRootView as DetailsFragment).handleSuccess(DetailsApi.requestGetContentById, t)
                } else {
                    (mRootView as VideoDetailActivity).handleSuccess(DetailsApi.requestGetContentById, t)
                }
            }

            override fun onCompleted() {
            }
        })
    }


    /**
     * 初始化所有数据
     */
    override fun initAllData(): ArrayList<DetailsBean> {
        val data = ArrayList<DetailsBean>()
        data.add(initTitleTypeData("有些事，既然能出现在生命中。\n" +
                "就一定有它的定数"))
        data.add(initNameTypeData("公司/用户名称", "1小时前"))
        data.add(initContentTypeData("有些事，既然出现在生命中。就一定有它的\n" +
                "定数，凡事不必太过勉强。只要做到合适适\n" +
                "度，就不必一直深记在生命当中。应该让它\n" +
                "随风飞翔寻找属于自己的一片天，而不是一\n" +
                "直囚禁在思念中。"))
        data.add(initPictureTypeData(""))

        return data
    }

    /**
     * 初始化标题Item
     */
    override fun initTitleTypeData(title: String): DetailsBean {
        val detailsBean = DetailsBean(DetailsBean.titleType)
        detailsBean.titleTypeEntity = DetailsBean.TextEntity(title)
        return detailsBean
    }

    /**
     * 初始化副标题Item
     */
    override fun initNameTypeData(name: String, time: String): DetailsBean {
        val detailsBean = DetailsBean(DetailsBean.userType)
        detailsBean.userTypeEntity = DetailsBean.UserEntity(name, time)
        return detailsBean
    }

    /**
     * 初始化内容Item
     */
    override fun initContentTypeData(content: String): DetailsBean {
        val detailsBean = DetailsBean(DetailsBean.contentType)
        detailsBean.contentTypeEntity = DetailsBean.TextEntity(content)
        return detailsBean
    }


    /**
     * 初始化图片Item
     */
    override fun initPictureTypeData(url: String): DetailsBean {
        val detailsBean = DetailsBean(DetailsBean.pictureType)
        detailsBean.pictureTypeEntity = DetailsBean.PictureEntity(url)
        return detailsBean
    }
}

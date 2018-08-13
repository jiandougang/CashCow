package com.advertisement.cashcow.module.main.information

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.broadcast.NormalBroadcastReceiver
import com.advertisement.cashcow.common.manager.EmptyActivity
import com.advertisement.cashcow.common.network.api.InformationApi
import com.advertisement.cashcow.common.network.bean.information.InformationAdApiBean
import com.advertisement.cashcow.common.network.bean.information.InformationBannerAdApiBean
import com.advertisement.cashcow.common.network.bean.information.StatisticsUnreadMessageApiBean
import com.advertisement.cashcow.module.login.password.LoginByPasswordFragment
import com.advertisement.cashcow.module.main.detail.DetailsFragment
import com.advertisement.cashcow.module.main.information.messagesCenter.MessagesCenterFragment
import com.advertisement.cashcow.module.main.search.SearchFragment
import com.advertisement.cashcow.module.video.VideoDetailActivity
import com.advertisement.cashcow.module.web.BasicWebActivity
import com.advertisement.cashcow.util.CacheConfigUtils
import com.advertisement.cashcow.util.LocalCommonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import com.hazz.kotlinmvp.view.recyclerview.adapter.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_information.*


open class InformationFragment : BaseFragment(), InformationContract.View, OnItemClickListener, View.OnClickListener {


    protected var mPresenter: InformationPresenter? = null

    private var informationAdapter: InformationAdapter? = null
    private var lRecyclerViewAdapter: LRecyclerViewAdapter? = null
    private var normalBroadcastReceiver: NormalBroadcastReceiver? = null

    private lateinit var arrayData: ArrayList<InformationBean>
    private var currentPage: Int = 1
    var title = ""

    override fun lazyLoad() {
        mPresenter = InformationPresenter(InformationFragment.javaClass.name)
        mPresenter!!.attachView(this)
        mPresenter!!.requestGetLastedVersion(context)
        arrayData = mPresenter!!.initAllData(context!!, currentPage.toString(), title)

        normalBroadcastReceiver = NormalBroadcastReceiver(object : NormalBroadcastReceiver.ReceiverCallBack {
            override fun callBack(intent: Intent) {
                if (CacheConfigUtils.parseUserInfo(context!!).resultData != null) {
                    mPresenter!!.requestStatisUnreadNews(context!!,
                            CacheConfigUtils.parseUserInfo(context!!).resultData?.id.toString())
                } else
                    tv_statistics.visibility = View.GONE

            }
        })

        val filter = IntentFilter()
        filter.addAction(NormalBroadcastReceiver.BroadcastName)
        activity!!.registerReceiver(normalBroadcastReceiver, filter)
    }


    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun getLayoutId(): Int = R.layout.fragment_information


    companion object {
        fun getInstance(title: String): InformationFragment {
            val fragment = InformationFragment()
            val bundle = Bundle()
            fragment.title = title
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (normalBroadcastReceiver != null) {
            activity!!.unregisterReceiver(normalBroadcastReceiver)
            normalBroadcastReceiver = null
        }
    }

    /**
     * 初始化 ViewI
     */
    override fun initView() {
        informationAdapter = InformationAdapter(activity!!, arrayData)
        informationAdapter!!.setOnItemClickListener(this)
        informationAdapter!!.setParameter(title)

        lRecyclerViewAdapter = LRecyclerViewAdapter(informationAdapter)

        rv_information.let {
            it.layoutManager = linearLayoutManager
            it.adapter = lRecyclerViewAdapter
            it.itemAnimator = DefaultItemAnimator()
            it.setPullRefreshEnabled(true)
            it.setFooterViewHint("玩命加载中", "没有更多了", "")

            it.setOnLoadMoreListener {
                currentPage++
                mPresenter!!.requestQueryAds(context!!, title, currentPage.toString())
            }
            it.setOnRefreshListener {
                currentPage = 0
                rv_information.setLoadMoreEnabled(true)
                informationAdapter?.clear(2)
                mPresenter!!.requestGetTopAd(context, title)
                mPresenter!!.requestQueryAds(context!!, title, currentPage.toString())
            }
        }

        cl_search.setOnClickListener(this)
        iv_message.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.cl_search -> {
                val intent = Intent(activity, EmptyActivity::class.java)
                intent.putExtra(EmptyActivity.Activity_Key, SearchFragment.javaClass.name)
                activity!!.startActivity(intent)
                activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
            }

            v?.id == R.id.iv_message -> {
                val intent = Intent(activity, EmptyActivity::class.java)

                if (CacheConfigUtils.parseUserInfo(context!!).resultData != null) {
                    intent.putExtra(EmptyActivity.Activity_Key, MessagesCenterFragment.javaClass.name)
                } else {
                    intent.putExtra(LoginByPasswordFragment.Finish_Activity_For_Result, LoginByPasswordFragment.Finish_Activity_For_Result)
                    intent.putExtra(EmptyActivity.Activity_Key, LoginByPasswordFragment.javaClass.name)
                }
                activity!!.startActivity(intent)
                activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
            }
        }
    }

    override fun startActivity(entity: InformationBean.VideoEntity) {
        val intent = Intent(activity, VideoDetailActivity::class.java)
        startActivity(intent)
        activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
    }


    override fun onItemClick(obj: Any?, position: Int) {
        when ((obj as InformationBean).type) {
            InformationBean.bannerType -> {
                if (obj.bannerTypeEntity?.bannerList?.isEmpty()!!) return

                val tempObj = obj.bannerTypeEntity?.bannerList?.get(position)?.obj
                        as InformationBannerAdApiBean.ResultDataBean

                onItemClick(mPresenter!!.parseBannerAdToQueryAd(tempObj), position)
            }

            InformationBean.filterType ->
                R.layout.item_information_recycleview_filter_type

            InformationBean.advTypeSinglePic -> {
                startNextPage(
                        {
                            startDetailsFragment(obj.advertisementTypeSinglePicEntity?.id.toString(), InformationBean.advTypeSinglePic)
                        },
                        obj.advertisementTypeSinglePicEntity?.linkUrl!!,
                        obj.advertisementTypeSinglePicEntity?.id!!,
                        obj.advertisementTypeSinglePicEntity?.adsc!!,
                        obj.advertisementTypeSinglePicEntity?.gold!!)
            }
            InformationBean.advTypeMulPic ->
                startNextPage({ startDetailsFragment(obj.advertisementTypeMultiPicEntity?.id.toString(), InformationBean.advTypeMulPic) },
                        obj.advertisementTypeMultiPicEntity?.linkUrl,
                        obj.advertisementTypeMultiPicEntity?.id!!,
                        obj.advertisementTypeMultiPicEntity?.adsc!!,
                        obj.advertisementTypeMultiPicEntity?.gold!!)

            InformationBean.appWithPicType ->
                startNextPage({ startDetailsFragment(obj.appEntity?.id.toString(), InformationBean.appWithPicType) },
                        obj.appEntity?.linkUrl!!,
                        obj.appEntity?.id!!,
                        obj.appEntity?.adsc!!,
                        obj.appEntity?.gold!!)

            InformationBean.videoType,
            InformationBean.appWithVideoType -> {
                startNextPage({ startVideoActivity(obj) }, obj.appEntity?.linkUrl!!,
                        obj.appEntity?.id!!,
                        obj.appEntity?.adsc!!,
                        obj.appEntity?.gold!!)

            }

            else -> {}

        }
    }


    protected open fun startNextPage(callBack: () -> Unit, linkUrl: String?, adId: String, adsc: Int,gold:Int) {
        if (CacheConfigUtils.parseUserInfo(context!!).resultData != null) {
            mPresenter!!.requestClickEvent(context, CacheConfigUtils.parseUserInfo(context!!).resultData?.id.toString(), adId)
        }

        if (StringUtils.isEmpty(linkUrl)) {
            callBack()
        } else {
            val intent = Intent(activity, BasicWebActivity::class.java)
            intent.putExtra(BasicWebActivity.LoadURL, linkUrl)
            intent.putExtra(BasicWebActivity.AdvertisementId, adId)
            intent.putExtra(BasicWebActivity.AdvertisementDuration, adsc)
            intent.putExtra(BasicWebActivity.AdvertisementCoins, gold)


            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
        }
    }

    private fun startVideoActivity(obj: InformationBean) {
        val intent = Intent(activity, VideoDetailActivity::class.java)
        intent.putExtra(VideoDetailActivity.InformationBean_Type, obj.type)
        intent.putExtra(VideoDetailActivity.InformationBean_Advertisement_Id, obj.appEntity?.id)

        activity!!.startActivity(intent)
        activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
    }

    protected fun startDetailsFragment(id: String, type: String) {
        val intent = Intent(activity, EmptyActivity::class.java)
        intent.putExtra(EmptyActivity.Activity_Key, DetailsFragment.javaClass.name)
        intent.putExtra(VideoDetailActivity.InformationBean_Type, type)

        intent.putExtra(EmptyActivity.DetailsFragment_Advertisement_Id, id)
        activity!!.startActivity(intent)
        activity!!.overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)
    }

    override fun preLoad(type: String, obj: Any) {
    }

    override fun afterLoad(type: String, obj: Any) {
    }

    override fun handleError(type: String, obj: Any) {
    }

    override fun handleSuccess(type: String, obj: Any) {
        when (type) {
            InformationApi.requestGetTopAd -> {

                val informationApi = obj as InformationBannerAdApiBean
                if (informationApi.resultData?.size == 0) return

                val position = 0//指定列表中的第0个item

                val bannerEntityList = ArrayList<BannerEntity>()
                for (j in 0 until (informationApi.resultData?.size ?: 0)) {//创建banner实体
                    bannerEntityList.add(BannerEntity(informationApi.resultData?.get(j)!!,
                            LocalCommonUtils.encodePathWithUtf8(informationApi.resultData?.get(j)?.filesrc!!)!!))
                }

                informationAdapter?.getDataList()?.get(0)?.bannerTypeEntity = InformationBean.BannerTypeItem(bannerEntityList)

                //RecyclerView局部刷新
                lRecyclerViewAdapter?.notifyItemChanged(lRecyclerViewAdapter!!.getAdapterPosition(false, position), InformationBean.advTypeSinglePic)
            }

            InformationApi.requestQueryAds -> {

                rv_information.refreshComplete(InformationApi.REQUEST_COUNT)
                val informationApi = obj as InformationAdApiBean
                informationAdapter?.addAll(mPresenter!!.parseAdData(informationApi.rows))

                if (informationApi.pageNo == informationApi.pages){
                    rv_information.setLoadMoreEnabled(false)
                    rv_information.setNoMore(true)
                }
            }

            InformationApi.requestStatisUnreadNews -> {
                obj as StatisticsUnreadMessageApiBean
                tv_statistics.visibility = View.VISIBLE
                tv_statistics.text = obj.resultData.toString()
            }

            InformationApi.requestGetLastedVersion ->{

            }
        }
    }
}

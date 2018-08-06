package com.advertisement.cashcow.module.video

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseActivity
import com.advertisement.cashcow.common.manager.EmptyActivity
import com.advertisement.cashcow.common.network.api.DetailsApi
import com.advertisement.cashcow.common.network.bean.DetailsApiBean
import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import com.advertisement.cashcow.module.login.password.LoginByPasswordFragment
import com.advertisement.cashcow.module.main.detail.*
import com.advertisement.cashcow.module.main.information.InformationBean
import com.advertisement.cashcow.thirdLibs.avplayer.cover.ControllerCover
import com.advertisement.cashcow.thirdLibs.avplayer.play.DataInter
import com.advertisement.cashcow.util.CacheConfigUtils
import com.advertisement.cashcow.util.ClipboardUtils
import com.advertisement.cashcow.util.LocalCommonUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler
import com.kk.taurus.playerbase.entity.DataSource
import com.kk.taurus.playerbase.receiver.ReceiverGroup
import com.kk.taurus.playerbase.widget.BaseVideoView
import com.othershe.nicedialog.BaseNiceDialog
import com.othershe.nicedialog.NiceDialog
import com.othershe.nicedialog.ViewConvertListener
import kotlinx.android.synthetic.main.activity_video_detail.*
import me.yokeyword.fragmentation.SupportFragment


/**
 * 作者：吴国洪 on 2018/6/13
 * 描述：视频详情页
 * presenter沿用DetailFragmentPresent,功能和DetailFragment一样,
 * 这里需要视频转屏,fragment操作不方便
 */
class VideoDetailActivity : BaseActivity(), DetailsContract.View {

    private var receiverGroup: ReceiverGroup? = null
    private var isLandscape: Boolean = false
    private var advertisementId: String? = null

    private val mPresenter by lazy { DetailsPresenter(VideoDetailActivity.javaClass.name) }

    private var detailsAdapter: DetailsAdapter? = null

    private lateinit var arrayData: ArrayList<DetailsBean>

    private var collectionStars = 0

    private var collectionStatus = ""

    private var userInfo: LoginByPasswordApiBean? = null


    var type: String? = null

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        const val InformationBean_Type = "DetailsFragment_Type"
        const val InformationBean_Advertisement_Id = "DetailsFragment_Advertisement_Id"

        const val REQ_CODE = 666

    }

    private val mOnEventAssistHandler by lazy {
        object : OnVideoViewEventHandler() {
            override fun onAssistHandle(assist: BaseVideoView?, eventCode: Int, bundle: Bundle?) {
                super.onAssistHandle(assist, eventCode, bundle)

                when (eventCode) {
                    DataInter.Event.EVENT_CODE_REQUEST_BACK -> {
                        if (isLandscape) {
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        } else {
                            finish()
                        }
                    }
                    DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN -> {
                        requestedOrientation = if (isLandscape)
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        else
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }
                }
            }
        }
    }

    override fun layoutId(): Int = R.layout.activity_video_detail

    override fun initData() {
        type = intent.getStringExtra(EmptyActivity.DetailsFragment_Type)

        arrayData = ArrayList()
        mPresenter.attachView(this)
        advertisementId = intent.getStringExtra(InformationBean_Advertisement_Id)
        userInfo = CacheConfigUtils.parseUserInfo(this)
        mPresenter.requestGetContentById(this.advertisementId.toString(), userInfo?.resultData?.id.toString())


    }

    override fun initView() {
        if (type == InformationBean.appWithVideoType) {
            cl_app_type.visibility = View.VISIBLE
        }

        detailsAdapter = DetailsAdapter(this, arrayData)
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
                                        mPresenter.requestAddwxEvent(this@VideoDetailActivity, userInfo?.resultData?.id!!, advertisementId.toString())
                                    }
                                    dialog.dismiss()
                                }
                                holder.getView<TextView>(R.id.tv_content).text = text
                                holder.getView<TextView>(R.id.tv_confirm).text = "复制${fileSrcArray[0]}到剪切版"
                            }
                        })
                        .setMargin(52)
                        .show(supportFragmentManager)
            }
        })
        rv_detail.layoutManager = linearLayoutManager
        rv_detail.adapter = detailsAdapter
        rv_detail.itemAnimator = DefaultItemAnimator()

        receiverGroup = ReceiverGroup()

        //Controller组件
        receiverGroup!!.addReceiver(DataInter.ReceiverKey.KEY_CONTROLLER_COVER, ControllerCover(this))
        receiverGroup!!.groupValue.putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, true)

        updateVideoViewLayoutState(false)

        videoView.setReceiverGroup(receiverGroup)
        videoView.setEventHandler(mOnEventAssistHandler)




        nv_bar.let {
            it.setOnBackClickListener {
                finish()
            }

            it.setOnRightClickListener {

                if (collectionStatus == "0") {//已收藏
                    mPresenter.requestCancelStore((userInfo as LoginByPasswordApiBean).resultData?.id!!, advertisementId!!)

                } else {//未收藏
                    if (userInfo?.resultData != null) {
                        mPresenter.requestStoreAd((userInfo as LoginByPasswordApiBean).resultData?.id!!, advertisementId!!)
                    } else {
                        val intent = Intent(this, EmptyActivity::class.java)
                        intent.putExtra(EmptyActivity.Activity_Key, LoginByPasswordFragment.javaClass.name)
                        intent.putExtra(LoginByPasswordFragment.Finish_Activity_For_Result, LoginByPasswordFragment.Finish_Activity_For_Result)

                        startActivityForResult(intent, REQ_CODE)
                        overridePendingTransition(R.anim.activity_slide_enter_left, R.anim.activity_slide_enter_left)

                    }
                }
            }
        }
    }

    override fun configSetting() {
        super.configSetting()
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DetailsFragment.REQ_COLLECTION_CODE && resultCode == SupportFragment.RESULT_OK) {
            userInfo = CacheConfigUtils.parseUserInfo(this)
            mPresenter.requestStoreAd((userInfo as LoginByPasswordApiBean).resultData?.id!!, advertisementId!!)
        }
    }

    override fun onPause() {
        super.onPause()
        videoView.pause()
    }

    override fun onResume() {
        super.onResume()
        videoView.resume()
    }

    public override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandscape = true
            updateVideoViewLayoutState(true)
        } else {
            isLandscape = false
            updateVideoViewLayoutState(false)
        }
        receiverGroup!!.groupValue.putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, isLandscape)
    }

    override fun onBackPressedSupport() {
        if (isLandscape) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else super.onBackPressedSupport()
    }

    /**
     * 横竖屏切换状态变更
     * @param landscape true为横屏 ，false为竖屏
     */
    private fun updateVideoViewLayoutState(landscape: Boolean) {
        val layoutParams = videoView.layoutParams as ConstraintLayout.LayoutParams
        if (landscape) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.setMargins(0, 0, 0, 0)

        } else {
            layoutParams.width = ScreenUtils.getScreenWidth()
            layoutParams.height = layoutParams.width * 9 / 16
            layoutParams.setMargins(0, 0, 0, 0)

        }
        videoView.layoutParams = layoutParams

    }

    override fun handleError(type: String, obj: Any) {
        when (type) {
            DetailsApi.requestStoreAd -> {
                if (obj.toString() == "此广告已收藏") {
                    collectionStatus = "0"
                    nv_bar.setRightTextAndImage((collectionStars).toString(), R.mipmap.details_collection_star)
                }
                ToastUtils.showShort(obj.toString())
            }
        }
    }

    override fun handleSuccess(type: String, obj: Any) {
        when (type) {
            DetailsApi.requestGetContentById -> {
                val detailsBean = obj as DetailsApiBean
                tv_app_name.text = detailsBean.appname

                detailsAdapter?.addAll(mPresenter.parseAdData(detailsBean))

                val data = DataSource(LocalCommonUtils.encodePathWithUtf8(detailsBean.video))
                videoView.setDataSource(data)
                videoView.start()


                if (detailsBean.storestatus == "0") {
                    nv_bar.setRightTextAndImage(detailsBean.storenum.toString(), R.mipmap.details_collection_star)
                } else {
                    nv_bar.setRightTextAndImage(detailsBean.storenum.toString(), R.mipmap.detail_dis_collection_star)
                }

                if (userInfo?.resultData != null) {
                    mPresenter.requestViewEvent(this, userInfo?.resultData?.id.toString(), advertisementId!!, detailsBean.adsc)
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

    override fun preLoad(type: String, obj: Any) {
    }

    override fun afterLoad(type: String, obj: Any) {
    }
}

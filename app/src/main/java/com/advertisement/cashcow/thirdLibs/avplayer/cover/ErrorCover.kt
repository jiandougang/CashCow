package com.advertisement.cashcow.thirdLibs.avplayer.cover

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.thirdLibs.avplayer.play.DataInter
import com.kk.taurus.playerbase.config.PConst
import com.kk.taurus.playerbase.event.BundlePool
import com.kk.taurus.playerbase.event.EventKey
import com.kk.taurus.playerbase.event.OnPlayerEventListener
import com.kk.taurus.playerbase.receiver.BaseCover
import com.kk.taurus.playerbase.receiver.ICover
import com.kk.taurus.playerbase.receiver.IReceiverGroup
import com.kk.taurus.playerbase.utils.NetworkUtils

/**
 * Created by Taurus on 2018/4/20.
 */

class ErrorCover(context: Context) : BaseCover(context) {

    internal val STATUS_ERROR = -1
    internal val STATUS_UNDEFINE = 0
    internal val STATUS_MOBILE = 1
    internal val STATUS_NETWORK_ERROR = 2

    internal var mStatus = STATUS_UNDEFINE

    internal var mInfo: TextView? = null
    internal var mRetry: TextView? = null

    private var mErrorShow: Boolean = false

    private var mCurrPosition: Int = 0

    private val mOnGroupValueUpdateListener = object : IReceiverGroup.OnGroupValueUpdateListener {
        override fun filterKeys(): Array<String> {
            return arrayOf(DataInter.Key.KEY_NETWORK_STATE)
        }

        override fun onValueUpdate(key: String, value: Any) {
            if (key == DataInter.Key.KEY_NETWORK_STATE) {
                val networkState = value as Int
                if (networkState == PConst.NETWORK_STATE_WIFI && mErrorShow) {
                    val bundle = BundlePool.obtain()
                    bundle.putInt(EventKey.INT_DATA, mCurrPosition)
                    requestRetry(bundle)
                }
                handleStatusUI(networkState)
            }
        }
    }

    override fun onReceiverBind() {
        super.onReceiverBind()

        initView()
        groupValue.registerOnGroupValueUpdateListener(mOnGroupValueUpdateListener)

    }

    override fun onCoverAttachedToWindow() {
        super.onCoverAttachedToWindow()
        handleStatusUI(NetworkUtils.getNetworkState(context))
    }

    override fun onReceiverUnBind() {
        super.onReceiverUnBind()
        groupValue.unregisterOnGroupValueUpdateListener(mOnGroupValueUpdateListener)
    }


    private fun initView() {
        findViewById<TextView>(R.id.tv_retry).setOnClickListener({
            handleStatus()
        })
    }

    private fun handleStatus() {
        val bundle = BundlePool.obtain()
        bundle.putInt(EventKey.INT_DATA, mCurrPosition)
        when (mStatus) {
            STATUS_ERROR -> {
                setErrorState(false)
                requestRetry(bundle)
            }
//            STATUS_MOBILE -> {
//                App.ignoreMobile = true
//                setErrorState(false)
//                requestResume(bundle)
//            }
            STATUS_NETWORK_ERROR -> {
                setErrorState(false)
                requestRetry(bundle)
            }
        }
    }

    private fun handleStatusUI(networkState: Int) {
        if (!groupValue.getBoolean(DataInter.Key.KEY_NETWORK_RESOURCE))
            return
        if (networkState < 0) {
            mStatus = STATUS_NETWORK_ERROR
            setErrorInfo("无网络！")
            setHandleInfo("重试")
            setErrorState(true)
        } else {
            if (networkState == PConst.NETWORK_STATE_WIFI) {
                if (mErrorShow) {
                    setErrorState(false)
                }
            }
//            else {
//                if (App.ignoreMobile)
//                    return
//                mStatus = STATUS_MOBILE
//                setErrorInfo("您正在使用移动网络！")
//                setHandleInfo("继续")
//                setErrorState(true)
//            }
        }
    }

    private fun setErrorInfo(text: String) {
        mInfo!!.text = text
    }

    private fun setHandleInfo(text: String) {
        mRetry!!.text = text
    }

    private fun setErrorState(state: Boolean) {
        mErrorShow = state
        setCoverVisibility(if (state) View.VISIBLE else View.GONE)
        if (!state) {
            mStatus = STATUS_UNDEFINE
        } else {
            notifyReceiverEvent(DataInter.Event.EVENT_CODE_ERROR_SHOW, null)
        }
        groupValue.putBoolean(DataInter.Key.KEY_ERROR_SHOW, state)
    }

    override fun onPlayerEvent(eventCode: Int, bundle: Bundle) {
        when (eventCode) {
            OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET -> handleStatusUI(NetworkUtils.getNetworkState(context))
            OnPlayerEventListener.PLAYER_EVENT_ON_TIMER_UPDATE -> mCurrPosition = bundle.getInt(EventKey.INT_ARG1)
        }
    }

    override fun onErrorEvent(eventCode: Int, bundle: Bundle) {
        mStatus = STATUS_ERROR
        if (!mErrorShow) {
            setErrorInfo("出错了！")
            setHandleInfo("重试")
            setErrorState(true)
        }
    }

    override fun onReceiverEvent(eventCode: Int, bundle: Bundle) {

    }

    override fun onCreateCoverView(context: Context): View {
        return View.inflate(context, R.layout.widget_error_cover, null)
    }

    override fun getCoverLevel(): Int {
        return ICover.COVER_LEVEL_MEDIUM
    }
}

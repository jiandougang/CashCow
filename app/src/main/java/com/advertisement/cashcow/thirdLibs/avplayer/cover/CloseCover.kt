package com.advertisement.cashcow.thirdLibs.avplayer.cover

import android.content.Context
import android.os.Bundle
import android.view.View

import com.advertisement.cashcow.R
import com.advertisement.cashcow.thirdLibs.avplayer.play.DataInter
import com.kk.taurus.playerbase.receiver.BaseCover
import com.kk.taurus.playerbase.receiver.ICover

class CloseCover(context: Context) : BaseCover(context), View.OnClickListener {

    override fun onReceiverBind() {
        super.onReceiverBind()
        findViewById<View>(R.id.iv_close).setOnClickListener(this)
    }


    override fun onClick(v: View) {
        notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_CLOSE, null)
    }

    override fun onReceiverUnBind() {
        super.onReceiverUnBind()
    }

    override fun onCreateCoverView(context: Context): View {
        return View.inflate(context, R.layout.widget_close_cover, null)
    }

    override fun onPlayerEvent(eventCode: Int, bundle: Bundle) {

    }

    override fun onErrorEvent(eventCode: Int, bundle: Bundle) {

    }

    override fun onReceiverEvent(eventCode: Int, bundle: Bundle) {

    }

    override fun getCoverLevel(): Int {
        return ICover.COVER_LEVEL_HIGH
    }


}

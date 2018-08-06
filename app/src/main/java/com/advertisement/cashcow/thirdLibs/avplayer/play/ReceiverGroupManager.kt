package com.advertisement.cashcow.thirdLibs.avplayer.play

import android.content.Context
import com.advertisement.cashcow.thirdLibs.avplayer.cover.CompleteCover
import com.advertisement.cashcow.thirdLibs.avplayer.cover.ControllerCover
import com.advertisement.cashcow.thirdLibs.avplayer.cover.ErrorCover
import com.advertisement.cashcow.thirdLibs.avplayer.cover.LoadingCover
import com.advertisement.cashcow.thirdLibs.avplayer.play.DataInter.ReceiverKey.*
import com.kk.taurus.playerbase.receiver.GroupValue
import com.kk.taurus.playerbase.receiver.ReceiverGroup

/**
 * Created by Taurus on 2018/4/18.
 */

class ReceiverGroupManager private constructor() {

    fun getLittleReceiverGroup(context: Context): ReceiverGroup {
        return getLiteReceiverGroup(context, null)
    }

    fun getLittleReceiverGroup(context: Context, groupValue: GroupValue): ReceiverGroup {
        val receiverGroup = ReceiverGroup(groupValue)
        receiverGroup.addReceiver(KEY_LOADING_COVER, LoadingCover(context))
        receiverGroup.addReceiver(KEY_COMPLETE_COVER, CompleteCover(context))
        receiverGroup.addReceiver(KEY_ERROR_COVER, ErrorCover(context))
        return receiverGroup
    }

    private fun getLiteReceiverGroup(context: Context, groupValue: GroupValue? = null): ReceiverGroup {
        val receiverGroup = ReceiverGroup(groupValue)
        receiverGroup.addReceiver(KEY_LOADING_COVER, LoadingCover(context))
        receiverGroup.addReceiver(KEY_CONTROLLER_COVER, ControllerCover(context))
        receiverGroup.addReceiver(KEY_COMPLETE_COVER, CompleteCover(context))
        receiverGroup.addReceiver(KEY_ERROR_COVER, ErrorCover(context))
        return receiverGroup
    }

    companion object {

        private var i: ReceiverGroupManager? = null

        fun get(): ReceiverGroupManager? {
            if (null == i) {
                synchronized(ReceiverGroupManager::class.java) {
                    if (null == i) {
                        i = ReceiverGroupManager()
                    }
                }
            }
            return i
        }
    }


}

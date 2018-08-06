package com.advertisement.cashcow.thirdLibs.bottomDialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.support.annotation.UiThread
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.advertisement.cashcow.R

class BottomDialog internal constructor(private val builder: Builder?) {

    init {
        this.builder!!.bottomDialog = initBottomDialog(builder)
    }

    @UiThread
    fun show() {
        if (builder?.bottomDialog != null)
            builder.bottomDialog!!.show()
    }

    @UiThread
    fun dismiss() {
        if (builder?.bottomDialog != null)
            builder.bottomDialog!!.dismiss()
    }

    @SuppressLint("InflateParams")
    @UiThread
    private fun initBottomDialog(builder: Builder): Dialog {
        val bottomDialog = Dialog(builder.context, R.style.BottomDialogs)
//        val view = LayoutInflater.from(builder.context).inflate(R.layout.library_bottom_dialog, null)


        bottomDialog.setContentView(this.builder?.contentView)
        bottomDialog.setCancelable(true)
        bottomDialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        bottomDialog.window!!.setGravity(Gravity.BOTTOM)


        return bottomDialog
    }

    class Builder(var context: Context) {

        var bottomDialog: Dialog? = null
        var contentView:View? = null

        fun setContentView(view: View):Builder {
            this.contentView = view
            return this
        }


        @UiThread
        fun build(): BottomDialog {
            return BottomDialog(this)
        }

        @UiThread
        fun show(): Dialog?{
            bottomDialog?.show()
            return bottomDialog
        }

        @UiThread
        fun dismiss(): Dialog? {
            bottomDialog?.dismiss()
            return bottomDialog
        }

    }

    interface ButtonCallback {

        fun setContentLayout(dialog: BottomDialog)
    }

}

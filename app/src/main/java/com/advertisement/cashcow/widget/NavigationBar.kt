package com.advertisement.cashcow.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.advertisement.cashcow.R
import kotlinx.android.synthetic.main.widget_navigation_bar.view.*

/**
 * 作者：吴国洪 on 2018/5/30
 * 描述：自定义导航栏
 * 用法：xml中设置title,rightText,rightDrawable
 *       rightText,rightDrawable不可同时设置
 */
class NavigationBar @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
    : FrameLayout(context, attributeSet, defAttrStyle), View.OnClickListener {

    private var rightClickCallbacks: (() -> Unit)? = null
    private var backClickCallbacks: (() -> Unit)? = null

    init {
        LayoutInflater
                .from(context)
                .inflate(R.layout.widget_navigation_bar, this, true)

        initAttrs(context, attributeSet, defAttrStyle)//获取自定义属性的值
    }

    private fun initAttrs(context: Context, attributeSet: AttributeSet?, defAttrStyle: Int) {
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.NavigationBar, defAttrStyle, 0)
        val ctx = this

        val title = array.getString(R.styleable.NavigationBar_navigationBarTitle)
        val rithtTextColor = array.getColor(R.styleable.NavigationBar_navigationBarRightTextColor, resources.getColor(R.color.colorPrimaryDark))

        fl_nav_back.setOnClickListener(this)

        tv_nav_title.apply {
            text = title
        }

        val drawableId = array.getResourceId(R.styleable.NavigationBar_navigationBarRightDrawable, R.mipmap.ic_launcher)
        if (drawableId != R.mipmap.ic_launcher) {
            iv_nav_right.apply {
                setImageDrawable(resources.getDrawable(drawableId))
                visibility = View.VISIBLE
                setOnClickListener(ctx)
            }
        }

        val rightText = array.getString(R.styleable.NavigationBar_navigationBarRightText)
        if (!TextUtils.isEmpty(rightText)) {
            tv_nav_right.apply {
                text = rightText
                visibility = View.VISIBLE
                setTextColor(rithtTextColor)
                setOnClickListener(ctx)

            }
        }

        if (drawableId != R.mipmap.ic_launcher &&
                !TextUtils.isEmpty(rightText)) {
            cl_nav_pic.apply {
                setOnClickListener(ctx)
                visibility = View.VISIBLE
            }

            tv_nav_pic.text = rightText
            tv_nav_title.setTextColor(rithtTextColor)
            iv_nav_pic.setImageResource(drawableId)
            tv_nav_right.visibility = View.GONE
            iv_nav_right.visibility = View.GONE
        }

        array.recycle()
    }

    fun setOnRightClickListener(rightClickCallbacks: () -> Unit) {
        this.rightClickCallbacks = rightClickCallbacks
    }

    fun setOnBackClickListener(backClickCallbacks: () -> Unit) {
        this.backClickCallbacks = backClickCallbacks
    }

    fun setTitle(text: String) {
        tv_nav_title.text = text
    }

    fun setRightTextAndImage(text: String, resId: Int) {
        tv_nav_pic.text = text
        iv_nav_pic.setImageResource(resId)

        val ctx = this

        cl_nav_pic.apply {
            setOnClickListener(ctx)
            visibility = View.VISIBLE
        }

        tv_nav_right.visibility = View.GONE
        iv_nav_right.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.tv_nav_right ||
                    v?.id == R.id.iv_nav_right -> rightClickCallbacks?.invoke()

            v?.id == R.id.fl_nav_back -> backClickCallbacks?.invoke()

            v?.id == R.id.cl_nav_pic -> rightClickCallbacks?.invoke()
        }
    }


}
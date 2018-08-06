package com.advertisement.cashcow.widget.progressbar

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import com.advertisement.cashcow.R
import com.blankj.utilcode.util.ConvertUtils

/**
 * 作者：吴国洪 on 2018/6/15
 * 描述：真正的仿微信网页打开的进度条
 *
 * 两种方式
 * 1，setLayoutParams 的形式
 * 2，动画形式(推荐)
 *
 * 下面的所有属性都可以自己采用 get set 来自定义
 *
 */
class HProgressBarLoading @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(mContext, attrs, defStyleAttr) {
    var max: Int = 0//进度条最大的进度
    private var mDefaultHeight: Int = 0//高度
    private var processAnimation: Animator? = null //进度动画
    private var hideAnimation:AnimationSet? = null

    var curProgress: Int = 0
        private set//当前的进度
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mPaint: Paint? = null
    private var mColor: Int = 0

    init {
        initAttr(attrs)

    }

    private fun initAttr(attrs: AttributeSet?) {
        val array = mContext.obtainStyledAttributes(attrs, R.styleable.hprogress)
        max = array.getInt(R.styleable.hprogress_max, 10)
        curProgress = array.getInt(R.styleable.hprogress_progress, 0)
        mDefaultHeight = array.getInt(R.styleable.hprogress_progressHeight, 200)
        mColor = array.getColor(R.styleable.hprogress_progressColor, Color.parseColor("#0AC416"))
        array.recycle()

        mPaint = Paint()
        mPaint!!.color = mColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        //矩形宽度为view的80%
        //mDefaultHeight 为你自定义设置的属性
        when (widthMode) {
            View.MeasureSpec.EXACTLY -> mWidth = widthSize
            View.MeasureSpec.AT_MOST -> //mDefaultWidth 为你自定义设置的属性
                mWidth = ConvertUtils.dp2px(300f)
            View.MeasureSpec.UNSPECIFIED -> mWidth = widthSize
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            mHeight = heightSize
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            //mDefaultHeight 为你自定义设置的属性
            mHeight = ConvertUtils.dp2px(mDefaultHeight.toFloat())
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val result = mWidth * (curProgress.toFloat() / 100.toFloat())
        canvas.drawRect(0f, 0f, result, mDefaultHeight.toFloat(), mPaint!!)
    }


    fun setCurProgress(curProgress: Int, time: Long, animationEndOperate: () -> Unit) {
        if (this.curProgress == 100) {//重置mCurProgress为0
            this.curProgress = 0
        }

        stopAllAnimation()
        //注意是从 mCurProgress->curProgress 来动画来实现
        processAnimation = ValueAnimator.ofInt(this.curProgress, curProgress)
        (processAnimation as ValueAnimator?)?.duration = time
        (processAnimation as ValueAnimator?)?.interpolator = LinearInterpolator()
        (processAnimation as ValueAnimator?)?.addUpdateListener { animation ->
            this.curProgress = animation.animatedValue as Int
            postInvalidate()//通知刷新
        }
        (processAnimation as ValueAnimator?)!!.start()
        (processAnimation as ValueAnimator?)!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                animationEndOperate()
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    fun setNormalProgress(newProgress: Int) {
        curProgress = newProgress
        postInvalidate()
    }


     fun stopAllAnimation(){
        processAnimation?.cancel()
    }
}

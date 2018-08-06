package com.advertisement.cashcow.common.base

import android.content.Context
import android.os.Bundle
import com.advertisement.cashcow.thirdLibs.avplayer.AudioService.AudioServiceActivityLeak
import com.blankj.utilcode.util.KeyboardUtils
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator


/**
 * @author 吴国洪
 * created: 2018/05/25
 * desc:BaseActivity基类
 */
abstract class BaseActivity : SupportActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configSetting()
        setContentView(layoutId())
        initData()
        initView()
        initListener()

    }

     override fun attachBaseContext(base: Context) {
         //解决Activity使用AudioManager内存泄漏
        super.attachBaseContext(AudioServiceActivityLeak.preventLeakOf(base))
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        // 设置横向(和安卓4.x动画相同)
         return  DefaultHorizontalAnimator()

    }

    private fun initListener() {
    }



    /**
     *  加载布局
     */
    abstract fun layoutId(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化 View
     */
    abstract fun initView()

    /**
     * activity配置
     */
    open fun configSetting(){
    }

    override fun onDestroy() {
        super.onDestroy()
        KeyboardUtils.fixSoftInputLeaks(this)

    }

}



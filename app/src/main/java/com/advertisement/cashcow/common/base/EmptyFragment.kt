package com.advertisement.cashcow.common.base

import android.os.Bundle
import com.advertisement.cashcow.R
import me.yokeyword.fragmentation.SupportFragment


/**
 * 作者：吴国洪 on 2018/7/02
 * 子fragment间的跳转需要一个根fragment
 */

class EmptyFragment : BaseFragment() {

    private var targetFragment: SupportFragment? = null

    /**
     * 懒加载
     */
    override fun lazyLoad() {
    }

    override fun getLayoutId(): Int = R.layout.root_empty

    companion object {
        fun getInstance(supportFragment: SupportFragment): EmptyFragment {
            val fragment = EmptyFragment()
            val bundle = Bundle()
            fragment.targetFragment = supportFragment
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        if (findChildFragment(targetFragment!!::class.java) == null) {
            loadRootFragment(R.id.fl_root_container, targetFragment)
        }
    }


}

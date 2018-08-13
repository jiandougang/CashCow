package com.advertisement.cashcow.module.main.mine.goldCoinsDetail

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.advertisement.cashcow.common.base.BaseFragment
import java.util.*

/**
 * 作者：吴国洪 on 2018/8/8
 * 描述：tab滑动Adapter
 */
class TabPageAdapter(fm: FragmentManager, private val titles: Array<String>, private val fragments: ArrayList<BaseFragment>) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

    override fun getItem(position: Int): BaseFragment {
        return fragments[position]
    }
}

package com.advertisement.cashcow.module.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent
import android.widget.Toast
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseActivity
import com.advertisement.cashcow.module.main.information.InformationFragment
import com.advertisement.cashcow.module.main.mine.MineFragment
import com.advertisement.cashcow.util.CleanLeakUtils
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


/**
 * @author Jake.Ho
 * created: 2017/10/25
 * desc:
 */


class MainActivity : BaseActivity() {

    private val mTitles = arrayOf("资讯", "特色", "特价", "招商", "我的")

    // 未被选中的图标
    private val mIconUnSelectIds = intArrayOf(R.mipmap.tab_information, R.mipmap.tab_feature, R.mipmap.tab_special_price, R.mipmap.tab_busine, R.mipmap.tab_mine)
    // 被选中的图标
    private val mIconSelectIds = intArrayOf(R.mipmap.tab_information_light, R.mipmap.tab_feature_light, R.mipmap.tab_special_price_light, R.mipmap.tab_busine_light, R.mipmap.tab_mine_light)

    private val mTabEntities = ArrayList<CustomTabEntity>()

    private var mInformationFragment: InformationFragment? = null
    private var mFeaturesFragment: InformationFragment? = null
    private var mSpecialPriceFragment: InformationFragment? = null
    private var mInvestment: InformationFragment? = null
    private var mMineFragment: MineFragment? = null


    //默认为0
    private var mIndex = 0
    private var mPreIndex = 0
    private var exitTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt("currTabIndex")
        }
        super.onCreate(savedInstanceState)
        initTab()
        tab_layout.currentTab = mIndex
        switchFragment(mIndex)

    }


    override fun onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this)
        super.onDestroy()
    }

    override fun layoutId(): Int = R.layout.activity_main

    //初始化底部菜单
    private fun initTab() {
        (0 until mTitles.size)
                .mapTo(mTabEntities) { TabEntity(mTitles[it], mIconSelectIds[it], mIconUnSelectIds[it]) }
        //为Tab赋值
        tab_layout.setTabData(mTabEntities)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                //切换Fragment
                switchFragment(position)
                mPreIndex = position
            }

            override fun onTabReselect(position: Int) {

            }
        })
    }

    /**
     * 切换Fragment
     * @param position 下标
     */
    private fun switchFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (position) {
            0 -> {
                mInformationFragment?.let {
                    transaction.show(it)
                } ?: InformationFragment.getInstance(getString(R.string.information)).let {
                    mInformationFragment = it
                    transaction.add(R.id.fl_container, it, "home")
                }
            }

            1 -> {
                mFeaturesFragment?.let {
                    transaction.show(it)
                } ?: InformationFragment.getInstance(getString(R.string.feature)).let {
                    mFeaturesFragment = it
                    transaction.add(R.id.fl_container, it, "search")
                }
            }

            2 -> {
                mSpecialPriceFragment?.let {
                    transaction.show(it)
                } ?: InformationFragment.getInstance(getString(R.string.special_price)).let {
                    mSpecialPriceFragment = it
                    transaction.add(R.id.fl_container, it, "details")
                }
            }

            3 -> {
                mInvestment?.let {
                    transaction.show(it)
                } ?: InformationFragment.getInstance(getString(R.string.investment)).let {
                    mInvestment = it
                    transaction.add(R.id.fl_container, it, "invite_friends")
                }
            }

            4 -> {
                mMineFragment?.let {
                    transaction.show(it)
                } ?: MineFragment.getInstance().let {
                    mMineFragment = it
                    transaction.add(R.id.fl_container, it, "mine")
                }
            }
            else -> {

            }
        }

        mIndex = position
        tab_layout.currentTab = mIndex
        transaction.commitAllowingStateLoss()
    }


    /**
     * 隐藏所有的Fragment
     * @param transaction transaction
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mInformationFragment?.let { transaction.hide(it) }
        mFeaturesFragment?.let { transaction.hide(it) }
        mMineFragment?.let { transaction.hide(it) }
        mSpecialPriceFragment?.let { transaction.hide(it) }
        mInvestment?.let { transaction.hide(it) }
    }


    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {

            if (System.currentTimeMillis() - exitTime > 2000)
            {
                Toast.makeText(applicationContext, getString(R.string.tab_again_exit_app), Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis()
            } else {
                finish()
                System.exit(0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}

package com.hazz.kotlinmvp.view.recyclerview

/**
 * Created by 吴国洪 on 2018/05/28.
 * desc: 多布局条目类型
 */

interface MultipleType<in T> {

    fun getLayoutId(item: T, position: Int): Int
}

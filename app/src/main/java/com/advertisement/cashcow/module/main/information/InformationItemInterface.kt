package com.advertisement.cashcow.module.main.information

/**
 * 作者：吴国洪 on 2018/6/14
 * 描述：资讯模块Item点击监听
 */
interface InformationItemInterface<T> {

    fun onItemClickListener(entity: T)
}

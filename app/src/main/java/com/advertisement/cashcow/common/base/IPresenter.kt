package com.advertisement.cashcow.common.base

/**
 * 作者：吴国洪 on 2018/5/28
 * 描述：activity 或 fragment 的Presenter类型，用于处理各种逻辑数据
 */
interface IPresenter<in V: IBaseView> {

    fun attachView(mRootView: V)

    fun detachView()

}

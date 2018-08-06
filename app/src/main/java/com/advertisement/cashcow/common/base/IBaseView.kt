package com.advertisement.cashcow.common.base

/**
 * 作者：吴国洪 on 2018/5/28
 * 描述：activity 或 fragment需要实现的回调接口
 */
interface IBaseView {

    /** 处理错误返回
     *
     * @param type  错误类型
     * @param obj 类型实体
     */
    fun handleError(type: String, obj: Any)

    /** 处理请求成功返回
     *
     * @param type  成功类型
     * @param obj 类型实体
     */
    fun handleSuccess(type: String, obj: Any)

    /** 处理请求前操作
     *
     * @param type  类型
     * @param obj 类型实体
     */
    fun preLoad(type: String, obj: Any)

    /** 处理请求后操作
     *
     * @param type  类型
     * @param obj 类型实体
     */
    fun afterLoad(type: String, obj: Any)
}

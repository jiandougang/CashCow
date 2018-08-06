package com.advertisement.cashcow.module.main.detail

/**
 * 作者：吴国洪 on 2018/6/23
 * 描述：Details实体
 */
class DetailsBean(var type: String) {
    companion object {
        val titleType = "titleType"
        val userType = "userType"
        val contentType = "contentType"
        val pictureType = "pictureType"
        val contactType = "contactType"
    }

    //////////////////////////////////数据集////////////////////////////////////////////////

    var titleTypeEntity: DetailsBean.TextEntity? = null
    var userTypeEntity: DetailsBean.UserEntity? = null
    var contentTypeEntity: DetailsBean.TextEntity? = null
    var pictureTypeEntity: DetailsBean.PictureEntity? = null
    var contactTypeEntity: DetailsBean.ContactEntity? = null


    //////////////////////////////////数据实体////////////////////////////////////////////////
    /**
     * 描述：详情模块文本实体
     *
     * @param text 文本
     */
    data class TextEntity(val text:String )

    /**
     * 描述：详情模块名称实体
     *标题
     * @param name 名称
     * @param time 时间
     */
    data class UserEntity(val name:String,val time:String)

    /**
     * 描述：详情模块图片实体
     *
     * @param url 地址
     */
    data class PictureEntity(val url:String )

    /**
     * 描述：联系方式实体
     *
     * @param qq qq号
     * @param wechat 微信
     * @param phone 电话
     */
    data class ContactEntity(val qq:String,val wechat:String,val phone:String )
}

package com.advertisement.cashcow.module.main.information

/**
 * 作者：吴国洪 on 2018/6/06
 * 描述：资讯模块Item实体
 * 一个列表包含banner(包含多张广告banner)，种类选项(包含多个按钮)，单图片广告，多图片广告，视频广告，APP下载广告
 */
class InformationBean(var type: String) {

    companion object {
        const val bannerType = "banner"
        const val filterType = "filter"

        const val advLink = "advLink"//第三方广告链接


        const val advTypeMulPic = "advTypeMulPic"
        const val advTypeSinglePic = "advTypeSinglePic"
        const val videoType = "video"//普通视频
        const val appWithPicType = "appWithPicType"//大图加下载
        const val appWithVideoType = "appWithVideoType"//视频加下载

        const val financialHeaderType = "financialHeaderType"//金融子类头部Item
        const val subFilterType = "subFilterType"//子类的子分类

    }

    //////////////////////////////////数据集////////////////////////////////////////////////
    /***
     * 广告 banner条项
     */
    var bannerTypeEntity: BannerTypeItem? = null

    /***
     * 筛选种类条项
     */
    var filterTypeEntity: FilterTypeItem? = null

    var advLinkEntity: String? = null

    /***
     * 广告单图片条项
     */
    var advertisementTypeSinglePicEntity: AdvertisementSinglePicEntity? = null

    /***
     * 广告多图片条项
     */
    var advertisementTypeMultiPicEntity: AdvertisementMultiPicEntity? = null

    /***
     * 广告app视频条项
     */
    var appEntity: VideoEntity? = null

    //////////////////////////////////子数据集////////////////////////////////////////////////
    /**
     * banner数据
     *
     * @param type 类型
     * @param bannerList 数据
     */
    data class BannerTypeItem(val bannerList: List<BannerEntity>?)

    /**
     * 筛选数据种类
     *
     * @param type 类型
     * @param filterList 数据
     */
    data class FilterTypeItem(val filterList: ArrayList<FilterEntity>?)


    //////////////////////////////////数据实体////////////////////////////////////////////////
    /**
     * 描述：资讯模块种类筛选实体
     *
     * @param text 按钮文本
     * @param resId 按钮图片资源Id
     */
    data class FilterEntity(val text: String, val resId: Int)


    /**
     * 描述：广告单图片数据实体
     *
     * @param text 广告标题文本
     * @param publisher 发布者
     * @param releaseTime 发布时间
     * @param picPath 图片数据，三张图片
     * @param linkUrl 第三方广告url
     * @param adsc 广告时长
     */
    data class AdvertisementSinglePicEntity(val id: String?, val text: String?,
                                            val publisher: String?, val releaseTime: String?,
                                            val picPath: String?, val linkUrl: String?,var adsc:Int)


    /**
     * 描述：广告多图片数据实体
     *
     * @param text 广告标题文本
     * @param publisher 发布者
     * @param releaseTime 发布时间
     * @param picPathList 图片数据，三张图片
     * @param linkUrl 第三方广告url
     * @param adsc 广告时长
     */
    data class AdvertisementMultiPicEntity(val id: String?, val text: String?,
                                           val publisher: String?, val releaseTime: String?,
                                           val picPathList: ArrayList<String>, val linkUrl: String?,var adsc:Int)

    /**
     * 描述：下载App数据实体
     *
     * @param text 广告标题文本
     * @param thumbPic 视频帧图片
     * @param publisher 发布者
     * @param releaseTime 发布时间
     * @param videoUrl 视频地址
     * @param appUrl app下载地址
     * @param adsc 广告时长
     */
    data class VideoEntity(val id: String?, val text: String?,
                           val thumbPic: String?, val publisher: String?,
                           val releaseTime: String?,val linkUrl: String?, val appUrl: String?,
                           var adsc:Int)


}

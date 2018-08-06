package com.advertisement.cashcow.module.main.mine.personalInformation

import android.content.Context
import android.os.Environment
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.bean.LoginByPasswordApiBean
import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.advertisement.cashcow.module.main.mine.other.InitBindingPhoneNumberPasswordFragment
import com.advertisement.cashcow.module.main.mine.other.ModifyNickNameFragment
import com.advertisement.cashcow.thirdLibs.clipHeadIcon.ClipImageActivity
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

/**
 * 作者：吴国洪 on 2018/5/26
 * 描述：MineFragment逻辑处理类
 */
class PersonalInformationPresenter(type: String) : BasePresenter<PersonalInformationContract.View>(), PersonalInformationContract.Presenter {

    var type: String? = null

    init {
        this.type = type
    }

    override fun requestSaveBasicInfo(context: Context, userId: String, nickname: String,
                                      phone: String, password: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["userid"] = userId

        if (!StringUtils.isEmpty(nickname))
            parameters["nickname"] = nickname

        if (!StringUtils.isEmpty(phone))
            parameters["phone"] = phone

        if (!StringUtils.isEmpty(password))
            parameters["password"] = password

        when (type) {
            InitBindingPhoneNumberPasswordFragment.javaClass.name ->
                mRootView as InitBindingPhoneNumberPasswordFragment


            ModifyNickNameFragment.javaClass.name ->
                mRootView as ModifyNickNameFragment

            else ->
                mRootView as PersonalInformationFragment
        }

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(Api::class.java)

        novate.call(api.requestSaveBasicInfo(parameters), object : BaseSubscriber<LoginByPasswordApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                mRootView!!.handleError(Api.requestSaveBasicInfo, e.toString())
            }

            override fun onStart() {
                mRootView!!.preLoad(Api.requestSaveBasicInfo, "")
            }

            override fun onNext(t: LoginByPasswordApiBean) {
                LogUtils.d(t.toString())
                if (t.resultCode == "0") {
                    mRootView!!.handleSuccess(Api.requestSaveBasicInfo, t)
                } else {
                    mRootView!!.handleError(Api.requestSaveBasicInfo, t.resultMsg.toString())
                }
            }

            override fun onCompleted() {
                mRootView!!.afterLoad(Api.requestSaveBasicInfo, "")

            }
        })
    }

    override fun getFile(): File {
        val file = File(Environment.getExternalStorageDirectory(), ClipImageActivity.IMAGE_FILE_NAME)
        if (!file.exists()) {
            file.parentFile.mkdirs()
        }
        return file
    }

    /**
     * 上传用户头像
     *
     * @param id 通过登录接口获取的用户id
     * @param picFile 文件对象
     *
     */
    override fun requestUploadAvatar(id: String, picFile: File) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userid", id)
                .addFormDataPart("uploadFile", picFile.name, RequestBody.create(MediaType.parse("image/*"), picFile))
                .build()

        val context: Context = (mRootView as PersonalInformationFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val mineApi = novate.create(Api::class.java)

        novate.call(mineApi.requestUploadAvatar(requestBody), object : BaseSubscriber<TextApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")

                (mRootView as PersonalInformationFragment).handleError(Api.requestUserById, e.toString())
            }

            override fun onStart() {
            }

            override fun onNext(t: TextApiBean) {
                LogUtils.i(t.toString())
                (mRootView as PersonalInformationFragment).handleSuccess(Api.requestUserById, t)
            }

            override fun onCompleted() {
            }

        })
    }

}

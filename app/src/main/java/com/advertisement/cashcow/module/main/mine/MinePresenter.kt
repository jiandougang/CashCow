package com.advertisement.cashcow.module.main.mine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BasePresenter
import com.advertisement.cashcow.common.network.NetworkConfig
import com.advertisement.cashcow.common.network.api.Api
import com.advertisement.cashcow.common.network.bean.TextApiBean
import com.advertisement.cashcow.common.network.bean.mine.CumulativeApiBean
import com.advertisement.cashcow.common.network.bean.mine.MineApiBean
import com.advertisement.cashcow.common.network.bean.mine.ModifyPasswordApiBean
import com.advertisement.cashcow.module.main.mine.cumulativeIncome.CumulativeIncomeFragment
import com.advertisement.cashcow.module.main.mine.other.*
import com.advertisement.cashcow.thirdLibs.clipHeadIcon.ClipImageActivity
import com.advertisement.cashcow.wxapi.WxUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.tamic.novate.BaseSubscriber
import com.tamic.novate.Throwable
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * 作者：吴国洪 on 2018/5/26
 * 描述：MineFragment逻辑处理类
 */
class MinePresenter : BasePresenter<MineContract.View>(), MineContract.Presenter {


    override fun requestIsOk(context: Context, phoneNum: String, vCode: String, vCodeType: String, className: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["phone"] = phoneNum
        parameters["vcode"] = vCode
        parameters["vcodetype"] = vCodeType

        when (className) {
            ModifyPhoneNumberFragment.javaClass.name -> {
                mRootView as ModifyPhoneNumberFragment
            }
            else -> {
                mRootView as ForgetTradingPasswordFragment
            }
        }


        val novate = NetworkConfig.getInstance(context)

        val mineApi = novate.create(Api::class.java)

        novate.call(mineApi.requestIsOk(parameters), object : BaseSubscriber<TextApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")

                mRootView!!.handleError(Api.requestIsOk, e.toString())
            }

            override fun onStart() {

            }

            override fun onNext(t: TextApiBean) {
                LogUtils.i(t.toString())
                if ("0" == t.resultCode) {
                    mRootView!!.handleSuccess(Api.requestIsOk, t)
                } else {
                    mRootView!!.handleError(Api.requestIsOk, t.resultMsg.toString())

                }
            }

            override fun onCompleted() {
            }

        })
    }

    override fun requestGetVerificationCode(context: Context, type: String, phoneNum: String, className: String) {

        val parameters: MutableMap<String, String> = HashMap()

        parameters["phone"] = phoneNum
        parameters["vcodetype"] = type

        when (className) {
            ModifyPhoneNumberFragment.javaClass.name -> {
                mRootView as ModifyPhoneNumberFragment
            }
            else -> {
                mRootView as ForgetTradingPasswordFragment
            }
        }

        val novate = NetworkConfig.getInstance(context)

        val mineApi = novate.create(Api::class.java)

        novate.call(mineApi.requestGetVerificationCode(parameters), object : BaseSubscriber<TextApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")

                mRootView!!.handleError(Api.requestGetVerificationCode, e.toString())
            }

            override fun onStart() {

            }

            override fun onNext(t: TextApiBean) {
                LogUtils.i(t.toString())
                if ("0" == t.resultCode) {
                    mRootView!!.handleSuccess(Api.requestGetVerificationCode, t)
                } else {
                    mRootView!!.handleError(Api.requestGetVerificationCode, t.resultMsg.toString())

                }
            }

            override fun onCompleted() {
            }

        })
    }

    override fun requestUpdateDealPass(userId: String, oldPassword: String, newPassword: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["userid"] = userId
        parameters["newdealpass"] = newPassword
        parameters["olddealpass"] = oldPassword


        val context: Context = (mRootView as ModifyTradingPasswordFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val mineApi = novate.create(Api::class.java)

        novate.call(mineApi.requestUpdateDealPass(parameters), object : BaseSubscriber<ModifyPasswordApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")

                (mRootView as ModifyTradingPasswordFragment).handleError(Api.requestResetDealPwd, e.toString())
            }

            override fun onStart() {
                (mRootView as ModifyTradingPasswordFragment).preLoad(Api.requestResetDealPwd, "")

            }

            override fun onNext(t: ModifyPasswordApiBean) {
                LogUtils.i(t.toString())
                if ("0" == t.resultCode) {
                    (mRootView as ModifyTradingPasswordFragment).handleSuccess(Api.requestResetDealPwd, t)
                } else {
                    (mRootView as ModifyTradingPasswordFragment).handleError(Api.requestResetDealPwd, t.resultMsg.toString())

                }
            }

            override fun onCompleted() {
                (mRootView as ModifyTradingPasswordFragment).afterLoad(Api.requestUpdateLoginPass, "")
            }

        })
    }

    override fun requestResetDealPwd(phoneNum: String, dealPassword: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["phone"] = phoneNum
        parameters["dealpass"] = dealPassword


        val context: Context = (mRootView as ModifyTradingPasswordWithVerificationCodeFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val mineApi = novate.create(Api::class.java)

        novate.call(mineApi.requestResetDealPwd(parameters), object : BaseSubscriber<TextApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")

                (mRootView as ModifyTradingPasswordWithVerificationCodeFragment).handleError(Api.requestResetDealPwd, e.toString())
            }

            override fun onStart() {
                (mRootView as ModifyTradingPasswordWithVerificationCodeFragment).preLoad(Api.requestResetDealPwd, "")

            }

            override fun onNext(t: TextApiBean) {
                LogUtils.i(t.toString())
                if ("0" == t.resultCode) {
                    (mRootView as ModifyTradingPasswordWithVerificationCodeFragment).handleSuccess(Api.requestResetDealPwd, t)
                } else {
                    (mRootView as ModifyTradingPasswordWithVerificationCodeFragment).handleError(Api.requestResetDealPwd, t.resultMsg.toString())

                }
            }

            override fun onCompleted() {
                (mRootView as ModifyTradingPasswordWithVerificationCodeFragment).afterLoad(Api.requestUpdateLoginPass, "")
            }

        })
    }


    override fun requestUpdateLoginPassword(userId: String, oldPassword: String, newPassword: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["userid"] = userId
        parameters["newloginpass"] = newPassword
        parameters["oldloginpass"] = oldPassword

        val context: Context = (mRootView as ModifyLoginPasswordFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val mineApi = novate.create(Api::class.java)

        novate.call(mineApi.requestUpdateLoginPass(parameters), object : BaseSubscriber<ModifyPasswordApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")

                (mRootView as ModifyLoginPasswordFragment).handleError(Api.requestUpdateLoginPass, e.toString())
            }

            override fun onStart() {
                (mRootView as ModifyLoginPasswordFragment).preLoad(Api.requestUpdateLoginPass, "")

            }

            override fun onNext(t: ModifyPasswordApiBean) {
                LogUtils.i(t.toString())
                if ("0" == t.resultCode) {
                    (mRootView as ModifyLoginPasswordFragment).handleSuccess(Api.requestUpdateLoginPass, t)
                } else {
                    (mRootView as ModifyLoginPasswordFragment).handleError(Api.requestUpdateLoginPass, t.resultMsg.toString())

                }
            }

            override fun onCompleted() {
                (mRootView as ModifyLoginPasswordFragment).afterLoad(Api.requestUpdateLoginPass, "")
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


    override fun wechatShare(context: Context, scene: Int, userId: String) {
        val webPage = WXWebpageObject()
        webPage.webpageUrl = NetworkConfig.Invite_Friends_Url + userId

        val msg = WXMediaMessage(webPage)
        msg.title = "摇钱树"
        msg.description = "摇钱树，一个可以帮你赚钱的平台"

        val  thumb = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launch_wechat),300,300,true)
        msg.thumbData = ConvertUtils.bitmap2Bytes(thumb, Bitmap.CompressFormat.JPEG)

        val req = SendMessageToWX.Req()
        req.message = msg
        req.transaction = "Invite_Friends_Url"

        req.scene = scene

        WxUtils.getIWxApiInstance(context).sendReq(req)
    }


    override fun requestUserById(id: String) {
        val parameters: MutableMap<String, String> = HashMap()

        parameters["id"] = id

        val context: Context = (mRootView as MineFragment).context!!

        val novate = NetworkConfig.getInstance(context)

        val mineApi = novate.create(Api::class.java)

        novate.call(mineApi.requestUserById(parameters), object : BaseSubscriber<MineApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")

                (mRootView as MineFragment).handleError(Api.requestUserById, e.toString())
            }

            override fun onStart() {

            }

            override fun onNext(t: MineApiBean) {
                LogUtils.i(t.toString())
                (mRootView as MineFragment).handleSuccess(Api.requestUserById, t)
            }

            override fun onCompleted() {
            }

        })
    }


    override fun requestUploadAvatar(id: String, picFile: File) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userid", id)
                .addFormDataPart("uploadFile", picFile.name, RequestBody.create(MediaType.parse("image/*"), picFile))
                .build()

        val context: Context = (mRootView as MineFragment).context

        val novate = NetworkConfig.getInstance(context)

        val mineApi = novate.create(Api::class.java)

        novate.call(mineApi.requestUploadAvatar(requestBody), object : BaseSubscriber<TextApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e("onError:${e.toString()}")

                (mRootView as MineFragment).handleError(Api.requestUserById, e.toString())
            }

            override fun onStart() {
            }

            override fun onNext(t: TextApiBean) {
                LogUtils.i(t.toString())
                (mRootView as MineFragment).handleSuccess(Api.requestUserById, t)
            }

            override fun onCompleted() {
            }

        })
    }


    override fun requestGetBenefits(userId: String, page: String) {
        val parameters: MutableMap<String, String> = HashMap()
        parameters["userid"] = userId
        parameters["page"] = page
        parameters["rows"] = "10"

        val context: Context = (mRootView as CumulativeIncomeFragment).context

        val novate = NetworkConfig.getInstance(context)

        val api = novate.create(Api::class.java)

        novate.call(api.requestGetBenefits(parameters), object : BaseSubscriber<CumulativeApiBean>(context) {
            override fun onError(e: Throwable?) {
                LogUtils.e(e.toString())
                (mRootView as CumulativeIncomeFragment).handleError(Api.requestGetBenefits, e.toString())
            }

            override fun onStart() {
                (mRootView as CumulativeIncomeFragment).preLoad(Api.requestGetBenefits, "")

            }

            override fun onNext(t: CumulativeApiBean) {
                LogUtils.d(t.toString())
                (mRootView as CumulativeIncomeFragment).handleSuccess(Api.requestGetBenefits, t)
            }

            override fun onCompleted() {
                (mRootView as CumulativeIncomeFragment).afterLoad(Api.requestGetBenefits, "")

            }
        })
    }
}

package com.advertisement.cashcow.module.main.mine.personalInformation

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.broadcast.NormalBroadcastReceiver
import com.advertisement.cashcow.module.main.mine.other.ModifyNickNameFragment
import com.advertisement.cashcow.module.main.mine.other.ModifyPhoneNumberFragment
import com.advertisement.cashcow.module.main.mine.other.PasswordManagementFragment
import com.advertisement.cashcow.thirdLibs.bottomDialog.BottomDialog
import com.advertisement.cashcow.thirdLibs.clipHeadIcon.ClipImageActivity
import com.advertisement.cashcow.util.CacheConfigUtils
import com.advertisement.cashcow.util.LocalCommonUtils
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_personal_information.*
import java.io.File


/**
 * 作者：吴国洪 on 2018/7/2
 * 描述：个人资料页面
 */

class PersonalInformationFragment : BaseFragment(), View.OnClickListener, PersonalInformationContract.View {

    private val LOCAL = 1
    private val CAMERA = 2
    private val CUT = 3

    private var bottomDialog: BottomDialog? = null

    private val mPresenter by lazy { PersonalInformationPresenter(PersonalInformationFragment.javaClass.name) }
    private var normalBroadcastReceiver: NormalBroadcastReceiver? = null

    private var finishActivityForResult = ""

    override fun getLayoutId(): Int = R.layout.fragment_personal_information

    companion object {
        const val REQ_CODE_NICK_NAME = 666

        const val PHONE_KEY = "PHONE_KEY"
        const val Finish_Activity_For_Result = "Finish_Activity_For_Result"


        fun getInstance(finishActivityForResult: String?): PersonalInformationFragment {

            val fragment = PersonalInformationFragment()
            val bundle = Bundle()
            fragment.finishActivityForResult = finishActivityForResult.toString()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        cl_avatar.setOnClickListener(this)
        cl_nick_name.setOnClickListener(this)
        cl_password_management.setOnClickListener(this)
        cl_phone_number.setOnClickListener(this)
        tv_sign_out.setOnClickListener(this)
        tv_nick_name.text = CacheConfigUtils.parseUserInfo(context!!).resultData?.nickname
        tv_phone_number.text = CacheConfigUtils.parseUserInfo(context!!).resultData?.phone
        nv_bar.let {
            it.setOnBackClickListener {
                activity.onBackPressed()
            }
        }
        bottomDialog = BottomDialog.Builder(context!!).setContentView(setDialogContent()).build()
        sd_avatar.setImageURI(CacheConfigUtils.parseUserInfo(context!!).resultData?.avatar)
    }

    override fun onBackPressedSupport(): Boolean {
        if (this.finishActivityForResult == Finish_Activity_For_Result) {
            activity!!.setResult(RESULT_OK)
        }

        val intent = Intent()
        intent.action = NormalBroadcastReceiver.BroadcastName
        context.sendBroadcast(intent)
        return super.onBackPressedSupport()
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.cl_avatar -> {
                PermissionUtils.permission(PermissionConstants.getPermissions(PermissionConstants.STORAGE)[1])
                        .callback(object : PermissionUtils.FullCallback {
                            override fun onGranted(permissionsGranted: List<String>) {
                                bottomDialog?.show()

                            }

                            override fun onDenied(permissionsDeniedForever: List<String>,
                                                  permissionsDenied: List<String>) {
                                ToastUtils.showShort(permissionsDenied[0])
                            }
                        })
                        .request()
            }

            v?.id == R.id.tv_local_album -> {
                val intentFromGallery: Intent = if (android.os.Build.VERSION.SDK_INT >= 19) { // 判断是不是4.4
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                } else {
                    Intent(Intent.ACTION_GET_CONTENT)
                }
                intentFromGallery.type = "image/*" // 设置文件类型
                startActivityForResult(intentFromGallery, LOCAL)
                bottomDialog?.dismiss()
            }

            v?.id == R.id.tv_cancel -> {
                bottomDialog?.dismiss()
            }

            v?.id == R.id.tv_take_phote -> {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPresenter.getFile()))
                startActivityForResult(intent, CAMERA)
            }

            v?.id == R.id.cl_nick_name -> {
                startForResult(ModifyNickNameFragment.getInstance(), REQ_CODE_NICK_NAME)
            }

            v?.id == R.id.cl_phone_number -> {
                start(ModifyPhoneNumberFragment.getInstance())
            }

            v?.id == R.id.cl_avatar -> {
                bottomDialog?.show()
            }

            v?.id == R.id.cl_password_management -> {
                start(PasswordManagementFragment.getInstance())
            }

            v?.id == R.id.tv_sign_out -> {
                CacheConfigUtils.getConfigCacheInstance(context).clear()
                activity.onBackPressed()
            }

        }

    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle) {
        super.onFragmentResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_NICK_NAME && resultCode == RESULT_OK) {
            tv_nick_name.text = CacheConfigUtils.parseUserInfo(context!!).resultData?.nickname
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            CUT -> {
                val path = data?.getStringExtra(ClipImageActivity.RESULT_PATH)
                val picFile = File(path)
                sd_avatar.setImageURI(LocalCommonUtils.getImageContentUriFromFile(context!!, picFile))
                mPresenter.requestUploadAvatar(CacheConfigUtils.parseUserInfo(context!!).resultData?.id.toString(), picFile)
            }
            LOCAL -> {
                val intent = Intent(context, ClipImageActivity::class.java)
                intent.putExtra(ClipImageActivity.PASS_PATH, LocalCommonUtils.getFilePath(context!!, data?.data))
                startActivityForResult(intent, CUT)

            }
            CAMERA -> {
                // 照相机程序返回的,再次调用图片剪辑程序去修剪图片
                val intent = Intent(context, ClipImageActivity::class.java)
                intent.putExtra(ClipImageActivity.PASS_PATH, Environment.getExternalStorageDirectory().toString() + "/" + ClipImageActivity.IMAGE_FILE_NAME)
                startActivityForResult(intent, CUT)
            }
        }
    }

    private fun setDialogContent(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_select_photo, null)
        view.findViewById<TextView>(R.id.tv_take_phote).setOnClickListener(this)
        view.findViewById<TextView>(R.id.tv_local_album).setOnClickListener(this)
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener(this)
        return view
    }


    override fun lazyLoad() {
        mPresenter.attachView(this)

        normalBroadcastReceiver = NormalBroadcastReceiver(object : NormalBroadcastReceiver.ReceiverCallBack {
            override fun callBack(intent: Intent) {
                tv_phone_number.text = intent.getStringExtra(PHONE_KEY)
            }
        })

        val filter = IntentFilter()
        filter.addAction(NormalBroadcastReceiver.BroadcastName)
        activity!!.registerReceiver(normalBroadcastReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (normalBroadcastReceiver != null) {
            activity!!.unregisterReceiver(normalBroadcastReceiver)
            normalBroadcastReceiver = null
        }
    }

    override fun handleError(type: String, obj: Any) {
    }


    override fun handleSuccess(type: String, obj: Any) {
    }


    override fun preLoad(type: String, obj: Any) {
    }


    override fun afterLoad(type: String, obj: Any) {
    }

}

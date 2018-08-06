package com.advertisement.cashcow.thirdLibs.clipHeadIcon

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.advertisement.cashcow.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


/**
 * 裁剪头像图片的Activity
 *
 */
class ClipImageActivity : FragmentActivity() {
    private var mClipImageLayout: ClipImageLayout? = null
    private var ivBack: ImageView? = null
    private var tvUse: TextView? = null
//    private var rxPermissions: RxPermissions? = null

    internal var l: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.tv_use -> {
                val bitmap = mClipImageLayout!!.clip()

                val path = Environment.getExternalStorageDirectory().toString() + File.separator + IMAGE_FILE_NAME
                saveBitmap(bitmap, path)

                val intent = Intent()
                intent.putExtra(RESULT_PATH, path)
                setResult(Activity.RESULT_OK, intent)

                finish()
            }
            R.id.iv_back -> finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clip_image)
//        rxPermissions = RxPermissions(this)

        ivBack = findViewById<View>(R.id.iv_back) as ImageView
        tvUse = findViewById<View>(R.id.tv_use) as TextView
        tvUse!!.setOnClickListener(l)
        ivBack!!.setOnClickListener(l)
        mClipImageLayout = findViewById<View>(R.id.clipImageLayout) as ClipImageLayout
        val path = intent.getStringExtra(PASS_PATH)
        // 有的系统返回的图片是旋转了，有的没有旋转，所以处理
        val degreee = readBitmapDegree(path)
        val bitmap = createBitmap(path)
        if (bitmap != null) {
            if (degreee == 0) {
                mClipImageLayout!!.setImageBitmap(bitmap)
            } else {
                mClipImageLayout!!.setImageBitmap(rotateBitmap(degreee, bitmap))
            }
        } else {
            finish()
        }
    }


    private fun saveBitmap(bitmap: Bitmap, path: String) {
        val f = File(path)
        if (f.exists()) {
            f.delete()
        }

        var fOut: FileOutputStream? = null
        try {
            f.createNewFile()
            fOut = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
        } catch (e1: IOException) {
            e1.printStackTrace()
        } finally {
            try {
                fOut?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 创建图片
     *
     * @param path
     * @return
     */
    private fun createBitmap(path: String?): Bitmap? {
        if (path == null) {
            return null
        }

        val opts = BitmapFactory.Options()
        //不在内存中读取图片的宽高
        opts.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, opts)
        val width = opts.outWidth

        opts.inSampleSize = if (width > 1080) width / 1080 else 1//注意此处为了解决1080p手机拍摄图片过大所以做了一定压缩，否则bitmap会不显示

        opts.inJustDecodeBounds = false// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true
        opts.inInputShareable = true
        opts.inDither = false
        opts.inPurgeable = true
        var `is`: FileInputStream? = null
        var bitmap: Bitmap? = null
        try {
            `is` = FileInputStream(path)
            bitmap = BitmapFactory.decodeFileDescriptor(`is`.fd, null, opts)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (`is` != null) {
                    `is`.close()
                    `is` = null
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return bitmap
    }

    // 读取图像的旋转度
    private fun readBitmapDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree
    }

    // 旋转图片
    private fun rotateBitmap(angle: Int, bitmap: Bitmap): Bitmap {
        // 旋转图片 动作
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.width, bitmap.height, matrix, false)
    }

    companion object {
        val IMAGE_FILE_NAME = "clip_temp.jpg"
        val RESULT_PATH = "result_path"
        val PASS_PATH = "pass_path"
    }

}

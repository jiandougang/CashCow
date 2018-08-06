package com.advertisement.cashcow.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.TimeUtils
import java.io.File
import java.io.FileNotFoundException
import java.net.URLEncoder
import java.util.*

/**
 * 作者：吴国洪 on 2018/6/27
 * 描述：本地的公共方法
 */
object LocalCommonUtils {

    /**
     * 计算1分钟内倒数剩余时间
     * @param key 计数时间是根据key来保存在本地当中，下次重新获取key来继续计数
     * @param needReset 是否需要重置时间
     *
     * return 重置时间或者间隔大于59秒直接返回59秒
     */
    fun calculateRemainingTime(context: Context, key: String, needReset: Boolean): Int {
        val seconds: Int?
        var difference: Long? = null

        val now = TimeUtils.getNowDate()
        val ago = CacheConfigUtils.getConfigCacheInstance(context).getSerializable("($key)_count_down_time") as? Date

        if (ago != null) {
            difference = TimeUtils.getTimeSpan(now, ago, TimeConstants.SEC)
        }
        seconds = if (difference == null || difference > 59 || needReset) {//没有本地时间或者时间间隔大于60s,重新更新本地时间
            CacheConfigUtils.getConfigCacheInstance(context).put("($key)_count_down_time", TimeUtils.getNowDate())
            59
        } else {//如果有缓存,则以本地时间与当时时间隔为准
            difference.toInt()
            59 - difference.toInt()
        }

        return seconds
    }

    /**
     * 获取剩余计算时间，用于是否显示倒数时间
     * @param key 计数时间是根据key来保存在本地当中，下次重新获取key来继续计数
     *
     * return 倒数时间大小
     */
    fun getCalculateRemainingTime(context: Context, key: String): Int {
        var difference = 0L

        val now = TimeUtils.getNowDate()
        val ago = CacheConfigUtils.getConfigCacheInstance(context).getSerializable("($key)_count_down_time") as? Date

        if (ago != null) {
            difference = TimeUtils.getTimeSpan(now, ago, TimeConstants.SEC)
        }

        return difference.toInt()
    }


    /**
     * 通过uri获取文件路径
     *
     * @param context
     * @param mUri
     * @return
     */
    fun getFilePath(context: Context, mUri: Uri?): String? {
        return try {
            if (mUri!!.scheme == "file") {
                mUri.path
            } else {
                getFilePathByUri(context, mUri)
            }
        } catch (ex: FileNotFoundException) {
            null
        }

    }

    /**
     * 获取文件路径通过url
     *
     * @param context
     * @param mUri
     * @return
     */
    @Throws(FileNotFoundException::class)
    private fun getFilePathByUri(context: Context, mUri: Uri): String {
        val cursor = context.contentResolver.query(mUri, null, null, null, null)
        cursor!!.moveToFirst()
        val filePath = cursor.getString(1)
        cursor.close()
        return filePath
    }

    /**
     * 获取Uri content:// URI from the given corresponding path to a file
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */

    fun getImageContentUriFromFile(context: Context, imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID), MediaStore.Images.Media.DATA + "=? ",
                arrayOf(filePath), null)
        return if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            cursor.close()
            Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                null
            }
        }
    }


    /**
     * 解码含有中文的文件名，返回全路径
     *
     * @param path  全路径
     * @return 返回全路径
     */
    fun encodePathWithUtf8(path: String?): String? {
        var newPath = path
        val thumbPicName = FileUtils.getFileName(newPath)

        val encodeThumbPicName = URLEncoder.encode(thumbPicName, "UTF-8")

        newPath = newPath?.replace(thumbPicName, encodeThumbPicName)
        return newPath
    }


    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    fun getTimeFormatText(date: Date?): String? {
        val minute = (60 * 1000).toLong()// 1分钟
        val hour = 60 * minute// 1小时
        val day = 24 * hour// 1天
        val month = 31 * day// 月
        val year = 12 * month// 年

        if (date == null) {
            return null
        }
        val diff = Date().time - date.time
        var r: Long = 0
        if (diff > year) {
            r = diff / year
            return r.toString() + "年前"
        }
        if (diff > month) {
            r = diff / month
            return r.toString() + "个月前"
        }
        if (diff > day) {
            r = diff / day
            return r.toString() + "天前"
        }
        if (diff > hour) {
            r = diff / hour
            return r.toString() + "个小时前"
        }
        if (diff > minute) {
            r = diff / minute
            return r.toString() + "分钟前"
        }
        return "刚刚"
    }
}

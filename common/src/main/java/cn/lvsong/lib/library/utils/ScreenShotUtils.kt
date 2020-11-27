package cn.lvsong.lib.library.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.FrameLayout
import cn.lvsong.lib.library.utils.StatusBarUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Desc: 截屏
 * Author: Jooyer
 * Date: 2018-09-27
 * Time: 17:48
 */
object ScreenShotUtils {

    // 压缩 https://www.cnblogs.com/Free-Thinker/p/6394723.html
    /**
     *
     * @param hasNotch  --> 是否有刘海...
     */
    fun screenShot(contentSwitchView: FrameLayout, hasNotch: Boolean = false): Boolean {
        val file = FileUtil.newFile(FileUtil.SCREEN_SHOT_NAME)
        file.deleteOnExit()
        val realWidth = contentSwitchView.width
        val realHeight = contentSwitchView.height
        contentSwitchView.isDrawingCacheEnabled = true
        contentSwitchView.buildDrawingCache()

        val bp = Bitmap.createBitmap(contentSwitchView.getDrawingCache(true),
                0, 0, realWidth, realHeight)
        contentSwitchView.isDrawingCacheEnabled = false
        contentSwitchView.destroyDrawingCache()
        try {
            if (null != bp) {
                val fos = FileOutputStream(file)
                bp.compress(Bitmap.CompressFormat.PNG, 40, fos)
                fos.flush()
                fos.close()

                compressImage(file, realWidth, realHeight)
            }
            return true
        } catch (e: Exception) {
            println("screenShot==========${e.message}")
        }

        return false
    }

    /**
     * 对 decorView 截屏
     */
    fun screenShot(activity: Activity): Boolean {
        val file = FileUtil.newFile(FileUtil.SCREEN_SHOT_NAME)
        val decorView = activity.window.decorView
        val statusBarHeight = StatusBarUtil.getStatusBarHeight(activity)
        val realWidth = DensityUtil.getRealWidth(activity)
        // ScreenUtils.getRealHeight() 只是减去底部导航栏的高度...
        val realHeight = DensityUtil.getScreenHeight(activity) - statusBarHeight
//        println("screenShot==========realWidth: $realWidth  --> realHeight: $realHeight  --> statusBarHeight: $statusBarHeight")
        decorView.isDrawingCacheEnabled = true
        decorView.buildDrawingCache()

        val bp = Bitmap.createBitmap(decorView.getDrawingCache(true),
                0, 0, realWidth, realHeight)
        decorView.isDrawingCacheEnabled = false
        decorView.destroyDrawingCache()
        try {
            if (null != bp) {
                val fos = FileOutputStream(file)
                bp.compress(Bitmap.CompressFormat.PNG, 40, fos)
                fos.flush()
                fos.close()

                compressImage(file, realWidth, realHeight)
            }
            return true
        } catch (e: Exception) {
            println("screenShot==========${e.message}")
        }

        return false
    }


    private fun compressImage(file: File, displayWidth: Int, displayHeight: Int) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true//只测量image 不加载到内存
        BitmapFactory.decodeFile(file.absolutePath, options)//测量image

        options.inPreferredConfig = Bitmap.Config.RGB_565//设置565编码格式 省内存
        options.inSampleSize = calculateInSampleSize(options, displayWidth, displayHeight)//获取压缩比 根据当前屏幕宽高去压缩图片

        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)//按照Options配置去加载图片到内存

        val out = ByteArrayOutputStream()//字节流输出
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)//压缩成JPEG格式 压缩像素质量为50%
        try {
            val fos = FileOutputStream(file)//创建一个文件输出流
            val bytes = out.toByteArray()//字节数组
            val count = bytes.size//字节数组的长度
            fos.write(bytes, 0, count)//写到文件中
            fos.close()//关闭流
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {//计算图片的压缩比
        val height = options.outHeight//图片的高度
        val width = options.outWidth//图片的宽度 单位1px 即像素点

        var inSampleSize = 1//压缩比

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }


}
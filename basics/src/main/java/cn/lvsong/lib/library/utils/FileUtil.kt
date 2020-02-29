package cn.lvsong.lib.library.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns
import androidx.annotation.NonNull
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream


/**
 * Desc: 文件操作
 * Author: Jooyer
 * Date: 2018-08-10
 * Time: 14:29
 */
object FileUtil {
    val FILE_DIR = Environment.getExternalStorageDirectory().absolutePath + File.separator + "Beauty"
    val SCREEN_SHOT_NAME = "screen_shot.png"

    /** Bitmap 转 Uri
     * @param bp  目标图片
     * @param name bp的存储名称,包含后缀
     * 注: 默认文件夹是 molue
     */
    fun bitmapToUri(bp: Bitmap, name: String): Uri? {
        val file = newFile(name)
        return try {
            val fos = FileOutputStream(file)
            bp.compress(Bitmap.CompressFormat.JPEG, 85, fos)
            fos.flush()
            fos.close()
            Uri.fromFile(file)
        } catch (e: Exception) {
            null
        }
    }


    /**
     * 通过文件路路径获取其 Uri
     */
    fun getImageContentUri(context: Context, path: String): Uri {
        val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID),
                MediaStore.Images.Media.DATA + "=? ",
                arrayOf(path), null)
        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            cursor.close()
            return Uri.withAppendedPath(baseUri, "" + id)
        } else {
            val contentValues = ContentValues(1)
            contentValues.put(MediaStore.Images.Media.DATA, path)
            return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        }
    }

    /** 裁剪图片
     * @param uri 需要裁剪的 Uri
     * @param outUri  输出的 Uri
     */
    fun startScaleImage(uri: Uri, outUri: Uri, context: Activity, requestCode: Int) {
        val cropIntent = Intent("com.android.camera.action.CROP")
        cropIntent.setDataAndType(uri, "image/*")

        // 开启裁剪：打开的Intent所显示的View可裁剪
        cropIntent.putExtra("crop", "true")
        // 裁剪宽高比
        cropIntent.putExtra("aspectX", 1)
        cropIntent.putExtra("aspectY", 1)
        // 裁剪输出大小
        cropIntent.putExtra("outputX", 320)
        cropIntent.putExtra("outputY", 320)
        cropIntent.putExtra("scale", true)
        // 去掉黑边
        cropIntent.putExtra("scaleUpIfNeeded", true)
        /**
         * return-data
         * 这个属性决定我们在 onActivityResult 中接收到的是什么数据，
         * 如果设置为true 那么data将会返回一个bitmap
         * 如果设置为false，则会将图片保存到本地并将对应的uri返回，当然这个uri得有我们自己设定。
         * 系统裁剪完成后将会将裁剪完成的图片保存在我们所这设定这个uri地址上。我们只需要在裁剪完成后直接调用该uri来设置图片，就可以了。
         */
        cropIntent.putExtra("return-data", false)
        // 当 return-data 为 false 的时候需要设置这句
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, outUri)
        // 图片输出格式
//        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 头像识别 会启动系统的拍照时人脸识别
        cropIntent.putExtra("noFaceDetection", true)
        //需要系统打开文件时
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        context.startActivityForResult(cropIntent, requestCode)
    }

    /**
     * 通过 Uri 获取 文件地址
     */
    fun getFilePathByUri(context: Context, uri: Uri): String {

        val scheme = uri.scheme
        var data: String = ""

        if (scheme == null)
            data = uri.path
        else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver.query(uri, arrayOf(ImageColumns.DATA),
                    null, null, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }


    fun newFile(@NonNull name: String): File {
        val dir = File(FILE_DIR)
        if (!dir.exists()) {
            dir.mkdir()
        }
        val noMedia = File("$dir/.nomedia")
        return File(dir, name)
    }

    /**
     * drawable 存 file
     */
    fun drawableToFile(@NonNull drawable: BitmapDrawable, @NonNull fileName: String) {
        val bos = BufferedOutputStream(FileOutputStream(newFile(fileName)))
        try {
            drawable.bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            bos.flush()
            bos.close()
        }
    }


}
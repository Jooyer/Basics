package cn.lvsong.lib.library.utils

import android.app.Activity
import android.content.*
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns
import androidx.annotation.NonNull
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat


/**
 * Desc: 文件操作
 * Author: Jooyer
 * Date: 2018-08-10
 * Time: 14:29
 */
object FileUtil {
    val FILE_DIR =
        Environment.getExternalStorageDirectory().absolutePath + File.separator + "Beauty"
    val SCREEN_SHOT_NAME = "screen_shot.png"

    /**
     * 将图片保存本地并获取其 Uri
     * @param savedPath --> 图片的保存地址
     */
    fun bitmap2URI(bp: Bitmap, savedPath: String): Uri? {
        val file = File(savedPath)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        return try {
            val fos = FileOutputStream(file)
            bp.compress(Bitmap.CompressFormat.PNG, 85, fos)
            fos.flush()
            fos.close()
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    /**
     * 通过文件路路径获取其 Uri
     */
    fun getImageContentUri(context: Context, path: String): Uri? {
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(path), null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            cursor.close()
            return Uri.withAppendedPath(baseUri, "" + id)
        } else {
            val contentValues = ContentValues(1)
            contentValues.put(MediaStore.Images.Media.DATA, path)
            return context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
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
            data = uri.path ?: ""
        else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path ?: ""
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver.query(
                uri, arrayOf(ImageColumns.DATA),
                null, null, null
            )
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
     * 创建文件
     * @param filePath -> 文件路径
     */
    fun createFile(filePath: String): File {
        val file = File(filePath)
        if (!file.parentFile.exists()) {
            // 创建目录后再创建文件
            createDir(file.parentFile.absolutePath)
            file.createNewFile()
        }
        return file
    }


    /**
     * 创建文件夹
     * @param dirPath --> 目录路径
     */
    fun createDir(dirPath: String) {
        val dir = File(dirPath)
        if (dir.parentFile.exists()) {
            dir.mkdir()
        } else {
            // 父目录不存在先创建父目录
            createDir(dir.parentFile.absolutePath)
            dir.mkdir()
        }
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

    /**
     *  获取某个 目录|文件 下文件大小
     *  @param filePath -> 目录|文件 路径
     */
    fun getFileSizes(filePath: String): Long {
        var size = 0L
        val file = File(filePath)

        if (!file.exists()) {
            return size
        }

        if (file.isDirectory) {
            file.listFiles()?.forEach {
                if (it.isDirectory) {
                    getFileSizes(it.absolutePath)
                } else {
                    size += it.length()
                }
            }
        } else {
            size += file.length()
        }
        return size
    }

    /** https://blog.csdn.net/pan6015/article/details/84339103
     * 对返回的文件大小进行格式化
     * @param size -> 文件大小
     * @param pattern -> 1. 不要小数 "#"; 2.一位小数 "#.0"; 3. 两位小数 "#.00", 默认两位小数
     */
    fun formatFileSize(size: Long, pattern: String = "#.00"): String {
        val decimalFormat = DecimalFormat(pattern)
        return when {
            size < 1024L -> {
                "${decimalFormat.format(size)} B"
            }
            size < 1204L * 1204L -> {
                "${decimalFormat.format(size / 1024)} KB"
            }
            size < 1204L * 1204L * 1204L -> {
                "${decimalFormat.format(size / 1048576)} MB"
            }
            else -> {
                "${decimalFormat.format(size / 1073741824)} GB"
            }
        }
    }

    /**
     * 删除某个文件|目录
     * @param filePath -> 目录|文件 路径
     */
    fun deleteFiles(filePath: String): Boolean {
        val file = File(filePath)
        if (!file.exists()) {
            return false
        }
        if (file.isFile) {
            file.delete()
            return true
        }
        val listFiles = file.listFiles()
        listFiles?.forEach {
            if (it.isFile) {
                it.delete()
            } else {
                deleteFiles(it.absolutePath)
            }
        }
        file.delete() // 删除目录本身
        return true
    }


    /**
     * 获取文件的 Uri
     * @param filePath --> 已知文件路径
     */
    fun getUriByFilePath(context: Context, filePath: String): Uri? {
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            "${MediaStore.Images.Media.DATA}=?",
            arrayOf(filePath),
            null
        )
        return if (null != cursor && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID))
            val baseUri = Uri.parse("content://media//external/images/media")
            cursor.close()
            Uri.withAppendedPath(baseUri, "$id")
        } else {
            val contentValue = ContentValues(1)
            contentValue.put(MediaStore.Images.Media.DATA, filePath)
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValue
            )
        }
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    fun getFilePath(context: Context, uri: Uri): String? {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     *
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * 通过 Uri 获取文件真实路径
     * @param contentUri
     * @param mContext
     * @return 文件真实路径
     */
    fun getRealPathFromURI(contentUri: Uri, mContext: Context): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(mContext, contentUri, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
}
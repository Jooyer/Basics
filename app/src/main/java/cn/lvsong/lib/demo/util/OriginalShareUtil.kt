package cn.lvsong.lib.demo.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import cn.lvsong.lib.library.utils.FileUtil
import java.io.File


/** https://www.jianshu.com/p/e55ae4140d3a --> 生成携带数据的分享图片
 * Desc: 调用原生分享
 * Author: Jooyer
 * Date: 2018-10-31
 * Time: 15:48
 */
object OriginalShareUtil {

    fun originalShareText(context: Context, title: String, content: String = "") {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND//设置分享行为
        shareIntent.type = "text/plain"//设置分享内容的类型
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        // 添加分享内容标题
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        //添加分享内容
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, content)
        context.startActivity(Intent.createChooser(shareIntent, "分享魔掠小说到..."))
    }


    fun originalShareImage(context: Context, file: File) {
        val shareIntent = Intent()
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileUtil.getImageContentUri(context, file.absolutePath)
        } else {
            Uri.fromFile(file)
        }
        shareIntent.action = Intent.ACTION_SEND//设置分享行为
        shareIntent.type = "image/*"//设置分享内容的类型
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(shareIntent, "分享魔掠小说到..."))
    }


}
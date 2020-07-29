package cn.lvsong.lib.library.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File
import java.lang.IllegalArgumentException

/**
 *
 * @ProjectName:    android
 * @ClassName:      InstallUtil
 * @Description:    安装APK
 * @Author:         Jooyer
 * @CreateDate:     2020/6/17 13:55
 * @UpdateUser:     更新者
 * @UpdateDate:     2020/6/17 13:55
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object InstallUtil {

    fun installApk(context: Context, file: File, authority: String?) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                if (authority.isNullOrEmpty()) {
                    throw IllegalArgumentException("authority 不能为空")
                }
                installApk8x(context, file, authority!!)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                if (authority.isNullOrEmpty()) {
                    throw IllegalArgumentException("authority 不能为空")
                }
                installApk7x(context, file, authority!!)
            }
            else -> {
                installApk6x(context, file)
            }
        }
    }

    /**
     * Android 6.x 及以下安装 APK
     * Build.VERSION.SDK_INT <= Build.VERSION_CODES.M
     *
     * @param context 上下文
     * @param file      要安装的APK文件对象
     */
    private fun installApk6x(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive") // APK的MimeType
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 在新栈中启动
        context.startActivity(intent)
    }

    /**
     * Android 7.x 安装APK，需要配置FileProvider
     * Build.VERSION.SDK_INT>=Build.VERSION_CODES.N
     *
     * @param context   上下文
     * @param file          要安装的APK文件
     * @param authority FileProvider配置的authority
     * 与manifest中定义的provider中的authorities="xxx.provider"保持一致
     */
    private fun installApk7x(context: Context, file: File, authority: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(context, authority, file) // 通过FileProvider获取Uri
        intent.setDataAndType(uri, "application/vnd.android.package-archive") // APK的MimeType
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 在新栈中启动
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // 授权读取URI权限
        context.startActivity(intent)
    }

    /**
     * Android 8.x 及以上安装APK，除配置FileProvider外，
     * 需要先在Manifest.xml清单文件中配置以下权限：
     * <uses-permission></uses-permission>  android:name="android.permission.REQUEST_INSTALL_PACKAGES"  />
     * Build.VERSION.SDK_INT>=Build.VERSION_CODES.O
     *
     * @param context   上下文
     * @param file          要安装的APK文件
     * @param authority FileProvider配置的authority
     */
    private fun installApk8x(context: Context, file: File, authority: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(context, authority, file) // 通过FileProvider获取Uri
        intent.setDataAndType(uri, "application/vnd.android.package-archive") // APK的MimeType
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 在新栈中启动
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // 授权读取URI权限
        context.startActivity(intent)
    }

    /**
     * 检测是否安装支付宝
     * @param context
     * @return
     */
    fun isAliPayInstalled(context: Context): Boolean {
        val uri: Uri = Uri.parse("alipays://platformapi/startApp")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val componentName = intent.resolveActivity(context.packageManager)
        return componentName != null
    }

    /**
     * 检测是否安装微信
     * @param context
     * @return
     */
    fun isWeixinAvilible(context: Context): Boolean {
        val packageManager = context.packageManager // 获取packagemanager
        val info = packageManager.getInstalledPackages(0) // 获取所有已安装程序的包信息
        if (info != null) {
            for (i in info.indices) {
                val pn = info[i].packageName
                if (pn == "com.tencent.mm") {
                    return true
                }
            }
        }
        return false
    }

}
package cn.lvsong.lib.library.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.MessageFormat
import java.util.*

/**
 * https://www.jianshu.com/p/4a60b064a0ab
 * Desc: 权限请求
 * Author: Jooyer
 * Date: 2020-05-12
 * Time: 11:29
 */

/* 用法:

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        XPermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        XPermissionUtils.onActivityResult(requestCode, resultCode, data)
    }


 */

object XPermissionUtils {
    private var mRequestCode = -1
    private var mOnPermissionListener: OnPermissionListener? = null
    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsAgain(
        context: Context, permissions: Array<String>,
        requestCode: Int
    ) {
        if (context is Activity) {
//            ((AppCompatActivity) context).requestPermissions(permissions, requestCode);
            ActivityCompat.requestPermissions(context, permissions, requestCode)
        } else {
            throw IllegalArgumentException("Context must be an Activity")
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissions(
        context: Context, requestCode: Int,
        permissions: Array<String>, listener: OnPermissionListener?
    ) {
        mRequestCode = requestCode
        mOnPermissionListener = listener
        val deniedPermissions = getDeniedPermissions(context, permissions)
        if (deniedPermissions.isNotEmpty()) {
            requestPermissionsAgain(context, permissions, requestCode)
        } else {
            if (mOnPermissionListener != null) mOnPermissionListener?.onPermissionGranted()
        }
    }

    /**
     * 安装APK权限申请,主要是 未知应用安装
     */
    @TargetApi(Build.VERSION_CODES.O)
    fun requestInstallPermission(
        context: Activity,
        requestCode: Int,
        listener: OnPermissionListener?
    ) {
        mRequestCode = requestCode
        mOnPermissionListener = listener
        if (context.packageManager.canRequestPackageInstalls()) {
            mOnPermissionListener?.onPermissionGranted()
        } else {
            //注意这个是8.0新API
            val packageURI = Uri.parse(MessageFormat.format("package:{0}", context.packageName))
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivityForResult(intent, requestCode)
        }
    }

    /**
     * 请求权限结果，在重写 Activity的onRequestPermissionsResult()方法内调用。
     *     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
     *           XPermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
     *           super.onRequestPermissionsResult(requestCode, permissions, grantResults)
     *         }
     *
     */
    fun onRequestPermissionsResult(
        context: Activity, requestCode: Int,
        permissions: Array<String>, grantResults: IntArray?
    ) {
        if (mRequestCode != -1 && requestCode == mRequestCode) {
            if (mOnPermissionListener != null) {
                val deniedPermissions = getDeniedPermissions(context, permissions)
                if (deniedPermissions.isNotEmpty()) {
                    val alwaysDenied = hasAlwaysDeniedPermission(context, *permissions)
                    mOnPermissionListener?.onPermissionDenied(deniedPermissions, alwaysDenied)
                } else {
                    mOnPermissionListener?.onPermissionGranted()
                }
            }
        }
    }

    /**
     * 安装未知应用时需要在重写了 onActivityResult() 方法内调用
     *    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
     *      super.onActivityResult(requestCode, resultCode, data)
     *      XPermissionUtils.onActivityResult(requestCode, resultCode, data)
     *    }
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (mRequestCode == requestCode && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mOnPermissionListener?.onPermissionGranted()
        }
    }

    /**
     * 获取请求权限中需要授权的权限
     */
    private fun getDeniedPermissions(context: Context, permissions: Array<String>): Array<String> {
        val deniedPermissions: MutableList<String> = ArrayList()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                deniedPermissions.add(permission)
            }
        }
        return deniedPermissions.toTypedArray()
    }

    /**
     * 是否彻底拒绝了某项权限
     */
    private fun hasAlwaysDeniedPermission(
        context: Context,
        vararg deniedPermissions: String
    ): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false
        var rationale: Boolean
        for (permission in deniedPermissions) {
            rationale = ActivityCompat.shouldShowRequestPermissionRationale(
                (context as Activity),
                permission
            )
            if (!rationale) return true
        }
        return false
    }

    interface OnPermissionListener {
        // 同意
        fun onPermissionGranted()

        // 拒绝
        fun onPermissionDenied(deniedPermissions: Array<String>, alwaysDenied: Boolean)
    }
}
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
import androidx.fragment.app.FragmentActivity
import java.text.MessageFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 *  https://www.jianshu.com/p/4a60b064a0ab
 *  https://blog.csdn.net/weixin_42046829/article/details/116446887
 *  https://blog.csdn.net/jingzz1/article/details/107338872/
 * Desc: 权限请求
 * Author: Jooyer
 * Date: 2020-05-12
 * Time: 11:29
 */

/* 用法

    1. 请求未知来源应用安装权限方案
    // ActivityResultLauncher必需在activity的onCreate()方法或fragment的onCreate()、onAttach()里先注册，然后在需要调用的地方调用launch方法
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActLauncherInstallPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            XPermissionUtils.onSinglePermission(mActivity,Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,it)
        }
    }

    // 在需要请求权限地方调用
    mActLauncherInstallPermission.launch(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)

    2. 请求单个权限
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActLauncherPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            XPermissionUtils.onSinglePermission(mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE,it)
        }
    }

    // 在需要请求权限地方调用
    mActLauncherPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    3. 请求多个权限
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActLauncherPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            XPermissionUtils.onMultiPermissions(mPermissions,mPermissions, it)
        }

    }

    // 在需要请求权限地方调用
    mActLauncherPermissions.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE))

 */

object XPermissionUtils {

    /**
     * 单个权限请求
     * @param permission --> 请求的权限
     * @param hasGranted --> 是否同意授权
     */
    fun onSinglePermission(activity: FragmentActivity, permission: String, hasGranted: Boolean,permissionListener: OnPermissionListener) {
        if (hasGranted) {
            permissionListener.onPermissionGranted()
        } else {
            permissionListener.onPermissionDenied(listOf(permission), getShouldShowRequestPermissionRationale(activity, permission))
        }
    }

    /**
     * 一次请求多个权限
     * @param permissions --> 请求的权限
     * @param map --> 所有权限请求的结果
     */
    fun onMultiPermissions(activity: Activity, permissions: List<String>, map: Map<String, Boolean>,permissionListener: OnPermissionListener) {
        //通过的权限
        val grantedList = map.filterValues { it }.mapNotNull { it.key }

        //未通过的权限
        val deniedList = (map - grantedList.toSet()).map { it.key }

        //拒绝并且点了“不再询问”权限
        val alwaysDeniedList = deniedList.filterNot {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }

        // 权限全部通过
        if (permissions.size == grantedList.size) {
            permissionListener.onPermissionGranted()
        } else {
            permissionListener.onPermissionDenied(deniedList, alwaysDeniedList)
        }
    }

    /**
     * 获取请求权限中需要授权的权限
     * return list --> 如果为空则表示没有
     */
    fun getDeniedPermissions(context: Context, permissions: List<String>): List<String> {
        val deniedPermissions = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission)
            }
        }
        return deniedPermissions
    }

    /**
     *  判断是否是 勾选了对话框中”Don’t ask again”的选项
     *  return list --> 如果为空则表示没有
     */
    fun getShouldShowRequestPermissionRationale(context: Context, vararg deniedPermissions: String): List<String> {
        val list = ArrayList<String>()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return list
        }
        // // https://blog.csdn.net/wangpf2011/article/details/80589648
        for (permission in deniedPermissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((context as Activity), permission)) {
                list.add(permission)
            }
        }
        return list
    }

    interface OnPermissionListener {
        // 同意
        fun onPermissionGranted()

        /**
         * 拒绝
         * @param deniedPermissions --> 被拒权限集合
         * @param alwaysDenied --> 勾选了对话框中”Don’t ask again”的选项, ActivityCompat.shouldShowRequestPermissionRationale 返回false ,此集合包含false的权限
         * 拒绝并且点了“不再询问”权限 (不是很准确)  https://blog.csdn.net/wangpf2011/article/details/80589648
         */
        fun onPermissionDenied(deniedPermissions: List<String>, vararg alwaysDenied: List<String>)
    }
}
package cn.lvsong.lib.library.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import cn.lvsong.lib.library.R
import java.util.*

/**
 * https://blog.csdn.net/scau_zhangpeng/article/details/79098613
 *
 *
 * https://blog.csdn.net/ytfunnysite/article/details/78856754 --> 应用市场包名
 *
 *
 * https://blog.csdn.net/niezhipeng8/article/details/79103436 --> 应用市场上传地址(更详细)
 * https://blog.csdn.net/u012917700/article/details/52511133  --> 应用市场上传地址
 *
 *
 * http://www.devstore.cn/essay/essayInfo/295.html --> 推广
 *
 *
 *
 *
 * https://www.jianshu.com/p/3941d1fdeddc --> 根据渠道号跳转市场
 * Created by Jooyer on 2018/6/13
 * 跳转应用市场评价
 */
// 注意: Android11 , 需要在manifast.xml中配置下面信息
// 注意: Android11 , 需要在manifast.xml中配置下面信息
// 注意: Android11 , 需要在manifast.xml中配置下面信息

//<manifest>
//    <queries>
//    <!-- 应用商店 -->
//    <package android:name="com.xiaomi.market" />
//    <package android:name="com.huawei.appmarket" />
//    <package android:name="com.oppo.market" />
//    <package android:name="com.heytap.market" />
//    <package android:name="com.vivo.market" />
//    <package android:name="com.bbk.appstore" />
//    <package android:name="com.meizu.mstore" />
//    <package android:name="com.tencent.android.qqdownloader" />
//    <package android:name="com.wandoujia.phoenix2" />
//    </queries>
//</manifest>

object AppraiseUtil {

    const val PACKAGE_YYB = "com.tencent.android.qqdownloader"

    const val PACKAGE_WDJ = "com.wandoujia.phoenix2"

    /**
     * 获取已安装应用商店的包名列表 , 小米手机获取不到... 可以使用 getInstallAppMarkets()
     *
     * @param context
     * @return
     */
    fun installedAPPs(context: Context): ArrayList<String> {
        val pkgs = ArrayList<String>()
        val intent = Intent()
        intent.action = "android.intent.action.MAIN"
        intent.addCategory("android.intent.category.APP_MARKET")
        val pm = context.packageManager
        val infos = pm.queryIntentActivities(intent, 0)
        if (infos.size == 0) return pkgs
        val size = infos.size
        for (i in 0 until size) {
            var pkgName = ""
            try {
                val activityInfo = infos[i].activityInfo
                pkgName = activityInfo.packageName
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (!TextUtils.isEmpty(pkgName)) pkgs.add(pkgName)
        }
        return pkgs
    }

    /**
     * 获取已安装应用商店的包名列表
     * 获取有在AndroidManifest 里面注册<category android:name="android.intent.category.APP_MARKET"></category>的app
     *
     * @param context
     * @return
     */
    fun getInstallAppMarkets(context: Context): ArrayList<String> {
        //默认的应用市场列表，有些应用市场没有设置APP_MARKET通过隐式搜索不到
        val pkgList = ArrayList<String>()
        pkgList.add("com.xiaomi.market") // 小米应用商店
        pkgList.add("com.huawei.appmarket") // 华为应用商店
        pkgList.add("com.oppo.market") // oppo 应用商店
        pkgList.add("com.heytap.market") // oppo/一加 应用商店
        pkgList.add("com.vivo.market") // vivo 应用商店
        pkgList.add("com.bbk.appstore") // 步步高应用商店
        pkgList.add("com.meizu.mstore") // 魅族 应用商店

        pkgList.add("com.tencent.android.qqdownloader") // 应用宝
        pkgList.add("com.wandoujia.phoenix2") // 豌豆荚应用商店

//        pkgList.add("com.qihoo.appstore") // 360 手机助手
//        pkgList.add("com.baidu.appsearch") // 百度手机助手
//        pkgList.add("com.coolapk.market") // 酷安市场
        //不常用
//        pkgList.add("com.dragon.android.pandaspace"); // 91应用商店
//        pkgList.add("com.hiapk.marketpho"); // 安智应用商店
//        pkgList.add("com.yingyonghui.market"); // 应用汇应用商店
//        pkgList.add("com.mappn.gfan"); // 机锋应用商店
//        pkgList.add("com.pp.assistant"); // PP助手应用商店

        return selectedInstalledAPPs(context, pkgList)
    }

    /**
     * 过滤出已经安装的包名集合
     *
     * @param context
     * @param pkgs    -->待过滤包名集合
     * @return 已安装的包名集合
     */
    fun selectedInstalledAPPs(context: Context, pkgs: ArrayList<String>): ArrayList<String> {
        val installedList = ArrayList<String>()
        val installedPackages = context.packageManager.getInstalledPackages(0)
        installedPackages.forEach {
            installedList.add(it.packageName)
        }
        pkgs.retainAll(installedList)
        return pkgs
    }

    /**
     * 跳转到app详情界面
     *
     * @param appPkg    App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,
     * 否则调转到目标市场的应用详情界面,某些应用商店可能会失败
     */
    fun launchAppDetail(context: Context, appPkg: String, marketPkg: String?) {
        try {
            if (TextUtils.isEmpty(appPkg)) {
                Toast.makeText(context, R.string.appraise_package_null, Toast.LENGTH_SHORT).show()
                return
            }
            val uri = Uri.parse("market://details?id=$appPkg")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (!TextUtils.isEmpty(marketPkg)) intent.setPackage(marketPkg)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, R.string.appraise_package_null, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}
package cn.lvsong.lib.library.utils

import android.annotation.SuppressLint
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
object AppraiseUtils {

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
        pkgList.add("com.tencent.android.qqdownloader") // 应用宝
        pkgList.add("com.qihoo.appstore") // 360 手机助手
        pkgList.add("com.baidu.appsearch") // 百度手机助手
        pkgList.add("com.xiaomi.market") // 小米应用商店
        pkgList.add("com.huawei.appmarket") // 华为应用商店
        pkgList.add("com.oppo.market") // oppo 应用商店
        pkgList.add("com.heytap.market") // oppo/一加 应用商店
        pkgList.add("com.vivo.market") // vivo 应用商店
        pkgList.add("com.meizu.mstore") // 魅族 应用商店
        pkgList.add("com.coolapk.market") // 酷安市场
        pkgList.add("com.wandoujia.phoenix2") // 豌豆荚应用商店

        //不常用
//        pkgList.add("com.bbk.appstore"); // 步步高应用商店
//        pkgList.add("com.dragon.android.pandaspace"); // 91应用商店
//        pkgList.add("com.hiapk.marketpho"); // 安智应用商店
//        pkgList.add("com.yingyonghui.market"); // 应用汇应用商店
//        pkgList.add("com.mappn.gfan"); // 机锋应用商店
//        pkgList.add("com.pp.assistant"); // PP助手应用商店

        // 这个过滤,不能正确过滤一加应用市场
//        val pkgs = ArrayList<String>()
//        if (context == null) return pkgs
//        val intent = Intent()
//        intent.action = "android.intent.action.MAIN"
//        //        intent.addCategory("android.intent.category.APP_MARKET");
//        intent.addCategory("android.intent.category.LAUNCHER")
//        val pm = context.packageManager
//        val infos = pm.queryIntentActivities(intent, 0)
//        if (infos != null && infos.size > 0) {
//            for (i in infos.indices) {
//                var pkgName = ""
//                try {
//                    val activityInfo = infos[i].activityInfo
//                    pkgName = activityInfo.packageName
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                if (!TextUtils.isEmpty(pkgName)) {
//                    pkgs.add(pkgName)
//                }
//            }
//        }
//        //取两个list交集,去除重复
//        pkgList.retainAll(pkgs)

        return selectedInstalledAPPs(context,pkgList)
    }

    /**
     * 过滤出已经安装的包名集合
     *
     * @param context
     * @param pkgs    -->待过滤包名集合
     * @return 已安装的包名集合
     */
    fun selectedInstalledAPPs(context: Context, pkgs: ArrayList<String>): ArrayList<String> {
        val empty = ArrayList<String>()
        val pm = context.packageManager
        val installedPkgs = pm.getInstalledPackages(0)
        val li = installedPkgs.size
        val lj = pkgs.size
        for (j in 0 until lj) {
            for (i in 0 until li) {
                var installPkg = ""
                val checkPkg = pkgs[j]
                try {
                    installPkg = installedPkgs[i].applicationInfo.packageName
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (TextUtils.isEmpty(installPkg)) continue
                if (installPkg == checkPkg) {
                    empty.add(installPkg)
                    break
                }

                /*   // 需要的话,可以使用下面的
                // 如果非系统应用，则添加至appList,这个会过滤掉系统的应用商店，如果不需要过滤就不用这个判断
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    //将应用相关信息缓存起来，用于自定义弹出应用列表信息相关用
                    AppInfo appInfo = new AppInfo();
                    appInfo.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
                    appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));
                    appInfo.setPackageName(packageInfo.packageName);
                    appInfo.setVersionCode(packageInfo.versionCode);
                    appInfo.setVersionName(packageInfo.versionName);
                    appInfos.add(appInfo);
                    appList.add(installPkg);
                }
          * */
            }
        }
        return empty
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
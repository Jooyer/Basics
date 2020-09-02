package cn.lvsong.lib.demo

import android.app.Application
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.net.network.NetWorkMonitorManager
import cn.lvsong.lib.ui.StatusConfig

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-11-09
 * Time: 23:46
 */
class App :Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * 根据需要初始化,默认有平移尺寸和 StatusBarColor
         */
//        StatusConfig.INSTANCE.setTranslateY(DensityUtil.dp2pxRtInt(48))

        // 网络变化监听
        NetWorkMonitorManager.INSTANCE.init(this)
    }

}
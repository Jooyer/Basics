package cn.lvsong.lib.demo

import android.app.Application
import cn.lvsong.lib.net.network.NetWorkMonitorManager

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
//        StatusConfig.INSTANCE.setTranslateY(DensityUtils.dp2pxRtInt(48))
        NetWorkMonitorManager.INSTANCE.init(this)
    }

}
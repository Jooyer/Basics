package cn.lvsong.lib.demo

import cn.lvsong.lib.library.LSApp
import cn.lvsong.lib.net.retrofit.RxRetrofit

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-11-09
 * Time: 23:46
 */
class App :LSApp() {

    override fun onCreate() {
        super.onCreate()
        RxRetrofit.instance.init(this,"Demo","http://192.168.1.14:5555/api/",true)
        /**
         * 根据需要初始化,默认有平移尺寸和 StatusBarColor
         */
//        StatusConfig.INSTANCE.setTranslateY(DensityUtils.dp2pxRtInt(48))
    }

}
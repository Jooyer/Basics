package cn.lvsong.lib.library

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.tencent.mmkv.MMKV

/**
 * Desc: 推荐继承此 Application ,因为它已经帮忙对 MultiDex 和 MMKV 进行了初始化
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 18:27
 */
open class LSApp : Application(){

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        MMKV.initialize(this)
    }


}
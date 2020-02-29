package cn.lvsong.lib.demo.model

import android.util.Log
import cn.lvsong.lib.ui.mvp.BaseModel

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-11-09
 * Time: 23:39
 */
class LazyModel : BaseModel() {

    fun testRunOnCreateMethod(){
        Log.e("Test","LazyModel====================")
    }

}
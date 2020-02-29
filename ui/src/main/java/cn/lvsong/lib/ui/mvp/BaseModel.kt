package cn.lvsong.lib.ui.mvp

import io.reactivex.disposables.Disposable

/**
 * Desc:
 * Author: Jooyer
 * Date: 2018-07-24
 * Time: 17:10
 */
open class BaseModel : IBaseModel {

    // 如果想使用 AutoDispose 也可以的
    // https://www.jianshu.com/p/8993b247947a
    val mRequestList = LinkedHashMap<String, Disposable>()

    override fun cancelAllRequests() {
        for ((_, value) in mRequestList) {
            if (!value.isDisposed) {
                println("cancelAllRequests==============")
                value.dispose()
            }
        }
    }


}
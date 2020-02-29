package cn.lvsong.lib.ui.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent


/**
 * 源码解析: https://blog.csdn.net/xiatiandefeiyu/article/details/78643482
 *  参考自: https://blog.csdn.net/candyguy242/article/details/80662169
 *  自定义View中使用: https://blog.csdn.net/zhangphil/article/details/77049812
 * Desc: Presenter 基类
 * Author: Jooyer
 * Date: 2018-07-24
 * Time: 12:51
 */
interface IBasePresenter : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(provider: LifecycleOwner)

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(provider: LifecycleOwner)

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(provider: LifecycleOwner)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(provider: LifecycleOwner)

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onLifecycleChanged(provider: LifecycleOwner, event: Lifecycle.Event)
}
package cn.lvsong.lib.ui

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.*
import cn.lvsong.lib.net.network.NetWorkMonitor
import cn.lvsong.lib.net.network.NetWorkMonitorManager
import cn.lvsong.lib.net.network.NetworkType

/**
 * 源码解析: https://blog.csdn.net/xiatiandefeiyu/article/details/78643482
 *  参考自: https://blog.csdn.net/candyguy242/article/details/80662169
 *  自定义View中使用: https://blog.csdn.net/zhangphil/article/details/77049812
 *
 *  ---------------------------------------------------------------------------
 *
 * https://blog.csdn.net/NJP_NJP/article/details/103524778
 * 初始化方式:
 * ViewModelProvider(this).get(NetModel::class.java)
 */
open class BaseViewModel(@NonNull application: Application) : AndroidViewModel(application),
    LifecycleObserver {
    // 储存网络请求状态
    val mLoadState = MutableLiveData<LoadState>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(provider: LifecycleOwner) {
        NetWorkMonitorManager.INSTANCE.register(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(provider: LifecycleOwner) {
        NetWorkMonitorManager.INSTANCE.unregister(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(provider: LifecycleOwner) {

    }

    /**
     *  在子类中 写入下面注释的方法，即可监听网络变化
     */
//    @NetWorkMonitor([NetworkType.NETWORK_4G, NetworkType.NETWORK_WIFI, NetworkType.NETWORK_NONE])
//    fun onNetWorkStateChange(state: NetworkType) {
//    }

}
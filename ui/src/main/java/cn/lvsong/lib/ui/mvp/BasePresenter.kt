package cn.lvsong.lib.ui.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import ccn.lvsong.lib.ui.network.NetStateChangeObserver
import cn.lvsong.lib.net.network.NetWorkReceiver
import cn.lvsong.lib.net.network.NetworkType


/**
 * Desc: BasePresenter
 * Author: Jooyer
 * Date: 2018-07-24
 * Time: 13:46
 */
abstract class BasePresenter<V : IBaseView, M : IBaseModel>(view: V,model:M) : IBasePresenter, NetStateChangeObserver {
    val TAG = BasePresenter::class.java.simpleName
    var mView: V = view
    protected var mModel = model


    /**
     *  可以做一些初始化工作,但是这些初始化工作尽量不要耗时,
     *  如果是 Fragment,虽然有懒加载模式,但是
     */
    override fun onCreate(provider: LifecycleOwner) {

    }

    override fun onResume(provider: LifecycleOwner) {
        // 添加网络变化观察者
        NetWorkReceiver.INSTANCE.registerObserver(this)
//        Log.i(TAG, "onResume===========${provider.lifecycle.currentState}")
    }

    override fun onPause(provider: LifecycleOwner) {
        // 移除网络变化观察者
        NetWorkReceiver.INSTANCE.unRegisterObserver(this)
//        Log.i(TAG, "onPause===========${provider.lifecycle.currentState}")

    }

    override fun onDestroy(provider: LifecycleOwner) {
        mModel.cancelAllRequests()
    }

    override fun onLifecycleChanged(provider: LifecycleOwner, event: Lifecycle.Event) {
//        Log.i(TAG, "onLifecycleChanged===========${provider.lifecycle.currentState}")
    }

    /**
     * 网络正常
     */
    override fun onNetConnected(info: NetworkType) {
//        println("BasePresenter===============onNetConnected " + info.name)
    }

    /**
     * 无网
     */
    override fun onNetDisconnected() {
//        println("BasePresenter===============onNetDisconnected")
    }

}
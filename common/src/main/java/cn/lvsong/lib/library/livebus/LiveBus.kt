package cn.lvsong.lib.library.livebus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * LiveData 实现事件总线
 */

/*

    val EVENT_KEY_COLLECT_SUCCESS = "EVENT_KEY_COLLECT_SUCCESS"


// 监听
        LiveBus.INSTANCE.with<Int>(CONSTANTS.EVENT_KEY_COLLECT_SUCCESS)
            .observe(this, Observer {
                mCollectChanged = true
            })


// 发送事件
        LiveBus.INSTANCE.with<Int>(CONSTANTS.EVENT_KEY_COLLECT_SUCCESS).setValue(200)


 */


class LiveBus private constructor() {

    private val mBusMap by lazy {
        mutableMapOf<String, BusLiveData<*>>()
    }

    /**
     * 获取订阅对象
     */
    fun <T> with(key: String): BusLiveData<T> {
        return mBusMap.getOrPut(key) { BusLiveData<T>() } as BusLiveData<T>
    }

    /**
     * 取消粘性事件
     */
    fun removeSticky(key: String, owner: LifecycleOwner): Boolean {
        if (mBusMap.containsKey(key)) {
            mBusMap.remove(key)?.let {
                it.removeObservers(owner)
            }
            return true
        }
        return false
    }


    class BusLiveData<T> : MutableLiveData<T>() {

        private var mVersion = START_VERSION

        /**
         * 粘性事件发送的数据
         */
        private var mStickyData: T? = null

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            observe(owner, observer, false)
        }

        /**
         * 发送数据
         */
        override fun setValue(value: T) {
            mVersion++
            super.setValue(value)
        }

        /**
         * 子线程发送数据
         */
        override fun postValue(value: T) {
            mVersion++
            super.postValue(value)
        }

        /**
         * 发送粘性数据
         */
        fun setValueSticky(value: T) {
            mStickyData = value
            setValue(value)
        }

        /**
         * 子线程发送粘性数据
         */
        fun postValueSticky(value: T) {
            mStickyData = value
            postValue(value)
        }

        /**
         * 自定义 Observer包装类
         */
        private class ObserverWrapper<T>(
            val observer: Observer<in T>,
            val liveData: BusLiveData<T>,
            val sticky: Boolean
        ) : Observer<T> {
            // 通过标志位过滤数据,防止事件丢失
            private var mLastVersion = liveData.mVersion

            override fun onChanged(t: T?) {
                if (mLastVersion >= liveData.mVersion) {
                    // 回调粘性事件
                    if (sticky && liveData.mStickyData != null) {
                        observer.onChanged(liveData.mStickyData)
                    }
                    return
                }
                mLastVersion = liveData.mVersion
                observer.onChanged(t)
            }
        }

        /**
         * 对外提供的观察者方法
         */
        fun observe(owner: LifecycleOwner, observer: Observer<in T>, sticky: Boolean) {
            super.observe(owner, ObserverWrapper(observer, this, sticky))
        }
    }

    companion object {
        private const val START_VERSION = -1
        val INSTANCE: LiveBus by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            LiveBus()
        }
    }

}
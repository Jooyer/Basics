package cn.lvsong.lib.net.network

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import android.os.Build
import android.telephony.TelephonyManager
import cn.lvsong.lib.net.utils.NetWorkUtil
import java.lang.reflect.InvocationTargetException
import java.util.*


/** https://www.jianshu.com/p/3a95523fb21b
 * AnnotationApplication
 * Created by anonyper on 2019/6/10.
 * 用法:
 * 1. 在Application中初始化
 *     NetWorkMonitorManager.INSTANCE.init(this)
 * 2. @Override // 也可以写在 onResume(),根据需要灵活处理
 *   protected void onStart() {
 *   super.onStart();
 *   NetWorkMonitorManager.INSTANCE.register(this);
 *   }
 * 3. @Override // 也可以写在onStop(),根据需要灵活处理
 *   protected void onDestroy() {
 *   super.onDestroy();
 *   NetWorkMonitorManager.INSTANCE.unregister(this);
 *   }
 * 4. 在需要监听的地方写如下方法,注解和方法参数都必须如下,方法名(onNetWorkStateChange)可以随意,注解中数组可以按需要写,不写则监听WIFI,GPRS,NONE
 *     @NetWorkMonitor(arrayOf(NetworkType.NETWORK_WIFI))
 *    fun onNetWorkStateChange(state: NetworkType){}
 *
 */
class NetWorkMonitorManager private constructor() {

    private val ANDROID_NET_CHANGE_ACTION =
        "android.net.conn.CONNECTIVITY_CHANGE"

    /**
     * 存储接受网络状态变化消息的方法的map
     */
    private val mNetWorkStateChangedMethodMap: MutableMap<Any, NetWorkStateReceiverMethod> =
        HashMap()

    private lateinit var mApplication: Application

    /**
     * 监听网络改变的广播
     */
    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.equals(ANDROID_NET_CHANGE_ACTION, ignoreCase = true)) {
                postNetState(NetworkTypeUtil.getNetworkType(context))
            }
        }
    }

    /**
     * >=21 网络监听的回调
     */
    private var mNetworkCallback: NetworkCallback = object : NetworkCallback() {
        /**
         * 网络可用的回调连接成功
         */
        override fun onAvailable(network: Network) {
            NetWorkUtil.setNetWorkAvailable(true)
            postNetState(NetworkTypeUtil.getNetworkType(mApplication))
        }

        /**
         * 网络不可用时调用和onAvailable成对出现
         */
        override fun onLost(network: Network) {
            NetWorkUtil.setNetWorkAvailable(false)
            postNetState(NetworkType.NETWORK_NONE)
        }

        /**
         * 在网络连接正常的情况下，丢失数据会有回调 即将断开时
         */
        override fun onLosing(network: Network, maxMsToLive: Int) {
        }

        /**
         * 网络功能更改 满足需求时调用
         * @param network
         * @param networkCapabilities
         */
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
        }

        /**
         * 网络连接属性修改时调用
         * @param network
         * @param linkProperties
         */
        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        }

        /**
         * 网络缺失network时调用
         */
        override fun onUnavailable() {
            NetWorkUtil.setNetWorkAvailable(false)
        }
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NetWorkMonitorManager()
        }
    }


    /**
     * 初始化 传入application
     *
     * @param application
     */
    fun init(application: Application?) {
        if (application == null) {
            throw NullPointerException("application can not be null")
        }
        this.mApplication = application
        initMonitor()
    }

    /**
     * 初始化网络监听 根据不同版本做不同的处理
     */
    private fun initMonitor() {
        val connectivityManager =
            mApplication.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> { //API 大于26时
                connectivityManager.registerDefaultNetworkCallback(mNetworkCallback)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> { //API 大于21时
                val builder = NetworkRequest.Builder()
                val request = builder.build()
                connectivityManager.registerNetworkCallback(request, mNetworkCallback)
            }
            else -> { //低版本
                val intentFilter = IntentFilter()
                intentFilter.addAction(ANDROID_NET_CHANGE_ACTION)
                mApplication.registerReceiver(mReceiver, intentFilter)
            }
        }
    }

    /**
     * 注册监听
     * @param obj
     */
    fun register(obj: Any?) {
        if (obj != null) {
            val netWorkStateReceiverMethod = findMethod(obj)
            if (netWorkStateReceiverMethod != null) {
                mNetWorkStateChangedMethodMap[obj] = netWorkStateReceiverMethod
            }
        }
    }

    /**
     * 移除监听
     *
     * @param object
     */
    fun unregister(obj: Any?) {
        if (obj != null) {
            mNetWorkStateChangedMethodMap.remove(obj)
        }
    }

    /**
     * 反注册广播
     */
    fun onDestroy() {
        mApplication.unregisterReceiver(mReceiver)
    }


    /**
     * 网络状态发生变化，需要去通知更改
     * @param netWorkState
     */
    private fun postNetState(netWorkType: NetworkType) {
        val set: Set<Any> = mNetWorkStateChangedMethodMap.keys
        val iterator: Iterator<*> = set.iterator()
        while (iterator.hasNext()) {
            val obj = iterator.next()!!
            val netWorkStateReceiverMethod =
                mNetWorkStateChangedMethodMap[obj]
            invokeMethod(netWorkStateReceiverMethod, netWorkType)
        }
    }

    /**
     * 具体执行方法
     *
     * @param netWorkStateReceiverMethod
     * @param netWorkState
     */
    private fun invokeMethod(
        netWorkStateReceiverMethod: NetWorkStateReceiverMethod?,
        netWorkType: NetworkType
    ) {
        if (netWorkStateReceiverMethod != null) {
            try {
                val netWorkStates: Array<NetworkType> =
                    netWorkStateReceiverMethod.getNetWorkState()
                for (myState in netWorkStates) {
                    if (myState === netWorkType) {
                        netWorkStateReceiverMethod.method!!.invoke(
                            netWorkStateReceiverMethod.obj,
                            netWorkType
                        )
                        return
                    }
                }
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 找到对应的方法
     *
     * @param object
     * @return
     */
    private fun findMethod(obj: Any?): NetWorkStateReceiverMethod? {
        var targetMethod: NetWorkStateReceiverMethod? = null
        if (obj != null) {
            val myClass: Class<*> = obj.javaClass
            //获取所有的方法
            val methods = myClass.declaredMethods
            for (method in methods) {
                if (null != method.getAnnotation(NetWorkMonitor::class.java)) {
                    //获取方法参数
                    val parameters = method.parameterTypes
                    //参数的类型需要时NetWorkState类型
                    if (parameters[0].name == NetworkType::class.java.name) {
                        //是NetWorkState类型的参数
                        val netWorkMonitor: NetWorkMonitor =
                            method.getAnnotation(NetWorkMonitor::class.java)
                        targetMethod = NetWorkStateReceiverMethod()
                        //如果没有添加注解value值，默认就是所有网络状态变化都通知
                        val netWorkStates: Array<NetworkType> =
                            netWorkMonitor.monitorFilter
                        targetMethod.setNetWorkState(netWorkStates)
                        targetMethod.method = method
                        targetMethod.obj = obj
                        //只添加第一个符合的方法
                        return targetMethod
                    }
                }
            }
        }
        return targetMethod
    }

    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * 自定义
     *
     * @param context
     * @return
     */
    private fun getAPNType(context: Context): Int {
        //结果返回值
        var netType = 0
        //获取手机所有连接管理对象
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //获取NetworkInfo对象
        val networkInfo = manager.activeNetworkInfo ?: return netType
        //NetworkInfo对象为空 则代表没有网络
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        val nType = networkInfo.type
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = 1
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            val nSubType = networkInfo.subtype
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            netType = if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                && !telephonyManager.isNetworkRoaming
            ) {
                4
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA || (nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                        && !telephonyManager.isNetworkRoaming)
            ) {
                3
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS || nSubType == TelephonyManager.NETWORK_TYPE_EDGE || (nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                        && !telephonyManager.isNetworkRoaming)
            ) {
                2
            } else {
                2
            }
        }
        return netType
    }


}
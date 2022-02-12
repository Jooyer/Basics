package cn.lvsong.lib.net.network

/**
 * https://www.jianshu.com/p/1821eb89bdfd
 * 标记哪个方法需要监听网络变化, 方法名 onNetWorkStateChange 随意
 * 重要的事情说三遍: 默认只能在 BaseViewModel 的子类中进行监听,  如果需其他类中监听,请自行调用 NetWorkMonitorManager.INSTANCE.register(this) / NetWorkMonitorManager.INSTANCE.unregister(this)
 * 重要的事情说三遍: 默认只能在 BaseViewModel 的子类中进行监听,  如果需其他类中监听,请自行调用 NetWorkMonitorManager.INSTANCE.register(this) / NetWorkMonitorManager.INSTANCE.unregister(this)
 * 重要的事情说三遍: 默认只能在 BaseViewModel 的子类中进行监听,  如果需其他类中监听,请自行调用 NetWorkMonitorManager.INSTANCE.register(this) / NetWorkMonitorManager.INSTANCE.unregister(this)
 *
 * eg:
 *     @NetWorkMonitor([NetworkType.NETWORK_GPRS, NetworkType.NETWORK_WIFI, NetworkType.NETWORK_NONE])
 *      fun onNetWorkStateChange(state: NetworkType) {
 *      Log.e("NestedRefresh","onNetWorkStateChange============state: ${state.name}")
 *      }
 */
@Retention(AnnotationRetention.RUNTIME) //运行时注解
@Target(AnnotationTarget.FUNCTION) //标记在方法上
annotation class NetWorkMonitor(
    //监听的网络状态变化 默认全部监听并提示
    val monitorFilter: Array<NetworkType> = [NetworkType.NETWORK_AVAILABLE,NetworkType.NETWORK_WIFI,NetworkType.NETWORK_WIFI, NetworkType.NETWORK_GPRS, NetworkType.NETWORK_UNKNOWN, NetworkType.NETWORK_NONE]
)
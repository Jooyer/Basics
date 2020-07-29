package cn.lvsong.lib.net.network

/**
 * https://www.jianshu.com/p/1821eb89bdfd
 * 标记哪个方法需要监听网络变化
 */
@Retention(AnnotationRetention.RUNTIME) //运行时注解
@Target(AnnotationTarget.FUNCTION) //标记在方法上
annotation class NetWorkMonitor(
    //监听的网络状态变化 默认全部监听并提示
    val monitorFilter: Array<NetworkType> = [NetworkType.NETWORK_WIFI, NetworkType.NETWORK_4G, NetworkType.NETWORK_3G,
        NetworkType.NETWORK_2G, NetworkType.NETWORK_UNKNOWN, NetworkType.NETWORK_NONE]
)
package cn.lvsong.lib.net.network

import java.lang.reflect.Method


/** https://www.jianshu.com/p/3a95523fb21b
 * 保存接受状态变化的方法对象
 * AnnotationApplication
 * Created by anonyper on 2019/6/10.
 */
class NetWorkStateReceiverMethod {
    /**
     * 网络改变执行的方法
     */
    var method: Method? = null

    /**
     * 网络改变执行的方法所属的类
     */
    var obj: Any? = null

    /**
     * 监听的网络改变类型
     */
    private var netWorkState: Array<NetworkType> =
        arrayOf(NetworkType.NETWORK_AVAILABLE,NetworkType.NETWORK_WIFI, NetworkType.NETWORK_GPRS, NetworkType.NETWORK_UNKNOWN, NetworkType.NETWORK_NONE)

    fun getNetWorkState(): Array<NetworkType> {
        return netWorkState
    }

    fun setNetWorkState(netWorkState: Array<NetworkType>) {
        this.netWorkState = netWorkState
    }
}
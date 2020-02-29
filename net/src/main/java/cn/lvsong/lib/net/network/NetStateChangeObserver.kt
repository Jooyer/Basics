package ccn.lvsong.lib.ui.network

import cn.lvsong.lib.net.network.NetworkType

/**
 * Desc: 网络异常的回调
 * Author: Jooyer
 * Date: 2018-08-03
 * Time: 17:30
 */
interface NetStateChangeObserver {
    /**
     * @param info --> 可以获取更多信息
     * info.getType()
     * -->  == ConnectivityManager.TYPE_WIFI ---> WIFI
     * -->  == ConnectivityManager.TYPE_MOBILE ---> 手机流量
     * ----> 流量中分类:  info.getSubtype()
     * ----->  == TelephonyManager.NETWORK_TYPE_LTE ---> 4G LTE
     * ----->  == TelephonyManager.NETWORK_TYPE_UMTS (移动3G) || TelephonyManager.NETWORK_TYPE_HSDPA (联通3G)
     * TelephonyManager.NETWORK_TYPE_EVDO_0 (电信3G)
     * ----->  == TelephonyManager.NETWORK_TYPE_GPRS || TelephonyManager.NETWORK_TYPE_EDGE (两者为移动/联通2G)
     * TelephonyManager.NETWORK_TYPE_CDMA (电信2G)
     */
     fun onNetConnected(info: NetworkType)

     fun onNetDisconnected()
}
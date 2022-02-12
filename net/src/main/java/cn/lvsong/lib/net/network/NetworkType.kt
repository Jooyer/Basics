package cn.lvsong.lib.net.network

import androidx.annotation.Keep

/** 网络类型
 * Author: Jooyer
 * Date: 2018-09-21
 * Time: 15:22
 */
@Keep
enum class NetworkType(private val desc: String) {
    NETWORK_AVAILABLE("AVAILABLE"), // 网络可用
    NETWORK_WIFI("WIFI"), // Wi-Fi网络
    NETWORK_GPRS("GPRS"), //移动蜂窝网络
    NETWORK_UNKNOWN("UNKNOWN"), //未知网络
    NETWORK_NONE("NONE"); //没有网络

    override fun toString(): String {
        return desc
    }
}
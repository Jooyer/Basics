package cn.lvsong.lib.net.network;

import androidx.annotation.Keep;

/** 网络类型
 * Desc: https://www.jianshu.com/p/6fa0f1f1ce48?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=qq
 * Author: Jooyer
 * Date: 2018-09-21
 * Time: 15:22
 */
@Keep
public enum NetworkType {

    NETWORK_WIFI("WiFi"),
    NETWORK_4G("4G"),
    NETWORK_2G("2G"),
    NETWORK_3G("3G"),
    NETWORK_UNKNOWN("Unknown"),
    NETWORK_NO("No network");

    private String desc;
    NetworkType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}

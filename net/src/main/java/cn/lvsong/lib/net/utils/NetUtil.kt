package cn.lvsong.lib.net.utils

import com.tencent.mmkv.MMKV

/**
 * Desc: 判断 网络和Url
 * Author: Jooyer
 * Date: 2018-07-31
 * Time: 11:07
 */
object NetUtil {

    /**
     * 判断网络是否正常连接
     *
     * @return true --> 正常连接
     */
    fun isNetWorkAvailable(): Boolean {
        return  MMKV.defaultMMKV().decodeBool(cn.lvsong.lib.library.utils.Constants.KEY_NETWORK_STATE,true)
    }

    /**
     * 读取 BaseUrl
     */
    fun getBaseUrl(netUrl: String): String {
        var url = netUrl
        var head = ""
        var index = url.indexOf("://")
        if (index != -1) {
            head = url.substring(0, index + 3)
            url = url.substring(index + 3)
        }

        index = url.lastIndexOf("/")
        if (index != -1) {
            url = url.substring(0, index + 1)
        }
        return head + url
    }


}
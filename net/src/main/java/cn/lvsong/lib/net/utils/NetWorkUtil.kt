package cn.lvsong.lib.net.utils


/**
 * Desc: 判断 网络和Url
 * Author: Jooyer
 * Date: 2018-07-31
 * Time: 11:07
 */
object NetWorkUtil {

    /**
     * 网络是否可用
     */
    private var mNetWorkAvailable = false

    /**
     * 缓存Token
     */
    private var mAccessToken = ""

    /**
     * 判断网络是否正常连接
     *
     * @return true --> 正常连接
     */
    fun isNetWorkAvailable() = mNetWorkAvailable

    /**
     * @param netWorkAvailable  --> true 表示可用
     */
    fun setNetWorkAvailable(netWorkAvailable: Boolean) {
        mNetWorkAvailable = netWorkAvailable
    }

    fun getAccessToken() = mAccessToken

    /**
     * 设置Token
     */
    fun setAccessToken(token: String) {
        mAccessToken = token
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
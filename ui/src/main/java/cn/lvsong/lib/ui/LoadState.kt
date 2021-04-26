package cn.lvsong.lib.ui

/**
 * 数据加载状态,提供界面显示 加载/成功/失败UI 的依据
 * 参考: https://blog.csdn.net/NJP_NJP/article/details/103524778
 */
/**
 * @param msg --> 提示信息
 * @param code --> 状态码
 * @param
 * @param apiType --> 区别不同请求接口
 */
sealed class LoadState(val code: Int = 200, val apiType: Int, val msg: String) {

    class Loading(apiType: Int = 0, msg: String = "") : LoadState(apiType = apiType, msg = msg)

    class Success(code: Int = 200,apiType: Int = 0,  msg: String = "") :
        LoadState( code,apiType, msg)

    class Failure(apiType: Int = 0, msg: String = "") : LoadState(apiType=apiType, msg = msg)

    class NetError(apiType: Int = 0, msg: String = "") : LoadState(apiType=apiType, msg = msg)
}
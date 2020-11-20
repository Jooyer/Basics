package cn.lvsong.lib.ui

/**
 * 数据加载状态,提供界面显示 加载/成功/失败UI 的依据
 * 参考: https://blog.csdn.net/NJP_NJP/article/details/103524778
 */
/**
 * @param msg --> 提示信息
 * @param code --> 状态码
 * @param type --> 区别不同请求接口
 */
sealed class LoadState(val msg: String, val type: Int, val code: Int = 200) {

    class Loading(msg: String = "", type: Int = 0) : LoadState(msg, type)
    class Success(msg: String = "", type: Int = 0, code: Int = 200) : LoadState(msg, type, code)
    class Failure(msg: String = "", type: Int = 0) : LoadState(msg, type)
}
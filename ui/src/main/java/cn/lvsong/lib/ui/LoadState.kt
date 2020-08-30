package cn.lvsong.lib.ui

/**
 * 数据加载状态,提供界面显示 加载/成功/失败UI 的依据
 * 参考: https://blog.csdn.net/NJP_NJP/article/details/103524778
 */
sealed class LoadState(val msg: String) {

    class Loading(msg: String = "") : LoadState(msg)
    class Success(msg: String = "") : LoadState(msg)
    class Failure(msg: String = "") : LoadState(msg)
}
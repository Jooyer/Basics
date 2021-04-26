package cn.lvsong.lib.ui

/**
 * 数据加载状态,提供界面显示 加载/成功/失败UI 的依据
 * 参考: https://blog.csdn.net/NJP_NJP/article/details/103524778
 */
/**
 * @param msg --> 提示信息
 * @param code --> 状态码
 * @param apiType --> 区别不同请求接口
 * @param subType --> 同一种 code 又有不同提示,
 * eg: 获取列表,请求成功(code = 200)了,但是分为 列表有数据和列表为空,此时 apiType = 1(请求列表接口) , subType = 1(列表不为空) || subType = 2(列表为空)
 */
sealed class LoadState(val code: Int = 200, val apiType: Int, val subType: Int, val msg: String) {

    class Loading(apiType: Int = 0, subType: Int = 0, msg: String = "") :
        LoadState(apiType = apiType, subType = subType, msg = msg)

    class Success(code: Int = 200, apiType: Int = 0, subType: Int = 0, msg: String = "") :
        LoadState(code = code, apiType = apiType, subType = subType, msg = msg)

    class Failure(code: Int = 200, apiType: Int = 0, subType: Int = 0, msg: String = "") :
        LoadState(code = code, apiType = apiType, subType = subType, msg = msg)

    class NetError(code: Int = 200, apiType: Int = 0, subType: Int = 0, msg: String = "") :
        LoadState(code = code, apiType = apiType, subType = subType, msg = msg)
}
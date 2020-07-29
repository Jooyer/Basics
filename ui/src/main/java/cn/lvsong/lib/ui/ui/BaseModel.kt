package cn.lvsong.lib.ui.ui

import cn.lvsong.lib.net.response.Response

open class BaseModel {

    /** https://blog.csdn.net/NJP_NJP/article/details/103524778
     * 对数据脱壳与预处理
     * @param result --> 请求返回的数据实体
     * @param successCode --> 请求成功状态码,默认是 0
     */
    fun <T> processData(result: Response<T>, successCode:Int = 0): T? {
        return if (successCode == result.code) result.data else throw Throwable(result.msg)
    }



}
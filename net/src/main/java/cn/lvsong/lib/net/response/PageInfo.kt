package cn.lvsong.lib.net.response

import androidx.annotation.Keep

/**
 * Desc: 分页数据封装
 * Author: Jooyer
 * Date: 2019-05-11
 * Time: 19:57
 */
/**
 * @param total  --> 总数量
 * @param page --> 当前页码
 * @param size --> 每页大小
 * @param list --> 数据集合
 */
@Keep
data class PageInfo<T>(var total: Long, var page: Long, var size: Long, var list: T)

package cn.lvsong.lib.library.adapter


/** 原作者: zhy
 * Created by Jooyer on 16/6/28
 */
interface ItemViewDelegate<in T> {
    val itemViewLayoutId: Int

    fun isForViewType(item: T, position: Int): Boolean

    fun convert(holder: ViewHolder, bean: T, position: Int)

}

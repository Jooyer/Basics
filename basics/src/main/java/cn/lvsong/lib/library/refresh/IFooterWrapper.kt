package cn.lvsong.lib.library.refresh

/**
 * Desc: 提供上拉加载控件
 * Author: Jooyer
 * Date: 2019-08-06
 * Time: 15:38
 */
interface IFooterWrapper {

    /**
     * FooterView 一共移动的距离
     */
    fun onMoveDistance(distance: Int)

    /**
     * 上拉中
     */
    fun onPullUp()

    /**
     * 释放即可加载
     */
    fun onPullUpAndReleasable()

    /**
     * 准备加载
     */
    fun onLoadReady()

    /**
     * 加载中
     */
    fun onLoading()

    /**
     * 刷新完成
     */
    fun onLoadComplete(isLoadSuccess: Boolean)

    /**
     * 加载取消
     */
    fun onLoadCancel()

    /**
     * 没有数据
     */
    fun onNoMore()


    /**
     * 获取加载视图高度
     */
    fun getLoadHeight(): Int

}
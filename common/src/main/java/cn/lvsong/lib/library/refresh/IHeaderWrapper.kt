package cn.lvsong.lib.library.refresh

/**
 * Desc: 提供刷新头部控件
 * Author: Jooyer
 * Date: 2019-08-06
 * Time: 15:38
 */
interface IHeaderWrapper {

    /**
     * HeaderView 一共移动的距离
     */
    fun onMoveDistance(distance: Int)
    /**
     * 下拉中
     */
    fun onPullDown()

    /**
     * 释放即可刷新
     */
    fun onPullDownAndReleasable()

    /**
     * 自动刷新时,会回调这个方法,方便更新文本或者动画的准备
     */
    fun onAutoRefreshPreparing(){}

    /**
     * 刷新中
     */
    fun onRefreshing()

    /**
     * 刷新完成
     * @param isRefreshSuccess --> 此次刷新是成功还是失败,方便在刷新头更新文本
     */
    fun onRefreshComplete( isRefreshSuccess: Boolean)


    /**
     * 刷新失败
     */
    fun onRefreshFailure( )

    /**
     * 获取刷视图高度
     */
    fun getRefreshHeight(): Int

}
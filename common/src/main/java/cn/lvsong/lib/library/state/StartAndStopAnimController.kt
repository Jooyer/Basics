package cn.lvsong.lib.library.state

/**
 * Desc: 如果加载中视图实现了此接口,则我们可以在开始加载时显示Loading动画,在加载完成或者失败时结束Loading动画
 * Author: Jooyer
 * Date: 2021-04-17
 * Time: 15:06
 */
interface StartAndStopAnimController {

    fun onStartAnimator()

    fun onStopAnimator()

    /**
     * loading动画结束时回调,比如转一圈结束了则会回调
     * useStatusManager() 返回为true,即启用 StatusManager才有效
     * eg: 具体用法参考: ChrysanthemumView
     */
    fun setOnLoadingAnimatorEndListener(listener: OnLoadingAnimatorEndListener?){

    }

}
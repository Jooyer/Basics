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

}
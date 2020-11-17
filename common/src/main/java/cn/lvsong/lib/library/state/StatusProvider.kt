package cn.lvsong.lib.library.state

/**
 * Desc: 使用此接口,则会将用户自定义的控件绘制在最顶层,建议用在自定义 Toolbar上
 * Author: Jooyer
 * Date: 2020-09-09
 * Time: 18:49
 */
interface StatusProvider {
    /**
     * 是否有阴影
     * true --> 有阴影(自己实现阴影效果), false -->没有阴影
     */
    fun hasShadow():Boolean = false
}
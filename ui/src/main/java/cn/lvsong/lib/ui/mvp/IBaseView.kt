package cn.lvsong.lib.ui.mvp

/** 泛型使用: https://www.jianshu.com/p/fd1594dfa05d
 * Desc: Activity/Fragment 的 View 接口
 * Author: Jooyer
 * Date: 2018-07-24
 * Time: 13:50
 */
interface IBaseView {

    fun showLoading()

    fun showError(message: String)

    fun closeLoading()

}
package cn.lvsong.lib.library.banner

/**
 * Desc: 当 Banner 滑动后,会触发此回调
 * Author: Jooyer
 * Date: 2019-08-30
 * Time: 16:29
 */
interface OnPositionChangeListener {
    /**
     * 当前选中的图片位置, 从0开始计算
     */
    fun onPositionChange(position: Int)
}
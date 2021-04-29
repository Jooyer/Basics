package cn.lvsong.lib.library.topmenu

/**
 * 每一项菜单
 * Created by Jooyer on 2017/2/10
 */
class MenuItem {
    var icon = 0
    var text: String? = null

    constructor(icon: Int, text: String?) {
        this.icon = icon
        this.text = text
    }
}
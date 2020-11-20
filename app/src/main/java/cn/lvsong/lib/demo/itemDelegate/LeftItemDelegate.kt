package cn.lvsong.lib.demo.itemDelegate

import androidx.appcompat.widget.AppCompatImageView
import cn.lvsong.lib.demo.R
import cn.lvsong.lib.demo.util.ImageLoad
import cn.lvsong.lib.library.adapter.ItemViewDelegate
import cn.lvsong.lib.library.adapter.ViewHolder
import cn.lvsong.lib.library.utils.DensityUtil

/**
 * Desc: 左侧
 * Author: Jooyer
 * Date: 2020-11-17
 * Time: 20:08
 */
class LeftItemDelegate : ItemViewDelegate<String> {
    override val itemViewLayoutId = R.layout.item_left_msg_chat

    override fun isForViewType(item: String, position: Int) = position % 2 == 1

    override fun convert(holder: ViewHolder, bean: String, position: Int) {

        ImageLoad.loader.loadImgWithCircleRadius(
            holder.getView<AppCompatImageView>(R.id.aiv_avatar_left_chat),
            R.mipmap.ic_launcher_round,DensityUtil.dp2pxRtFloat(40)
        )

        holder.setText(R.id.btv_msg_left_chat, bean)
    }
}
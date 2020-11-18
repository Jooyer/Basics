package cn.lvsong.lib.demo.itemDelegate

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import cn.lvsong.lib.demo.R
import cn.lvsong.lib.demo.util.ImageLoad
import cn.lvsong.lib.library.adapter.ItemViewDelegate
import cn.lvsong.lib.library.adapter.ViewHolder
import cn.lvsong.lib.library.bubble.BubbleImageView
import cn.lvsong.lib.library.utils.DensityUtil
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

/**
 * Desc: 右侧
 * Author: Jooyer
 * Date: 2020-11-17
 * Time: 20:08
 */
class RightItemDelegate : ItemViewDelegate<String> {
    override val itemViewLayoutId = R.layout.item_right_img_chat

    override fun isForViewType(item: String, position: Int) = 1 != position % 2

    override fun convert(holder: ViewHolder, bean: String, position: Int) {

        ImageLoad.loader.loadImgWithCircleRadius(
            holder.getView<AppCompatImageView>(R.id.aiv_avatar_right_chat),
            R.mipmap.ic_launcher_round, DensityUtil.dp2pxRtFloat(40)
        )

        ImageLoad.loader.downloadImage(holder.itemView.context,
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1605680087572&di=e7781aebc50b086c207a42679cfe2e34&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F03%2F59%2F01300000246803122838591610680.jpg",
        object :CustomTarget<Bitmap>(){
            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
               holder.getView<BubbleImageView>(R.id.biv_avatar_right_chat).setBitmap(resource)
            }
        })
    }
}
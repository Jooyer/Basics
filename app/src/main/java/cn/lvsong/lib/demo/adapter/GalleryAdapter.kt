package cn.lvsong.lib.demo.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import cn.lvsong.lib.demo.R
import cn.lvsong.lib.demo.util.ImageLoad
import cn.lvsong.lib.library.banner.BannerAdapter
import cn.lvsong.lib.library.banner.BannerHolder

/**
 * Desc: 缩放效果
 * Author: Jooyer
 * Date: 2019-08-28
 * Time: 12:01
 */
// PS: itemOriginalWidth 必须参数
class GalleryAdapter( private val itemWidth: Int,  data: List<String>, layoutId: Int) :
    BannerAdapter<String>(data, layoutId) {
    override fun onBindViewHolder(holder: BannerHolder, position: Int) {
        // 因为需要缩放,这里必须重新设置每一个 Item 宽度
        holder.itemView.layoutParams.width = itemWidth
        holder.itemView.findViewById<AppCompatTextView>(R.id.tv_position).text = "$position"
        ImageLoad.loader.loadImage(
            holder.itemView.findViewById<AppCompatImageView>(R.id.iv_banner),
            mData[position]
        )
    }

}
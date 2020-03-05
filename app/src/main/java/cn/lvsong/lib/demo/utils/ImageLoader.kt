package cn.lvsong.lib.demo.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


/** https://blog.csdn.net/niuba123456/article/details/86313749
 * Desc: 图片加载
 * Date: 2019-08-11
 * Time: 9:34
 */
class ImageLoader {
    companion object {
        val loader: ImageLoader by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ImageLoader()
        }
        val options = RequestOptions()

    }

    fun loadImage(iv: ImageView, path: String) {
        Glide.with(iv.context)
            .load(path)
            .into(iv)
    }

    fun loadImage(iv: ImageView, drawableId: Int) {
        Glide.with(iv.context)
            .load(drawableId)
            .into(iv)
    }

    /**
     * 加载圆角图片
     * @param radius --> 圆角大小(px)
     */
    fun loadFillet(iv: ImageView, radius: Int, path: String) {
        Glide.with(iv.context).load(path)
            .apply(options.transform(CenterCrop(), RoundedCorners(radius))).into(iv)
    }

    /**
     * 加载圆形图片
     */
    fun loadCircular(iv: ImageView, path: String) {
        Glide.with(iv.context).load(path)
            .apply(options.transform(CircleCrop())).into(iv)
    }

}

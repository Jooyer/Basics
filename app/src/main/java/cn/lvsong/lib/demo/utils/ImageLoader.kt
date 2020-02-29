package cn.lvsong.lib.demo.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Desc: 图片加载
 * Date: 2019-08-11
 * Time: 9:34
 */
class ImageLoader {
    companion object {
        val loader: ImageLoader by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ImageLoader()
        }
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

}
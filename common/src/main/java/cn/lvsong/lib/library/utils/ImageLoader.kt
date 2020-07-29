package cn.lvsong.lib.library.utils

import android.graphics.Color
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import cn.lvsong.lib.library.other.CircleBorderTransform
import cn.lvsong.lib.library.other.RoundedCornersTransform
import java.io.File


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

    /**
     * 加载 centerCrop 模式图片
     * @param path --> 图片路径
     * @param placeHolder --> 图片占位图
     */
    fun loadImgWithCenterCrop(iv: ImageView, path: String, @DrawableRes placeHolder: Int) {
        Glide.with(iv)
            .load(path)
            .apply(
                RequestOptions().priority(Priority.NORMAL)
                    .centerCrop()
                    .placeholder(placeHolder).error(placeHolder)
            )
            .into(iv)
    }

    /**
     * 加载 centerCrop 模式图片
     * @param path --> 图片路径
     */
    fun loadImgWithCenterCrop(iv: ImageView, path: String) {
        Glide.with(iv)
            .load(path)
            .apply(
                RequestOptions().priority(Priority.NORMAL)
                    .centerCrop()
            )
            .into(iv)
    }


    /**
     * 加载 centerCrop 模式图片
     * @param  drawableId --> 图片资源地址
     */
    fun loadImgWithCenterCrop(iv: ImageView, @DrawableRes drawableId: Int) {
        Glide.with(iv)
            .load(drawableId)
            .apply(
                RequestOptions().priority(Priority.NORMAL)
                    .centerCrop()
            )
            .into(iv)
    }

    /**
     * 加载  centerCrop 模式图片
     * @param file --> 图片资源文件
     */
    fun loadImgWithCenterCrop(iv: ImageView, file: File, @DrawableRes placeHolder: Int) {
        Glide.with(iv)
            .load(file)
            .apply(
                RequestOptions().centerCrop()
                    .placeholder(placeHolder).error(placeHolder)
            )
            .into(iv)
    }


    /**
     * 加载  centerCrop 模式图片
     * @param file --> 图片资源文件
     */
    fun loadImgWithCenterCrop(iv: ImageView, file: File) {
        Glide.with(iv)
            .load(file)
            .apply(RequestOptions().centerCrop())
            .into(iv)
    }

    /**
     * 加载圆形图片,Glide会根据控件大小,将图片裁剪为圆形
     * @param path --> 图片路径
     */
    fun loadImgWithCircleCrop(iv: ImageView, path: String) {
        Glide.with(iv)
            .load(path)
            .apply(RequestOptions().circleCrop())
            .into(iv)
    }

    /**
     * 加载圆形图片,Glide会根据控件大小,将图片裁剪为圆形
     * @param  drawableId --> 图片资源地址
     */
    fun loadImgWithCircleCrop(iv: ImageView, @DrawableRes drawableId: Int) {
        Glide.with(iv)
            .load(drawableId)
            .apply(RequestOptions().circleCrop())
            .into(iv)
    }

    /**
     * 加载四周圆角一定的图片
     * @param path --> 图片路径
     * @param radius --> 圆角半径,单位为dp, 默认10dp
     */
    fun loadImgWithCircleRadius(iv: ImageView, path: String, radius: Float = 10F) {
        Glide.with(iv)
            .load(path)
            .apply(
                RequestOptions.bitmapTransform(
                    RoundedCorners(
                        DensityUtil.dp2pxRtFloat(radius).toInt()
                    )
                )
            )
            .into(iv)
    }

    /**
     * 加载四周四个圆角大小可变的图片
     * @param path --> 图片路径
     * @param tl --> 左上圆角半径,单位为dp, 默认0dp
     * @param tr --> 右上圆角半径,单位为dp, 默认0dp
     * @param br --> 右下圆角半径,单位为dp, 默认0dp
     * @param bl --> 左下圆角半径,单位为dp, 默认0dp
     * PS: 按顺时针传递参数的,需要留意下
     */
    fun loadImgWithFourCorners(
        iv: ImageView,
        path: String,
        tl: Float = 0F,
        tr: Float = 0F,
        br: Float = 0F,
        bl: Float = 0F
    ) {
        Glide.with(iv)
            .load(path)
            .apply(
                RequestOptions.bitmapTransform(
                    RoundedCornersTransform(
                        DensityUtil.dp2pxRtFloat(tl),
                        DensityUtil.dp2pxRtFloat(tr),
                        DensityUtil.dp2pxRtFloat(br),
                        DensityUtil.dp2pxRtFloat(bl)
                    )
                )
            )
            .into(iv)
    }

    /**
     * 加载四周四个圆角大小可变的图片
     * @param drawableId --> 图片资源地址
     * @param tl --> 左上圆角半径,单位为dp, 默认0dp
     * @param tr --> 右上圆角半径,单位为dp, 默认0dp
     * @param br --> 右下圆角半径,单位为dp, 默认0dp
     * @param bl --> 左下圆角半径,单位为dp, 默认0dp
     * PS: 按顺时针传递参数的,需要留意下
     */
    fun loadImgWithFourCorners(
        iv: ImageView,
        @DrawableRes drawableId: Int,
        tl: Float = 0F,
        tr: Float = 0F,
        br: Float = 0F,
        bl: Float = 0F
    ) {
        Glide.with(iv)
            .load(drawableId)
            .apply(
                RequestOptions.bitmapTransform(
                    RoundedCornersTransform(
                        DensityUtil.dp2pxRtFloat(tl),
                        DensityUtil.dp2pxRtFloat(tr),
                        DensityUtil.dp2pxRtFloat(br),
                        DensityUtil.dp2pxRtFloat(bl)
                    )
                )
            )
            .into(iv)
    }


    /**
     * 加载四周四个圆角大小可变的图片
     * @param path --> 图片路径
     * @param radius --> 圆角半径,单位为dp, 默认5dp
     * PS: 按顺时针传递参数的,需要留意下
     */
    fun loadImgWithFourCorners(
        iv: ImageView,
        path: String,
        radius: Float = 5F
    ) {
        Glide.with(iv)
            .load(path)
            .apply(
                RequestOptions.bitmapTransform(
                    RoundedCornersTransform(
                        DensityUtil.dp2pxRtFloat(radius),
                        DensityUtil.dp2pxRtFloat(radius),
                        DensityUtil.dp2pxRtFloat(radius),
                        DensityUtil.dp2pxRtFloat(radius)
                    )
                )
            )
            .into(iv)
    }

    /**
     * 加载四周四个圆角大小可变的图片
     * @param drawableId --> 图片资源地址
     * @param radius --> 圆角半径,单位为dp, 默认5dp
     * PS: 按顺时针传递参数的,需要留意下
     */
    fun loadImgWithFourCorners(
        iv: ImageView,
        @DrawableRes drawableId: Int,
        radius: Float = 5F
    ) {
        Glide.with(iv)
            .load(drawableId)
            .apply(
                RequestOptions.bitmapTransform(
                    RoundedCornersTransform(
                        DensityUtil.dp2pxRtFloat(radius),
                        DensityUtil.dp2pxRtFloat(radius),
                        DensityUtil.dp2pxRtFloat(radius),
                        DensityUtil.dp2pxRtFloat(radius)
                    )
                )
            )
            .into(iv)
    }


    /**
     * 加载带圆环的圆形图片
     * @param path --> 图片路径
     * @param borderWidth --> 圆环的厚度,单位为dp, 默认5dp
     * @param borderColor --> 圆环的颜色,默认白色
     */
    fun loadImgWithCircleAndRing(iv: ImageView, path: String, borderWidth: Float = 5F,@ColorInt borderColor: Int = Color.WHITE){
        Glide.with(iv)
            .load(path)
            .apply(RequestOptions.bitmapTransform(CircleBorderTransform(DensityUtil.dp2pxRtFloat(borderWidth),borderColor)))
            .into(iv)
    }


    /**
     * 加载带圆环的圆形图片
     * @param drawableId --> 图片资源地址
     * @param borderWidth --> 圆环的厚度,单位为dp, 默认5dp
     * @param borderColor --> 圆环的颜色,默认白色
     */
    fun loadImgWithCircleAndRing(iv: ImageView, @DrawableRes drawableId: Int, borderWidth: Float = 5F, @ColorInt borderColor: Int = Color.WHITE){
        Glide.with(iv)
            .load(drawableId)
            .apply(RequestOptions.bitmapTransform(CircleBorderTransform(DensityUtil.dp2pxRtFloat(borderWidth),borderColor)))
            .into(iv)
    }


}

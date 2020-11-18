package cn.lvsong.lib.demo.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import cn.lvsong.lib.library.utils.DensityUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import java.io.File


/** https://blog.csdn.net/niuba123456/article/details/86313749
 * Desc: 图片加载
 * Date: 2019-08-11
 * Time: 9:34
 */
class ImageLoad {
    companion object {
        val loader: ImageLoad by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ImageLoad()
        }
        val options = RequestOptions()

    }

    /**
     * 加载图片
     * @param path --> 图片地址
     */
    fun loadImage(iv: ImageView, path: String) {
        Glide.with(iv.context)
            .load(path)
            .into(iv)
    }

    /**
     * 加载图片
     * @param drawableId --> 图片资源ID
     */
    fun loadImage(iv: ImageView, drawableId: Int) {
        Glide.with(iv.context)
            .load(drawableId)
            .into(iv)
    }

    /**
     * 加载GIF
     * @param drawableId --> GIF文件
     */
    fun loadGif(iv: ImageView, @DrawableRes drawableId: Int) {
        Glide.with(iv.context)
            .asGif()
            .load(drawableId)
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
                options.priority(Priority.NORMAL)
                    .centerCrop()
            )
            .into(iv)
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
                options.priority(Priority.NORMAL)
                    .centerCrop()
                    .placeholder(placeHolder).error(placeHolder)
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
                options.priority(Priority.NORMAL)
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
                options.centerCrop()
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
            .apply(options.centerCrop())
            .into(iv)
    }

    /**
     * 加载圆形图片,Glide会根据控件大小,将图片裁剪为圆形
     * @param path --> 图片路径
     */
    fun loadImgWithCircleCrop(iv: ImageView, path: String) {
        Glide.with(iv)
            .load(path)
            .apply(options.circleCrop())
            .into(iv)
    }

    /**
     * 加载圆形图片,Glide会根据控件大小,将图片裁剪为圆形
     * @param  drawableId --> 图片资源地址
     */
    fun loadImgWithCircleCrop(iv: ImageView, @DrawableRes drawableId: Int) {
        Glide.with(iv)
            .load(drawableId)
            .apply(options.circleCrop())
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
                // 注释掉这个,是因为不能给 ImageView 在xml中设置 scanType=centerCrop
//                RequestOptions.bitmapTransform(
//                    RoundedCorners(
//                        DensityUtil.dp2pxRtFloat(radius).toInt()
//                    )
//                )
                options
                    .transform(
                        CenterCrop(),
                        RoundedCorners(
                            DensityUtil.dp2pxRtFloat(radius).toInt()
                        )
                    )
            )
            .into(iv)
    }

    /**
     * 加载四周圆角一定的图片
     * @param path --> 图片路径
     * @param drawable --> 占位图
     * @param radius --> 圆角半径,单位为dp, 默认10dp
     */
    fun loadImgWithCircleRadius(
        iv: ImageView,
        path: String,
        drawable: Drawable,
        radius: Float = 10F
    ) {
        Glide.with(iv)
            .load(path)
            .apply(
                options
                    .transform(
                        CenterCrop(),
                        RoundedCorners(
                            DensityUtil.dp2pxRtFloat(radius).toInt()
                        )
                    ).placeholder(drawable)
            )
            .into(iv)
    }

    /**
     * 加载四周圆角一定的图片
     * @param path --> 图片路径
     * @param placeHolder --> 占位图
     * @param radius --> 圆角半径,单位为dp, 默认10dp
     */
    fun loadImgWithCircleRadius(
        iv: ImageView,
        path: String,
        @DrawableRes placeHolder: Int,
        radius: Float = 10F
    ) {
        Glide.with(iv)
            .load(path)
            .apply(
                options
                    .transform(
                        CenterCrop(),
                        RoundedCorners(
                            DensityUtil.dp2pxRtFloat(radius).toInt()
                        )
                    ).placeholder(placeHolder).error(placeHolder)
            )
            .into(iv)
    }

    /** https://blog.csdn.net/sinat_31057219/article/details/104630864
     * 加载四周圆角一定的图片
     * @param path --> 图片资源ID
     * @param radius --> 圆角半径,单位为dp, 默认10dp
     */
    fun loadImgWithCircleRadius(iv: ImageView, @DrawableRes path: Int, radius: Float = 10F) {
        Glide.with(iv)
            .load(path)
            .apply(
                options
                    .transform(
                        CenterCrop(),
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
    fun loadImgWithCircleAndRing(
        iv: ImageView,
        path: String,
        borderWidth: Float = 5F,
        @ColorInt borderColor: Int = Color.WHITE
    ) {
        Glide.with(iv)
            .load(path)
            .apply(
                RequestOptions.bitmapTransform(
                    CircleBorderTransform(
                        DensityUtil.dp2pxRtFloat(
                            borderWidth
                        ), borderColor
                    )
                )
            )
            .into(iv)
    }

    /**
     * 加载带圆环的圆形图片
     * @param drawableId --> 图片资源地址
     * @param borderWidth --> 圆环的厚度,单位为dp, 默认5dp
     * @param borderColor --> 圆环的颜色,默认白色
     */
    fun loadImgWithCircleAndRing(
        iv: ImageView,
        @DrawableRes drawableId: Int,
        borderWidth: Float = 5F,
        @ColorInt borderColor: Int = Color.WHITE
    ) {
        Glide.with(iv)
            .load(drawableId)
            .apply(
                RequestOptions.bitmapTransform(
                    CircleBorderTransform(
                        DensityUtil.dp2pxRtFloat(
                            borderWidth
                        ), borderColor
                    )
                )
            )
            .into(iv)
    }

    /**
     * 加载带高斯模糊效果
     * @param path --> 图片路径
     */
    fun loadImgWithGaussianBlur(iv: ImageView, path: String) {
        Glide.with(iv)
            .load(path)
            .apply(RequestOptions.bitmapTransform(GlideBlurTransformation(iv.context)))
            .into(iv)
    }


    /**
     * 下载图片
     * @param path --> 图片路径
     * @param target --> 下载的回调
     */
    fun downloadImage(context: Context, path: String, target: CustomTarget<Bitmap>) {
        Glide.with(context)
            .asBitmap()
            .load(path)
            .into(target)
    }

}

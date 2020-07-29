package cn.lvsong.lib.library.other

import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Util
import java.nio.ByteBuffer
import java.security.MessageDigest


/**
 * 可以在加载圆形图片时,给其外侧设置一个圆环
 */
class CircleBorderTransform(private val borderWidth: Float, private val borderColor: Int) :
    BitmapTransformation() {
    private val ID = javaClass.name
    override fun transform(
        pool: BitmapPool,
        source: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        // 目标直径
        val destMinEdge = outWidth.coerceAtMost(outHeight)
        // 目标半径 & 中心点坐标
        val radius = destMinEdge / 2f
        // 修正源宽高
        val srcWidth = source.width
        val srcHeight = source.height
        val scaleX = (destMinEdge - borderWidth * 2) / srcWidth.toFloat()
        val scaleY = (destMinEdge - borderWidth * 2) / srcHeight.toFloat()
        val maxScale = scaleX.coerceAtLeast(scaleY)
        val scaledWidth = maxScale * srcWidth
        val scaledHeight = maxScale * srcHeight
        // 源绘制起始坐标
        val left = (destMinEdge - scaledWidth) / 2f
        val top = (destMinEdge - scaledHeight) / 2f
        // 新建画布
        val outBitmap = pool[destMinEdge, destMinEdge, Bitmap.Config.ARGB_8888]
        val canvas = Canvas(outBitmap)
        // 绘制内圆
        val srcPaint =
            Paint(Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)
        val destRect = RectF(left, top, left + scaledWidth, top + scaledHeight)
        // 绘制内部小圆形
        canvas.drawCircle(radius, radius, radius - borderWidth, srcPaint)
        srcPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(source, null, destRect, srcPaint)
        // 绘制外圆
        val borderPaint = Paint(Paint.DITHER_FLAG or Paint.ANTI_ALIAS_FLAG)
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.FILL
        borderPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
        canvas.drawCircle(radius, radius, radius, borderPaint)
        return outBitmap
    }

   override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID.toByteArray(Key.CHARSET))
        val radiusData: ByteArray =
            ByteBuffer.allocate(java.lang.Float.BYTES * 2).putFloat(borderWidth)
                .putFloat(borderColor.toFloat()).array()
        messageDigest.update(radiusData)
    }

    override fun equals(o: Any?): Boolean {
        return if (o is CircleBorderTransform) {
            borderColor == o.borderColor && borderWidth == o.borderWidth
        } else false
    }

    override fun hashCode(): Int {
        var hashcode: Int = Util.hashCode(borderWidth)
        hashcode = Util.hashCode(borderColor, hashcode)
        hashcode = Util.hashCode(ID.hashCode(), hashcode)
        return hashcode
    }

}
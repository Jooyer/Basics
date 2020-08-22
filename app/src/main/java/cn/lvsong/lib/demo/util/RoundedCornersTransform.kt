package cn.lvsong.lib.demo.util

import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Util
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.*


/**
 * 四个角可以单独设置大小的 BitmapTransformation
 */
class RoundedCornersTransform(tl: Float, tr: Float, br: Float, bl: Float) :
    BitmapTransformation() {
    private val ID = javaClass.name
    private val radius: FloatArray = FloatArray(4)
    override fun transform(
        pool: BitmapPool,
        source: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val outBitmap =
            pool[source.width, source.height, Bitmap.Config.ARGB_8888]
        val canvas = Canvas(outBitmap)
        val paint = Paint()
        //关联画笔绘制的原图bitmap
        val shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true
        val rectF = RectF(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat())
        // 左上
        var r = radius[0]
        canvas.save()
        canvas.clipRect(0, 0, canvas.width / 2, canvas.height / 2)
        canvas.drawRoundRect(rectF, r, r, paint)
        canvas.restore()
        // 右上
        r = radius[1]
        canvas.save()
        canvas.clipRect(canvas.width / 2, 0, canvas.width, canvas.height / 2)
        canvas.drawRoundRect(rectF, r, r, paint)
        canvas.restore()
        // 右下
        r = radius[2]
        canvas.save()
        canvas.clipRect(
            canvas.width / 2,
            canvas.height / 2,
            canvas.width,
            canvas.height
        )
        canvas.drawRoundRect(rectF, r, r, paint)
        canvas.restore()
        // 左下
        r = radius[3]
        canvas.save()
        canvas.clipRect(0, canvas.height / 2, canvas.width / 2, canvas.height)
        canvas.drawRoundRect(rectF, r, r, paint)
        canvas.restore()
        return outBitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID.toByteArray(Key.CHARSET))
        val radiusData: ByteArray =
            ByteBuffer.allocate(4).putInt(radius.contentHashCode()).array()
        messageDigest.update(radiusData)
    }

    override fun equals(o: Any?): Boolean {
        return if (o is RoundedCornersTransform) {
            Arrays.equals(radius, o.radius)
        } else false
    }

    override fun hashCode(): Int {
        return Util.hashCode(ID.hashCode(), radius.contentHashCode())
    }

    /**
     * 构造方法
     *
     * @param tl 左上
     * @param tr 右上
     * @param br 右下
     * @param bl 左下
     */
    init {
        radius[0] = tl
        radius[1] = tr
        radius[2] = br
        radius[3] = bl
    }
}
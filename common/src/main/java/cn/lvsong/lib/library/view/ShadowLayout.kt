package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import cn.lvsong.lib.library.R
import kotlin.math.abs

/** 来自: https://www.jianshu.com/p/ddcf1d2f32d2
 * Desc: 实现 Android 的 ShadowLayout
 * Author: Jooyer
 * Date: 2020-03-04
 * Time: 17:54
 */

/* 用法:  注意--> shadow_layout_shadow_color值必须是8位,即有alpha
<cn.lvsong.lib.library.view.ShadowLayout
        android:layout_width="match_parent"
        android:layout_height="@dimen/height_130"
        android:paddingStart="@dimen/padding_5"
        android:paddingEnd="@dimen/padding_5"
        app:shadow_layout_background_color="#FFFFFF"
        app:shadow_layout_shadow_color="#D366BB6A"
        app:shadow_layout_offsetX="0dp"
        app:shadow_layout_offsetY="2dp"
        app:shadow_layout_radius="5dp"
        app:shadow_layout_blur="5dp"
        >
 */
class ShadowLayout : ConstraintLayout {

    /**
     * 阴影颜色
     */
    private var mShadowColor: Int = 0
    /**
     * 阴影圆角
     */
    private var mShadowRadius: Float = 0.toFloat()
    /**
     * ShadowLayout圆角
     */
    private var mCornerRadius: Float = 0.toFloat()
    /**
     * 阴影水平偏移
     */
    private var mDx: Float = 0.toFloat()
    /**
     * 阴影垂直偏移
     */
    private var mDy: Float = 0.toFloat()
    /**
     * 背景颜色
     */
    private var mBackgroundColor: Int = 0

    private var mInvalidateShadowOnSizeChanged = true
    private var mForceInvalidateShadow = false

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0 && (background == null || mInvalidateShadowOnSizeChanged || mForceInvalidateShadow)) {
            mForceInvalidateShadow = false
            setBackgroundCompat(w, h)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (mForceInvalidateShadow) {
            mForceInvalidateShadow = false
            setBackgroundCompat(right - left, bottom - top)
        }
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        initAttributes(context, attrs)
        refreshPadding()
    }

    private fun refreshPadding() {
        val xPadding = (mShadowRadius + Math.abs(mDx)).toInt()
        val yPadding = (mShadowRadius + Math.abs(mDy)).toInt()
        setPadding(xPadding, yPadding, xPadding, yPadding)
    }

    private fun setBackgroundCompat(w: Int, h: Int) {
        val bitmap = createShadowBitmap(
            w,
            h,
            mCornerRadius,
            mShadowRadius,
            mDx,
            mDy,
            mShadowColor,
            Color.TRANSPARENT
        )
        val drawable = BitmapDrawable(resources, bitmap)
        background = drawable
    }


    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout)
            mCornerRadius = attr.getDimension(R.styleable.ShadowLayout_sl_layout_radius, 0f)
            mShadowRadius = attr.getDimension(R.styleable.ShadowLayout_sl_shadow_radius, 0f)
            mDx = attr.getDimension(R.styleable.ShadowLayout_sl_offset_x, 0f)
            mDy = attr.getDimension(R.styleable.ShadowLayout_sl_offset_y, 0f)
            mShadowColor = attr.getColor(
                R.styleable.ShadowLayout_sl_shadow_color,
                Color.parseColor("#22000000")
            )
            mBackgroundColor = attr.getColor(
                R.styleable.ShadowLayout_sl_background_color,
                Integer.MIN_VALUE
            )
            attr.recycle()
        }
    }

    private fun createShadowBitmap(
        shadowWidth: Int, shadowHeight: Int, cornerRadius: Float, shadowRadius: Float,
        dx: Float, dy: Float, shadowColor: Int, fillColor: Int
    ): Bitmap {
        val output: Bitmap = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val shadowRect = RectF(
            shadowRadius,
            shadowRadius,
            shadowWidth - shadowRadius,
            shadowHeight - shadowRadius
        )

        if (dy > 0) {
            shadowRect.top += dy
            shadowRect.bottom -= dy
        } else if (dy < 0) {
            shadowRect.top += abs(dy)
            shadowRect.bottom -= abs(dy)
        }

        if (dx > 0) {
            shadowRect.left += dx
            shadowRect.right -= dx
        } else if (dx < 0) {
            shadowRect.left += abs(dx)
            shadowRect.right -= abs(dx)
        }

        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = fillColor
        paint.style = Paint.Style.FILL

        // 绘制阴影部分
        paint.setShadowLayer(shadowRadius, dx, dy, shadowColor)
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, paint)

        // 绘制内部部分
        if (mBackgroundColor != Integer.MIN_VALUE) {
            paint.clearShadowLayer()
            paint.color = mBackgroundColor
            val backgroundRect = RectF(
                paddingLeft.toFloat(),
                paddingTop.toFloat(),
                (width - paddingRight).toFloat(),
                (height - paddingBottom).toFloat()
            )
            canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, paint)
        }

        return output
    }


    fun setInvalidateShadowOnSizeChanged(invalidateShadowOnSizeChanged: Boolean) {
        mInvalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged
    }

    /**
     * 设置阴影无效
     */
    fun invalidateShadow() {
        mForceInvalidateShadow = true
        background = null
        requestLayout()
    }

    fun setShadowBackgroundColor(@ColorRes color:Int){
        background = null
        mBackgroundColor = ContextCompat.getColor(context,color)
        requestLayout()
    }

}
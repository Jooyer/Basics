package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import cn.lvsong.lib.library.R

/**
 *
 *  https://www.jianshu.com/p/ddcf1d2f32d2  --> 另一种实现,将阴影作为控件的一部分需要给控件设置一些padding值，才能让阴影显示出来
 *
 * 参考: https://hub.fastgit.org/ZhangHao555/AndroidGroupShadow
 * Desc: 父控件实现 shadow
 * Author: Jooyer
 * Date: 2020-11-22
 * Time: 10:33
 */
class ShadowLayout(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mShadowRectF = RectF()
    private val mAroundRectF = RectF()

    constructor(context: Context) : this(context, null)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val layoutParams = child.layoutParams
            if (layoutParams is LayoutParams) {
                if (layoutParams.mShadowRadius > 0
                    && layoutParams.mShadowColor != Color.TRANSPARENT
                ) {
                    mAroundRectF.left = child.left.toFloat()
                    mAroundRectF.right = child.right.toFloat()
                    mAroundRectF.top = child.top.toFloat()
                    mAroundRectF.bottom = child.bottom.toFloat()

                    mShadowRectF.left = child.left.toFloat()
                    mShadowRectF.right = child.right.toFloat()
                    mShadowRectF.top = child.top.toFloat()
                    mShadowRectF.bottom = child.bottom.toFloat()
                    mPaint.style = Paint.Style.FILL
                    // 绘制阴影
                    mPaint.setShadowLayer(
                        layoutParams.mShadowRadius,
                        layoutParams.mDy,
                        layoutParams.mDx,
                        layoutParams.mShadowColor
                    )
                    mPaint.color = layoutParams.mShadowColor
                    canvas.drawRoundRect(
                        mShadowRectF,
                        layoutParams.mCornerRadius,
                        layoutParams.mCornerRadius,
                        mPaint
                    )

                    // 绘制子控件背景
                    mPaint.clearShadowLayer()
                    mPaint.color = layoutParams.mBackgroundColor
                    canvas.drawRoundRect(
                        mAroundRectF, layoutParams.mCornerRadius,
                        layoutParams.mCornerRadius, mPaint
                    )
                }
            }
        }
    }

    class LayoutParams : ConstraintLayout.LayoutParams {
        var mDy = 0f
        var mDx = 0f
        var mShadowColor = 0
        var mBackgroundColor = 0
        var mShadowRadius = 0f
        var mCornerRadius = 0f

        constructor(source: ViewGroup.LayoutParams?) : super(source) {
            if (source is LayoutParams) {
                mDy = source.getXOffset()
                mDx = source.getYOffset()
                mShadowColor = source.getShadowColor()
                mBackgroundColor = source.getBackgroundColor()
                mShadowRadius = source.getShadowRadius()
                mCornerRadius = source.getCornerRadius()
            }
        }

        constructor(width: Int, height: Int) : super(width, height) {}

        constructor(source: LayoutParams) : super(source) {
            mDy = source.getXOffset()
            mDx = source.getYOffset()
            mShadowColor = source.getShadowColor()
            mBackgroundColor = source.getBackgroundColor()
            mShadowRadius = source.getShadowRadius()
            mCornerRadius = source.getCornerRadius()
        }

        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
            val attributes =
                c.obtainStyledAttributes(attrs, R.styleable.ShadowLayout)
            mDy = attributes.getDimension(
                R.styleable.ShadowLayout_layout_offset_x,
                0f
            )
            mDx = attributes.getDimension(
                R.styleable.ShadowLayout_layout_offset_y,
                0f
            )
            mShadowRadius = attributes.getDimension(
                R.styleable.ShadowLayout_layout_shadow_radius,
                0f
            )
            mShadowColor =
                attributes.getColor(R.styleable.ShadowLayout_layout_shadow_color, 0)
            mBackgroundColor = attributes.getColor(
                R.styleable.ShadowLayout_layout_background_color,
                0
            )
            mCornerRadius = attributes.getDimension(
                R.styleable.ShadowLayout_layout_around_radius,
                0f
            )
            attributes.recycle()
        }

        fun getXOffset(): Float {
            return mDx
        }

        fun setXOffset(xOffset: Float) {
            mDx = xOffset
        }

        fun getYOffset(): Float {
            return mDy
        }

        fun setYOffset(yOffset: Float) {
            mDy = yOffset
        }

        fun getShadowColor(): Int {
            return mShadowColor
        }

        fun setShadowColor(mShadowColor: Int) {
            this.mShadowColor = mShadowColor
        }

        fun getBackgroundColor(): Int {
            return mBackgroundColor
        }

        fun setBackgroundColor(mBackgroundColor: Int) {
            this.mBackgroundColor = mBackgroundColor
        }

        fun getShadowRadius(): Float {
            return mShadowRadius
        }

        fun setShadowRadius(mShadowRadius: Float) {
            this.mShadowRadius = mShadowRadius
        }

        fun getCornerRadius(): Float {
            return mCornerRadius
        }

        fun setCornerRadius(mCornerRadius: Float) {
            this.mCornerRadius = mCornerRadius
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(this.context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    init {
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }
}
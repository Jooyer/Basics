package cn.lvsong.lib.library.banner

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import cn.lvsong.lib.library.utils.DensityUtil
import kotlin.math.max

/**
 * Desc: 水平滑动的指示器
 * Author: Jooyer
 * Date: 2020-11-28
 * Time: 11:17
 */
class IndicatorView1(context: Context) : View(context), Indicator {

    private val mInterpolator = DecelerateInterpolator()

    private val mOffsetDuration = 500L

    /**
     * 指示器数量
     */
    private var mPageCount = 0

    /**
     * 选中指示器位置,默认 0
     */
    private var mSelectedPage = 0

    /**
     * Item滑动偏移量
     */
    private var mScrollOffset = 0F


    /**
     * 默认指示器半径比率
     */
    private var mNormalIndicatorRatio = 1F

    /**
     * 默认指示器半径
     */
    private var mNormalIndicatorRadius = DensityUtil.dp2pxRtInt(4)

    /**
     * 选中指示器比率
     */
    private var mSelectedIndicatorRatio = 1F

    /**
     * 选中指示器半径
     */
    private var mSelectedIndicatorRadius = DensityUtil.dp2pxRtInt(5)

    /**
     * 指示器间隔
     */
    private var mSpacing = DensityUtil.dp2pxRtInt(1)

    /**
     * 指示器画笔
     */
    private val mIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 指示器默认颜色
     */
    @ColorInt
    private var mNormalIndicatorColor = Color.GRAY

    /**
     * 指示器选中颜色
     */
    @ColorInt
    private var mSelectedIndicatorColor = Color.WHITE

    /**
     * 存放指示器每一个位置
     */
    private val mRectF = RectF()

    override fun initIndicatorCount(pageCount: Int) {
        mPageCount = pageCount
        // 只有一页,指示器没有意义
        visibility = if (mPageCount > 1) VISIBLE else GONE
        requestLayout()
    }

    override fun getIndicatorView() = this

    override fun getIndicatorViewLayoutParam(): ConstraintLayout.LayoutParams {
        val indicatorLayoutParam =
            ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        indicatorLayoutParam.startToStart = 0
        indicatorLayoutParam.endToEnd = 0
        indicatorLayoutParam.bottomToBottom = 0
        indicatorLayoutParam.bottomMargin = DensityUtil.dp2pxRtInt(5F)

        return indicatorLayoutParam
    }

    override fun onPageScrolled(scrollOffset: Float) {
//        Log.e(
//            "Indicator",
//            "onPageScrolled========>>>>>scrollOffset: $scrollOffset, mSelectedPage: $mSelectedPage"
//        )
        mScrollOffset = scrollOffset
        invalidate()
    }

    override fun onPageSelected(position: Int) {
//        Log.e(
//            "Indicator",
//            "onPageSelected1========>>>>>position: $position, mSelectedPage: $mSelectedPage ,mScrollOffset: $mScrollOffset"
//        )
        if (position != mSelectedPage) {
            mSelectedPage = position
//            Log.e(
//                "Indicator",
//                "onPageSelected2========>>>>>position: $position, mSelectedPage: $mSelectedPage "
//            )
        } else {
            if (mScrollOffset > 0F) {
                val offsetAnimator = ValueAnimator.ofFloat(mScrollOffset, 0F)
                offsetAnimator.duration = (mOffsetDuration * mScrollOffset).toLong()
                offsetAnimator.addUpdateListener {
                    mScrollOffset = it.animatedValue as Float
                    invalidate()
                }
                offsetAnimator.start()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 告诉父类, 子控件具体大小
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val result: Int

        when (mode) {
            MeasureSpec.EXACTLY -> {
                result = width
            }
            else -> {
                val selectedRadius = mSelectedIndicatorRadius * mSelectedIndicatorRatio
                val normalRadius = mNormalIndicatorRadius * mNormalIndicatorRatio
                val spaceDistance = (mPageCount - 1) * mSpacing
                // 因为 获取半径最大值,所以需要 * 2
                result =
                    (max(
                        selectedRadius,
                        normalRadius
                    ) * 2 * mPageCount + spaceDistance + paddingLeft + paddingRight).toInt()
            }
        }

        return result
    }


    private fun measureHeight(heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)
        val result: Int

        when (mode) {
            MeasureSpec.EXACTLY -> {
                result = size
            }
            else -> {
                val selectedRadius = mSelectedIndicatorRadius * mSelectedIndicatorRatio
                val normalRadius = mNormalIndicatorRadius * mNormalIndicatorRatio
                return (max(selectedRadius, normalRadius) * 2 + paddingTop + paddingBottom).toInt()
            }
        }

        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mPageCount > 0) {
            val midY = height / 2F
            drawPagerCountCircle(canvas, midY)
            drawCurPagerCircle(canvas, midY)
        }
    }

    /**
     * 绘制选中的指示器
     */
    private fun drawCurPagerCircle(canvas: Canvas, midY: Float) {
        mIndicatorPaint.color = mSelectedIndicatorColor
        val startX = indicatorStartX(mSelectedPage)
        val nextIndicatorStartX = indicatorStartX((mSelectedPage + 1) % mPageCount)
        val radius = getSelectedRadius()
        val left = startX - radius
        val right = startX + radius
        val nextIndicatorLeft = nextIndicatorStartX - radius
        val nextIndicatorRight = nextIndicatorStartX + radius
        val leftX =
            left + (nextIndicatorLeft - left) * mInterpolator.getInterpolation(mScrollOffset)
        val rightX =
            right + (nextIndicatorRight - right) * mInterpolator.getInterpolation(mScrollOffset)

        mRectF.set(
            leftX,
            midY - getSelectedRadius(),
            rightX,
            midY + getSelectedRadius()
        )
        canvas.drawRoundRect(mRectF, getSelectedRadius(), getSelectedRadius(), mIndicatorPaint)
    }

    /**
     * 绘制默认的指示器
     * @param midY --> 垂直方向,居中位置
     */
    private fun drawPagerCountCircle(canvas: Canvas, midY: Float) {
        mIndicatorPaint.color = mNormalIndicatorColor
        for (index in 0 until mPageCount) {
            val startX = indicatorStartX(index)
            mRectF.set(
                startX - getNormalRadius(),
                midY - getNormalRadius(),
                startX + getNormalRadius(),
                midY + getNormalRadius()
            )
            canvas.drawRoundRect(mRectF, getNormalRadius(), getNormalRadius(), mIndicatorPaint)
        }
    }

    private fun getNormalRadius() = mNormalIndicatorRadius * mNormalIndicatorRatio

    private fun getSelectedRadius() = mSelectedIndicatorRadius * mSelectedIndicatorRatio

    /**
     *  计算指示器X轴起点
     */
    private fun indicatorStartX(index: Int): Float {
        val selectedRadius = getSelectedRadius()
        val normalRadius = getNormalRadius()
        val perWidth = max(normalRadius, selectedRadius)
        val perSpacing = perWidth * 2 + mSpacing
        // 第一个圆: 在 半径 + paddingLeft 位置
        // 第二个圆: 在 1.5个半径 + paddingLeft + mSpacing 位置
        return paddingLeft + perWidth + perSpacing * index
    }

    /**
     * 设置小球间间隔
     */
    fun setSpacing(spacing: Int): IndicatorView1 {
        mSpacing = spacing
        return this
    }

}
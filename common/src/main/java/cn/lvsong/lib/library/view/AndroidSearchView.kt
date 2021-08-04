package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import cn.lvsong.lib.library.R

/** 参考: https://www.jianshu.com/p/cdbb4dff4cac?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=weixin
 * Desc: Android 自定义View绘制搜索按钮
 * Author: Jooyer
 * Date: 2020-02-28
 * Time: 13:35
 */
class AndroidSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * View默认最小宽度
     */
    private val DEFAULT_MIN_WIDTH = 100

    /**
     * 控件宽
     */
    private var mViewWidth = 0

    /**
     * 控件高
     */
    private var mViewHeight = 0

    /**
     * 画笔
     */
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 圆的半径
     */
    private var mCircleRadius = 0f

    /**
     * 线的长度
     */
    private var mLineLength = 0f

    init {
        mPaint.style = Paint.Style.STROKE

        initAttr(context, attrs, defStyleAttr)
    }


    private fun initAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val array =
            context.obtainStyledAttributes(attrs, R.styleable.AndroidSearchView, defStyleAttr, 0)
        mPaint.color =
            array.getColor(R.styleable.AndroidSearchView_asv_color, Color.argb(255, 0, 0, 0))
        mPaint.strokeWidth = array.getDimension(
            R.styleable.AndroidSearchView_asv_line_width,
            dip2px(context, 1.5f).toFloat()
        )
        array.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        //计算半径
        mCircleRadius = mViewWidth.coerceAtMost(mViewHeight) * 0.35f / 2f
        //计算把柄的长度
        mLineLength = mViewWidth.coerceAtMost(mViewHeight) * 0.18f * 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(mViewWidth / 2f, mViewHeight / 2f)
        canvas.rotate(45f)
        canvas.drawCircle(0f, 0f, mCircleRadius, mPaint)
        canvas.drawLine(mCircleRadius, 0f, mLineLength, 0f, mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(handleMeasure(widthMeasureSpec), handleMeasure(heightMeasureSpec))
    }

    /**
     * 处理MeasureSpec
     */
    private fun handleMeasure(measureSpec: Int): Int {
        var result = DEFAULT_MIN_WIDTH
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            //处理wrap_content的情况
            if (specMode == MeasureSpec.AT_MOST) {
                result = result.coerceAtMost(specSize)
            }
        }
        return result
    }

    private fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }


    fun setColorAndLineWidth(@ColorInt color: Int,lineWidth:Float) {
        mPaint.color = color
        mPaint.strokeWidth = lineWidth
        postInvalidate()
    }

}
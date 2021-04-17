package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import cn.lvsong.lib.library.R

/**
 * https://www.jianshu.com/p/4d611e73d9bb?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=weixin
 * Desc: Android 自定义View绘制关闭按钮
 * Author: Jooyer
 * Date: 2020-02-28
 * Time: 13:33
 */
/* 用法:


 */
class CloseView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    /**
     * 普通模式
     */
    private val MODE_NORMAL = 1

    /**
     * 有圆模式
     */
    private val MODE_CIRCLE = 2

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
     * 线的长度
     */
    private var mLineLength = 0f

    /**
     * 画笔
     */
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 线颜色
     */
    private var mColor = 0

    /**
     * 线宽
     */
    private var mLineWidth = 0f

    /**
     * 内padding
     */
    private var mPadding = 0f

    /**
     * 背景内padding
     */
    private var mBgPadding = 0f

    /**
     * 模式
     */
    private var mMode = 0

    /**
     * 圆的颜色
     */
    private var mCircleColor = 0

    /**
     * 圆的边线宽
     */
    private var mCircleLineWidth = 0f

    /**
     * 控件背景色
     */
    private var mBgColor = Color.TRANSPARENT

    /**
     * 是否绘制背景色
     */
    private var mHasBg = false
    
    init {
        initAttr(context, attrs, defStyleAttr)
        mPaint.color = mColor
        mPaint.style = Paint.Style.STROKE
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = mLineWidth
    }
    

    private fun initAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CloseView, defStyleAttr, 0)
        mColor = array.getColor(R.styleable.CloseView_cv_line_color, Color.DKGRAY)
        mPadding = array.getDimension(R.styleable.CloseView_cv_line_padding, dip2px(context, 4f).toFloat())
        mLineWidth = array.getDimension(R.styleable.CloseView_cv_line_width, dip2px(context, 1.5f).toFloat())
        mMode = array.getInt(R.styleable.CloseView_cv_mode, MODE_NORMAL)
        //如果不指定圆的颜色，颜色和线的颜色一致
        mCircleColor = array.getColor(R.styleable.CloseView_cv_circle_line_color, mColor)
        //如果不指定圆的线宽，则和线的线宽的一致
        mCircleLineWidth = array.getDimension(R.styleable.CloseView_cv_circle_line_width, 0f)
        mHasBg = array.getBoolean(R.styleable.CloseView_cv_circle_has_bg, mHasBg)
        mBgColor = array.getColor(R.styleable.CloseView_cv_circle_bg_color, mBgColor)
        mBgPadding = array.getDimension(R.styleable.CloseView_cv_circle_bg_padding, 0f)
        if (mBgPadding < mCircleLineWidth) {
            mBgPadding = mCircleLineWidth
        }
        array.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mViewWidth = w
        mViewHeight = h
        mLineLength = mViewWidth.coerceAtMost(mViewHeight) * 0.65f / 2f - mPadding
    }

    override fun onDraw(canvas: Canvas) {
        //将画布中心移动到中心点
        canvas.translate(mViewWidth / 2F, mViewHeight / 2F)
        //线旋转45，将十字旋转
        canvas.rotate(45f)
        // 绘制背景
        if (mHasBg && mMode == MODE_CIRCLE) {
            mPaint.style = Paint.Style.FILL
            mPaint.color = mBgColor
            val radius = (mViewWidth - mBgPadding * 2).coerceAtMost(mViewHeight - mBgPadding * 2) / 2F
            canvas.drawCircle(0f, 0f, radius, mPaint)
        }
        //画交叉线
        drawCrossLine(canvas)
        //画圆模式
        if (mMode == MODE_CIRCLE && mCircleLineWidth > 0) { // 如果指定为0 ,则表示不绘制圆环
            drawCircle(canvas)
        }
    }

    /**
     * 画交叉线
     */
    private fun drawCrossLine(canvas: Canvas) {
        val count = 4
        val angle = (360f / count).toInt()
        //画十字
        canvas.save()
        mPaint.style = Paint.Style.STROKE
        mPaint.color = mColor
        mPaint.strokeWidth = mLineWidth
        for (i in 0 until count) {
            //旋转4次，每次画一条线，每次90度，合起来就是一个十字了
            canvas.rotate((angle * i).toFloat())
            canvas.drawLine(0f, 0F, mLineLength, 0F, mPaint)
        }
        canvas.restore()
    }

    /**
     * 画圆环
     */
    private fun drawCircle(canvas: Canvas) {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = mCircleColor
        mPaint.strokeWidth = mCircleLineWidth
        val radius = mViewWidth.coerceAtMost(mViewHeight) * 0.9F / 2F
        canvas.drawCircle(0F, 0F, radius, mPaint)
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

    /**
     * 叉叉的颜色
     * @param color --> 默认#444444
     */
    fun setColor(@ColorInt color: Int) {
        mColor = color
        mPaint.color = mColor
        postInvalidate()
    }

    /**
     * 设置叉叉线的宽度(厚度)
     * @param lineWidth --> 默认1.5dp
     */
    fun setLineWidth(lineWidth: Float) {
        mLineWidth = lineWidth
    }

    /**
     * 使得里面 × 变小,这样不影响点击范围
     * @param padding --> 默认4dp
     */
    fun setLinePadding(padding: Float) {
        mPadding = padding
    }

    /**
     * 设置控件是圆形(此时背景色和圆环才有效果)还是方形
     * @param mode --> 默认方形(1)
     */
    fun setMode(mode: Int) {
        mMode = mode
    }

    /**
     * 设置控件是否拥有背景色
     * @param hasBg --> 默认false
     */
    fun setHasBg(hasBg: Boolean) {
        mHasBg = hasBg
    }

    /**
     * 设置控件背景色
     * @param bgColor --> 默认透明
     */
    fun setBgColor(bgColor: Int) {
        mBgColor = bgColor
    }

    
    
    private fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5F).toInt()
    }


}
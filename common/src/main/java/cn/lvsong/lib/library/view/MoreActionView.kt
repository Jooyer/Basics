package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import cn.lvsong.lib.library.R

/**
 * 参考: https://www.jianshu.com/p/7a61d5714136?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=weixin
 * Desc: Android 自定义View绘制更多操作按钮
 * Author: Jooyer
 * Date: 2020-02-28
 * Time: 13:30
 */
/* 用法:

     <cn.lvsong.lib.library.view.MoreActionView
         android:id="@+id/more_action"
         android:layout_width="40dp"
         android:layout_height="40dp"
         android:layout_marginTop="20dp"
         app:mav_color="@android:color/black"
         app:mav_dot_radius="1.5dp"
         app:mav_orientation="vertical" />

     <cn.lvsong.lib.library.view.MoreActionView
         android:id="@+id/more_action2"
         android:layout_width="40dp"
         android:layout_height="40dp"
         android:layout_marginTop="20dp"
         app:mav_color="@android:color/black"
         app:mav_dot_radius="1.5dp"
         app:mav_orientation="horizontal" />

 */
class MoreActionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * 水平排列
     */
    private val ORIENTATION_HORIZONTAL = 1

    /**
     * 垂直排列
     */
    private val ORIENTATION_VERTICAL = 2

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
     * 点的半径
     */
    private var mDotRadius = 0f

    /**
     * 每个点之间的距离
     */
    private var mDotDistance = 0f

    /**
     * 排列方向
     */
    private var mOrientation = 0


    init {
        initAttr(context, attrs, defStyleAttr)
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = mDotRadius
    }

    private fun initAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val array =
            context.obtainStyledAttributes(attrs, R.styleable.MoreActionView, defStyleAttr, 0)
        mPaint.color  = array.getColor(
            R.styleable.MoreActionView_mav_color,
            ContextCompat.getColor(context, R.color.color_444444)
        )
        mOrientation =
            array.getInt(R.styleable.MoreActionView_mav_orientation, ORIENTATION_HORIZONTAL)
        mDotRadius = array.getDimension(
            R.styleable.MoreActionView_mav_dot_radius,
            dip2px(context, 2f).toFloat()
        )
        array.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        //计算半径
        val radius = Math.min(mViewWidth, mViewHeight) / 2f
        //计算每个点之间的距离，半径的一半，再减去原点的直径
        mDotDistance = radius * 3f / 5f - mDotRadius * 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(mViewWidth / 2f, mViewHeight / 2f)
        //垂直排列
        if (mOrientation == ORIENTATION_VERTICAL) {
            canvas.rotate(90f)
        } else {
            //水平排列
            canvas.rotate(0f)
        }
        //画中心的点
        canvas.drawCircle(0f, 0f, mDotRadius, mPaint)
        //画左侧的点
        canvas.drawCircle(-mDotDistance, 0f, mDotRadius, mPaint)
        //画右侧的点
        canvas.drawCircle(mDotDistance, 0f, mDotRadius, mPaint)
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
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    private fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 设置点大小
     */
    fun setDotRadius(dotRadius: Float):MoreActionView {
        mDotRadius = dotRadius
        postInvalidate()
        return this
    }

    /**
     * 设置方向
     */
    fun setOrientation(orientation: Int):MoreActionView {
        mOrientation = orientation
        postInvalidate()
        return this
    }

    /**
     * 设置点颜色
     */
    fun setColor(@ColorInt color: Int):MoreActionView {
        mPaint.color = color
        return this
    }
}
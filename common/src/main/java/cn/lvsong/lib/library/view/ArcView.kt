package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.R

/**
 *
 * @ProjectName:    android
 * @Package:        cn.lvsong.lib.library.view
 * @ClassName:      ArcView
 * @Description:    弧形背景
 * @Author:         Jooyer
 * @CreateDate:     2020/6/1 14:04
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version:        1.0
 */

/*
 用法

    <com.yayd.live.base.view.ArcView
        android:layout_width="match_parent"
        android:layout_height="@dimen/height_220"
        app:av_bezier_ratio="1.4"
        app:av_color="@color/color_FCF2F2"
        app:av_rectangle_height="@dimen/padding_140"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

 */


class ArcView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRectangleHeight = DensityUtil.dp2pxRtFloat(100F)
    private val mPath = Path()
    /**
     * 贝塞尔曲线y点高度与控件高度的比值,默认是1.0
     */
    private var mRatio = 1F

    init {
        mPaint.style = Paint.Style.FILL
        parse(context, attrs)
    }

    private fun parse(context: Context, attrs: AttributeSet) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.ArcView)
        mRectangleHeight = arr.getDimensionPixelOffset(R.styleable.ArcView_av_rectangle_height, mRectangleHeight.toInt()).toFloat()
        mRatio = arr.getFloat(R.styleable.ArcView_av_bezier_ratio, mRatio)
        val color = arr.getColor(R.styleable.ArcView_av_color, Color.RED)
        arr.recycle()
        mPaint.color = color

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.e("Test", "onSizeChanged=========w: $w =====h: $h =====mRectangleHeight: $mRectangleHeight")
        mPath.moveTo(0F, 0F)
        mPath.lineTo(0F, mRectangleHeight)
        mPath.quadTo(w / 2F, h * mRatio, w.toFloat(), mRectangleHeight)
        mPath.lineTo(w.toFloat(), 0F)
        mPath.close()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(mPath, mPaint)
    }

}
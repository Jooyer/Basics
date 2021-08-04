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

    <cn.lvsong.lib.library.view.ArcView
        android:id="@+id/btn_5"
        android:layout_width="match_parent"
        android:layout_height="@dimen/height_200"
        android:layout_margin="@dimen/padding_5"
        app:av_arc_control_offset="@dimen/padding_20"
        app:av_arc_offset="@dimen/padding_20"
        app:av_background_color="@color/color_2878FF"
        app:av_layout_offset_x="0dp"
        app:av_layout_offset_y="@dimen/padding_2"
        app:av_layout_shadow_color="@color/color_26000000"
        app:av_layout_shadow_radius="@dimen/padding_5"
        />

 */


class ArcView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 曲线与直线交点处 相对于图形高度的偏移量
     */
    private var mArcOffset = 0F

    /**
     * 曲线控制点相对于控件高度偏移量
     */
    private var mArcControlOffset = 0F

    private val mPath = Path()

    init {
        mPaint.style = Paint.Style.FILL
        mShadowPaint.style= Paint.Style.FILL
        parse(context, attrs)
    }

    private fun parse(context: Context, attrs: AttributeSet) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.ArcView)
        mArcOffset = arr.getDimension(R.styleable.ArcView_av_arc_offset, 0F)
        mArcControlOffset = arr.getDimension(R.styleable.ArcView_av_arc_control_offset, 0F)
        val color = arr.getColor(R.styleable.ArcView_av_background_color, Color.WHITE)
        val radius = arr.getDimension(R.styleable.ArcView_av_layout_shadow_radius,0F)
        val dx = arr.getDimension(R.styleable.ArcView_av_layout_offset_x,0F)
        val dy = arr.getDimension(R.styleable.ArcView_av_layout_offset_y,0F)
        val shadowColor = arr.getColor(R.styleable.ArcView_av_layout_shadow_color,0)
        arr.recycle()
        mPaint.color = color
        // 绘制阴影需要的数据
        mShadowPaint.setShadowLayer(radius, dx,dy,shadowColor)
        mShadowPaint.color = shadowColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mPath.reset()
        mPath.moveTo(0F, 0F)
        mPath.lineTo(0F, height - mArcOffset)

        mPath.quadTo(
            width / 2F, height + mArcControlOffset,
            width.toFloat(), height - mArcOffset
        )
        mPath.lineTo(width.toFloat(), height - mArcOffset)
        mPath.lineTo(width.toFloat(), 0F)
        mPath.close()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(mPath,mShadowPaint)
        canvas.drawPath(mPath, mPaint)
    }

}
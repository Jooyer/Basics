package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.R

/**
 * 基线到中线的距离=(Descent+Ascent)/2-Descent
 * @ProjectName:    android
 * @Package:        cn.lvsong.lib.library.view
 * @ClassName:      BadgeView
 * @Description:    角标
 * @Author:         Jooyer
 * @CreateDate:     2020/6/4 11:51
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version:        1.0
 */

/*
  用法
       <cn.lvsong.lib.library.view.BadgeView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="@dimen/padding_5"
        app:bv_background_color="@android:color/holo_red_light"
        app:bv_lr_padding="@dimen/padding_8"
        app:bv_more_style="plus"
        app:bv_number="111"
        app:bv_stoke_width="2dp"
        app:bv_tb_padding="@dimen/padding_5"
        app:bv_text_medium="false"
        app:bv_text_size="@dimen/text_size_14"
        app:bv_stoke_color="@color/main_theme_color"
        />
    />

 */
class BadgeView(context: Context, attr: AttributeSet?) : View(context, attr) {

    constructor(context: Context) : this(context, null)

    /**
     * 背景画笔
     */
    private val mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 轮廓画笔
     */
    private val mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 文本画笔
     */
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 当形状为矩形时,即文本比较长,背景形状
     */
    private val mRectF = RectF()

    /**
     * 当形状为矩形时,即文本比较长,轮廓形状
     */
    private val mStrokeRectF = RectF()

    /**
     * 上下间隔
     */
    private var mTBPadding = DensityUtil.dp2pxRtInt(3F) * 2

    /**
     * 左右间隔
     */
    private var mLRPadding = DensityUtil.sp2pxRtFloat(5F).toInt() * 2

    /**
     * 数值
     */
    private var mNumber = 0

    /**
     * 文字大小
     */
    private var mTextSize = DensityUtil.sp2pxRtFloat(14F)

    /**
     * 轮廓宽度
     */
    private var mStrokeWidth = DensityUtil.dp2pxRtFloat(1F)

    /**
     * >99 时显示分割,或者显示+ (2),默认显示... (1)
     */
    private var mMoreStyle = 1

    init {
        mTextPaint.textSize = mTextSize
        mTextPaint.color = Color.WHITE
        mStrokePaint.style = Paint.Style.STROKE
        mStrokePaint.color = Color.WHITE
        mStrokePaint.strokeWidth = mStrokeWidth
        mBgPaint.style = Paint.Style.FILL
        mBgPaint.color = Color.RED
        parse(context, attr)
    }

    private fun parse(context: Context, attr: AttributeSet?) {
        attr?.let { attrs ->
            val arr = context.obtainStyledAttributes(attrs, R.styleable.BadgeView)
            val medium = arr.getBoolean(R.styleable.BadgeView_bv_text_medium, true)
            mTextPaint.isFakeBoldText = medium
            mTBPadding =
                arr.getDimensionPixelOffset(R.styleable.BadgeView_bv_tb_padding, mTBPadding / 2) * 2
            mLRPadding =
                arr.getDimensionPixelOffset(R.styleable.BadgeView_bv_lr_padding, mLRPadding / 2) * 2
            mStrokeWidth = arr.getDimension(R.styleable.BadgeView_bv_stoke_width, mStrokeWidth)
            mStrokePaint.strokeWidth = mStrokeWidth
            mStrokePaint.color = arr.getColor(R.styleable.BadgeView_bv_stoke_color, Color.WHITE)
            mTextSize =
                arr.getDimensionPixelSize(R.styleable.BadgeView_bv_text_size, mTextSize.toInt())
                    .toFloat()
            mTextPaint.textSize = mTextSize
            mTextPaint.color = arr.getColor(R.styleable.BadgeView_bv_text_color, Color.WHITE)
            mBgPaint.color = arr.getColor(R.styleable.BadgeView_bv_background_color, Color.RED)
            mNumber = arr.getInt(R.styleable.BadgeView_bv_number, mNumber)
            visibility = if (mNumber > 0) VISIBLE else GONE
            mMoreStyle = arr.getInt(R.styleable.BadgeView_bv_more_style, mMoreStyle)
            arr.recycle()

            if(0 == mTBPadding){
                mTBPadding = DensityUtil.dp2pxRtInt(1F)
            }

            if(0 == mLRPadding){
                mLRPadding = DensityUtil.dp2pxRtInt(1F)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val textWidth = mTextPaint.measureText(getShowText()).toInt() + mLRPadding
        val textHeight = (mTBPadding + mTextSize).toInt()
        if (textWidth > textHeight) {
            setMeasuredDimension(textWidth, textHeight)
            mRectF.set(
                0.5F,
                0.5F,
                textWidth - 0.5F,
                textHeight - 0.5F
            )

            mStrokeRectF.set(
                mStrokeWidth / 2,
                mStrokeWidth / 2,
                textWidth - mStrokeWidth / 2,
                textHeight - mStrokeWidth / 2
            )
        } else {
            setMeasuredDimension(textHeight, textHeight)
        }
    }

    override fun onDraw(canvas: Canvas) {
        val textWidth = mTextPaint.measureText(getShowText()).toInt()
        val fontMetrics = mTextPaint.fontMetrics
        val y = (height - fontMetrics.descent - fontMetrics.ascent) / 2 + 1
        if (mNumber < 10) {
            canvas.drawCircle(width / 2F, height / 2F, height / 2F - 0.5F, mBgPaint)
            canvas.drawCircle(width / 2F, height / 2F, (height - mStrokeWidth) / 2F, mStrokePaint)
        } else {
            canvas.drawRoundRect(mRectF, height / 2F, height / 2F, mBgPaint)
            canvas.drawRoundRect(
                mStrokeRectF,
                (height - mStrokeWidth) / 2F,
                (height - mStrokeWidth) / 2F,
                mStrokePaint
            )
        }
        canvas.drawText(getShowText(), (width - textWidth) / 2F, y, mTextPaint)
    }

    private fun getShowText(): String {
        return if (mNumber > 99) {
            if (1 == mMoreStyle) { // 显示 ...
                "99..."
            } else { // 显示 +
                "99+"
            }
        } else {
            "$mNumber"
        }
    }

    /**
     * 设置具体消息数量
     */
    fun setNumber(number: Int) {
        mNumber = number
        invalidate()
    }

}
package cn.lvsong.lib.library.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.R

/**
 * Desc: 广告倒计时
 * Author: Jooyer
 * Date: 2018-11-02
 * Time: 11:17
 */
class CountDownView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    private val mRingPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var mRingWidth = 0F
    private var mSweepAngle = 0F
    private var mInfo = "5s"
    private var mWidth = 0
    private val mTextRect = Rect()

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        mRingPaint.style = Paint.Style.STROKE
        mCirclePaint.style = Paint.Style.FILL
        parse(context, attrs)
    }

    private fun parse(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val arr = context.obtainStyledAttributes(attrs, R.styleable.CountDownView)
            mRingPaint.color = arr.getColor(R.styleable.CountDownView_cdv_ring_color, Color.RED)
            mRingWidth = arr.getDimension(
                R.styleable.CountDownView_cdv_ring_width,
                DensityUtil.dp2pxRtInt(2).toFloat()
            )
            mRingPaint.strokeWidth = mRingWidth
            mCirclePaint.color = arr.getColor(R.styleable.CountDownView_cdv_bg_color, Color.BLUE)
            mTextPaint.color = arr.getColor(R.styleable.CountDownView_cdv_text_color, Color.WHITE)
            mTextPaint.textSize = arr.getDimension(
                R.styleable.CountDownView_cdv_text_size,
                DensityUtil.dp2pxRtInt(24).toFloat()
            )
            arr.getString(R.styleable.CountDownView_cdv_text_info)?.let {
                mInfo = it
            }
            arr.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = Math.min(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        // 内圆
        canvas.drawCircle(mWidth / 2F, mWidth / 2F, mWidth / 2F - mRingWidth / 2, mCirclePaint)

        mTextRect.set(0, 0, 0, 0)
        mTextPaint.getTextBounds(mInfo, 0, mInfo.length, mTextRect)
        val fontMetrics = mTextPaint.fontMetrics
        // 计算文字基线
        val baseLine =
            (mWidth / 2 + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent)
        val x = (mWidth - mTextRect.width()) / 2F
        canvas.drawText(mInfo, x, baseLine, mTextPaint)
        canvas.save()
        canvas.rotate(-90F, mWidth / 2F, mWidth / 2F)
        // 外部圆弧
        canvas.drawArc(
            mRingWidth, mRingWidth, mWidth - mRingWidth, mWidth - mRingWidth,
            0F, mSweepAngle, false, mRingPaint
        )
        canvas.restore()
    }

    /**
     * 开始倒计时
     * @param duration --> 总计多少秒,单位秒
     */
    fun startCountDown(duration: Long) {
        val countDownAnim = ValueAnimator.ofFloat(duration.toFloat(), 0F)
        countDownAnim.duration = duration * 1000
        countDownAnim.interpolator = LinearInterpolator()
        countDownAnim.addUpdateListener {
            val leftTime = it.animatedValue as Float
            if ( duration >= (leftTime + 1).toLong()) { // 文字和初始化一致,如果绘制会发生闪动
                mInfo = "${(leftTime + 1).toInt()}s"
            }
            mSweepAngle = 360 * (duration - leftTime) / duration
            invalidate()
        }

        countDownAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                setTimeLeftZero()
            }
        })

        countDownAnim.start()
    }

    private fun setTimeLeftZero() {
        mInfo = "0s"
        invalidate()
    }


}
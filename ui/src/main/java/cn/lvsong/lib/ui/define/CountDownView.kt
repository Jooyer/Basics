package cn.lvsong.lib.ui.define

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import cn.lvsong.lib.library.utils.DensityUtils
import cn.lvsong.lib.ui.R

/**
 * Desc: 广告倒计时
 * Author: Jooyer
 * Date: 2018-11-02
 * Time: 11:17
 */
class CountDownView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    private val mRingPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var mRingWidth = 0F
    private var mSweepAngle = 0F
    private var mInfo = "5s"
    private var mWidth = 0
    private val mTextRect = Rect()
    private val mCountDownAnim = ValueAnimator.ofFloat(6F, 1F)

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
            mRingWidth = arr.getDimension(R.styleable.CountDownView_cdv_ring_width, DensityUtils.dpToPx(2).toFloat())
            mRingPaint.strokeWidth = mRingWidth

            mCirclePaint.color = arr.getColor(R.styleable.CountDownView_cdv_circle_color, Color.BLUE)

            mTextPaint.color = arr.getColor(R.styleable.CountDownView_cdv_text_color, Color.WHITE)
            mTextPaint.textSize = arr.getDimension(R.styleable.CountDownView_cdv_text_size, DensityUtils.dpToPx(24).toFloat())

            arr.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = Math.min(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 内圆
        canvas.drawCircle(mWidth / 2F, mWidth / 2F, mWidth / 2F - mRingWidth, mCirclePaint)

        // 绘制文字
        mTextRect.set(0, 0, 0, 0)
        mTextPaint.getTextBounds(mInfo, 0, mInfo.length, mTextRect)
        val fontMetrics = mTextPaint.fontMetrics
        // 计算文字基线
        val baseLine = (mWidth / 2 + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent)
        val x = (mWidth - mTextRect.width()) / 2F

        canvas.drawText(mInfo, x, baseLine, mTextPaint)

        canvas.save()
        canvas.rotate(-90F, mWidth / 2F, mWidth / 2F)
        // 外部圆弧
        canvas.drawArc(mRingWidth / 2, mRingWidth / 2, mWidth - mRingWidth / 2, mWidth - mRingWidth / 2,
                0F, mSweepAngle, false, mRingPaint)
        canvas.restore()
    }


    fun startCountDown(duration: Long) {
        mCountDownAnim.duration = duration * 1000
        mCountDownAnim.interpolator = LinearInterpolator()
        mCountDownAnim.addUpdateListener {
            val leftTime = it.animatedValue as Float
            if (leftTime.toInt() <= 5) {
                mInfo = "${leftTime.toInt()}s"
            }
            mSweepAngle = 360 * (6F - leftTime) / duration
            invalidate()
        }

        mCountDownAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                setTimeLeftZero()
            }
        })

        mCountDownAnim.start()
    }

    private fun setTimeLeftZero() {
        mInfo = "0s"
        invalidate()
    }


}
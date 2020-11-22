package cn.lvsong.lib.library.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.renderscript.Sampler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import cn.lvsong.lib.library.R
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * https://www.jianshu.com/p/6fcbba182cd3  (  WifiManager.calculateSignalLevel(level, 5); )
 * Desc: 语音效果
 * Author: Jooyer
 * Date: 2018-08-08
 * Time: 14:44
 */
class VoiceAnimView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val mPaint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    private val mRectF = RectF()

    /**
     * 正常颜色, 默认值 #c9c9c9
     */
    private var mNormalColor = Color.parseColor("#c9c9c9")

    /**
     * 选中颜色, 默认值 #4b4b4b
     */
    private var mCheckedColor = Color.parseColor("#4b4b4b")

    /**
     * 当前动画进度
     */
    private var mState = -1

    /**
     * 弧形线条数量,默认3条
     */
    private var mArcCount = 3

    /**
     * 动画执行时间,默认 1000毫秒
     */
    private var mAnimDuration = 2000

    /**
     * 当前控件是否可见
     */
    private var isVisible: Boolean = false

    /**
     * 控件高度
     */
    private var mViewHeight = 0
    private var bottom_x = 0
    private var bottom_y = 0

    private lateinit var mAnimator: ValueAnimator

    private lateinit var mResetAnimator: ValueAnimator

    /**
     * 形状控制
     */
    enum class Style {
        RECT, ROUND
    }

    private var style = Style.ROUND

    init {
        mPaint.style = Paint.Style.STROKE
        parse(context, attrs)
    }

    private fun parse(
        context: Context,
        attrs: AttributeSet?
    ) {
        if (null != attrs) {
            val array = context.obtainStyledAttributes(
                attrs,
                R.styleable.VoiceAnimView
            )
            mArcCount =
                array.getInt(R.styleable.VoiceAnimView_vav_arc_count, 3)
            mAnimDuration = array.getInt(
                R.styleable.VoiceAnimView_vav_anim_duration,
                mAnimDuration
            )
            mNormalColor = array.getColor(
                R.styleable.VoiceAnimView_vav_normal_color,
                Color.parseColor("#c9c9c9")
            )
            mCheckedColor = array.getColor(
                R.styleable.VoiceAnimView_vav_checked_color,
                Color.parseColor("#4b4b4b")
            )
            array.recycle()
        }
        val arr = IntArray(mArcCount + 1)
        for (i in 0..mArcCount) {
            arr[i] = i
        }

        mAnimator = ValueAnimator.ofInt(*arr)
        mAnimator.duration = mAnimDuration.toLong()
        mAnimator.addUpdateListener { animation ->
            mState = animation.animatedValue as Int
            invalidate()
        }

        mResetAnimator = ValueAnimator.ofInt(mArcCount, mArcCount + 1)
        mResetAnimator.duration = mAnimDuration.toLong() / mArcCount
        mResetAnimator.addUpdateListener { animation ->
            mState = animation.animatedValue as Int
            invalidate()
        }


        mAnimator.addListener(object :AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                if (isVisible){
                    mResetAnimator.start()
                }
            }
        })

        mResetAnimator.addListener(object :AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                if (isVisible){
                    mAnimator.start()
                }
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val height = resolveSize(heightSize, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val width = resolveSize(widthSize, widthMeasureSpec)
        val calc_wifi_height = (width / sqrt(2.0)).toInt()
        mViewHeight = calc_wifi_height.coerceAtMost(height)
        val padding_bottom = (height - mViewHeight) / mArcCount
        bottom_x = width / 2
        bottom_y = height - padding_bottom
        setMeasuredDimension(width, height)
        mPaint.strokeWidth = mViewHeight / 8f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var startAngle: Int
        var sweepAngle: Int
        mRectF.setEmpty()
        for (i in 0 until mArcCount) {
            val radius = mViewHeight / mArcCount * (i + 1)
            if (style == Style.RECT) {
                mPaint.strokeCap = Paint.Cap.BUTT
                startAngle = -135 + 5 * i
                sweepAngle = abs(startAngle + 90) * 2
            } else {
                mPaint.strokeCap = Paint.Cap.ROUND
                startAngle = -130 + 5 * i
                sweepAngle = abs(startAngle + 90) * 2
            }

            if (mArcCount == mState) { // 通过这个数,类似reset,重置界面
                mPaint.color = mNormalColor
            } else {
                if (i > mState) {
                    mPaint.color = mNormalColor
                } else {
                    mPaint.color = mCheckedColor
                }
            }
            mRectF[bottom_x - radius.toFloat(), bottom_y - radius.toFloat(), bottom_x + radius.toFloat()] =
                bottom_y + radius.toFloat()
            canvas.drawArc(mRectF, startAngle.toFloat(), sweepAngle.toFloat(), false, mPaint)

            ///////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////   下面是 WIFI 效果    ////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////

//            if (1 == i) {
//                paint.setStyle(Paint.Style.FILL);
//                if (style == Style.RECT) {
//
//                    mRectF.set(bottom_x - radius,
//                            bottom_y - radius,
//                            bottom_x + radius,
//                            bottom_y + radius);
//                    canvas.drawArc(mRectF, startAngle, sweepAngle, true, paint);
//                } else {
//                    canvas.drawCircle(bottom_x, bottom_y - mViewHeight / 8F, mViewHeight / 8F, paint);
//                }
//
//            } else {
//                paint.setStyle(Paint.Style.STROKE);
//                mRectF.set(bottom_x - radius + mViewHeight / 16F,
//                        bottom_y - radius + mViewHeight / 16F,
//                        bottom_x + radius - mViewHeight / 16F,
//                        bottom_y + radius - mViewHeight / 16F);
//                canvas.drawArc(mRectF, startAngle, sweepAngle, false, paint);
//            }
        }
    }


    override fun onVisibilityAggregated(isVisible: Boolean) {
        super.onVisibilityAggregated(isVisible)
        this.isVisible = isVisible
    }

    /**
     * 设置样式
     *
     * @param style --> Style.ROUND(圆角), Style.RECT(直角)
     */
    fun setStyle(style: Style) {
        this.style = style
        postInvalidate()
    }

    fun startAnim() {
        if (!mAnimator.isRunning) {
            mAnimator.start()
        }
    }

    fun stopAnim() {
        mAnimator.cancel()
        mResetAnimator.cancel()
    }

}
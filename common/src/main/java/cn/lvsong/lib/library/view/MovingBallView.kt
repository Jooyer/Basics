package cn.lvsong.lib.library.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.R

/** https://www.jianshu.com/p/54b458d54ef8
 *
 * @ProjectName:    android
 * @Package:        cn.lvsong.lib.library.view
 * @ClassName:      MovingSmallBallView
 * @Description:    可移动的小球
 * @Author:         Jooyer
 * @CreateDate:     2020/6/19 10:27
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version:        1.0
 */
class MovingBallView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    /**
     * 进度动画
     */
    private val mMovingAnimator = ValueAnimator.ofFloat(0F, 360F)

    /**
     * 背景线条画笔
     */
    private val mBgLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 走过的线条画笔
     */
    private val mMovedLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 中间实心圆画笔
     */
    private val mSolidCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 白色画笔
     */
    private val mWhitePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 中间实心圆渐变角度, 类似 android:angle="180" 作用
     */
    private var meGradientAngle = 0F

    /**
     * 小球画笔
     */
    private val mBallPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 小球半径
     */
    private var mBallRadius = DensityUtil.dp2pxRtFloat(10F)

    /**
     * 中间实心圆半径
     */
    private var mSolidCircleRadius = 0F

    /**
     * 当前走过的角度
     */
    private var mCurAngle = -1F

    /**
     * 渐变起始颜色
     */
    private var mMovedLienStartColor = Color.RED

    /**
     * 渐变结束颜色
     */
    private var mMovedLienEndColor = Color.RED

    /**
     * 控件的宽度
     */
    private var mWidth = 0

    /**
     * 背景圆环,渐变圆环,小球三者与圆心的距离
     */
    private var mAllRingRadius = 0F

    private var mTriangleLength = 0F
    private var mRoundedCorner = 50F
    private var mQuadrilateralLength = 0F

    private var mRecorderWidth = 0F

    /**
     * 录制状态
     *  1 --> 未录制
     *  2 --> 录制中(播放中)
     *  3 --> 准备播放
     */
    private var mRecordState = 1

    private val mReadyPlayPath = Path()

    private val mQuadrilateralPath = Path()

    init {
        mBgLinePaint.style = Paint.Style.STROKE
        mMovedLinePaint.style = Paint.Style.STROKE
        mSolidCirclePaint.style = Paint.Style.FILL
        mBallPaint.style = Paint.Style.FILL
        mWhitePaint.style = Paint.Style.FILL
        mWhitePaint.color = Color.WHITE

        mMovingAnimator.interpolator = LinearInterpolator()
        mMovingAnimator.addUpdateListener {
            mCurAngle = it.animatedValue as Float
            invalidate()
        }
//        mMovingAnimator.addListener(object :AnimatorListenerAdapter(){
//            override fun onAnimationEnd(animation: Animator?) {
//                Log.e("MovingBallView","onAnimationEnd==========")
//            }
//
//            override fun onAnimationCancel(animation: Animator?) {
//                Log.e("MovingBallView","onAnimationCancel==========")
//            }
//        })

        parse(context, attrs)
    }

    private fun parse(context: Context, attrs: AttributeSet) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.MovingBallView)
        val bgLineColor = arr.getColor(R.styleable.MovingBallView_mbv_bg_line_color, Color.GRAY)
        val bgLineWidth = arr.getDimension(R.styleable.MovingBallView_mbv_bg_line_width, DensityUtil.dp2pxRtFloat(5F))
        mMovedLienStartColor = arr.getColor(R.styleable.MovingBallView_mbv_moved_line_start_color, Color.RED)
        mMovedLienEndColor = arr.getColor(R.styleable.MovingBallView_mbv_moved_line_end_color, Color.RED)
        val movedLineWidth = arr.getDimension(R.styleable.MovingBallView_mbv_moved_line_width, DensityUtil.dp2pxRtFloat(6F))
        val ballColor = arr.getColor(R.styleable.MovingBallView_mbv_ball_color, Color.RED)
        mMovingAnimator.duration = arr.getInt(R.styleable.MovingBallView_mbv_ball_duration, 30000).toLong()
        mBallRadius = arr.getDimension(R.styleable.MovingBallView_mbv_ball_radius, mBallRadius)
        mSolidCircleRadius = arr.getDimension(R.styleable.MovingBallView_mbv_solid_circle_radius, 0F)
        meGradientAngle = arr.getFloat(R.styleable.MovingBallView_mbv_solid_circle_gradient_angle, 0F)
        mTriangleLength = arr.getDimension(R.styleable.MovingBallView_mbv_triangle_length, 0F)
        mQuadrilateralLength = arr.getDimension(R.styleable.MovingBallView_mbv_quadrilateral_length, 0F)
        mRoundedCorner = arr.getDimension(R.styleable.MovingBallView_mbv_round_corner, DensityUtil.dp2pxRtFloat(5F))
        mRecorderWidth = arr.getDimension(R.styleable.MovingBallView_mbv_microphone_width, DensityUtil.dp2pxRtFloat(20F))
        mBgLinePaint.color = bgLineColor
        mBgLinePaint.strokeWidth = bgLineWidth
        mMovedLinePaint.strokeWidth = movedLineWidth
        mBallPaint.color = ballColor

        arr.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = if (w > h) w else h
        mAllRingRadius = mWidth / 2F - mBallRadius

        val movedLineGradient = LinearGradient(mWidth / 2F, 0F, mWidth / 2F, mWidth / 1F,
                mMovedLienStartColor, mMovedLienEndColor, Shader.TileMode.CLAMP)
        mMovedLinePaint.shader = movedLineGradient
        val solidCircleGradient = LinearGradient(0F, 0F, mWidth / 1F, mWidth / 1F,
                mMovedLienStartColor, mMovedLienEndColor, Shader.TileMode.CLAMP)
        // 渐变方向可以发生改变
        val matrix = Matrix()
        matrix.setRotate(meGradientAngle, mWidth / 2F, mWidth / 2F)
        solidCircleGradient.setLocalMatrix(matrix)
        mSolidCirclePaint.shader = solidCircleGradient
        if (mTriangleLength > 0) {
            val offset = (Math.sqrt(Math.pow(mTriangleLength * 1.0, 2.0) - Math.pow(mTriangleLength / 2.0, 2.0)) / 2).toFloat()
            mReadyPlayPath.moveTo(mWidth / 2F, mWidth / 2F)
            // 左上
            mReadyPlayPath.lineTo(mWidth / 2F - offset, mWidth / 2F - mTriangleLength / 2)
            mReadyPlayPath.arcTo(mWidth / 2F - offset,
                    mWidth / 2F - mTriangleLength / 2,
                    mWidth / 2F - offset + mRoundedCorner,
                    mWidth / 2F - mTriangleLength / 2 + mRoundedCorner,
                    180F, 120F, false)
            // 右中
//            mReadyPlayPath.lineTo(mWidth / 2F + offset, mWidth / 2F)
            mReadyPlayPath.arcTo(mWidth / 2F + offset - mRoundedCorner,
                    mWidth / 2F - mRoundedCorner / 2,
                    mWidth / 2F + offset,
                    mWidth / 2F + mRoundedCorner / 2,
                    -73F, 140F, false)
            // 左下
//            mReadyPlayPath.lineTo(mWidth / 2F - offset, mWidth / 2F + mTriangleLength / 2)
            mReadyPlayPath.arcTo(mWidth / 2F - offset,
                    mWidth / 2F + mTriangleLength / 2 - mRoundedCorner,
                    mWidth / 2F - offset + mRoundedCorner,
                    mWidth / 2F + mTriangleLength / 2,
                    60F, 120F, false)

            mReadyPlayPath.lineTo(mWidth / 2F - offset, mWidth / 2F - mTriangleLength / 2)
        }
        if (mQuadrilateralLength > 0) {
            mQuadrilateralPath.addRoundRect(mWidth / 2 - mQuadrilateralLength / 2,
                    mWidth / 2 - mQuadrilateralLength / 2,
                    mWidth / 2 + mQuadrilateralLength / 2,
                    mWidth / 2 + mQuadrilateralLength / 2,
                    mRoundedCorner, mRoundedCorner,
                    Path.Direction.CCW
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        mWhitePaint.style = Paint.Style.FILL
        // 背景圆环
        canvas.drawCircle(mWidth / 2F, mWidth / 2F, mAllRingRadius, mBgLinePaint)
        // 中间渐变实心圆
        if (mSolidCircleRadius > 0F) {
            canvas.drawCircle(mWidth / 2F, mWidth / 2F, mSolidCircleRadius, mSolidCirclePaint)
        }

        if (mCurAngle >= 0) {
            // 圆弧进度
            canvas.drawArc(mBallRadius,
                    mBallRadius,
                    mWidth - mBallRadius,
                    mWidth - mBallRadius,
                    -90F, mCurAngle, false, mMovedLinePaint)

            // 小球 ,弧度 = 角度 x π/180 , 角度 = 弧度 x 180/π
            canvas.drawCircle((mWidth / 2F + (mAllRingRadius) * Math.sin(mCurAngle * Math.PI / 180)).toFloat(),
                    (mWidth / 2F - (mAllRingRadius) * Math.cos(mCurAngle * Math.PI / 180)).toFloat(),
                    mBallRadius, mBallPaint
            )
        }
        when (mRecordState) {
            1 -> { // 未录制
                canvas.drawRoundRect((mWidth - mRecorderWidth * 0.7F) / 2,
                        (mWidth - mRecorderWidth) / 2,
                        (mWidth + mRecorderWidth * 0.7F) / 2,
                        (mWidth + mRecorderWidth) / 2,
                        mRecorderWidth * 0.7F / 2,
                        mRecorderWidth * 0.7F / 2,
                        mWhitePaint
                )
                mWhitePaint.style = Paint.Style.STROKE
                mWhitePaint.strokeCap = Paint.Cap.ROUND
                mWhitePaint.strokeWidth = mRecorderWidth * 0.1F
                canvas.drawArc((mWidth - mRecorderWidth * 1.1F) / 2,
                        (mWidth - mRecorderWidth) / 2,
                        (mWidth + mRecorderWidth * 1.1F) / 2,
                        (mWidth + mRecorderWidth * 1.4F) / 2,
                        5F,
                        170F,
                        false,
                        mWhitePaint
                )
                canvas.drawLine(mWidth / 2F,
                        (mWidth + mRecorderWidth * 1.4F) / 2,
                        mWidth / 2F,
                        (mWidth + mRecorderWidth * 2F) / 2, mWhitePaint)

            }
            2 -> { // 录制中(播放中)
                canvas.drawPath(mQuadrilateralPath, mWhitePaint)
            }
            else -> { // 准备播放
                canvas.drawPath(mReadyPlayPath, mWhitePaint)
            }
        }
    }

    fun startRecord() {
        mRecordState = 2
        mMovingAnimator.start()
    }


    fun stopRecord() {
        // 需记录原本具体录制的时长
        mRecordState = 3
        mMovingAnimator.cancel()
        invalidate()
    }

    fun playRecord(){
        startRecord()
    }

    fun resetRecord() {
        mRecordState = 1
        mCurAngle = -1F
        mMovingAnimator.cancel()
        invalidate()
    }


}
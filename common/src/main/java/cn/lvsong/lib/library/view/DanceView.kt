package cn.lvsong.lib.library.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import cn.lvsong.lib.library.R

/**
 *
 * @ProjectName:    MVVMTest
 * @Package:        cn.lvsong.lib.library.view
 * @ClassName:      DanceView
 * @Description:    跳动的柱子
 * @Author:         Jooyer
 * @CreateDate:     2020/5/29 11:23
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version:        1.0
 */

/*
    <cn.lvsong.lib.library.view.DanceView
        android:id="@+id/dv_tips_item_people"
        android:layout_width="@dimen/width_18"
        android:layout_height="@dimen/height_18"
        android:layout_marginStart="@dimen/padding_40"
        android:layout_marginBottom="@dimen/padding_30"
        app:dv_height_ratio="0.7"
        app:dv_pillar_color="@color/color_FE2864"
        app:dv_pillar_count="4"
        app:dv_pillar_duration="800"
        app:dv_space_ratio="0.5"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

 */

class DanceView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    constructor(context: Context) : this(context, null)

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mAnimatorSet = AnimatorSet()
    private val mRectangles = ArrayList<RectF>()

    /**
     * 柱子数量,默认为4
     */
    private var mPillarCount = 4

    /**
     * 动画执行时间
     */
    private var mDuration = 1000L

    /**
     * 除开第一个之后每一个的延迟时间
     */
    private var mDelayDuration = 250L

    /**
     * 柱子的宽度
     */
    private var mPillarWidth = 0F

    /**
     * 柱子的高度
     */
    private var mPillarHeight = 0F

    /**
     * 空白间隔与柱子宽的比率
     */
    private var mSpaceRatio = 0.8F

    /**
     * 柱子高度与控件高度的比率
     */
    private var mHeightRatio = 0.8F

    /**
     * 柱子上下间隔
     */
    private var mSpaceTop = 0F

    /**
     * 留白间隔大小
     */
    private var mSpaceWidth = 0F

    /**
     * 当前控件是否可见
     */
    private var isVisible:Boolean = false

    /**
     * 当前控件动画是否正在执行
     */
    private var isAnimation:Boolean = false

    init {
        mPaint.style = Paint.Style.FILL
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.color = Color.RED
        initParse(context, attrs)
    }

    private fun initParse(context: Context, attrs: AttributeSet?) {
        attrs?.let { attr ->
            val arr = context.obtainStyledAttributes(attr, R.styleable.DanceView)
            mPillarCount = arr.getInt(R.styleable.DanceView_dv_pillar_count, 4)
            mDuration = arr.getInt(R.styleable.DanceView_dv_pillar_duration, 1000).toLong()
            mDelayDuration = mDuration / mPillarCount
            mSpaceRatio = arr.getFloat(R.styleable.DanceView_dv_space_ratio, 0.8F)
            mHeightRatio = arr.getFloat(R.styleable.DanceView_dv_height_ratio, 0.8F)
            val pillarColor = arr.getColor(R.styleable.DanceView_dv_pillar_color, Color.RED)
            mPaint.color = pillarColor
            arr.recycle()

            for (i in 0 until mPillarCount) {
                mRectangles.add(RectF())
            }
        }
    }

    /**
     * 计算柱子宽度
     * mPillarCount * mPillarWidth + mSpaceRatio * mPillarWidth * (mPillarCount + 1) = w * 1F
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mPillarWidth = w / (mPillarCount + mSpaceRatio * (mPillarCount + 1))
        mSpaceWidth = mPillarWidth * mSpaceRatio
        mPillarHeight = h * mHeightRatio
        mSpaceTop = (h - mPillarHeight) / 2F
        initAnimators()
    }

    override fun onVisibilityAggregated(isVisible: Boolean) {
        super.onVisibilityAggregated(isVisible)
        this.isVisible = isVisible
        if (isAnimation && !isVisible){
            cancelAnimator()
        }else if (!isAnimation && isVisible){
            startAnimator()
        }
    }

    override fun onDraw(canvas: Canvas) {
        mRectangles.forEach {
            canvas.drawRoundRect(it, mPillarWidth / 2, mPillarWidth / 2, mPaint)
        }
    }

    private fun initAnimators() {
        val list = ArrayList<Animator>()
        for (i in 0 until mPillarCount) {
            val animator = ValueAnimator.ofFloat(0F, 1F)
            animator.duration = mDuration
            animator.addUpdateListener {
                val percent = it.animatedValue as Float
                mRectangles[i].set(
                    mSpaceWidth * (i + 1) + mPillarWidth * i,
                    mPillarHeight * (1F - percent) + mSpaceTop,
                    (mPillarWidth + mSpaceWidth) * (i + 1),
                    mPillarHeight + mSpaceTop
                )
                invalidate()
            }
            animator.startDelay = mDelayDuration * i
            animator.repeatMode = ValueAnimator.REVERSE
            animator.repeatCount = ValueAnimator.INFINITE

            list.add(animator)
        }
        mAnimatorSet.playTogether(list)
        if (isVisible) {
            startAnimator()
        }else{
            cancelAnimator()
        }
    }

    /**
     * 开始动画
     */
    fun startAnimator() {
        mAnimatorSet.start()
        isAnimation = true
    }

    /**
     * 取消动画
     */
    fun cancelAnimator(){
        mAnimatorSet.cancel()
        isAnimation = false
    }

}
package cn.lvsong.lib.library.bubble

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.LinearLayout
import cn.lvsong.lib.library.R

/**
 * Desc: IM 聊天气泡效果, 弹框带箭头的气泡效果
 * Author: Jooyer
 * Date: 2018-08-15
 * Time: 10:02
 */

/*

<cn.lvsong.lib.ui.define.BubbleLinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:arrowPosition="@dimen/padding_50"
        app:angle="@dimen/padding_3"
        app:arrowHeight="@dimen/padding_8"
        app:arrowLocation="top_right"
        app:arrowWidth="@dimen/width_10"
        app:bubbleColor="@color/color_write"
>

 */

class BubbleLinearLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private var mArrowWidth: Float = 0.toFloat()
    private var mAngle: Float = 0.toFloat()
    private var mArrowHeight: Float = 0.toFloat()
    private var mArrowPosition: Float = 0.toFloat()
    private var mArrowLocation: BubbleDrawable.ArrowLocation = BubbleDrawable.ArrowLocation.LEFT
    private var mBubbleColor: Int = 0
    private var mArrowCenter: Boolean = false
    private val mRectF = RectF()

    constructor(context: Context) : this(context, null)

    init {
        attrs?.let {
            val array = context.obtainStyledAttributes(attrs, R.styleable.BubbleLinearLayout)
            mArrowWidth = array.getDimension(
                R.styleable.BubbleLinearLayout_arrowWidth,
                BubbleDrawable.Builder.DEFAULT_ARROW_WITH
            )
            mArrowHeight = array.getDimension(
                R.styleable.BubbleLinearLayout_arrowHeight,
                BubbleDrawable.Builder.DEFAULT_ARROW_HEIGHT
            )
            mAngle = array.getDimension(
                R.styleable.BubbleLinearLayout_angle,
                BubbleDrawable.Builder.DEFAULT_ANGLE
            )
            mArrowPosition = array.getDimension(
                R.styleable.BubbleLinearLayout_arrowPosition,
                BubbleDrawable.Builder.DEFAULT_ARROW_POSITION
            )
            mBubbleColor = array.getColor(
                R.styleable.BubbleLinearLayout_bubbleColor,
                BubbleDrawable.Builder.DEFAULT_BUBBLE_COLOR
            )
            val location = array.getInt(R.styleable.BubbleLinearLayout_arrowLocation, 0)
            mArrowLocation = BubbleDrawable.ArrowLocation.mapIntToValue(location)
            mArrowCenter = array.getBoolean(R.styleable.BubbleLinearLayout_arrowCenter, false)
            array.recycle()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (r-l > 0 && b - t > 0) {
            setUp(r-l, b - t)
        }
    }


    private fun setUp(left: Int, right: Int, top: Int, bottom: Int) {
        if (right < left || bottom < top)
            return

        mRectF.set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
        background = BubbleDrawable.Builder()
            .rect(mRectF)
            .arrowLocation(mArrowLocation)
            .bubbleType(BubbleDrawable.BubbleType.COLOR)
            .aroundRadius(mAngle)
            .arrowHeight(mArrowHeight)
            .arrowWidth(mArrowWidth)
            .arrowPosition(mArrowPosition)
            .bubbleColor(mBubbleColor)
            .arrowCenter(mArrowCenter)
            .build()
    }

    private fun setUp(width: Int, height: Int) {
        setUp(
            paddingLeft, width - paddingRight,
            paddingTop, height - paddingBottom
        )
    }

    fun setArrowAngle(angle: Float): BubbleLinearLayout {
        mAngle = angle
        return this
    }

    fun setArrowHeight(arrowHeight: Float): BubbleLinearLayout {
        mArrowHeight = arrowHeight
        return this
    }

    fun setArrowLocation(arrowLocation:  BubbleDrawable.ArrowLocation): BubbleLinearLayout {
        mArrowLocation = arrowLocation
        return this
    }

    fun setArrowPosition(arrowPosition: Float): BubbleLinearLayout {
        mArrowPosition = arrowPosition
        return this
    }

    fun setArrowWidth(arrowWidth: Float): BubbleLinearLayout {
        mArrowWidth= arrowWidth
        return this
    }

    fun setArrowCenter(arrowCenter: Boolean): BubbleLinearLayout {
        mArrowCenter = arrowCenter
        return this
    }

    fun setBubbleColor(bubbleColor: Int): BubbleLinearLayout {
        mBubbleColor = bubbleColor
        return this
    }

    fun setUpBubbleDrawable() {
        background = null
        post { setUp(width, height) }
    }

    /**
     * 动态设置箭头距离左边的距离
     */
    fun setArrowLeftMargin(arrowPosition: Int) {
        setArrowPosition(arrowPosition.toFloat())
        requestLayout()
    }

}
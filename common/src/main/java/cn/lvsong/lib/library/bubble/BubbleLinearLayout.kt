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
        app:bll_arrow_position="@dimen/padding_50"
        app:bll_around_radius="@dimen/padding_3"
        app:bll_arrow_height="@dimen/padding_8"
        app:bll_arrow_location="top_right"
        app:bll_arrow_width="@dimen/width_10"
        app:bll_bubble_color="@color/color_FFFFFF"
>

 */

class BubbleLinearLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    /**
     * 四周圆角半径
     */
    private var mAroundRadius: Float = 0F
    /**
     * 箭头宽度
     */
    private var mArrowWidth: Float = 0F
    /**
     * 箭头高度
     */
    private var mArrowHeight: Float = 0F

    /**
     * 箭头偏移量,靠左(箭头在上边/下边)或者靠上(箭头在左边或者右边)的间隔
     */
    private var mArrowPosition: Float = 0F

    /**
     * 箭头所在位置,上下左右
     */
    private var mArrowLocation: BubbleDrawable.ArrowLocation = BubbleDrawable.ArrowLocation.LEFT

    /**
     * 气泡颜色
     */
    private var mBubbleColor: Int = 0

    /**
     * 箭头是否居中,如果居中则 mArrowPosition无效
     */
    private var mArrowCenter: Boolean = false

    private val mRectF = RectF()

    constructor(context: Context) : this(context, null)

    init {
        attrs?.let {
            val array = context.obtainStyledAttributes(attrs, R.styleable.BubbleLinearLayout)
            mArrowWidth = array.getDimension(
                R.styleable.BubbleLinearLayout_bll_arrow_width,
                BubbleDrawable.Builder.DEFAULT_ARROW_WITH
            )
            mArrowHeight = array.getDimension(
                R.styleable.BubbleLinearLayout_bll_arrow_height,
                BubbleDrawable.Builder.DEFAULT_ARROW_HEIGHT
            )
            mAroundRadius = array.getDimension(
                R.styleable.BubbleLinearLayout_bll_around_radius,
                BubbleDrawable.Builder.DEFAULT_ANGLE
            )
            mArrowPosition = array.getDimension(
                R.styleable.BubbleLinearLayout_bll_arrow_position,
                BubbleDrawable.Builder.DEFAULT_ARROW_POSITION
            )
            mBubbleColor = array.getColor(
                R.styleable.BubbleLinearLayout_bll_bubble_color,
                BubbleDrawable.Builder.DEFAULT_BUBBLE_COLOR
            )
            val location = array.getInt(R.styleable.BubbleLinearLayout_bll_arrow_location, 0)
            mArrowLocation = BubbleDrawable.ArrowLocation.mapIntToValue(location)
            mArrowCenter = array.getBoolean(R.styleable.BubbleLinearLayout_bll_arrow_center, false)
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
            .aroundRadius(mAroundRadius)
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
        mAroundRadius = angle
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

//    fun setUpBubbleDrawable() {
//        background = null
//        post { setUp(width, height) }
//    }

    /**
     * 动态设置箭头距离左边的距离
     */
    fun setArrowLeftMargin(arrowPosition: Int) {
        setArrowPosition(arrowPosition.toFloat())
        requestLayout()
    }

}
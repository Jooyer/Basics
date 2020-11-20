package cn.lvsong.lib.library.bubble

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import cn.lvsong.lib.library.R

/**
 * Desc: IM 聊天气泡效果, 弹框带箭头的气泡效果
 * Author: Jooyer
 * Date: 2018-08-15
 * Time: 10:02
 */

/*
        // 下面四个padding,将文本放在了气泡中间,根据实际需要调整
        android:paddingStart="@dimen/padding_18"
        android:paddingTop="@dimen/padding_10"
        android:paddingEnd="@dimen/padding_10"
        android:paddingBottom="@dimen/padding_10"

    <cn.lvsong.lib.library.bubble.BubbleTextView
        android:id="@+id/btv_msg_left_chat"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="@dimen/padding_20"
        android:paddingStart="@dimen/padding_18"
        android:paddingTop="@dimen/padding_10"
        android:paddingEnd="@dimen/padding_10"
        android:paddingBottom="@dimen/padding_10"
        android:textColor="@color/color_FFFFFF"
        android:textSize="20sp"
        app:btv_around_radius="@dimen/padding_15"
        app:btv_arrow_height="@dimen/padding_8"
        app:btv_arrow_position="@dimen/padding_20"
        app:btv_arrow_width="@dimen/padding_10"
        app:btv_bubble_color="@color/color_B1B6D1"
        tools:text="数据加载失败,点击重试,数据加载失败,点击重试数据加载失败,点击重试" />


 */

class BubbleTextView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {

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
            val array = context.obtainStyledAttributes(attrs, R.styleable.BubbleTextView)
            mArrowWidth = array.getDimension(
                R.styleable.BubbleTextView_btv_arrow_width,
                BubbleDrawable.Builder.DEFAULT_ARROW_WITH
            )
            mArrowHeight = array.getDimension(
                R.styleable.BubbleTextView_btv_arrow_height,
                BubbleDrawable.Builder.DEFAULT_ARROW_HEIGHT
            )
            mAroundRadius = array.getDimension(
                R.styleable.BubbleTextView_btv_around_radius,
                BubbleDrawable.Builder.DEFAULT_ANGLE
            )
            mArrowPosition = array.getDimension(
                R.styleable.BubbleTextView_btv_arrow_position,
                BubbleDrawable.Builder.DEFAULT_ARROW_POSITION
            )
            mBubbleColor = array.getColor(
                R.styleable.BubbleTextView_btv_bubble_color,
                BubbleDrawable.Builder.DEFAULT_BUBBLE_COLOR
            )
            val location = array.getInt(R.styleable.BubbleTextView_btv_arrow_location, 0)
            mArrowLocation = BubbleDrawable.ArrowLocation.mapIntToValue(location)
            mArrowCenter = array.getBoolean(R.styleable.BubbleTextView_btv_arrow_center, false)
            array.recycle()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (r - l > 0 && b - t > 0) {
            setUp(r - l, b - t)
        }
    }

    private fun setUp( right: Int, bottom: Int) {
        if (right < 0 || bottom < 0)
            return

        mRectF.set(0F, 0F, right.toFloat(), bottom.toFloat())
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
    
}
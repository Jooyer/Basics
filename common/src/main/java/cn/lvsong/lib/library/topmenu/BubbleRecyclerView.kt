package cn.lvsong.lib.library.topmenu

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import cn.lvsong.lib.library.R
import cn.lvsong.lib.library.bubble.BubbleDrawable

/**
 * Created by Jooyer on 2017/3/19
 * 注意,设置android:paddingxxx是无效的, 只能设置 brv_padding_top
 */

/* xml中用法如下

<cn.lvsong.lib.library.topmenu.BubbleRecyclerView
 xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/rv_menu_publish_dynamic"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:brv_around_radius="@dimen/padding_10"
    app:brv_arrow_height="@dimen/padding_8"
    app:brv_arrow_offset="@dimen/padding_12"
    app:brv_arrow_width="@dimen/padding_8"
    app:brv_bubble_color="@color/color_E89943"
    app:brv_top_padding="@dimen/padding_8"
    tools:listitem="@layout/left_img_right_text_view" />

 */

class BubbleRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    /**
     * 箭头宽度
     */
    private var mArrowWidth = 0F

    /**
     * 四周圆角大小
     */
    private var mAroundRadius = 0F

    /**
     * 箭头高度
     */
    private var mArrowHeight = 0F

    /**
     * 箭头偏移距离
     */
    private var mArrowOffset = 0F

    /**
     * 背景色
     */
    private var mBubbleColor: Int = 0

    /**
     * 控件顶部内边距,因为有箭头,所以需要给一个内边距,将内部Item挤下来
     */
    private var mPaddingTop: Int = 0

    /**
     * 箭头是否居中
     */
    private var mArrowCenter: Boolean = false

    constructor(context: Context) : this(context, null)

    init {
        attrs?.let {
            val array = context.obtainStyledAttributes(attrs, R.styleable.BubbleRecyclerView)
            mArrowWidth = array.getDimension(
                    R.styleable.BubbleRecyclerView_brv_arrow_width,
                    BubbleDrawable.Builder.DEFAULT_ARROW_WITH
            )
            mArrowHeight = array.getDimension(
                    R.styleable.BubbleRecyclerView_brv_arrow_height,
                    BubbleDrawable.Builder.DEFAULT_ARROW_HEIGHT
            )
            mAroundRadius = array.getDimension(
                    R.styleable.BubbleRecyclerView_brv_around_radius,
                    BubbleDrawable.Builder.DEFAULT_ANGLE
            )
            mArrowOffset = array.getDimension(
                    R.styleable.BubbleRecyclerView_brv_arrow_offset,
                    BubbleDrawable.Builder.DEFAULT_ARROW_POSITION
            )
            mBubbleColor = array.getColor(
                    R.styleable.BubbleRecyclerView_brv_bubble_color,
                    BubbleDrawable.Builder.DEFAULT_BUBBLE_COLOR
            )
            mPaddingTop = array.getDimensionPixelSize(R.styleable.BubbleRecyclerView_brv_top_padding, dp2px(10F).toInt())
            mArrowCenter = array.getBoolean(R.styleable.BubbleRecyclerView_brv_arrow_center, false)
            setPadding(0, mPaddingTop, 0, 0)
            array.recycle()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (r - l > 0 && b - t > 0) {
            setUp(r - l, b - t)
        }
    }


    private fun setUp(left: Int, right: Int, top: Int, bottom: Int) {
        if (right < left || bottom < top)
            return
        val rectF = RectF(left.toFloat(), 0F, right.toFloat(), bottom.toFloat())
        background = BubbleDrawable.Builder()
                .rect(rectF)
                .arrowLocation( BubbleDrawable.ArrowLocation.TOP_RIGHT)
                .bubbleType(BubbleDrawable.BubbleType.COLOR)
                .aroundRadius(mAroundRadius)
                .arrowHeight(mArrowHeight)
                .arrowWidth(mArrowWidth)
                .arrowPosition(mArrowOffset)
                .bubbleColor(mBubbleColor)
                .arrowCenter(mArrowCenter)
                .build()
    }

    private fun setUp(width: Int, height: Int) {
        setUp(
                paddingLeft, width - paddingRight,
                0, height - paddingBottom
        )
    }

    /**
     * 设置四周圆角大小
     */
    fun setAroundRadius(aroundRadius: Float): BubbleRecyclerView {
        mAroundRadius = aroundRadius
        return this
    }

    /**
     * 设置箭头高度
     */
    fun setArrowHeight(arrowHeight: Float): BubbleRecyclerView {
        mArrowHeight = arrowHeight
        return this
    }

    /**
     * 设置箭头偏移量
     */
    fun setArrowOffset(offset: Float): BubbleRecyclerView {
        mArrowOffset = offset
        return this
    }

    /**
     * 设置箭头宽度
     */
    fun setArrowWidth(arrowWidth: Float): BubbleRecyclerView {
        mArrowWidth = arrowWidth
        return this
    }

    /**
     * 设置箭头是否在中心位置
     */
    fun setArrowCenter(arrowCenter: Boolean): BubbleRecyclerView {
        mArrowCenter = arrowCenter
        return this
    }

    /**
     * 设置Bubble颜色
     */
    fun setBubbleColor(@ColorInt bubbleColor: Int): BubbleRecyclerView {
        mBubbleColor = bubbleColor
        return this
    }

    /**
     * 设置控件顶部内边距
     */
    fun setPaddingTop(paddingTop: Int): BubbleRecyclerView {
        mPaddingTop = paddingTop
        setPadding(0, mPaddingTop, 0, 0)
        return this
    }

    /**
     * 清除背景色
     */
    fun setUpBubbleDrawable() {
        background = null
        post { setUp(width, height) }
    }

    fun getNotAvailableSize() = (mPaddingTop + if (mArrowWidth > mArrowHeight) mArrowWidth else mArrowHeight).toInt()

    private fun dp2px(def: Float): Float {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                def,
                context.resources.displayMetrics
        )
    }
}
package cn.lvsong.lib.library.bubble

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import cn.lvsong.lib.library.R

/** https://www.jianshu.com/p/19929fabedca
 * Desc: IM 聊天聊天图片控件
 * Author: Jooyer
 * Date: 2020-11-17
 * Time: 20:24
 */
class BubbleImageView(context: Context, attrs: AttributeSet? = null) :
    AppCompatImageView(context, attrs) {

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
     * 箭头是否居中,如果居中则 mArrowPosition无效
     */
    private var mArrowCenter: Boolean = false

    private val mRectF = RectF()

    private var mBitmap: Bitmap? = null

    init {
        attrs?.let {
            val array = context.obtainStyledAttributes(attrs, R.styleable.BubbleImageView)
            mArrowWidth = array.getDimension(
                R.styleable.BubbleImageView_biv_arrow_width,
                BubbleDrawable.Builder.DEFAULT_ARROW_WITH
            )
            mArrowHeight = array.getDimension(
                R.styleable.BubbleImageView_biv_arrow_height,
                BubbleDrawable.Builder.DEFAULT_ARROW_HEIGHT
            )
            mAroundRadius = array.getDimension(
                R.styleable.BubbleImageView_biv_around_radius,
                BubbleDrawable.Builder.DEFAULT_ANGLE
            )
            mArrowPosition = array.getDimension(
                R.styleable.BubbleImageView_biv_arrow_position,
                BubbleDrawable.Builder.DEFAULT_ARROW_POSITION
            )
            val location = array.getInt(R.styleable.BubbleImageView_biv_arrow_location, 0)
            mArrowLocation = BubbleDrawable.ArrowLocation.mapIntToValue(location)
            mArrowCenter = array.getBoolean(R.styleable.BubbleImageView_biv_arrow_center, false)
            array.recycle()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (r - l > 0 || b - t > 0) {
            setUp(r - l, b - t)
        }
    }


    private fun setUp(left: Int, right: Int, top: Int, bottom: Int) {
        if (right < left || bottom < top || null == mBitmap)
            return
        mRectF.set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
        setImageDrawable(
            BubbleDrawable.Builder()
                .rect(mRectF)
                .arrowLocation(mArrowLocation)
                .aroundRadius(mAroundRadius)
                .arrowHeight(mArrowHeight)
                .arrowWidth(mArrowWidth)
                .bubbleBitmap(mBitmap)
                .arrowPosition(mArrowPosition)
                .arrowCenter(mArrowCenter)
                .build()
        )
    }

    private fun setUp(width: Int, height: Int) {
        setUp(
            paddingLeft, width - paddingRight,
            paddingTop, height - paddingBottom
        )
    }

    /**
     * 设置图片
     */
    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
        requestLayout()
    }
}
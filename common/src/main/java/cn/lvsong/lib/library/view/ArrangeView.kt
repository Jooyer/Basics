package cn.lvsong.lib.library.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.R

/**
 * 头像部分重叠
 * @ProjectName:    MVVMTest
 * @Package:        cn.lvsong.lib.library.view
 * @ClassName:      ArrangeView
 * @Description:    头像按序排列
 * @Author:         Jooyer
 * @CreateDate:     2020/5/29 11:38
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version:        1.0
 * ViewGroup及其子类如果要想指定子View的绘制顺序只需两步:
 * 1，setChildrenDrawingOrderEnabled(true) 开启自定义子View的绘制顺序;
 * 2，用setZ(float),自定义Z值，值越大越优先绘制；
 * https://github.com/hnsycsxhzcsh/DiscussionAvatarView
 */


/*
    <cn.lvsong.lib.library.view.ArrangeView
        android:id="@+id/av_avatar_item_people"
        android:layout_width="wrap_content"
        android:layout_height="@dimen/height_25"
        android:layout_marginEnd="@dimen/padding_32"
        app:av_first_show_top="false"
        app:av_show_count="6"
        app:av_space_width="5dp"
        app:layout_constraintBottom_toBottomOf="@id/dv_tips_item_people"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="@id/dv_tips_item_people" />
 */

class ArrangeView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    /**
     * 数据适配器
     */
    private var mArrangeAdapter: ArrangeAdapter? = null

    /**
     *  每一个Item重叠部分,默认20dp
     */
    private var mSpaceWidth = 0

    /**
     * 一共显示多个ItemView,默认4个
     */
    private var mShowCount = 4

    /**
     * 第一个在最上面,默认不是的(最后一个在最上面)
     */
    private var firstIsTop = false

    init {
        isChildrenDrawingOrderEnabled = true
        parseAttrs(context, attrs)
    }

    private fun parseAttrs(context: Context, attrs: AttributeSet) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.ArrangeView)
        mSpaceWidth =
                arr.getDimensionPixelOffset(
                        R.styleable.ArrangeView_av_space_width,
                        DensityUtil.dp2pxRtInt(20F)
                )
        mShowCount = arr.getInt(R.styleable.ArrangeView_av_show_count, mShowCount)
        firstIsTop = arr.getBoolean(R.styleable.ArrangeView_av_first_show_top, firstIsTop)
        arr.recycle()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var calcWidth = 0
        var calcHeight = 0
        mArrangeAdapter?.let { adapter ->
            val childCount = if (adapter.count > mShowCount) mShowCount else adapter.count
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
                if (i < mShowCount) {
                    if (0 == i) {
                        calcWidth = child.measuredWidth
                        calcHeight = child.measuredHeight
                    } else {
                        calcWidth += child.measuredWidth - mSpaceWidth
                    }
                }
                calcHeight = child.measuredHeight.coerceAtLeast(height)
            }
            setMeasuredDimension(
                    if (MeasureSpec.EXACTLY == widthMode) width else calcWidth,
                    if (MeasureSpec.EXACTLY == heightMode) height else calcHeight
            )
        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = childCount
        if (childCount > 0) {
            var top = (this.height - height)
            var left = 0
            for (i in 0 until childCount) {
                if (i < mShowCount) {
                    val child = getChildAt(i)
                    val width = child.measuredWidth
                    val height = child.measuredHeight
                    if (0 == i) {
                        child.layout(left, top, width, height)
                    } else {
                        left += width - mSpaceWidth
                        child.layout(left, top, left + width, height)
                    }
                }
            }
        }
    }

    /**
     * 数据适配器
     */
    abstract class ArrangeAdapter(val data: List<String>) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getRangeView(position, parent)
        }

        override fun getItem(position: Int) = data[position]
        override fun getItemId(position: Int) = position.toLong()
        override fun getCount() = data.size
        abstract fun getRangeView(position: Int, parent: ViewGroup): View
    }

    /**
     * 设置适配器
     */
    fun setAdapter(adapter: ArrangeAdapter) {
        removeAllViews()
        this.mArrangeAdapter = adapter
        val count = adapter.count
        for (i in 0 until count) {
            if (i < mShowCount) {
                val child = adapter.getView(i, null, this)
                if (firstIsTop) {
                    child.z = (count - 1 - i).toFloat()
                }
                this.addView(child)
            }
        }
    }

}
package cn.lvsong.lib.library.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.lvsong.lib.library.R
import cn.lvsong.lib.library.listener.OnClickFastListener

/**
 * @ProjectName: android
 * @Package: cn.lvsong.lib.library.view
 * @ClassName: NineImageLayout
 * @Description: 仿朋友圈图片展示
 * @Author: ChenYangQi
 * @CreateDate: 2020/6/3 17:22
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: 1.0
 */
/*

   <cn.lvsong.lib.library.view.NineImageLayout
            android:id="@+id/nl_images2"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/padding_50"
            app:nl_item_gap="@dimen/padding_10"
            app:nl_keep_place="false"
            app:nl_left_padding="@dimen/padding_10"
            app:nl_right_padding="@dimen/padding_10"
            app:nl_single_image_width_ratio="0.8" />

 */
class NineImageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    /**
     * 当只有一张图时Item宽度
     */
    private var singleViewWidth = 0

    /**
     * 当只有一张图时Item高度
     */
    private var singleViewHeight = 0

    /**
     * 屏幕宽度
     */
    private val screenWidth: Int

    /**
     * 计算此控件宽度
     */
    private var calcWidth = 0

    /**
     * 2张图时,是否要和3张图大小保持一致,默认是false
     */
    private var needKeepPlace = false

    /**
     * 图片之间间隔的大小
     */
    private var itemMargin = 5

    /**
     * 控件 leftPadding
     */
    private var leftPadding = 5

    /**
     * 控件 rightPadding
     */
    private var rightPadding = 5

    /**
     * 单个图片的宽度和高度
     */
    private var itemWidth = 0

    /**
     * 一张图片允许的最大宽高范围
     */
    private var singleImageWidthRatio = 0.8f

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.NineImageLayout)
        singleImageWidthRatio = array.getFloat(
            R.styleable.NineImageLayout_nl_single_image_width_ratio,
            dip2px(getContext(), singleImageWidthRatio).toFloat()
        )
        itemMargin = array.getDimensionPixelSize(
            R.styleable.NineImageLayout_nl_item_gap,
            dip2px(getContext(), itemMargin.toFloat())
        )
        leftPadding = array.getDimensionPixelSize(
            R.styleable.NineImageLayout_nl_left_padding,
            dip2px(getContext(), leftPadding.toFloat())
        )
        rightPadding = array.getDimensionPixelSize(
            R.styleable.NineImageLayout_nl_right_padding,
            dip2px(getContext(), rightPadding.toFloat())
        )
        needKeepPlace = array.getBoolean(R.styleable.NineImageLayout_nl_keep_place, needKeepPlace)
        array.recycle()
        screenWidth = resources.displayMetrics.widthPixels
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var viewHeight = 0
        // 获取组件的宽度
        calcWidth = screenWidth - leftPadding - rightPadding
        itemWidth = (calcWidth - 2 * itemMargin) / 3
        val count = childCount
        if (count == 1) {  //一张图片的宽高
            //TODO 单独处理
            setMeasuredDimension(singleViewWidth, singleViewHeight)
            return
        } else if (2 == count || 4 == count) {
            viewHeight = if (needKeepPlace) { // 2张图(4张图) 和 3张图宽度一样
                itemWidth * if (4 == count) 2 else 1
            } else {
                (calcWidth - itemMargin) / 2 * if (4 == count) 2 else 1
            }
        } else if (count == 3) {
            viewHeight = itemWidth
        } else if (count <= 6) {
            viewHeight = 2 * itemWidth + itemMargin
        } else if (count <= 9) {
            viewHeight = screenWidth
        }
        setMeasuredDimension(screenWidth, viewHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        var top = 0
        var left = 0
        var right = 0
        var bottom = 0
        for (i in 0 until count) {
            val childView = getChildAt(i)
            if (1 == count) { // 只有一张图
                left = leftPadding
                right = left + singleViewWidth
                bottom = top + singleViewHeight
            } else if (2 == count || 4 == count) {
                if (needKeepPlace) { // 2张图(4张图) 和 3张图宽度一样
                    if (3 == i) {
                        top = itemWidth + itemMargin
                    }
                    if (0 == i % 3) { // 左边第一张
                        left = leftPadding
                    } else {
                        left += itemWidth + itemMargin
                    }
                    right = left + itemWidth
                    bottom = top + itemWidth
                } else {
                    if (2 == i || 3 == i) {
                        top = (calcWidth - itemMargin) / 2 + itemMargin
                    }
                    if (0 == i % 2) { // 左边第一张
                        left = leftPadding
                    } else {
                        left += (calcWidth - itemMargin) / 2 + itemMargin
                    }
                    right = left + (calcWidth - itemMargin) / 2
                    bottom = top + (calcWidth - itemMargin) / 2
                }
            } else {
                if (0 == i % 3) { // 左边第一个
                    left = leftPadding
                    right = left + itemWidth
                } else if (1 == i % 3) { // 中间
                    left += itemWidth + itemMargin
                    right = left + itemWidth
                } else { // 最后一个
                    left += itemWidth + itemMargin
                    right = screenWidth - rightPadding
                }
                if (3 == i || 6 == i) {
                    top += itemWidth + itemMargin
                }
                bottom = top + itemWidth
            }
            childView.layout(left, top, right, bottom)
        }
    }

    /**
     * 单张图片的展示处理
     *
     * @param width --> 图片的宽度
     * @param height --> 图片的高度
     * @param view --> 展示图片的ImageView
     */
    fun setSingleImage(width: Int, height: Int, view: View) {
        if (childCount != 1) {
            removeAllViews()
            addView(view)
        }
        if (width >= height) {
            singleViewWidth =
                (singleImageWidthRatio * view.context.resources.displayMetrics.widthPixels).toInt()
            singleViewHeight = (singleImageWidthRatio * view.context.resources
                .displayMetrics.widthPixels * height / width).toInt()
        } else {
            singleViewHeight =
                (singleImageWidthRatio * view.context.resources.displayMetrics.widthPixels).toInt()
            singleViewWidth = (singleImageWidthRatio * view.context.resources
                .displayMetrics.widthPixels * width / height).toInt()
        }
        getChildAt(0).layout(0, 0, singleViewWidth, singleViewHeight)
        setMeasuredDimension(singleViewWidth, singleViewHeight)
    }

    /**
     * 设置数据源
     *
     * @param adapter
     */
    fun setAdapter(adapter: NineImageAdapter) {
        removeAllViews()
        for (i in 0 until adapter.getItemCount()) {
            val view = adapter.createView(LayoutInflater.from(context), this, i)
            adapter.bindView(view, i)
            removeView(view)
            addView(view)
            bindClickEvent(i, view, adapter)
        }
    }

    /**
     * 绑定点击事件
     *
     * @param position
     * @param view
     */
    private fun bindClickEvent(position: Int, view: View, adapter: NineImageAdapter?) {
        if (adapter == null) {
            return
        }
        view.setOnClickListener(object : OnClickFastListener() {
            override fun onFastClick(v: View) {
                adapter.onItemClick(position, v)
            }
        })
    }

    /**
     * 获取MarginLayoutParams必须要重写这几个方法
     *
     * @return
     */
    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 图片适配器
     */
    abstract class NineImageAdapter {
        abstract fun getItemCount():Int
        abstract fun createView(inflater: LayoutInflater, parent: ViewGroup, position: Int): View
        abstract fun bindView(view: View, position: Int)

        /**
         * 点击事件,根据需要重写
         * @param position --> 点击位置,从0开始计算
         * @param view --> 当前点击的View
         */
        open fun onItemClick(position: Int, view: View) {

        }
    }
}
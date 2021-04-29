package cn.lvsong.lib.library.topmenu

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import cn.lvsong.lib.library.R

/**
 * 封装一个PopupWindow 实现类似QQ,支付宝等右上角弹框效果
 * Created by Jooyer on 2017/2/10
 *
 *
 * mTopMenu = TopMenu(this@ChatActivity, adapter)
 * .setWidth(DensityUtil.dp2px(125F).toInt())
 * .setHeight(DensityUtil.dp2px(124F).toInt())
 * .setShowBackground(false)
 * // 使得弹框右侧距离屏幕间隔, 如果间隔够了,箭头位置还没有对准控件中间,可以在BubbleRecyclerView所在布局中使用 brv_arrow_offset
 * .setPopupXOffset(-DensityUtil.dp2px(2F).toInt())
 * // 使得弹框上下偏移
 * .setPopupYOffset(-DensityUtil.dp2px(5F).toInt())
 * .setItemDecoration(itemDecoration)
 *
 *
 * mTopMenu.show(it, null, null)
 */
class TopMenu(private val mContext: Context, private val mMenuAdapter: RecyclerView.Adapter<*>) {

    private  val TAG = "TopRightMenu"
    private  val DEFAULT_AMEND = 200

    /**
     * 弹窗默认高度
     */
    private  val DEFAULT_HEIGHT = 480

    /**
     * 弹窗默认高度
     */
    private  val DEFAULT_WIDTH = 320

    /**
     * 默认弹出或者关闭动画
     */
    private val DEFAULT_ANIM_STYLE = R.style.TopMenu_Anim

    private var mPopupHeight = DEFAULT_HEIGHT
    private var mPopupWidth = DEFAULT_WIDTH

    /**
     * 默认显示背景 --> 背景变暗
     */
    private var isShowBackground = true

    /**
     * 默认显示动画
     */
    private var isShowAnimationStyle = true

    /**
     * 动画ID
     */
    private var mAnimationStyle = 0

    /**
     * 默认的透明度值
     */
    private val mAlpha = 0.7f

    /**
     * 弹框在Y轴偏移量,正值向下偏移,反之向上
     */
    private var mPopupYOffset = 0

    /**
     * 弹框偏移距离
     */
    private var mPopupXOffset = 0

    private var mPopupWindow: PopupWindow? = null
    private var mRecyclerView: BubbleRecyclerView
    private var mParent: View = LayoutInflater.from(mContext).inflate(R.layout.menu_publish_dynamic, null)

    init {
        mRecyclerView = mParent.findViewById<View>(R.id.rv_menu_publish_dynamic) as BubbleRecyclerView
        mRecyclerView.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.overScrollMode = View.OVER_SCROLL_NEVER
    }


    /**
     * 设置背景颜色变化动画
     *
     * @param from     --> 开始值
     * @param to       --> 结束值
     * @param duration --> 持续时间
     */
    private fun setBackgroundAlpha(from: Float, to: Float, duration: Int) {
        val lp = (mContext as Activity).window.attributes
        val animator = ValueAnimator.ofFloat(from, to)
        animator.duration = duration.toLong()
        animator.addUpdateListener { animation ->
            lp.alpha = animation.animatedValue as Float
            mContext.window.attributes = lp
        }
        animator.start()
    }

    /**
     * 确定 弹框的位置
     */
    private fun reviseFrameAndOrigin(anchor: View, frame: Rect, origin: Point): IntArray {
        val location = IntArray(2)
        anchor.getLocationInWindow(location)
        if (origin.x < 0 || origin.y < 0) {
            origin[anchor.width shr 1] = anchor.height shr 1
        }
        if (frame.isEmpty || !frame.contains(origin.x + location[0], origin.y + location[1])) {
            anchor.getWindowVisibleDisplayFrame(frame)
        }
        return location
    }

    private val popupWindow: PopupWindow
        private get() {
            mPopupWindow = PopupWindow(mContext)
            mPopupWindow!!.contentView = mParent
            mPopupWindow!!.width = mPopupWidth
            mPopupWindow!!.height = mPopupHeight + mRecyclerView.getNotAvailableSize()
            if (isShowAnimationStyle) mPopupWindow!!.animationStyle =
                if (mAnimationStyle <= 0) DEFAULT_ANIM_STYLE else mAnimationStyle
            mPopupWindow!!.isFocusable = true
            mPopupWindow!!.isOutsideTouchable = true
            mPopupWindow!!.setBackgroundDrawable(ColorDrawable())
            mPopupWindow!!.setOnDismissListener {
                if (isShowBackground) setBackgroundAlpha(
                    mAlpha,
                    1f,
                    300
                )
            }
            mRecyclerView.adapter = mMenuAdapter
            return mPopupWindow as PopupWindow
        }
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    /**
     * 设置宽度
     */
    fun setWidth(width: Int): TopMenu {
        if (width > 0) {
            mPopupWidth = width
        } else {
            throw IllegalArgumentException("宽度不能为空,且必须大于0!")
        }
        return this
    }

    /**
     * 设置高度
     */
    fun setHeight(height: Int): TopMenu {
        if (height > 0) {
            mPopupHeight = height
        }
        return this
    }

    /**
     * 设置弹窗偏X方向移距离,默认0
     */
    fun setPopupXOffset(popupOffset: Int): TopMenu {
        mPopupXOffset = popupOffset
        return this
    }

    /**
     * 设置弹窗偏Y方向移距离,默认0
     */
    fun setPopupYOffset(yOffset: Int): TopMenu {
        mPopupYOffset = yOffset
        return this
    }

    /**
     * 设置背景是否变暗,默认true
     */
    fun setBackDark(isShowBackground: Boolean): TopMenu {
        this.isShowBackground = isShowBackground
        return this
    }

    /**
     * 设置是否显示动画,默认true,如果没有调用 setAnimationStyle() 则使用默认动画
     */
    fun setShowAnimationStyle(isShowAnimationStyle: Boolean): TopMenu {
        this.isShowAnimationStyle = isShowAnimationStyle
        return this
    }

    /**
     * 设置动画
     */
    fun setAnimationStyle(animationStyle: Int): TopMenu {
        mAnimationStyle = animationStyle
        return this
    }

    /**
     * 设置箭头偏移量
     * @param offset  --> 箭头位置,当arrowLocation确定时箭头初始位置的偏移量,默认50px
     */
    fun setArrowOffset(offset: Float): TopMenu {
        if (mRecyclerView != null) {
            (mRecyclerView as BubbleRecyclerView).setArrowOffset(offset)
        }
        return this
    }

    /**
     * 设置分割线
     */
    fun setItemDecoration(itemDecoration: ItemDecoration?): TopMenu {
        // 防止重复添加
        if (mRecyclerView.itemDecorationCount > 0) {
            mRecyclerView.removeItemDecorationAt(0)
        }
        if (null != itemDecoration) {
            mRecyclerView.addItemDecoration(itemDecoration)
        }
        return this
    }

    /**
     * 设置四周圆角大小
     * @param aroundRadius --> 默认20px
     */
    fun setAroundRadius(aroundRadius: Float): TopMenu {
        if (mRecyclerView != null) {
            (mRecyclerView as BubbleRecyclerView).setAroundRadius(aroundRadius)
        }
        return this
    }

    /**
     * 设置箭头高度
     * @param arrowHeight  --> 默认25px
     */
    fun setArrowHeight(arrowHeight: Float): TopMenu {
        if (mRecyclerView != null) {
            (mRecyclerView as BubbleRecyclerView).setArrowHeight(arrowHeight)
        }
        return this
    }

    /**
     * 设置箭头宽度
     * @param arrowWidth --> 默认25px
     */
    fun setArrowWidth(arrowWidth: Float): TopMenu {
        if (mRecyclerView != null) {
            (mRecyclerView as BubbleRecyclerView).setArrowWidth(arrowWidth)
        }
        return this
    }

    /**
     * 设置箭头是否在中心位置
     * @param arrowCenter  --> 默认false
     */
    fun setArrowCenter(arrowCenter: Boolean): TopMenu {
        if (mRecyclerView != null) {
            (mRecyclerView as BubbleRecyclerView).setArrowCenter(arrowCenter)
        }
        return this
    }

    /**
     * 设置Bubble颜色
     * @param bubbleColor --> 默认红色(Color.RED)
     */
    fun setBubbleColor(@ColorInt bubbleColor: Int): TopMenu {
        if (mRecyclerView != null) {
            (mRecyclerView as BubbleRecyclerView).setBubbleColor(bubbleColor)
        }
        return this
    }

    /**
     * 设置控件顶部内边距
     * @param paddingTop  --> 默认10dp
     */
    fun setPaddingTop(paddingTop: Int): TopMenu {
        if (mRecyclerView != null) {
            (mRecyclerView as BubbleRecyclerView).paddingTop = paddingTop
        }
        return this
    }

    /**
     * 显示弹框
     */
    fun showAsDropDown(anchor: View?): TopMenu {
        showAsDropDown(anchor, 0, 0)
        return this
    }

    /**
     * 显示弹框
     */
    fun showAsDropDown(anchor: View?, offsetX: Int, offsetY: Int): TopMenu {
        if (null == mPopupWindow) {
            mPopupWindow = popupWindow
        }
        if (!mPopupWindow!!.isShowing) {
            mPopupWindow!!.showAsDropDown(anchor, offsetX, offsetY)
            if (isShowBackground) setBackgroundAlpha(1f, mAlpha, 300)
        }
        return this
    }

    /**
     * 显示 PopupWindow
     *
     * @return https://blog.csdn.net/xiey94/article/details/93174035 --> 对showAsDropDown解析
     */
    fun show(anchor: View, frame: Rect?, origin: Point?): TopMenu {
        var frame = frame
        var origin = origin
        if (null == mPopupWindow) {
            mPopupWindow = popupWindow
        }
        if (null == frame) frame = Rect()
        if (null == origin) origin = Point(-1, -1)
        val location = reviseFrameAndOrigin(anchor, frame, origin)
        val x = location[0]
        val y = location[1]
        val width = anchor.width
        val height = anchor.height
        val contentHeight = mPopupWindow!!.contentView.measuredHeight
        if (!mPopupWindow!!.isShowing) {
            if (y + height + contentHeight < frame.bottom) {
                mPopupWindow!!.showAsDropDown(
                    anchor,
                    -mPopupWidth + width + mPopupXOffset,
                    mPopupYOffset
                )
            }
            if (isShowBackground) {
                setBackgroundAlpha(1f, mAlpha, 300)
            }
        }
        return this
    }

    /**
     * 取消弹框
     */
    fun dismiss() {
        if (null != mPopupWindow && mPopupWindow!!.isShowing) mPopupWindow!!.dismiss()
    }

}
package cn.lvsong.lib.library.banner

import android.content.res.Resources
import android.graphics.PointF
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * https://www.jianshu.com/p/3994bbdcc624  --> 滑动大小渐变
 * https://blog.csdn.net/myself0719/article/details/79795624  --> 分析透彻
 * https://blog.csdn.net/ww897532167/article/details/86585214
 * Desc: 无限循环 , 和上一个思路一致
 * Author: Jooyer
 * Date: 2019-08-28
 * Time: 18:25
 */
class HorizontalLayoutManager(private val spaceWidth: Int) : RecyclerView.LayoutManager(),
    RecyclerView.SmoothScroller.ScrollVectorProvider {
    /**
     * 记录 onLayoutChildren 次数,因为首次手动滑动时,会在抬起手来,还会回调一次 onLayoutChildren()
     * 如果有小伙伴知道更优解决方法,记得提 issue , 先谢过!!!
     */
    private var mLayoutCount = 1
    private var mSpaceWidth = spaceWidth


    override
    fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        if (childCount == 0) {
            return null
        }
        val firstChildPos = getPosition(getChildAt(0)!!)
        val direction = if (targetPosition < firstChildPos) -1 else 1
        return PointF(direction.toFloat(), 0f)
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    /** detachAndScrapView()  removeAndRecycleView(),与 detachAndScrapAttachedViews() 区别
     * detachAndScrapView() --> 需要刷新的时候使用，子view临时保存在Recycler中的mAttachedScrap中,不会引起重新布局，因此是轻量级的
     * removeAndRecycleView() --> 在子view移出屏幕回收时使用，保存在Recycler中的mCachedViews中,会引起重新布局
     * detachAndScrapAttachedViews() --> 因为RecyclerView初始化的时候onLayoutChildren会调用两次，第一次的时候屏幕上已经填充好子view了，
     * 第二次到来的时候又会重新调用一次fill填充子view，因此fill之前先调用轻量级的detachAndScrapAttachedViews把子view都给移除掉，
     * 临时保存在一个集合里，然后进入fill的时候会从这个集合取出来重新添加到RecyclerView中，这就是前面说的保存在mAttachedScrap的子view会马上用到的原因
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {

        if (mLayoutCount >= itemCount) {
            return
        }

        //分离并且回收当前附加的所有View
        detachAndScrapAttachedViews(recycler)

        if (itemCount == 0) {
            return
        }

        //横向绘制子View,则需要知道 X轴的偏移量
        var offsetX = 0
        mLayoutCount++

        //绘制并添加view
        for (i in 0 until itemCount) {
            val view = recycler.getViewForPosition(i)
            addView(view)

            measureChildWithMargins(view, 0, 0)
            val viewWidth = getDecoratedMeasuredWidth(view)
            val viewHeight = getDecoratedMeasuredHeight(view)

            layoutDecorated(view, offsetX, 0, offsetX + viewWidth, viewHeight)
            offsetX += viewWidth + mSpaceWidth

            if (offsetX > width) {
                break
            }
        }
    }

    //是否可横向滑动
    override fun canScrollHorizontally() = true

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val linearSmoothScroller = LinearSmoothScroller(recyclerView.context)
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    /**
     * scrollHorizontallyBy()/scrollVerticallyBy() 方法中,一般执行四个步骤
     * 1. dx/dy修正
     * 2. ItemView 填充
     * 3. ItemView 偏移
     * 4. ItemView 回收
     */
    override fun scrollHorizontallyBy(
        dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State
    ): Int {
        recycleViews(dx, recycler)
        fill(dx, recycler)
        offsetChildrenHorizontal(dx * -1)
        return dx
    }

    private fun fill(dx: Int, recycler: RecyclerView.Recycler) {
        //左滑
        if (dx > 0) {
            while (true) {
                //得到当前已添加（可见）的最后一个子View
                val lastVisibleView = getChildAt(childCount - 1) ?: break

                //如果滑动过后，View还是未完全显示出来就 不进行绘制下一个View
                if (lastVisibleView.right - dx > width)
                    break

                //得到View对应的位置
                val layoutPosition = getPosition(lastVisibleView)
                /**
                 * 例如要显示20个View，当前可见的最后一个View就是第20个，那么下一个要显示的就是第一个
                 * 如果当前显示的View不是第20个，那么就显示下一个，如当前显示的是第15个View，那么下一个显示第16个
                 * 注意区分 childCount 与 itemCount
                 */
                val nextView: View = if (layoutPosition == itemCount - 1) {
                    recycler.getViewForPosition(0)
                } else {
                    recycler.getViewForPosition(layoutPosition + 1)
                }
                addView(nextView)
                measureChildWithMargins(nextView, 0, 0)
                val viewWidth = getDecoratedMeasuredWidth(nextView)
                val viewHeight = getDecoratedMeasuredHeight(nextView)
                val offsetX = lastVisibleView.right + mSpaceWidth
                layoutDecorated(nextView, offsetX, 0, offsetX + viewWidth, viewHeight)
                break
            }
        } else { //右滑
            while (true) {
                val firstVisibleView = getChildAt(0) ?: break

                if (firstVisibleView.left - dx < 0) break

                val layoutPosition = getPosition(firstVisibleView)
                /**
                 * 如果当前第一个可见View为第0个，则左侧显示第20个View 如果不是，下一个就显示前一个
                 */
                val nextView = if (layoutPosition == 0) {
                    recycler.getViewForPosition(itemCount - 1)
                } else {
                    recycler.getViewForPosition(layoutPosition - 1)
                }
                addView(nextView, 0)
                measureChildWithMargins(nextView, 0, 0)
                val viewWidth = getDecoratedMeasuredWidth(nextView)
                val viewHeight = getDecoratedMeasuredHeight(nextView)
                val offsetX = firstVisibleView.left - mSpaceWidth
                layoutDecorated(nextView, offsetX - viewWidth, 0, offsetX, viewHeight)
                break
            }
        }
    }

    private fun recycleViews(dx: Int, recycler: RecyclerView.Recycler) {
        for (i in 0 until itemCount) {
            val childView = getChildAt(i) ?: return
            //左滑
            if (dx > 0) {
                //移除并回收 原点 左侧的子View
                if (childView.right - dx < 0) {
                    removeAndRecycleViewAt(i, recycler)
                }
            } else { //右滑
                //移除并回收 右侧即RecyclerView宽度之以外的子View
                if (childView.left - dx > width) {
                    removeAndRecycleViewAt(i, recycler)
                }
            }
        }
    }

    private fun dp2px(def: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            def, Resources.getSystem().displayMetrics
        ).toInt()
    }

}
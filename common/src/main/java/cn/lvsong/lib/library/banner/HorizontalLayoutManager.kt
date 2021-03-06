package cn.lvsong.lib.library.banner

import android.content.Context
import android.graphics.PointF
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*

/**
 * https://www.jianshu.com/p/3994bbdcc624  --> 滑动大小渐变
 * https://www.jianshu.com/p/ba8ad2ab9a57
 * https://blog.csdn.net/qq_40861368/article/details/101199470
 * https://blog.csdn.net/qq_34198206/article/details/89881817 --> 折叠缩放
 * https://blog.csdn.net/myself0719/article/details/79795624  --> 分析透彻
 * https://blog.csdn.net/ww897532167/article/details/86585214
 * Desc: 无限循环 , 和上一个思路一致
 * Author: Jooyer
 * Date: 2019-08-28
 * Time: 18:25
 */


/**
 * @param itemMargin --> Item 间隔,默认 0
 * @param itemScrollTime --> Item 从屏幕左侧完成消失的时间,此值越大在屏幕滑过时间越长,单位是毫秒
 */
open class HorizontalLayoutManager(
    private val context: Context,
    private val itemMargin: Int = 0,
    private val itemScrollTime: Long = 1200
) :
    RecyclerView.LayoutManager(),
    RecyclerView.SmoothScroller.ScrollVectorProvider {

    open lateinit var mOrientationHelper: OrientationHelper

    open var itemWidth: Int = 0

    /**
     * 记录 onLayoutChildren 次数,因为首次手动滑动时,会在抬起手来,还会回调一次 onLayoutChildren()
     * 如果有小伙伴知道更优解决方法,记得提 issue , 先谢过!!!
     */
    private var mLayoutCount = 1

    fun getItemWidthAndMargin(): Int {
        return itemWidth + itemMargin
    }

    fun getItemMargin() = itemMargin

    private val linearSmoothScroller: LinearSmoothScroller =
        object : LinearSmoothScroller(context) {
            // 返回越少,滑动越快
            override fun calculateTimeForDeceleration(dx: Int): Int {
                return (itemScrollTime * (1 - .3356) / 2).toInt()
            }

            override fun calculateDtToFit(
                viewStart: Int,
                viewEnd: Int,
                boxStart: Int,
                boxEnd: Int,
                snapPreference: Int
            ): Int {
                // https://blog.csdn.net/u014674862/article/details/81177261  --> API解析
                // https://blog.csdn.net/u013651026/article/details/105989914
                // 在 super.calculateDtToFit()中,     case SNAP_TO_START: return boxStart - viewStart; 通过日志发现只有 boxStart - viewStart < 0 是向左滑动
                // 而 boxStart == 0 , 只需要 viewStart > 0 即可, 所以人为的将 viewStart 进行调整,保证界面在自动滑动时永阳往左滑动
                return super.calculateDtToFit(
                    if (viewStart > 0) viewStart else -viewStart,
                    viewEnd,
                    boxStart,
                    boxEnd,
                    snapPreference
                )
            }

            override fun getHorizontalSnapPreference(): Int {
                return SNAP_TO_START
            }

        }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        // 下面这个决定了RecyclerView 左右滑动的方向,只有自动滚动时才会触发此方法
        // 由于手动滑动改变了 targetPosition, 所以下面判断会导致
        // 如果自动滑动向右的,而手动是向左的,那手动滑动结束后,自动开始滑动会也向左,ItemView会滑动多个
        // 所以强制自动滑动向右
//        val firstChildPos = getPosition(getChildAt(0)!!)
//        val direction = if (targetPosition < firstChildPos) -1 else 1
        return PointF(1F, 0F)
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
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
        if (!this::mOrientationHelper.isInitialized) {
            mOrientationHelper = OrientationHelper.createHorizontalHelper(this)
        }

//        if (mLayoutCount > itemCount) { // 默认 mLayoutCount=1, 如果 itemCount=1, 则会导致界面空白, 所以改为 >
//            return
//        }

        if (itemCount == 0 || state.isPreLayout) {
            removeAndRecycleAllViews(recycler)
            return
        }

        //分离并且回收当前附加的所有View
        detachAndScrapAttachedViews(recycler)

        // 计算一个Item宽度
        val scrap = recycler.getViewForPosition(0)
        measureChildWithMargins(scrap, 0, 0)
        itemWidth = getDecoratedMeasuredWidth(scrap)

        //横向绘制子View,则需要知道 X轴的偏移量
        var offsetX =
            (mOrientationHelper.totalSpace - mOrientationHelper.getDecoratedMeasurement(scrap)) / 2
        //绘制并添加view
        for (i in 0 until itemCount) {
            val view = recycler.getViewForPosition(i)
            addView(view)

            measureChildWithMargins(view, 0, 0)
//            val viewWidth = getDecoratedMeasuredWidth(view)
            val viewHeight = getDecoratedMeasuredHeight(view)
//            layoutDecorated(view, offsetX, 0, offsetX + viewWidth, viewHeight)
            layoutDecoratedWithMargins(
                view,
                offsetX,
                getItemTop(view),
                offsetX + itemWidth,
                getItemTop(view) + viewHeight
            )
            offsetX += itemWidth + itemMargin
            // 会造成指示器异常(主要是导致计算childCount出现问题)
//            if (offsetX > mOrientationHelper.totalSpace) {
//                break
//            }
        }
        doWithItem()
    }

    private fun getTotalHeight(): Int {
        return height - paddingTop - paddingBottom
    }

    private fun getItemTop(item: View): Int {
        return (getTotalHeight() - getDecoratedMeasuredHeight(item)) / 2 + paddingTop
    }

    //是否可横向滑动
    override fun canScrollHorizontally() = itemCount > 1


    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)

//        smoothScrollBy(recyclerView, position)
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
        doWithItem()
        return dx
    }

    private fun fill(dx: Int, recycler: RecyclerView.Recycler) {
//        Log.e("Horizontal","fill===========dx: $dx, ${if (dx >0) "左滑" else "右滑"}" )
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
                val offsetX = lastVisibleView.right + itemMargin
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
                val offsetX = firstVisibleView.left - itemMargin
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
                if (childView.right + itemMargin - dx < 0) {
                    removeAndRecycleViewAt(i, recycler)
                }
            } else { //右滑
                //移除并回收 右侧即RecyclerView宽度之以外的子View
                if (childView.left - dx > width + itemMargin) {
                    removeAndRecycleViewAt(i, recycler)
                }
            }
        }
    }

    /**
     * 处理Item不同效果
     */
    open fun doWithItem() {

    }

    fun getScrollTime() = itemScrollTime
}
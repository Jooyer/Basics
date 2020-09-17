package cn.lvsong.lib.library.banner

import android.content.Context
import android.graphics.PointF
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/** 参考: https://www.jianshu.com/p/ba8ad2ab9a57
 * Desc: 画廊效果
 * Author: Jooyer
 * Date: 2020-09-16
 * Time: 18:17
 */

class GalleryLayoutManager(private var loopTime: Long, private val factor: Float) :
    RecyclerView.LayoutManager(), RecyclerView.SmoothScroller.ScrollVectorProvider {

    private val scrollDirection = PointF(1F, 0f)

    private var smaller = 0f//最大缩小比例，原始都是1，也就是最多缩小为原来view的0.8，这个可以自己改

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF {
        if (childCount == 0) {
            return scrollDirection
        }
        // 下面这个决定了RecyclerView 左右滑动的方向,只有自动滚动时才会触发此方法
        // 由于手动滑动改变了 targetPosition, 所以下面判断会导致
        // 如果自动滑动向右的,而手动是向左的,那手动滑动结束后,自动开始滑动会也向左,ItemView会滑动多个
        // 所以强制自动滑动向右
//        val firstChildPos = getPosition(getChildAt(0)!!)
//        val direction = if (targetPosition < firstChildPos) -1 else 1
        return scrollDirection // 强制右滑,不能返回null否则界面将不会自动滑动了
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val linearSmoothScroller: LinearSmoothScroller =
            object : LinearSmoothScroller(recyclerView.context) {
                // 返回越少,滑动越快
                override fun calculateTimeForDeceleration(dx: Int): Int {
                    return (loopTime * (1 - .3356) / 2).toInt()
                }
            }

        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    //是否可横向滑动
    override fun canScrollHorizontally() = true

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {

    }

    //水平滚动的时候这里会执行
    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        handleHorizontalView()
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    private fun handleHorizontalView() {
        // View居中显示的时候left位置, 这个值也是在计算 ItemView 宽度时,减去的部分
        //  holder.itemView.layoutParams.width = (RecyclerView.width*(1 - 4*factor),  centerViewLeft = RecyclerView.width*4*factor
        val centerViewLeft = width * 2 * factor

        // 从一个位置移动到另一个位置 ItemView 移动的距离
        // 移动的距离 =  holder.itemView.layoutParams.width
        val moveX = width * (1 - 4 * factor)

        calculateScale(centerViewLeft, moveX)
    }


    /**@param centerViewLeft 水平滑动的时候是left位置，垂直滑动是top位置
     * @param moveDistance 水平滑动是x轴移动距离，垂直滑动是y轴移动距离*/
    private fun calculateScale(centerViewLeft: Float, moveDistance: Float) {
        repeat(childCount) { index ->
            getChildAt(index)?.apply {
                var factor = (this.left - centerViewLeft) / moveDistance
//                Log.e(
//                    "Gallery",
//                    "==========left: $left, centerViewLeft: $centerViewLeft, moveDistance: $moveDistance, factor: $factor"
//                )
                factor = (-1F).coerceAtLeast(factor)
                factor = 1F.coerceAtMost(factor)

                if (factor > 0) { //屏幕右边的 View 往中心滑动
                    scale(this, 1F - factor * smaller)
//                    Log.e("Gallery", "==========factor > 0")
                } else { //屏幕中间的 View 往左边滑动
                    scale(this, 1F + factor * smaller)
//                    Log.e("Gallery", "==========factor <= 0")
                }
            }
        }
    }

    fun scale(view: View, scale: Float) {
        view.apply {
            pivotX = this.width / 2F
            pivotY = this.height / 2F
            scaleX = scale
            scaleY = scale
        }
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        if (smaller == 0f) {
            smaller = 2 * factor / (1 - 4 * factor)
        }
        //因为布局首次加载完不滑动是不会执行scroll方法的，所以这里得修改view的scale
//        handleHorizontalView()
    }
}
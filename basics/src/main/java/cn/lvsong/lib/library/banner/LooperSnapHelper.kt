package cn.lvsong.lib.library.banner

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * https://github.com/979451341/EventLine/blob/master/LooperLayoutManager.java  --> 垂直循环
 * https://www.jianshu.com/p/0e4a93d8e2de --> 自定义LayoutManager和SnapHelper
 * https://www.jianshu.com/p/2359b9da63bb
 * https://juejin.im/post/5af383d9518825670a104dfc
 * https://www.jianshu.com/p/1837a801e599
 * https://blog.csdn.net/u012551350/article/details/93971801
 * https://blog.csdn.net/Duckdan/article/details/88857095
 * https://www.jianshu.com/p/25294efa68c3
 * https://blog.csdn.net/u014608640/article/details/86480630
 * https://github.com/leochuan/ViewPagerLayoutManager
 * https://blog.csdn.net/u014163726/article/details/81592523
 * https://blog.csdn.net/H176Nhx7/article/details/78139369
 * <p>
 * https://juejin.im/post/5cfa198ff265da1b8c197c2f
 * https://www.jianshu.com/p/e54db232df62  --> SnapHelper 分析很详细
 * Desc: 解决左滑, position值不正确
 * Author: Jooyer
 * Date: 2019-08-28
 * Time: 18:32
 */
class LooperSnapHelper(private val callback: OnPositionChangeListener) : PagerSnapHelper() {

    /**
     * 该方法会根据触发Fling操作的速率（参数velocityX和参数velocityY）来找到RecyclerView需要滚动到哪个位置，
     * 该位置对应的ItemView就是那个需要进行对齐的列表项。我们把这个位置称为targetSnapPosition，对应的View称为targetSnapView。
     * 如果找不到targetSnapPosition，就返回RecyclerView.NO_POSITION。
     * PS: 比如一屏可以显示5个 Item,  通过 参数velocityX和参数velocityY 计算的位置假设是  10, 则此时 我们把这个位置称为targetSnapPosition = 10 ,
     * 它就是最终列表应该显示的 Item 对应的位置, 这个 Item 对应的 ItemView 就是 对应的View称为targetSnapView
     */
    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager,
        velocityX: Int,
        velocityY: Int
    ): Int {
        val pos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        val nextPos = pos % layoutManager.itemCount
        callback.onPositionChange(nextPos)
        return nextPos
    }

}


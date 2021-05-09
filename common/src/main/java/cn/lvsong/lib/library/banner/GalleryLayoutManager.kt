package cn.lvsong.lib.library.banner

import android.content.Context
import kotlin.math.abs

/**
 *  http://www.10qianwan.com/articledetail/638119.html  -->  ViewPager2.PageTransformer
 * 参考: https://github.com/ZhangHao555/BannerRecyclerView
 * Desc: 画廊效果
 * Author: Jooyer
 * Date: 2020-09-16
 * Time: 18:17
 */

class GalleryLayoutManager(
    context: Context,
    spaceWidth: Int = 0,
    private val widthScale: Float = 0.85F,
    private val heightScale: Float = 0.85F,
    itemScrollTime: Long = 1200
) : HorizontalLayoutManager(context,spaceWidth, itemScrollTime) {

    override fun doWithItem() {
        if (heightScale >= 1 && widthScale >= 1) {
            return
        }
//        Log.e("GalleryLayoutManager", "=======childCount: $childCount")
        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue
            val itemMiddle = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.0F
            val screenMiddle = mOrientationHelper.totalSpace / 2.0F
//            Log.e(
//                "GalleryLayoutManager",
//                "=======itemMiddle: $itemMiddle, screenMiddle: $screenMiddle, index: $i"
//            )
            val interval = abs(screenMiddle - itemMiddle) * 1.0F
            if (interval < 0.0001F) {
                continue
            }
//            Log.e("GalleryLayoutManager", "=======interval: $interval")
            val ratioWidth = 1 - (1 - widthScale) * (interval / itemWidth)
            val ratioHeight = 1 - (1 - heightScale) * (interval / itemWidth)
//            Log.e(
//                "GalleryLayoutManager",
//                "=======ratioHeight: $ratioHeight, ratioWidth: $ratioWidth"
//            )
            child.scaleX = ratioWidth
            child.scaleY = ratioHeight
        }
    }
}
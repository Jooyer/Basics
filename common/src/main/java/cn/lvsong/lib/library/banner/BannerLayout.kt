package cn.lvsong.lib.library.banner

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import cn.lvsong.lib.library.R
import cn.lvsong.lib.library.utils.DensityUtil
import kotlin.math.abs

/** https://www.jianshu.com/p/1f72644bb560  -->  指示器
 *  https://www.jianshu.com/p/5ac538c067d9  -->  指示器
 *
 *  https://hub.fastgit.org/zguop/banner   --> 可以参考其 Banner 初始化
 *
 * Desc: Banner 容器
 * Author: Jooyer
 * Date: 2019-08-30
 * Time: 13:41
 */
class BannerLayout(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs),
    OnPositionChangeListener {

    /**
     * 轮询播放时间间隔,默认3秒,单位是毫秒
     */
    private var mLoopTime = 3000L

    /**
     * 记录当前滑动的位置
     */
    private var mCurrentPos = 0

    /**
     * 当滑动后,位置发生变化,如果需要监听,则设置此回调
     */
    private var mPositionChangeListener: OnPositionChangeListener? = null

    /**
     * RecyclerView 无限循环的核心
     */
    private val mLooperSnapHelper = LooperSnapHelper(this)

    /**
     * 自定义的 LayoutManager,自己控制 ItemView 的布局
     */
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    /**
     * Banner 载体
     */
    private lateinit var mBanner: RecyclerView

    private var mTotalScroll = 0F

    /**
     * 自动滑动的 Runnable
     */
    private val mAutoScrollRunnable = object : Runnable {
        override fun run() {
//            mIndicator?.onPageSelected(mCurrentPos)
            mCurrentPos = ++mCurrentPos % mLayoutManager.itemCount
//            Log.e("BannerLayout", "AutoScroll========mCurrentPos: $mCurrentPos")
            mBanner.smoothScrollToPosition(mCurrentPos)
            postDelayed(this, mLoopTime)
        }
    }

    private val mDelayRunnable = Runnable { mIndicator?.onPageSelected(mCurrentPos) }


    init {
        initView(context)
        // 防止 RecyclerView 中 Item 强占焦点
        descendantFocusability = FOCUS_BLOCK_DESCENDANTS
    }

    // 动态添加参考 --> https://www.jianshu.com/p/16e34f919e1a
    private fun initView(context: Context) {
        // RecyclerView
        mBanner = RecyclerView(context)
        mBanner.id = R.id.banner_scroll_view
        val bannerLp = LayoutParams(0, 0)
        bannerLp.startToStart = 0
        bannerLp.topToTop = 0
        bannerLp.endToEnd = 0
        bannerLp.bottomToBottom = 0
        mBanner.layoutParams = bannerLp
        addView(mBanner)

        // mBanner 滑动时
        mBanner.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (RecyclerView.SCROLL_STATE_IDLE == newState) { // 滑动停止
//                    Log.e(
//                        "BannerLayout",
//                        "onScrollStateChanged========mCurrentPos: $mCurrentPos"
//                    )
                    mTotalScroll = 0F
                    mPositionChangeListener?.onPositionChange(mCurrentPos)
                    postDelayed(mDelayRunnable, 200)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                mTotalScroll += abs(dx)
//                Log.e(
//                    "BannerLayout",
//                    "onScrolled========dx: $dx, mTotalScroll: $mTotalScroll, width: ${recyclerView.width}"
//                )
                mIndicator?.onPageScrolled(mTotalScroll / recyclerView.width)
            }
        })
    }

    /**
     * 自动滑动
     */
    private fun autoScroll(auto: Boolean) {
        removeCallbacks(mAutoScrollRunnable)
        if (auto && mLayoutManager.itemCount > 1) {
            postDelayed(mAutoScrollRunnable, mLoopTime)
        }
    }

    /**
     * 当手指按下时则不能自动滑动,在此处判断
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (MotionEvent.ACTION_DOWN == ev.actionMasked) { // 手指在 Banner 上,此时不再自动滑动
            autoScroll(false)
        } else if (MotionEvent.ACTION_UP == ev.actionMasked || MotionEvent.ACTION_CANCEL == ev.actionMasked) {
            autoScroll(true)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (null != mBanner.adapter) {
            autoScroll(true)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        autoScroll(false)
    }

    // 解决列表时滑动到2个Item交接位置卡住
    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (VISIBLE == visibility) {
            mBanner.smoothScrollToPosition(mCurrentPos)
        }
    }

    /**
     * 当手动滑动时,会触发 LooperSnapHelper.findTargetSnapPosition(),获得其移动后的位置
     */
    override fun onPositionChange(position: Int) {
        mCurrentPos = position
//        Log.e("BannerLayout", "onPositionChange========mCurrentPos: $mCurrentPos")
    }


/////////////////////////////////////////////////// 对外提供的方法  ////////////////////////////////////////////////////////

    private var mIndicator: Indicator? = null

    /**
     * 添加指示器
     */
    fun setIndicatorView(indicator: Indicator): BannerLayout {
        mIndicator?.apply { removeView(this.getIndicatorView()) }
        mIndicator = indicator
        addView(indicator.getIndicatorView(), indicator.getIndicatorViewLayoutParam())
        return this
    }

    /**
     * Item 自动循环出现的间隔,单位是毫秒
     */
    fun setLoopTime(loopTime: Long = 3000): BannerLayout {
        mLoopTime = loopTime
        return this
    }


    /**
     * 绑定管理器
     */
    fun setManager(manager: RecyclerView.LayoutManager): BannerLayout {
        mLayoutManager = manager
        mBanner.layoutManager = mLayoutManager
        return this
    }

    /**
     * 设置适配器
     */
    fun setAdapter(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>): BannerLayout {
        mBanner.adapter = adapter
        return this
    }

    /**
     * 最后调用此方法开始 轮播
     */
    fun loop() {
        if (null == mIndicator) {
            throw NullPointerException("必须调用 setIndicatorView() 设置指示器!!!")
        } else if (null == mBanner.adapter) {
            throw NullPointerException("必须调用 setAdapter() 设置适配器!!!")
        }

        // 初始时指示器
        mIndicator?.initIndicatorCount(mBanner.adapter!!.itemCount)
        mLooperSnapHelper.attachToRecyclerView(mBanner)
        Looper.myQueue().addIdleHandler {
            onResume()
            false
        }
    }


    /**
     * 提供 RecyclerView, 方便用户绑定数据
     * @param factor --> ItemView居中时左侧距离屏幕左边(或者右侧距离屏幕右边)间隔占 RecyclerView 宽度的比值
     * @param multiple -->  holder.itemView.layoutParams.width = itemWidth 这一句导致Item宽度没有占满父控件宽度
     *  下面添加这个ItemDecoration间隔,保证 holder.itemView 居中,如果 itemWidth = parent.width*(1 - 2*factor),
     *  也就是左右留白为  parent.width*factor,此时如果需要 ItemView 居中,则下面取值为 left = interval,
     *  同理,假设itemWidth = parent.width*(1 - 4*factor),则下面取值为 left = 2*interval
     *  结论: 设置 holder.itemView.layoutParams.width 减去N倍factor, 则这里 multiple = N/2
     *  @param spaceWidth --> Item 间隔
     *  @param widthScale --> 宽度缩放比率,默认1.0F,即不缩放
     *  @param heightScale --> 高度缩放比率,默认0.8F
     */
    @Deprecated("废弃")
    fun setGalleryBannerAdapter(
        adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
        spaceWidth: Int = 0,
        widthScale: Float = 1F,
        heightScale: Float = 0.8F
    ) {
//        mLayoutManager = GalleryLayoutManager(spaceWidth, widthScale, heightScale, mItemScrollTime)


//        mBanner.addItemDecoration(object : RecyclerView.ItemDecoration() {
//            override fun getItemOffsets(
//                outRect: Rect,
//                view: View,
//                parent: RecyclerView,
//                state: RecyclerView.State
//            ) {
//                val position = parent.getChildAdapterPosition(view)
//                val layoutManager = parent.layoutManager as GalleryLayoutManager
//                val count = layoutManager.itemCount
//                val interval = (parent.width * factor * multiple).toInt()
//                //   holder.itemView.layoutParams.width = itemWidth 这一句导致Item宽度没有沾满父控件宽度
//                //  下面添加这个ItemDecoration间隔,保证 holder.itemView 居中
//                // PS: 如果 itemWidth = parent.width*(1 - 2*factor),也就是左右留白为  parent.width*factor
//                // 此时如果需要 ItemView 居中,则下面取值为 left = interval
//                // 同理,假设itemWidth = parent.width*(1 - 4*factor),则下面取值为 left = 2*interval
//                //
//                outRect.apply {
//                    when (position) {
//                        0 -> {
//                            left = interval
//                        }
//                        count - 1 -> {
//                            right = interval
//                        }
//                    }
//                }
//            }
//        })

//        mBanner.layoutManager = mLayoutManager
//        mBanner.adapter = adapter
//        mLooperSnapHelper.attachToRecyclerView(mBanner)
//        Looper.myQueue().addIdleHandler {
//            onResume()
//            false
//        }
    }

    /**
     * 可以在 Activity/Fragment 生命周期内使用
     */
    fun onPause() {
        autoScroll(false)
    }

    /**
     * 可以在 Activity/Fragment 生命周期内使用
     */
    fun onResume() {
        autoScroll(true)
    }

    /**
     * 可以在 Activity/Fragment 生命周期内使用
     */
    fun onStop() {
        autoScroll(false)
    }

    /**
     * 当不需要指示器, 而是底部有一个横幅,上面有文本时,可以通过此回调自行处理
     */
    fun setOnPositionChangeListener(positionChangeListener: OnPositionChangeListener) {
        mPositionChangeListener = positionChangeListener
    }

}
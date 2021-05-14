package cn.lvsong.lib.library.banner

import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import cn.lvsong.lib.library.R
import org.jetbrains.annotations.NotNull

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
    private lateinit var mLayoutManager: HorizontalLayoutManager

    /**
     * Banner 载体
     */
    private lateinit var mBanner: RecyclerView

    /**
     * 滑动回调
     */
    private var mBannerScrollAdapter: BannerScrollAdapter? = null

    /**
     * 指示器
     */
    private var mIndicator: Indicator? = null

    /**
     * 自动滑动的 Runnable
     */
    private val mAutoScrollRunnable = object : Runnable {
        override fun run() {
            mCurrentPos = ++mCurrentPos % mLayoutManager.itemCount
            mBanner.smoothScrollToPosition(mCurrentPos)
            postDelayed(this, mLoopTime)
        }
    }

    init {
        initView(context)
        // 防止 RecyclerView 中 Item 强占焦点
        descendantFocusability = FOCUS_BLOCK_DESCENDANTS
    }

    // 动态添加参考 --> https://www.jianshu.com/p/16e34f919e1a
    private fun initView(context: Context) {
        mBanner = RecyclerView(context)
        mBanner.id = R.id.banner_scroll_view
        val bannerLp = LayoutParams(0, 0)
        bannerLp.startToStart = 0
        bannerLp.topToTop = 0
        bannerLp.endToEnd = 0
        bannerLp.bottomToBottom = 0
        mBanner.layoutParams = bannerLp
        addView(mBanner)
    }

    /**
     * 自动滑动
     * 参考 https://www.jb51.net/article/198584.htm 自动吸附效果
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
        if (MotionEvent.ACTION_DOWN == ev.action) { // 手指在 Banner 上,此时不再自动滑动
            autoScroll(false)
        } else if (MotionEvent.ACTION_UP == ev.action || MotionEvent.ACTION_CANCEL == ev.action) {
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

    // 解决列表时滑动部分切换了在回来时2个Item交接位置卡住
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
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////// 对外提供的方法  ////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 添加指示器
     */
    fun setIndicatorView(@NotNull indicator: Indicator): BannerLayout {
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
    fun setManager(@NotNull manager: HorizontalLayoutManager): BannerLayout {
        mLayoutManager = manager
        mBanner.layoutManager = mLayoutManager
        return this
    }

    /**
     * 设置适配器
     */
    fun setAdapter(@NotNull adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>): BannerLayout {
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
        // mBanner 滑动时
        mBanner.clearOnScrollListeners()
        mBannerScrollAdapter = object : BannerScrollAdapter(mBanner) {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                mIndicator?.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                mIndicator?.onPageSelected(position)
            }

        }
        mBanner.addOnScrollListener(mBannerScrollAdapter!!)
        // 这个位置不能颠倒了
        mLooperSnapHelper.attachToRecyclerView(mBanner)

        Looper.myQueue().addIdleHandler {
            onResume()
            false
        }
    }

    /**
     * 当数据有更新时需要调用此方法,它里面包含了对指示器的更新
     */
    fun notifyDataSetChanged() {
        mCurrentPos = 0
        mBanner.adapter?.notifyDataSetChanged()
        // 重新初始时指示器
        mIndicator?.initIndicatorCount(mBanner.adapter!!.itemCount)
        // 处理,开始 banner 只有一张图,更新后有多张图时不自动滚动
        autoScroll(true)
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
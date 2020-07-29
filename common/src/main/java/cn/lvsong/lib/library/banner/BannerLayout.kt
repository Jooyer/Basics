package cn.lvsong.lib.library.banner

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import cn.lvsong.lib.library.R
import cn.lvsong.lib.library.utils.DensityUtil

/**
 * Desc: Banner 容器
 * Author: Jooyer
 * Date: 2019-08-30
 * Time: 13:41
 */
class BannerLayout(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs),
        OnPositionChangeListener {

    enum class TYPE {
        NORMAL, STACK // 这个模式还没实现
    }

    /**
     * 指示器高度,默认 20dp
     */
    private var mIndicatorContainerHeight = DensityUtil.dp2pxRtInt(20F)

    /**
     * 指示器内部 View 的 margin,默认 5dp
     */
    private var mIndicatorMargin = DensityUtil.dp2pxRtInt(5F)

    /**
     * 轮询播放时间间隔,默认3秒,单位是毫秒
     */
    private var mLoopTime = 3000L

    /**
     * 记录当前滑动的位置
     */
    private var mCurrentPos = 0

    /**
     * 是否显示指示器,默认显示
     */
    private var mShowIndicatorView = true

    /**
     * 指示器选中状态图片
     */
    private var mSelectedDrawable: Drawable? = null

    /**
     * 指示器默认状态图片
     */
    private var mNormalDrawable: Drawable? = null

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
     * 指示器容器
     */
    private lateinit var mIndicatorContainer: LinearLayout

    /**
     * Banner 载体
     */
    private lateinit var mBanner: RecyclerView

    /**
     * 自动滑动的 Runnable
     */
    private val mAutoScrollRunnable = object : Runnable {
        override fun run() {
            mCurrentPos = ++mCurrentPos % mLayoutManager.itemCount
//            Log.e("BannerLayout", "AutoScroll========mCurrentPos: $mCurrentPos")
            mBanner.smoothScrollToPosition(mCurrentPos)
            postDelayed(this, mLoopTime)
        }
    }

    init {
        parse(context, attrs)
        initView(context)
    }

    private fun parse(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val aar = context.obtainStyledAttributes(it, R.styleable.BannerLayout)
            mIndicatorContainerHeight =
                    aar.getDimensionPixelSize(
                            R.styleable.BannerLayout_banner_indicator_height,
                            mIndicatorContainerHeight
                    )
            mIndicatorMargin =
                    aar.getDimensionPixelSize(
                            R.styleable.BannerLayout_banner_indicator_margin,
                            mIndicatorMargin
                    )
            mLoopTime = aar.getInt(R.styleable.BannerLayout_banner_loop_time, 3000).toLong()
            mShowIndicatorView =
                    aar.getBoolean(R.styleable.BannerLayout_banner_show_indicator, mShowIndicatorView)
            mSelectedDrawable =
                    aar.getDrawable(R.styleable.BannerLayout_banner_select_indicator_drawable)
            mNormalDrawable =
                    aar.getDrawable(R.styleable.BannerLayout_banner_normal_indicator_drawable)
            aar.recycle()
        }
    }

    // 动态添加参考 --> https://www.jianshu.com/p/16e34f919e1a
    private fun initView(context: Context) {
        // RecyclerView
        mBanner = RecyclerView(context)
        mBanner.id = R.id.banner_view
        val bannerLp = LayoutParams(0, 0)
        bannerLp.startToStart = 0
        bannerLp.topToTop = 0
        bannerLp.endToEnd = 0
        bannerLp.bottomToBottom = 0
        mBanner.layoutParams = bannerLp
        addView(mBanner)

        // 底部指示器的容器
        mIndicatorContainer = LinearLayout(context)
        mIndicatorContainer.orientation = LinearLayout.HORIZONTAL
        mIndicatorContainer.id = R.id.ll_indicator_container
        val mIndicatorContainerLp =
                LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mIndicatorContainerHeight)
        mIndicatorContainerLp.startToStart = 0
        mIndicatorContainerLp.endToEnd = 0
        mIndicatorContainerLp.bottomToBottom = 0
        mIndicatorContainerLp.bottomMargin = DensityUtil.dp2pxRtInt(5F)
        mIndicatorContainer.layoutParams = mIndicatorContainerLp
        addView(mIndicatorContainer)

        // mBanner 滑动时
        mBanner.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (RecyclerView.SCROLL_STATE_IDLE == newState) { // 滑动停止
                    mPositionChangeListener?.onPositionChange(mCurrentPos)
                    if (mShowIndicatorView) { // 指示器
                        changeIndicatorState()
                    }
//                    Log.e("BannerLayout","onScrolled========mCurrentPos: $mCurrentPos")
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                Log.e("BannerLayout","onScrolled========mCurrentPos: $mCurrentPos, dx: $dx")
            }
        })
    }

    /**
     * 添加指示器
     */
    private fun addIndicatorView(position: Int) {
        val indicatorView = AppCompatImageView(context)
        val indicatorViewLp = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                mIndicatorContainerHeight
        )
        indicatorViewLp.setMargins(mIndicatorMargin, 0, mIndicatorMargin, 0)
        indicatorView.layoutParams = indicatorViewLp
        if (1 == position) {
            indicatorView.setImageDrawable(mSelectedDrawable)
        } else {
            indicatorView.setImageDrawable(mNormalDrawable)
        }
        mIndicatorContainer.addView(indicatorView)
    }

    /**
     * 自动滑动
     */
    private fun autoScroll(auto: Boolean) {
        removeCallbacks(mAutoScrollRunnable)
        if (auto) {
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

    /**
     * 当手动滑动时,会触发 LooperSnapHelper.findTargetSnapPosition(),获得其移动后的位置
     */
    override fun onPositionChange(position: Int) {
//        mBanner.adapter?.let {
//            mCurrentPos = it.itemCount - position - 1
//        }
        mCurrentPos = position
        Log.e("BannerLayout", "onPositionChange========mCurrentPos: $mCurrentPos")
    }


    /**
     * 不显示指示器可以不调用此方法
     */
    private fun setBannerSize(size: Int) {
        if (size > 0 && mShowIndicatorView) {
            for (i in 1..size) {
                addIndicatorView(i)
            }
        }
    }

    /**
     * 当滑动改变时,需要更新指示器显示
     */
    private fun changeIndicatorState() {
        for (index in 0 until mIndicatorContainer.childCount) {
            val child = mIndicatorContainer.getChildAt(index) as AppCompatImageView
            if (mCurrentPos == index) {
                child.setImageDrawable(mSelectedDrawable)
            } else {
                child.setImageDrawable(mNormalDrawable)
            }
        }
    }


/////////////////////////////////////////////////// 对外提供的方法  ////////////////////////////////////////////////////////


    /**
     * 提供 RecyclerView, 方便用户绑定数据
     * @param spaceWidth --> Item 间隔
     */
    fun setBannerAdapter(
            adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
            spaceWidth: Int = 0
    ) {
        setBannerAdapter(TYPE.NORMAL, adapter, spaceWidth)
    }


    /**
     * 提供 RecyclerView, 方便用户绑定数据
     * @param spaceWidth --> Item 间隔
     * @param type --> 多种展示模式
     */
    fun setBannerAdapter(
            type: TYPE,
            adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
            spaceWidth: Int = 0
    ) {
        mLayoutManager = HorizontalLayoutManager(spaceWidth, mLoopTime)
        mBanner.layoutManager = mLayoutManager
        mBanner.adapter = adapter
        setBannerSize(adapter.itemCount)
        mLooperSnapHelper.attachToRecyclerView(mBanner)
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
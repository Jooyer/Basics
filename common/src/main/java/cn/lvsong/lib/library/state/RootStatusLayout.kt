package cn.lvsong.lib.library.state

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import cn.lvsong.lib.library.listener.OnClickFastListener
import cn.lvsong.lib.library.utils.DensityUtil


/**
 * Desc: 视图管理布局控件
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 11:23
 */
class RootStatusLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null, 0)

    /**
     * loading 加载id
     */
    private val LAYOUT_LOADING_ID = 1

    /**
     * 内容id
     */
    private val LAYOUT_CONTENT_ID = 2

    /**
     * 异常id
     */
    private val LAYOUT_ERROR_ID = 3

    /**
     * 网络异常id
     */
    private val LAYOUT_NETWORK_ERROR_ID = 4

    /**
     * 空数据id
     */
    private val LAYOUT_EMPTY_ID = 5

    /**
     * 存放布局集合
     */
    private val mLayoutViews = SparseArray<View>()

    /**
     * 视图管理器
     */
    private lateinit var mStatusLayoutManager: StatusManager

    /**
     * 当 Activity / Fragment 存在 Toolbar 时,需将StatusManager往下移动 Toolbar高度
     */
    private var mTransY = 0

    /**
     * 点击重试按钮回调
     */
    private var onRetryListener: OnRetryListener? = null

    fun setTransY(transY: Int) {
        mTransY = transY
    }

    fun setStatusManager(manager: StatusManager) {
        mStatusLayoutManager = manager
        addAllLayoutViewsToRoot()
    }

    private fun addAllLayoutViewsToRoot() {
        val params = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        if (0 != mStatusLayoutManager.mContentLayoutResId) {
            addLayoutResId(mStatusLayoutManager.mContentLayoutResId, LAYOUT_CONTENT_ID, params)
        } else if (null != mStatusLayoutManager.mContentLayoutView) {
            addLayoutView(mStatusLayoutManager.mContentLayoutView!!, params)
        }

        if (0 != mStatusLayoutManager.mLoadingLayoutResId) {
            addLayoutResId(mStatusLayoutManager.mLoadingLayoutResId, LAYOUT_LOADING_ID, params)
        }

        if (null != mStatusLayoutManager.mEmptyDataVs) {
            addView(mStatusLayoutManager.mEmptyDataVs, params)
        }

        if (null != mStatusLayoutManager.mErrorVs) {
            addView(mStatusLayoutManager.mErrorVs, params)
        }

        if (null != mStatusLayoutManager.mNetworkErrorVs) {
            addView(mStatusLayoutManager.mNetworkErrorVs, params)
        }
    }


//    private fun getCustomToolbar(viewGroup: ViewGroup) {
//        for (i in 0 until viewGroup.childCount) {
//            val child = viewGroup.getChildAt(i)
//            if (child is StatusProvider) {
//                val params = child.layoutParams
//                mOffsetY = if (ViewGroup.LayoutParams.WRAP_CONTENT == params.height) {
//                    params.height = dp2px(50F).toInt()
//                    if (child.hasShadow()) dp2px(45F).toInt() else params.height
//                } else {
//                    if (child.hasShadow()) (params.height - dp2px(5F)).toInt() else params.height
//                }
//
//                if (0 == params.width) { // 解决 ConstraintLayout 中使用时,给 android:layout_width="0dp" 导致视图宽度为0
//                    params.width = ScreenUtils.getRealWidth(context)
//                }
//
//                val childParent = child.parent as ViewGroup
//                // 移除掉原来的,则会导致界面错位,添加一个占位的,但是占位视图设置为 View.INVISIBLE(不可见的)
//                childParent.removeView(child)
//                // 占位View
//                val view = View(context)
//                view.setBackgroundColor(Color.TRANSPARENT)
//                // 保证原来的ID不变,是防止其他控件对此控件有依赖或者位置关系
//                view.id = child.id
//                view.visibility = View.INVISIBLE
//                childParent.addView(view, 0, params)
//                child.id = R.id.ct_tool_bar
//                addView(child, params)
//                break
//            } else {
//                if (child is ViewGroup) {
//                    getCustomToolbar(child)
//                }
//            }
//        }
//    }


    private fun addLayoutView(
        layoutView: View,
        param: ConstraintLayout.LayoutParams
    ) {
        mLayoutViews.put(LAYOUT_CONTENT_ID, layoutView)
        addView(layoutView, param)
    }

    private fun addLayoutResId(
        @LayoutRes layoutResId: Int, layoutId: Int,
        param: ConstraintLayout.LayoutParams
    ) {
        val view: View = LayoutInflater.from(context)
            .inflate(layoutResId, null)
        mLayoutViews.put(layoutId, view)
        if (LAYOUT_LOADING_ID == layoutId) {
            if (-1 != mStatusLayoutManager.mLoadingViewBackgroundColor)
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        mStatusLayoutManager.mLoadingViewBackgroundColor
                    )
                )
            view.visibility = View.GONE
        }
        addView(view, param)
    }

    /**
     * 显示loading
     */
    fun showLoading() {
        if (mLayoutViews.get(LAYOUT_LOADING_ID) != null) {
            showHideViewById(LAYOUT_LOADING_ID)
        }
    }

    /**
     * 显示内容
     */
    fun showContent() {
        if (mLayoutViews.get(LAYOUT_CONTENT_ID) != null)
            showHideViewById(LAYOUT_CONTENT_ID)
    }

    /**
     * 显示空数据
     */
    fun showEmptyData() {
        if (inflateLayout(LAYOUT_EMPTY_ID))
            showHideViewById(LAYOUT_EMPTY_ID)
    }

    /**
     * 显示网络异常
     */
    fun showNetWorkError() {
        if (inflateLayout(LAYOUT_NETWORK_ERROR_ID))
            showHideViewById(LAYOUT_NETWORK_ERROR_ID)
    }

    /**
     * 显示异常
     */
    fun showError() {
        if (inflateLayout(LAYOUT_ERROR_ID))
            showHideViewById(LAYOUT_ERROR_ID)
    }

    fun setOnRetryListener(listener: OnRetryListener?) {
        onRetryListener = listener
    }

    /**
     * 显示当前的,隐藏其他
     * @param layoutId --> 当前需要显示的 View ID
     */
    private fun showHideViewById(layoutId: Int) {
        for (i in 0 until mLayoutViews.size()) {
            val value = mLayoutViews.valueAt(i)
            when (val key = mLayoutViews.keyAt(i)) {
                LAYOUT_LOADING_ID -> { // 加载布局
                    if (layoutId == key) {   // 显示该 View
                        value.visibility = View.VISIBLE
                        val param = value.layoutParams as LayoutParams
                        param.topMargin = mTransY
                        value.layoutParams = param
                        if (value is ViewGroup) {
                            changeAnimator(value, true)
                        }
                    } else if (View.VISIBLE == value.visibility) {
                        value.animate()
                            .alpha(0F)
                            .setDuration(300)
                            .withEndAction {
                                value.visibility = View.GONE
                                value.alpha = 1F
                                if (value is ViewGroup) {
                                    changeAnimator(value, false)
                                }
                            }
                    }
                }
                LAYOUT_CONTENT_ID -> { // 内容布局一直显示
                    value.visibility = View.VISIBLE
                    value.translationY = 0F
                }
                LAYOUT_ERROR_ID -> { // 异常布局
                    if (layoutId == key) {   // 显示该 View
                        value.visibility = View.VISIBLE
                        val param = value.layoutParams as LayoutParams
                        param.topMargin = mTransY
                        value.layoutParams = param
                    } else {
                        value.visibility = View.GONE
                    }
                }
                LAYOUT_NETWORK_ERROR_ID -> { // 网络错误
                    if (layoutId == key) {   // 显示该 View
                        value.visibility = View.VISIBLE
                        val param = value.layoutParams as LayoutParams
                        param.topMargin = mTransY
                        value.layoutParams = param
                    } else {
                        value.visibility = View.GONE
                    }
                }
                LAYOUT_EMPTY_ID -> { // 空视图
                    if (layoutId == key) {   // 显示该 View
                        value.visibility = View.VISIBLE
                        val param = value.layoutParams as LayoutParams
                        param.topMargin = mTransY
                        value.layoutParams = param
                    } else {
                        value.visibility = View.GONE
                    }
                }
            }
        }

    }

    private fun changeAnimator(viewGroup: ViewGroup, start: Boolean) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is StartAndStopAnimController) {
                if (start){
                    child.onStartAnimator()
                }else{
                    child.onStopAnimator()
                }
                break
            } else {
                if (child is ViewGroup) {
                    changeAnimator(child, start)
                }
            }
        }
    }

    /**
     * 加载 StubView
     */
    private fun inflateLayout(layoutId: Int): Boolean {
        var isShow = false
        when (layoutId) {
            LAYOUT_NETWORK_ERROR_ID -> {
                isShow = when {
                    null != mStatusLayoutManager.mNetworkErrorView -> {
                        retryLoad(
                            mStatusLayoutManager.mNetworkErrorView!!,
                            mStatusLayoutManager.mNetWorkErrorRetryViewId
                        )
                        mLayoutViews.put(layoutId, mStatusLayoutManager.mNetworkErrorView!!)
                        return true
                    }
                    null != mStatusLayoutManager.mNetworkErrorVs -> {
                        val view: View = mStatusLayoutManager.mNetworkErrorVs!!.inflate()
                        mStatusLayoutManager.mNetworkErrorView = view
                        retryLoad(view, mStatusLayoutManager.mNetWorkErrorRetryViewId)
                        mLayoutViews.put(layoutId, view)
                        true
                    }
                    else -> false
                }
            }
            LAYOUT_ERROR_ID -> {
                isShow = when {
                    null != mStatusLayoutManager.mErrorView -> {
                        retryLoad(
                            mStatusLayoutManager.mErrorView!!,
                            mStatusLayoutManager.mErrorRetryViewId
                        )
                        mLayoutViews.put(layoutId, mStatusLayoutManager.mErrorView!!)
                        return true
                    }
                    null != mStatusLayoutManager.mErrorVs -> {
                        val view: View = mStatusLayoutManager.mErrorVs!!.inflate()
                        mStatusLayoutManager.mErrorView = view
                        retryLoad(view, mStatusLayoutManager.mErrorRetryViewId)
                        mLayoutViews.put(layoutId, view)
                        true
                    }
                    else -> false
                }
            }
            LAYOUT_EMPTY_ID -> {
                isShow = when {
                    null != mStatusLayoutManager.mEmptyDataView -> {
                        retryLoad(
                            mStatusLayoutManager.mEmptyDataView!!,
                            mStatusLayoutManager.mEmptyDataRetryViewId
                        )
                        mLayoutViews.put(layoutId, mStatusLayoutManager.mEmptyDataView!!)
                        return true
                    }
                    null != mStatusLayoutManager.mEmptyDataVs -> {
                        val view: View = mStatusLayoutManager.mEmptyDataVs!!.inflate()
                        mStatusLayoutManager.mEmptyDataView = view
                        retryLoad(view, mStatusLayoutManager.mEmptyDataRetryViewId)
                        mLayoutViews.put(layoutId, view)
                        true
                    }
                    else -> false
                }
            }
        }
        return isShow
    }

    /**
     *  加载重试按钮,并绑定监听
     */
    private fun retryLoad(view: View, layoutResId: Int) {
        val retryView: View? = view.findViewById(
            if (0 != mStatusLayoutManager.mRetryViewId) {
                mStatusLayoutManager.mRetryViewId
            } else {
                layoutResId
            }
        ) ?: return

        retryView?.setOnClickListener(object : OnClickFastListener(1200L) {
            override fun onFastClick(v: View) {
                onRetryListener?.onRetry()
            }
        })
    }

}

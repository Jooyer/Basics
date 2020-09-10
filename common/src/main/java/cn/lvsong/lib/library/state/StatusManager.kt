package  cn.lvsong.lib.library.state

import android.content.Context
import android.view.View
import android.view.ViewStub
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import cn.lvsong.lib.library.R

/**
 *      修饰符	                类成员	                    顶级声明
 *   public(default)	 任何地方可见	                    任何地方可见
 *  internal	        在module中可见	                在module中可见
 *  protected	        在子类中可见                      	–
 *  private	            在类内部可见	                    在当前文件中可见
 *
 *  值得一提的是，拓展方法并不能访问private和protected成员
 */

/**
 * Desc: 视图管理器 , 1. 加载中 ,2. 加载时网络异常, 3. 加载时发生其他错误 , 4. 空数据
 * PS: 使用注意:  使用需要重写上面所说的四种布局,但是如果涉及到重试按钮,则按钮ID必须是 R.id.view_retry_load_data ,至于用什么控件不限制
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 11:46
 */
class StatusManager(builder: Builder) {

    private var mContext: Context

    /**
     * 所有视图的根布局
     */
    private var mRootFrameLayout: RootStatusLayout

    /**
     * 内容视图
     */
    internal var mContentLayoutView: View

    /**
     * 内容视图布局
     */
    internal var mContentLayoutResId: Int = 0

    /**
     * Loading视图
     */
    internal var mLoadingLayoutResId: Int = 0

    /**
     * 网络异常 ViewStub
     */
    internal var mNetworkErrorVs: ViewStub? = null

    /**
     * 网络异常 View
     */
    internal var mNetworkErrorView: View? = null

    /**
     * 网络异常重试按钮 ID
     */
    internal var mNetWorkErrorRetryViewId: Int = 0

    /**
     * 空视图 ViewStub
     */
    internal var mEmptyDataVs: ViewStub? = null

    /**
     * 空视图View
     */
    internal var mEmptyDataView: View? = null

    /**
     * 空视图重试按钮 ID
     */
    internal var mEmptyDataRetryViewId: Int = 0

    /**
     * 请求错误 ViewStub
     */
    internal var mErrorVs: ViewStub? = null

    /**
     * 请求错误 View
     */
    internal var mErrorView: View? = null

    /**
     * 请求错误重试按钮 ID
     */
    internal var mErrorRetryViewId: Int = 0

    /**
     * 重试按钮 ID
     */
    internal var mRetryViewId: Int = 0

    /**
     * 开始加载时间
     */
    private var mStartTime: Long = 0

    /**
     * 延迟显示 ContentView
     */
    private var mDelayTime: Long = 1200

    /**
     * LoadingView背景色
     */
    @ColorRes
     var mLoadingViewBackgroundColor = R.color.main_theme_color

    /**
     * 显示loading
     */
    fun showLoading() {
        mRootFrameLayout.let { root ->
            mStartTime = System.currentTimeMillis()
            root.showLoading()
        }

    }

    /**
     * 显示内容
     */
    fun showContent() {
        val endTime = System.currentTimeMillis()
        if (endTime - mStartTime >= mDelayTime) {
            delayShowContent(0)
        } else {
            delayShowContent(mDelayTime + mStartTime - endTime)
        }
    }

    /**
     * 立刻显示内容
     */
    fun showContentImmediately(){
        mRootFrameLayout.showContent()
    }

    fun delayShowContent(delay: Long) {
        mRootFrameLayout.let { root ->
            root.postDelayed({
                root.showContent()
            }, delay)
        }
    }

    /**
     * 显示空数据
     */
    fun showEmptyData() {
        mRootFrameLayout.showEmptyData()
    }

    /**
     * 显示网络异常
     */
    fun showNetWorkError() {
        mRootFrameLayout.showNetWorkError()
    }

    /**
     * 显示异常
     */
    fun showError() {
        mRootFrameLayout.showError()
    }

    /**
     * 得到root 布局
     */
    fun getRootLayout(): View {
        return mRootFrameLayout
    }

    /**
     * 返回 CustomToolbar 或者 自定义的Toolbar 等实现  StatusProvider 接口的控件
     */
    fun getCustomView():View {
        return mRootFrameLayout.findViewById(R.id.ct_tool_bar)
    }

    class Builder(val context: Context) {
        var loadingLayoutResId: Int = 0
        var contentLayoutResId: Int = 0

        lateinit var contentLayoutView: View

        lateinit var netWorkErrorVs: ViewStub

        var netWorkErrorRetryViewId: Int = 0

        lateinit var emptyDataVs: ViewStub

        var emptyDataRetryViewId: Int = 0

        lateinit var errorVs: ViewStub

        var errorRetryViewId: Int = 0

        var retryViewId: Int = 0

        /**
         * LoadingView背景色
         */
        @ColorRes
        var mLoadingViewBackgroundColor = R.color.main_theme_color

        var delayTime: Long = 1200

        var onRetryListener: OnRetryListener? = null

        fun loadingView(@LayoutRes loadingLayout: Int): Builder {
            this.loadingLayoutResId = loadingLayout
            return this
        }

        // https://blog.csdn.net/a740169405/article/details/50351013
        // https://www.jianshu.com/p/63a066e7a5a9
        fun netWorkErrorView(@LayoutRes netWorkErrorLayout: Int): Builder {
            netWorkErrorVs = ViewStub(context)
            netWorkErrorVs.layoutResource = netWorkErrorLayout
            return this
        }

        fun emptyDataView(@LayoutRes noDataLayout: Int): Builder {
            emptyDataVs = ViewStub(context)
            emptyDataVs.layoutResource = noDataLayout
            return this
        }

        fun errorView(@LayoutRes errorLayout: Int): Builder {
            errorVs = ViewStub(context)
            errorVs.layoutResource = errorLayout
            return this
        }

        fun contentView(contentLayoutView: View): Builder {
            this.contentLayoutView = contentLayoutView
            return this
        }

        fun contentView(@LayoutRes contentLayout: Int): Builder {
            this.contentLayoutResId = contentLayout
            return this
        }

        fun netWorkErrorRetryViewId(@IdRes netWorkErrorRetryViewId: Int): Builder {
            this.netWorkErrorRetryViewId = netWorkErrorRetryViewId
            return this
        }

        fun emptyDataRetryViewId(@IdRes emptyDataRetryViewId: Int): Builder {
            this.emptyDataRetryViewId = emptyDataRetryViewId
            return this
        }

            fun errorRetryViewId(@IdRes errorRetryViewId: Int): Builder {
            this.errorRetryViewId = errorRetryViewId
            return this
        }

        fun retryViewId(@IdRes retryViewId: Int): Builder {
            this.retryViewId = retryViewId
            return this
        }

        /**
         * 设置LoadingView背景色
         */
        fun setLoadingViewBackgroundColor(@ColorRes bgColor: Int): Builder {
            mLoadingViewBackgroundColor = bgColor
            return this
        }

        fun delayTime(delayTime: Long): Builder {
            this.delayTime = delayTime
            return this
        }

        fun onRetryListener(onRetryListener: OnRetryListener): Builder {
            this.onRetryListener = onRetryListener
            return this
        }

        fun build(): StatusManager {
            return StatusManager(this)
        }
    }

    companion object {
        fun newBuilder(context: Context): Builder {
            return Builder(context)
        }
    }

    init {
        mContext = builder.context
        mLoadingLayoutResId = builder.loadingLayoutResId
        mNetworkErrorVs = builder.netWorkErrorVs
        mNetWorkErrorRetryViewId = builder.netWorkErrorRetryViewId
        mEmptyDataVs = builder.emptyDataVs
        mEmptyDataRetryViewId = builder.emptyDataRetryViewId
        mErrorVs = builder.errorVs
        mErrorRetryViewId = builder.errorRetryViewId
        mContentLayoutResId = builder.contentLayoutResId
        mRetryViewId = builder.retryViewId
        mContentLayoutView = builder.contentLayoutView
        mRootFrameLayout = RootStatusLayout(mContext)
        mDelayTime = builder.delayTime
        mLoadingViewBackgroundColor = builder.mLoadingViewBackgroundColor
        mRootFrameLayout.setOnRetryListener(builder.onRetryListener)
        mRootFrameLayout.setStatusManager(this)
    }


}

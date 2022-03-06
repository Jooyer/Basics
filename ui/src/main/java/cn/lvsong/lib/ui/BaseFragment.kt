package cn.lvsong.lib.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import cn.lvsong.lib.library.state.OnLoadingAnimatorEndListener
import cn.lvsong.lib.library.utils.OnLazyClickListener
import cn.lvsong.lib.library.state.OnRetryListener
import cn.lvsong.lib.library.state.StatusManager


/** 参考: https://blog.csdn.net/qq_36486247/article/details/102531304
 *
 *  https://juejin.im/post/5d63cdf7f265da03ed195f68   --> 提供一种新的懒加载方法,其实就是提前加载View,可以和本例结合
 *
 * Desc: Fragment,带懒加载功能
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 11:16
 */
abstract class BaseFragment<T : ViewBinding> : Fragment(),
    OnLazyClickListener, OnRetryListener, OnLoadingAnimatorEndListener {

    open lateinit var mActivity: FragmentActivity

    /**
     * 判断是不是第一次resume
     */
    private var isFirstResume = true

    /**
     * 根布局
     */
    private var mRoot: View? = null

    /**
     * 请求网络异常等界面管理
     */
    var mStatusManager: StatusManager? = null

    // ViewBinding 对象
    var mBinding: T? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            mActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 解决Android jetpack导航组件Navigation返回Fragment重走onCreateView方法刷新视图的问题 步骤1
        return if (null == mBinding || null == mBinding?.root) {
            mRoot = initStatusManager(inflater, container, savedInstanceState)
            mRoot
        } else {
            mRoot
        }
    }


    /**
     * 此函数开始数据加载的操作，且仅调用一次
     * 主要是加载动画,初始化展示数据的布局
     */
    private fun initStatusManager(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (0 != getLayoutId()) {
            val contentView = inflater.inflate(getLayoutId(), container, false)
            initializedViews(savedInstanceState, contentView)
            mBinding = getViewBinging(contentView)
            return if (useStatusManager()) {
                initialized(contentView)
            } else {
                contentView.visibility = View.VISIBLE
                contentView
            }
        }
        throw IllegalStateException("getLayoutId() 必须调用,且返回正常的布局ID")
    }

    private fun initialized(contentView: View): View {
        mStatusManager = StatusManager.newBuilder(contentView.context)
            .contentView(contentView)
            .loadingView(getLoadingViewLayoutId())
            .emptyDataView(getEmptyDataViewLayoutId())
            .netWorkErrorView(getNetWorkErrorViewLayoutId())
            .errorView(getErrorViewLayoutId())
            .retryViewId(R.id.view_retry_load_data)
            .setLoadingViewBackgroundColor(if (-1 == getLoadingViewBackgroundColor()) StatusConfig.INSTANCE.getLoadingViewBackgroundColor() else getLoadingViewBackgroundColor())
            .onRetryListener(this)
            .onLoadingAnimatorEndListener(if (useLoadingAnimatorEndListener()) this else null)
            .build()
        mStatusManager?.setTransY(if (-1 == getTransY()) StatusConfig.INSTANCE.getTranslateY() else getTransY())
        return mStatusManager?.getRootLayout()!!
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 解决Android jetpack导航组件Navigation返回Fragment重走onCreateView方法刷新视图的问题 步骤2
        if (isFirstResume) { // 如果有做相关处理则不再进行
            /**
             * 当请求状态发生变化时,进行分发
             */
            getCurrentViewModel()?.let {
                // 处理网络请求状态
                it.mLoadState.observe(viewLifecycleOwner, Observer { loadState ->
                    when (loadState) {
                        is LoadState.Loading -> {
                            onLoading(loadState.apiType, loadState.subType, loadState.msg)
                        }
                        is LoadState.Failure -> {
                            onFailure(
                                loadState.code,
                                loadState.apiType,
                                loadState.subType,
                                loadState.msg
                            )
                        }
                        is LoadState.NetError -> {
                            onNetError(
                                loadState.code,
                                loadState.apiType,
                                loadState.subType,
                                loadState.msg
                            )
                        }
                        else -> {
                            onSuccess(
                                loadState.code,
                                loadState.apiType,
                                loadState.subType,
                                loadState.msg
                            )
                        }
                    }
                })
            }

            setLogic()
            bindEvent()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (null != savedInstanceState) {
            // 可以做一些初始化工作
            val ft = parentFragmentManager.beginTransaction()
            if (isHidden) {
                ft.hide(this)
            } else {
                ft.show(this)
            }
            ft.commit()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFirstResume) {
            isFirstResume = false
            getCurrentViewModel()?.let {
                // 增加网络监听能力
                lifecycle.addObserver(it)
            }
            // 懒加载,处理数据
            onFirstUserVisible()
        } else {
            onUserVisible()
        }
    }

    /**
     * 第一次对用户可见的时候调用，在这里懒加载数据
     */
    open fun onFirstUserVisible() {

    }

    /**
     * 第二次包括第二次对用户可见的时候调用
     */
    open fun onUserVisible() {

    }

    /**
     * 获取ViewBinging对象
     */
    abstract fun getViewBinging(view: View): T

    /**
     * 展示布局
     */
    abstract fun getLayoutId(): Int

    // 在 Kotlin 中这个方法就没有必要重写了
    open fun initializedViews(savedInstanceState: Bundle?, contentView: View) {

    }

    /**
     * 实现过程
     */
    abstract fun setLogic()

    /**
     * 绑定监听
     */
    open fun bindEvent() {

    }

    /**
     *  当 Activity / Fragment 存在 Toolbar 时,需将StatusManager往下移动 Toolbar高度
     *  可以使用全局配置
     *  {@link cn.lvsong.lib.ui.mvp.StatusConfig }
     *  如果全局不合适,可以重写下面方法
     *  PS: 注意,返回的是 dp
     */
    open fun getTransY(): Int {
        return -1
    }


    /**
     * 设置LoadingView背景色
     */
    open fun getLoadingViewBackgroundColor(): Int {
        return -1
    }

    /**
     * 是否需要Loading动画结束后回调,默认是不需要的, 如果需要重写此方法并返回 true
     * 如果需要则自定义的 LoadingView 需要参考 ChrysanthemumView 重写 setOnLoadingAnimatorEndListener
     * 并且在适当的时机调用 OnLoadingAnimatorEndListener.onLoadingAnimatorEnd()
     */
    open fun useLoadingAnimatorEndListener(): Boolean {
        return false
    }

    /**
     *  是否使用视图布局管理器
     */
    open fun useStatusManager(): Boolean {
        return false
    }

    /**
     * 返回加载中布局ID
     */
    open fun getLoadingViewLayoutId() = R.layout.common_ui_loading_page

    /**
     * 返回空视图布局ID
     */
    open fun getEmptyDataViewLayoutId() = R.layout.common_ui_empty_page

    /**
     * 返回网路异常布局ID
     */
    open fun getNetWorkErrorViewLayoutId() = R.layout.common_ui_net_error_page

    /**
     * 返回错误/其他异常布局ID
     */
    open fun getErrorViewLayoutId() = R.layout.common_ui_error_page


    /**
     * 点击StatusManager视图中重试按钮
     */
    override fun onRetry() {

    }

    /**
     * 带延迟过滤的点击事件监听,防抖动
     * eg:  view.setOnClickListener(this) , 重写下面方法,则点击后会回调下面这个方法
     */
    override fun onTriggerClick(view: View) {

    }

    /**
     * 返回当前 Activity/Fragment 的 ViewModel
     * 需要根据请求不同状态显示UI效果时,则可以重写此方法
     */
    open fun getCurrentViewModel(): BaseViewModel? = null

    /**
     * 加载中,按需重写
     * @param apiType --> 区别不同请求接口
     * @param subType --> 同一种 code 又有不同提示,
     * eg: 获取列表,请求成功(code = 200)了,但是分为 列表有数据和列表为空,此时 apiType = 1(请求列表接口) , subType = 1(列表不为空) || subType = 2(列表为空)
     * @param msg --> 提示信息
     */
    open fun onLoading(apiType: Int = 0, subType: Int, msg: String = "") {

    }

    /**
     * 加载成功,按需重写
     * @param code --> 状态码
     * @param apiType --> 区别不同请求接口
     * @param subType --> 同一种 code 又有不同提示,
     * eg: 获取列表,请求成功(code = 200)了,但是分为 列表有数据和列表为空,此时 apiType = 1(请求列表接口) , subType = 1(列表不为空) || subType = 2(列表为空)
     * @param msg --> 提示信息
     */
    open fun onSuccess(code: Int = 200, apiType: Int = 0, subType: Int, msg: String = "") {

    }

    /**
     * 加载失败,按需重写
     *  @param code --> 状态码
     *  @param apiType --> 区别不同请求接口
     * @param subType --> 同一种 code 又有不同提示,
     * eg: 获取列表,请求成功(code = 200)了,但是分为 列表有数据和列表为空,此时 apiType = 1(请求列表接口) , subType = 1(列表不为空) || subType = 2(列表为空)
     *  @param msg --> 提示信息
     */
    open fun onFailure(code: Int = 200, apiType: Int = 0, subType: Int, msg: String = "") {

    }


    /**
     * 加载失败,按需重写
     *  @param code --> 状态码
     *  @param apiType --> 区别不同请求接口
     * @param subType --> 同一种 code 又有不同提示,
     * eg: 获取列表,请求成功(code = 200)了,但是分为 列表有数据和列表为空,此时 apiType = 1(请求列表接口) , subType = 1(列表不为空) || subType = 2(列表为空)
     *  @param msg --> 提示信息
     */
    open fun onNetError(code: Int = 200, apiType: Int = 0, subType: Int, msg: String = "") {

    }

    /**
     * loading动画结束时回调,比如转一圈结束了则会回调
     */
    override fun onLoadingAnimatorEnd() {

    }

}

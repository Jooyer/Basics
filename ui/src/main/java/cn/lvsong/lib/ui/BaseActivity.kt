package cn.lvsong.lib.ui

import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.lvsong.lib.library.utils.OnLazyClickListener
import cn.lvsong.lib.library.utils.StatusBarUtil
import cn.lvsong.lib.library.state.OnRetryListener
import cn.lvsong.lib.library.state.StatusManager

/**
 * https://segmentfault.com/q/1010000013729036
 * 当没有具体 Presenter 时,可以参考上面,写成: StatusActivity : BaseActivity<BasePresenter<*,*>>
 */

/** EasyMVP --> 可以参考其注解方式生成 Presenter
 * Desc: MVP 基类 Activity
 * Author: Jooyer
 * Date: 2018-07-24
 * Time: 12:49
 */
abstract class BaseActivity : AppCompatActivity(), OnRetryListener, OnLazyClickListener {

    /**
     * 页面显示加载中,加载失败等管理器
     */
    var mStatusManager: StatusManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.transparentStatusBar(
            this,
            if (-1 == getStatusBarColor()) StatusConfig.INSTANCE.getStatusBarColor() else getStatusBarColor(),
            if (-1 == needUseImmersive()) 1 == StatusConfig.INSTANCE.needUseImmersive() else 1 == needUseImmersive()
        )

        if (-1 == getDarkModel()) {
            StatusBarUtil.changeStatusTextColor(this, 2 == StatusConfig.INSTANCE.getDarkModel())
        } else {
            StatusBarUtil.changeStatusTextColor(this, 2 == getDarkModel())
        }

        requestWindowFeature()
        super.onCreate(savedInstanceState)

        if (useStartAnim()) {
            overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain)
        }

        if (0 != getLayoutId()) {
            setContentView(initStatusManager(savedInstanceState))
        }

        /**
         * 当请求状态发生变化时,进行分发
         */
        getCurrentViewModel()?.let {
            it.mLoadState.observe(this, Observer { loadState ->
                when (loadState) {
                    is LoadState.Loading -> {
                        onLoading(loadState.msg, loadState.type)
                    }
                    is LoadState.Failure -> {
                        onFailure(loadState.msg, loadState.type)
                    }
                    else -> {
                        onSuccess(loadState.msg, loadState.type)
                    }
                }
            })
        }

    }

    /**
     * 1 --> 状态栏文本是黑色, 2 --> 状态栏文本是白色
     * 默认是黑色
     */
    open fun getDarkModel(): Int {
        return -1
    }

    override fun onAttachedToWindow() {
        setLogic()
        bindEvent()
        Looper.myQueue().addIdleHandler {
            onLoad()
            false
        }
    }

    // 可以放这里加载数据,此时界面UI绘制完成
    open fun onLoad() {

    }

    /**
     * 返回当前 Activity/Fragment 的 ViewModel
     * 需要根据请求不同状态显示UI效果时,则可以重写此方法
     */
    open fun getCurrentViewModel(): BaseViewModel? = null

    /**
     * 初始化状态管理器
     */
    private fun initStatusManager(savedInstanceState: Bundle?): View {
        if (0 != getLayoutId()) {
            val contentView = LayoutInflater.from(this)
                .inflate(getLayoutId(), null)
            initializedViews(savedInstanceState, contentView)
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
        mStatusManager = StatusManager.newBuilder(this)
            .contentView(contentView)
            .loadingView(getLoadingViewLayoutId())
            .emptyDataView(getEmptyDataViewLayoutId())
            .netWorkErrorView(getNetWorkErrorViewLayoutId())
            .errorView(getErrorViewLayoutId())
            .retryViewId(R.id.view_retry_load_data)
            .setLoadingViewBackgroundColor(if (-1 == setLoadingViewBackgroundColor()) StatusConfig.INSTANCE.getLoadingViewBackgroundColor() else setLoadingViewBackgroundColor())
            .onRetryListener(this)
            .build()
        mStatusManager?.showLoading()
        return mStatusManager?.getRootLayout()!!
    }

    /**
     * 设置状态栏颜色,默认是白色,根据需要重写
     */
    open fun getStatusBarColor(): Int {
        return -1
    }

    /**
     * 是否 fitsSystemWindows, 即在顶部加入一个padding,默认不加入
     * 0 --> 不加, 1 --> 加上
     */
    open fun needUseImmersive(): Int {
        return -1
    }

    /**
     * 设置LoadingView背景色
     */
    open fun setLoadingViewBackgroundColor(): Int {
        return -1
    }


    /**
     * 是否使用启动动画,默认是true
     */
    open fun useStartAnim(): Boolean {
        return true
    }

    override fun finish() {
        super.finish()
        if (useStartAnim()) {
            overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_out)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //结束并移除任务列表
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask()
        }
    }

    /**
     * 加载中,按需重写
     * @param msg --> 提示信息
     * @param type --> 区别不同请求接口
     */
    open fun onLoading(msg: String = "", type: Int = 0) {

    }

    /**
     * 加载成功,按需重写
     * @param msg --> 提示信息
     * @param type --> 区别不同请求接口
     */
    open fun onSuccess(msg: String = "", type: Int = 0) {

    }

    /**
     * 加载失败,按需重写
     * @param msg --> 提示信息
     * @param type --> 区别不同请求接口
     */
    open fun onFailure(msg: String = "", type: Int = 0) {

    }

    /**
     * 展示布局
     */
    abstract fun getLayoutId(): Int


    /**
     * 初始化 View, 在 Kotlin 中这个方法就没有必要重写了
     */
    fun initializedViews(savedInstanceState: Bundle?, contentView: View) {

    }

    /**
     * 实现过程
     */
    abstract fun setLogic()

    /**
     * 绑定监听
     */
    abstract fun bindEvent()

    /**
     * 比如全屏,不要 title 等放这里
     */
    open fun requestWindowFeature() {

    }

    /**
     *  是否使用视图布局管理器
     */
    open fun useStatusManager(): Boolean {
        return false
    }

    /**
     * 点击StatusManager视图中重试按钮
     */
    override fun onRetry() {

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

    // 延迟 finish(),默认500 ms
    open fun delayFinish() {
        delayFinish(500)
    }

    /**
     *@param delayTime 单位 毫秒
     */
    open fun delayFinish(delayTime: Int) {
        window.decorView.postDelayed({
            finish()
        }, delayTime.toLong())
    }


    /**
     *  布局变亮
     */
    fun lightOn() {
        val attr = window.attributes
        attr.alpha = 1.0f
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = attr
    }

    fun lightOff(alpha: Float = 0.4f) {
        val attr = window.attributes
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        attr.alpha = alpha
        window.attributes = attr
    }

    /**
     * 带延迟过滤的点击事件监听,防抖动
     */
    override fun onTriggerClick(view: View) {

    }

}
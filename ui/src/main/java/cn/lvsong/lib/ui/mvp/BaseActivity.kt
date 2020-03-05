package cn.lvsong.lib.ui.mvp

import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import cn.lvsong.lib.library.rxbind.RxView
import cn.lvsong.lib.library.utils.StatusBarUtil
import cn.lvsong.lib.ui.R
import cn.lvsong.lib.ui.state.OnRetryListener
import cn.lvsong.lib.ui.state.StatusManager
import io.reactivex.disposables.CompositeDisposable

/** EasyMVP --> 可以参考其注解方式生成 Presenter
 * Desc: MVP 基类 Activity
 * Author: Jooyer
 * Date: 2018-07-24
 * Time: 12:49
 */
abstract class BaseActivity<T : IBasePresenter> : AppCompatActivity(),
    BaseView, OnRetryListener, RxView.OnFilterClick {

    /**
     *  装载 RxBus,防止内存泄漏
     */
    val mCompositeDisposable = CompositeDisposable()

    /**
     * 页面显示加载中,加载失败等管理器
     */
    var mStatusManager: StatusManager? = null

    var mPresenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.transparentStatusBar(
            this,
            if (-1 == getStatusBarColor()) StatusConfig.INSTANCE.getStatusBarColor() else getStatusBarColor(),
            if (-1 == needUseImmersive()) 1 == StatusConfig.INSTANCE.needUseImmersive() else 1 == needUseImmersive()
        )

        if (-1 == getDarkModel()){
            StatusBarUtil.changeState(this,1 == StatusConfig.INSTANCE.getDarkModel())
        }else{
            StatusBarUtil.changeState(this,1 == getDarkModel())
        }

        requestWindowFeature()
        super.onCreate(savedInstanceState)

        mPresenter = createPresenter()
        mPresenter?.let { presenter ->
            lifecycle.addObserver(presenter)
        }

        // 注册网络变化广播 , 我们将其移动到了 Presenter 中,故此处不用再注册了
//        NetWorkReceiver.INSTANCE.registerReceiver(this)

        if (useStartAnim()) {
            overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain)
        }

        if (0 != getLayoutId()) {
            setContentView(initStatusManager(savedInstanceState))
        }

    }

    /**
     * 1 --> 状态栏文本是黑色, 2 --> 状态栏文本是白色
     * 默认是黑色
     */
    open fun getDarkModel():Int {
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
            .onRetryListener(this)
            .build()
        mStatusManager?.setTransY(if (0 == getTransY()) StatusConfig.INSTANCE.getTranslateY() else getTransY())
        mStatusManager?.showLoading()
        return mStatusManager?.getRootLayout()!!
    }

    /**
     *  当 Activity / Fragment 存在 Toolbar 时,需将StatusManager往下移动 Toolbar高度
     *  可以使用全局配置
     *  {@link cn.lvsong.lib.ui.mvp.StatusConfig }
     *  如果全局不合适,可以重写下面方法
     *  PS: 注意,返回的是 px
     */
    open fun getTransY(): Int {
        return 0
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
        // 注消网络变化广播 , 我们将其移动到了 Presenter 中,故此处不用再注销了
//        NetWorkReceiver.INSTANCE.unRegisterReceiver(this)
        super.onDestroy()
        mCompositeDisposable.dispose()
        //结束并移除任务列表
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask()
        }
    }

    /**
     * 展示布局
     */
    abstract fun getLayoutId(): Int


    /**
     *   获取 Presenter 对象
     */
    open fun createPresenter(): T? {
        return null
    }

    /**
     * 初始化 View
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
     *  显示加载Loading
     */
    override fun showLoading() {

    }

    /**
     * 显示错误信息
     */
    override fun showError(message: String) {

    }

    /**
     * 关闭显示Loading
     */
    override fun closeLoading() {

    }

    /**
     * 点击视图中重试按钮
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


    override fun onClick(view: View) {

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


}
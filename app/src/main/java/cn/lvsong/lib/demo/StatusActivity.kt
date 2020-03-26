package cn.lvsong.lib.demo

import android.util.Log
import android.view.View
import cn.lvsong.lib.demo.presenter.StatusPresenter
import cn.lvsong.lib.ui.define.CustomSearchView
import cn.lvsong.lib.ui.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_status.*

/**
 * https://segmentfault.com/q/1010000013729036
 * 当没有具体 Presenter 时,可以参考上面,写成: StatusActivity : BaseActivity<BasePresenter<*,*>>
 */
class StatusActivity : BaseActivity<StatusPresenter>() {
    override fun createPresenter() = StatusPresenter(this)

    override fun getLayoutId() = R.layout.activity_status

    /**
     *  可以自行测试  1/0 效果
     */
    override fun needUseImmersive() = 1

    override fun useStatusManager() = true

    override fun setLoadingViewBackgroundColor() = R.color.color_2878FF

    override fun setLogic() {
        // 如果不调用 显示内容/显示错误等,一直会显示 loading
//        mStatusManager?.showContent() // 默认1200毫秒延时,其实一般网络请求成功差不多
        mStatusManager?.delayShowContent(3000)
    }

    // 根据需要重写
    override fun getStatusBarColor() = R.color.main_theme_color
    // 根据需要重写
//    override fun getTransY() = DensityUtils.dp2pxRtInt(50)

    override fun bindEvent() {
        mPresenter?.test()

        csv_test.setOnSearchListener(object :CustomSearchView.OnSearchListener{
            override fun onChanged(text: String) {
                Log.e("CustomSearchView","onChanged============text: $text")
            }
            override fun onClear() {
                Log.e("CustomSearchView","onClear============")
            }

            override fun onJump(view: View) {
                Log.e("CustomSearchView","onJump============")
            }

            override fun onSearch(text: String) {
                Log.e("CustomSearchView","onSearch============text: $text")
            }
        })
    }

}

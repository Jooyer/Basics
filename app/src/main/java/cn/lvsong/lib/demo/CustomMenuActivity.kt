package cn.lvsong.lib.demo

import android.view.View
import android.widget.Toast
import cn.lvsong.lib.demo.databinding.ActivityCustomMenuBinding
import cn.lvsong.lib.library.utils.click
import cn.lvsong.lib.library.utils.withTrigger
import cn.lvsong.lib.library.view.CustomSearchView
import cn.lvsong.lib.ui.BaseActivity

/**
 * 自定义各种菜单
 */
class CustomMenuActivity : BaseActivity<ActivityCustomMenuBinding>() {

    override fun needUseImmersive() = 1

    override fun getLayoutId() = R.layout.activity_custom_menu

    override fun getViewBinging(view: View): ActivityCustomMenuBinding {
        return ActivityCustomMenuBinding.bind(view)
    }

    override fun setLogic() {

    }

    override fun bindEvent() {

        mBinding?.csv1?.setOnSearchListener(object : CustomSearchView.OnSearchListener {
            override fun onClickBackArrow(view: View) {
                finish()
            }

            override fun onClear(view: View) {

            }

            override fun onJump(view: View) {

            }

            override fun onChanged(text: String) {

            }

            override fun onKeyboardSearch(view:View,text: String) {

            }

        })

        mBinding?.csv3?.setOnSearchListener(object :CustomSearchView.OnSearchListener{
            override fun onJump(view: View) {
                Toast.makeText(this@CustomMenuActivity,"点击可以在此执行跳转逻辑",Toast.LENGTH_SHORT).show()
            }
        })

        mBinding?.lirtTest?.withTrigger()?.click {
            mBinding?.lirtTest?.isChecked = !mBinding!!.lirtTest.isChecked
        }
    }

}
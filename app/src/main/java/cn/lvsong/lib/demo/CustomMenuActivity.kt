package cn.lvsong.lib.demo

import android.view.View
import android.widget.Toast
import cn.lvsong.lib.library.utils.click
import cn.lvsong.lib.library.utils.withTrigger
import cn.lvsong.lib.library.view.CustomSearchView
import cn.lvsong.lib.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_menu.*

/**
 * 自定义各种菜单
 */
class CustomMenuActivity : BaseActivity() {

    override fun needUseImmersive() = 1

    override fun getLayoutId() = R.layout.activity_custom_menu

    override fun setLogic() {

    }

    override fun bindEvent() {

        csv_1.setOnSearchListener(object : CustomSearchView.OnSearchListener {
            override fun onClickBackArrow(view: View) {
                finish()
            }

            override fun onClear(view: View) {

            }

            override fun onJump(view: View) {

            }

            override fun onChanged(text: String) {

            }

            override fun onSearch(view:View,text: String) {

            }

        })

        csv_3.setOnSearchListener(object :CustomSearchView.OnSearchListener{
            override fun onJump(view: View) {
                Toast.makeText(this@CustomMenuActivity,"点击可以在此执行跳转逻辑",Toast.LENGTH_SHORT).show()
            }
        })

        lirt_test.withTrigger().click {
            lirt_test.isChecked = !lirt_test.isChecked
        }
    }


}
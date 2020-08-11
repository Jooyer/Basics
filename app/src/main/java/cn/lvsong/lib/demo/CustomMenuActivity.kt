package cn.lvsong.lib.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.lvsong.lib.library.view.CustomSearchView
import cn.lvsong.lib.ui.ui.BaseActivity
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

            override fun onClear() {

            }

            override fun onJump(view: View) {

            }

            override fun onChanged(text: String) {

            }

            override fun onSearch(text: String) {

            }

        })

    }


}
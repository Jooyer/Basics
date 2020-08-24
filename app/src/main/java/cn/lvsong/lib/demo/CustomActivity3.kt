package cn.lvsong.lib.demo

import cn.lvsong.lib.ui.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_custom3.*

class CustomActivity3 : BaseActivity() {

    override fun needUseImmersive() = 1

    override fun getLayoutId() = R.layout.activity_custom3

    override fun setLogic() {


    }

    override fun bindEvent() {
        sv_star.setOnClickListener {
            sv_star.toggle()
        }

    }

}
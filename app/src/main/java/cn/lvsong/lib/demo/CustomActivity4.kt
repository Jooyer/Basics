package cn.lvsong.lib.demo

import cn.lvsong.lib.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_custom4.*

class CustomActivity4 : BaseActivity() {


    override fun getLayoutId() = R.layout.activity_custom4

    override fun needUseImmersive() = 1

    override fun setLogic() {
        window.decorView.postDelayed({
            cdv.startCountDown(5)
        },1200L)

    }

    override fun bindEvent() {

    }



}
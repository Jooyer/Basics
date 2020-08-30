package cn.lvsong.lib.demo

import android.view.View
import android.widget.Toast
import cn.lvsong.lib.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_toolbar.*

/**
 * 展示自定义Toolbar
 */
class CustomToolbarActivity : BaseActivity() {

    override fun needUseImmersive() = 1

    override fun getLayoutId() = R.layout.activity_custom_toolbar

    override fun setLogic() {


    }

    override fun bindEvent() {
        ct_1.setRightImageListener(View.OnClickListener {
            Toast.makeText(this,"点击右侧图片",Toast.LENGTH_SHORT).show()
            ct_1.alpha = 0.5f
        })

        ct_2.setRightImageListener(View.OnClickListener {
            Toast.makeText(this,"点击右侧第一个图片",Toast.LENGTH_SHORT).show()
        })

        ct_2.setRightImage2Listener(View.OnClickListener {
            Toast.makeText(this,"点击右侧倒数第二个图片",Toast.LENGTH_SHORT).show()
        })

        ct_3.setMoreViewListener(View.OnClickListener {
            Toast.makeText(this,"点击了更多按钮",Toast.LENGTH_SHORT).show()
        })

    }


}
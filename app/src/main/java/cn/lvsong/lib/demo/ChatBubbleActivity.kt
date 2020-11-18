package cn.lvsong.lib.demo

import cn.lvsong.lib.demo.itemDelegate.LeftItemDelegate
import cn.lvsong.lib.demo.itemDelegate.RightItemDelegate
import cn.lvsong.lib.library.adapter.MultiItemTypeAdapter
import cn.lvsong.lib.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_chat_bubble.*

class ChatBubbleActivity : BaseActivity() {

    override fun needUseImmersive() = 1

    override fun getLayoutId() = R.layout.activity_chat_bubble

    override fun setLogic() {

        val data = arrayListOf(
            "", "你好啊", "", "你好", "", "很高心认识你,请自我介绍下,来自哪里,兴趣爱好,工作经历和对未来的规划", ""
        )

        val adapter = MultiItemTypeAdapter(this, data)
        adapter.addItemViewDelegate(LeftItemDelegate())
        adapter.addItemViewDelegate(RightItemDelegate())

        rv_list_chat.adapter = adapter
    }

    override fun bindEvent() {

    }
}
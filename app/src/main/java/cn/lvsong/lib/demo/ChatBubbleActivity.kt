package cn.lvsong.lib.demo

import android.view.View
import cn.lvsong.lib.demo.databinding.ActivityChatBubbleBinding
import cn.lvsong.lib.demo.itemDelegate.LeftItemDelegate
import cn.lvsong.lib.demo.itemDelegate.RightItemDelegate
import cn.lvsong.lib.library.adapter.MultiItemTypeAdapter
import cn.lvsong.lib.ui.BaseActivity

class ChatBubbleActivity : BaseActivity<ActivityChatBubbleBinding>() {

    override fun needUseImmersive() = 1

    override fun getLayoutId() = R.layout.activity_chat_bubble

    override fun getViewBinging(view: View): ActivityChatBubbleBinding {
        return ActivityChatBubbleBinding.bind(view)
    }

    override fun setLogic() {

        val data = arrayListOf(
            "", "你好啊", "", "你好", "", "很高心认识你,请自我介绍下,来自哪里,兴趣爱好,工作经历和对未来的规划", ""
        )

        val adapter = MultiItemTypeAdapter(this, data)
        adapter.addItemViewDelegate(LeftItemDelegate())
        adapter.addItemViewDelegate(RightItemDelegate())

        mBinding?.rvListChat?.adapter = adapter
    }

    override fun bindEvent() {

    }
}
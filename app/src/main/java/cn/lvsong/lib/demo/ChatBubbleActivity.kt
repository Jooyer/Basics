package cn.lvsong.lib.demo

import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import cn.lvsong.lib.library.adapter.CommonAdapter
import cn.lvsong.lib.library.adapter.ViewHolder
import cn.lvsong.lib.ui.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_chat_bubble.*

class ChatBubbleActivity : BaseActivity() {

    override fun needUseImmersive() = 1

    override fun getLayoutId() = R.layout.activity_chat_bubble

    override fun setLogic() {

        val data = arrayListOf<String>("你好啊","","你好","","很高心认识你,请自我介绍下,来自哪里,兴趣爱好,工作经历和对未来的规划",
            "你好啊","","你好","","很高心认识你,请自我介绍下,来自哪里,兴趣爱好,工作经历和对未来的规划")
        val adapter = object :CommonAdapter<String>(this,R.layout.item_rv_chat,data){
            override fun convert(holder: ViewHolder, bean: String, position: Int) {
                val iv = holder.getView<AppCompatImageView>(R.id.iv)
                val tv = holder.getView<AppCompatTextView>(R.id.tv)
                if (position%2 ==0){
                    tv.visibility = View.VISIBLE
                    iv.visibility = View.GONE
                    tv.text = data[position]
                }else{
                    iv.visibility = View.VISIBLE
                    tv.visibility = View.GONE
                    iv.setImageResource(R.mipmap.ic_launcher)
                }
            }
        }
        rv_list_chat.adapter = adapter
    }

    override fun bindEvent() {

    }
}
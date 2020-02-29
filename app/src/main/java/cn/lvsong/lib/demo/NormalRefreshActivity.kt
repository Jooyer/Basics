package cn.lvsong.lib.demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.lvsong.lib.library.adapter.CommonAdapter
import cn.lvsong.lib.library.adapter.ViewHolder
import cn.lvsong.lib.library.refresh.DefaultFooterView
import cn.lvsong.lib.library.refresh.DefaultHeaderView
import cn.lvsong.lib.library.refresh.OnRefreshAndLoadListener
import cn.lvsong.lib.library.refresh.PowerRefreshLayout
import kotlinx.android.synthetic.main.activity_normal_refresh.*

/**
 * 普通刷新效果
 */
class NormalRefreshActivity : AppCompatActivity() {
    private val data = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_refresh)

        prl_container.addHeader(DefaultHeaderView(this))
        prl_container.addFooter(DefaultFooterView(this))

        prl_container.setOnRefreshAndLoadListener(object : OnRefreshAndLoadListener() {
            override fun onRefresh(refreshLayout: PowerRefreshLayout) {
                Log.e("Test", "onRefresh==============")

                data.clear()
                for (i in 0 until 10) {
                    data.add("-----$i------")
                }

                refreshLayout.postDelayed({
                    rv_list.adapter?.notifyDataSetChanged()
                    prl_container.stopRefresh(true)
                }, 3000)
            }

            override fun onLoad(refreshLayout: PowerRefreshLayout) {
                Log.e("Test", "onLoad==============")
                for (i in data.size until data.size + 10) {
                    data.add("-----$i------")
                }
                refreshLayout.postDelayed({
                    rv_list.adapter?.notifyDataSetChanged()
                    prl_container.stopLoadMore(true)
                }, 3000)
            }
        })

        // 设置没有数据了
//        prl_container.setNoMoreData(true)

        prl_container.postDelayed({
            // 自动刷新
            prl_container.setAutoRefresh(true)
        }, 300)


        rv_list.adapter = object : CommonAdapter<String>(this, R.layout.item_rv_list, data) {
            override fun convert(holder: ViewHolder, bean: String, position: Int) {
                holder.setText(R.id.tv_name, bean)
            }
        }

    }
}

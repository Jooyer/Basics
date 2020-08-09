package cn.lvsong.lib.demo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.lvsong.lib.library.refresh.*
import kotlinx.android.synthetic.main.activity_normal_refresh.*

/**
 * 普通刷新效果
 */
class NormalRefreshActivity : AppCompatActivity() {
    private val data = ArrayList<String>()
    private var mBaseAdapter:BaseAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_refresh)

        nrl_container.addHeader(DefaultHeaderView(this))
        nrl_container.addFooter(DefaultFooterView(this))

        nrl_container.setRefreshable(true)
        nrl_container.setLoadable(true)

        nrl_container.setOnRefreshAndLoadListener(object : OnNormalRefreshAndLoadListener() {
            override fun onRefresh(refreshLayout: UnNestedRefreshLayout) {
                Log.e("Test", "onRefresh==============")

                data.clear()
                for (i in 0 until 10) {
                    data.add("-----$i------")
                }
//                refreshLayout.setNoMoreData(true)
                refreshLayout.postDelayed({
//                    rv_list.adapter?.notifyDataSetChanged()
                    mBaseAdapter?.notifyDataSetChanged()
                    nrl_container.setFinishRefresh(true)
                }, 3000)
            }

            override fun onLoad(refreshLayout: UnNestedRefreshLayout) {
                Log.e("Test", "onLoad==============")
                for (i in data.size until data.size + 5) {
                    data.add("-----$i------")
                }
                refreshLayout.postDelayed({
//                    rv_list.adapter?.notifyDataSetChanged()
                    mBaseAdapter?.notifyDataSetChanged()
                    nrl_container.setFinishLoad(true)
                }, 1000)
            }
        })

        // 设置没有数据了
//        prl_container.setNoMoreData(true)

        nrl_container.postDelayed({
            // 自动刷新
            nrl_container.setAutoRefresh()
        }, 300)

        mBaseAdapter = object :BaseAdapter(){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = LayoutInflater.from(this@NormalRefreshActivity).inflate(R.layout.item_rv_list, null,false)
                view.findViewById<TextView>(R.id.tv_name).text = getItem(position)
                return view
            }

            override fun getItem(position: Int) = data[position]

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getCount() = data.size

        }
        rv_list.adapter = mBaseAdapter
    }
}

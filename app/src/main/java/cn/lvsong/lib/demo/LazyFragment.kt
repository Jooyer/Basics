package cn.lvsong.lib.demo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.lvsong.lib.demo.presenter.LazyPresenter
import cn.lvsong.lib.library.refresh.OnRefreshAndLoadListener
import cn.lvsong.lib.library.refresh.PowerRefreshLayout
import cn.lvsong.lib.ui.mvp.BaseFragment
import kotlinx.android.synthetic.main.fragment_blank.*

class LazyFragment : BaseFragment<LazyPresenter>() {

    private val data = ArrayList<String>()

    override fun createPresenter(): LazyPresenter = LazyPresenter(this)

    override fun getLayoutId() = R.layout.fragment_blank

    override fun setLogic() {
        for (i in 0..3) {
            data.add("<---- $i ---->")
        }


//        snl_container.addHeader(DefaultHeaderView(mActivity))
//        snl_container.addFooter(DefaultFooterView(mActivity))

        snl_container.setOnRefreshAndLoadListener(object : OnRefreshAndLoadListener() {
            override fun onRefresh(refreshLayout: PowerRefreshLayout) {
//                Log.e("PowerRefreshLayout", "onRefresh==============")

                data.clear()
                for (i in 0 until 10) {
                    data.add("-----$i------")
                }
                refreshLayout.postDelayed({
                    rv_list.adapter?.notifyDataSetChanged()
                    snl_container.setFinishRefresh(true)
                }, 1000)
            }

            override fun onLoad(refreshLayout: PowerRefreshLayout) {
//                Log.e("PowerRefreshLayout", "onLoad==============")
                for (i in data.size until data.size + 6) {
                    data.add("-----$i------")
                }
                refreshLayout.postDelayed({
                    rv_list.adapter?.notifyDataSetChanged()
                    snl_container.setFinishLoad(true)
                }, 1000)
            }
        })

        ////////////////////////////////////////////////////////////////////////////////////////////

        rv_list.layoutManager = LinearLayoutManager(mActivity)

        rv_list.adapter = object : RecyclerView.Adapter<Holder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
                return Holder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_rv_list,
                        parent,
                        false
                    )
                )
            }

            override fun getItemCount() = data.size

            override fun onBindViewHolder(holder: Holder, position: Int) {
                holder.tv_name.text = data[position]
                holder.tv_name.setOnClickListener {
                    Toast.makeText(activity, "点击了: $position", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onFirstUserVisible() {
//        Log.e("PowerRefreshLayout", "onFirstUserVisible==============")
        snl_container.postDelayed({
//            snl_container.setAutoRefresh()
            snl_container.setFinishRefresh(true)
        }, 600)

    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var tv_name: TextView = view.findViewById(R.id.tv_name)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            LazyFragment().apply {
                arguments = Bundle().apply {}
            }
    }


}

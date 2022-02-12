package cn.lvsong.lib.demo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.lvsong.lib.demo.data.Data
import cn.lvsong.lib.demo.databinding.FragmentBlankBinding
import cn.lvsong.lib.demo.viewmodel.NetModel
import cn.lvsong.lib.library.refresh.OnNestedRefreshAndLoadListener
import cn.lvsong.lib.library.refresh.NestedRefreshLayout
import cn.lvsong.lib.ui.BaseFragment
import cn.lvsong.lib.ui.BaseViewModel

class LazyFragment : BaseFragment<FragmentBlankBinding>() {

    private val mData = ArrayList<Data>()

    private lateinit var viewModel: NetModel

    private var mPage: Int = 1

    companion object {
        fun newInstance() =
            LazyFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun getLayoutId() = R.layout.fragment_blank

    override fun getViewBinging(view: View): FragmentBlankBinding {
        return FragmentBlankBinding.bind(view)
    }

    override fun getCurrentViewModel(): BaseViewModel {
        viewModel = ViewModelProvider(this).get(NetModel::class.java)
        return viewModel
    }

    override fun setLogic() {
        mBinding?.rvList?.layoutManager = LinearLayoutManager(mActivity)

        mBinding?.rvList?.adapter = object : RecyclerView.Adapter<Holder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
                return Holder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_rv_list,
                        parent,
                        false
                    )
                )
            }

            override fun getItemCount() = mData.size

            override fun onBindViewHolder(holder: Holder, position: Int) {
                holder.tv_name.text = mData[position].title
                holder.tv_name.setOnClickListener {
                    Toast.makeText(activity, "点击了: $position", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun bindEvent() {
        mBinding?.nrlRefreshLayout?.setOnRefreshAndLoadListener(object : OnNestedRefreshAndLoadListener() {
            override fun onRefresh(refreshLayout: NestedRefreshLayout) {
                viewModel.getData(mPage)
            }

            override fun onLoad(refreshLayout: NestedRefreshLayout) {
                mPage++
                viewModel.getData(mPage)
            }
        })

        viewModel.mListData.observe(this, Observer {
            if (1 == mPage) {
                mData.clear()
            }
            mData.addAll(it)
            mBinding?.rvList?.adapter?.notifyDataSetChanged()
        })
    }

    override fun onFirstUserVisible() {
        Log.e("LazyFragment", "onFirstUserVisible==============")
        mBinding?.nrlRefreshLayout?.setAutoRefresh(300)
    }

    /**
     * 解析 apiType 作用:
     * 如果在 NetModel请求数据时
     *   //更新加载状态
     *   mLoadState.value = LoadState.Success(type = 1)
     *   则可以理解此处的 1 == type,则为刷新或者加载数据
     *   当type = 2时,则为请求详情, getDetail()中设置的 LoadState.Success(type = 2)
     *   LoadState.Loading(), LoadState.Failure()均和上面类似,方便在一个地方处理所有成功时某些逻辑
     */
    override fun onSuccess(code: Int, apiType: Int, subType: Int, msg: String) {
        if (1 == apiType) {
            if (mBinding!!.nrlRefreshLayout.isRefreshing) {
                mBinding!!.nrlRefreshLayout.setFinishRefresh(true)
            } else if (mBinding!!.nrlRefreshLayout.isLoading) {
                mBinding!!.nrlRefreshLayout.setFinishLoad(true)
            }
        } else { // 其他操作返回的成功
            Log.e("LazyFragment", "onSuccess==============")
        }
    }

    override fun onFailure(code: Int, apiType: Int,subType: Int,  msg: String) {
        mBinding?.nrlRefreshLayout?.setFinishRefresh(false)
        mBinding?.nrlRefreshLayout?.setFinishLoad(false)
        Log.e("LazyFragment", "onFailure==============$msg")
    }


    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var tv_name: TextView = view.findViewById(R.id.tv_name)
    }

}

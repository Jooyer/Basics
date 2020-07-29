package cn.lvsong.lib.library.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.lvsong.lib.library.R


/** 原作者: zhy
 * Created by Jooyer on 16/6/28
 */
class LoadMoreWrapper<T>(private val mContext: Context,
                         private val mInnerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //加载个更多父布局
    private var mLoadMoreView: View? = null
    //加载更多失败
    private var mLoadMoreFailView: View? = null
    //加载更多进行中...
    private var mLoadingMoreView: View? = null
    //没有更多加载...
    private var mLoadMoreEndView: View? = null


    private var mOnLoadMoreListener: OnLoadMoreListener? = null

    private fun hasLoadMore(): Boolean {
        return mLoadMoreView != null
    }


    private fun isShowLoadMore(position: Int): Boolean {
        return hasLoadMore() && position >= mInnerAdapter.itemCount
    }

    override fun getItemViewType(position: Int): Int {
        return if (isShowLoadMore(position)) {
            ITEM_TYPE_LOAD_MORE
        } else mInnerAdapter.getItemViewType(position)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            val holder: ViewHolder
            if (mLoadMoreView != null) {
                holder = ViewHolder.createViewHolder(parent.context, mLoadMoreView!!)
                return holder
            }
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType)

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isShowLoadMore(position)) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener!!.onLoadMoreRequested()
            }
            return
        }
        mInnerAdapter.onBindViewHolder(holder, position)
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, object : WrapperUtils.SpanSizeCallback {
            override fun getSpanSize(layoutManager: GridLayoutManager, oldLookup: GridLayoutManager.SpanSizeLookup, position: Int): Int {
                return if (isShowLoadMore(position)) {
                    layoutManager.spanCount
                } else oldLookup.getSpanSize(position) ?: 1
            }
        })

    }


    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        mInnerAdapter.onViewAttachedToWindow(holder)
        if (isShowLoadMore(holder.layoutPosition)) {
            setFullSpan(holder)
        }

    }


    private fun setFullSpan(holder: RecyclerView.ViewHolder) {
        val lp = holder.itemView.layoutParams
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
            lp.isFullSpan = true

        }

    }


    override fun getItemCount(): Int {
        return mInnerAdapter.itemCount + if (hasLoadMore()) 1 else 0
    }

    /**
     * 隐藏加载更多
     *
     * @param status 2-->隐藏加载更多布局, 1-->加载中, 0-->没有更多 , -1-->加载失败
     */
    fun changeStatus(status: Int) {
        if (mLoadMoreView != null) {
            when (status) {
                -1 -> {
                    showLoadingFailView()
                    Log.i("info", "=====-1====")
                }

                0 -> {
                    showLoadEndView()
                    Log.i("info", "=====0====")
                }

                1 -> {
                    showLoadingView()
                    Log.i("info", "=====1====")
                }
                2 -> if (mLoadingMoreView != null) {
                    mLoadingMoreView!!.visibility = View.GONE
                    Log.i("info", "=====2====" + mLoadingMoreView!!.visibility)
                }
            }
        }
    }


    /**
     * 加载失败,点击重试...
     */
    private fun showLoadingFailView() {
        mLoadMoreFailView!!.visibility = View.VISIBLE
        mLoadingMoreView!!.visibility = View.GONE
        mLoadMoreEndView!!.visibility = View.GONE
        retryLoading(mLoadMoreFailView!!)
    }

    /**
     * 没有更多...
     */
    private fun showLoadEndView() {
        mLoadMoreFailView!!.visibility = View.GONE
        mLoadingMoreView!!.visibility = View.GONE
        mLoadMoreEndView!!.visibility = View.VISIBLE
    }

    /**
     * 正在加载中...
     */
    private fun showLoadingView() {
        mLoadMoreFailView!!.visibility = View.GONE
        mLoadingMoreView!!.visibility = View.VISIBLE
        mLoadMoreEndView!!.visibility = View.GONE

    }


    interface OnLoadMoreListener {
        fun onLoadMoreRequested()
    }


    fun setOnLoadMoreListener(loadMoreListener: OnLoadMoreListener): LoadMoreWrapper<*> {
        try {
            mOnLoadMoreListener = loadMoreListener
        } catch (e: Exception) {
        }

        return this
    }


    fun setLoadMoreView(loadMoreView: View): LoadMoreWrapper<*> {
        mLoadMoreView = loadMoreView
        mLoadMoreFailView = mLoadMoreView!!.findViewById(R.id.ll_loading_fail)
        mLoadingMoreView = mLoadMoreView!!.findViewById(R.id.ll_loading_more)
        mLoadMoreEndView = mLoadMoreView!!.findViewById(R.id.ll_loading_end)
        return this

    }


    fun setLoadMoreView(parent: ViewGroup, layoutId: Int): LoadMoreWrapper<*> {
        setLoadMoreView(LayoutInflater.from(mContext).inflate(layoutId, parent, false))
        return this

    }

    private fun retryLoading(view: View) {
        view.setOnClickListener {
            showLoadingView()
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener!!.onLoadMoreRequested()
            }
        }
    }

    companion object {
        val ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2
        val ITEM_TYPE_LOAD_MORE_ERROR = Integer.MAX_VALUE - 1
    }

}
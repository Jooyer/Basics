package cn.lvsong.lib.library.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


/** 原作者: zhy
 * Created by Jooyer on 16/6/28
 */
class EmptyWrapper<T>(private val mInnerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mEmptyView: View? = null
    private val mEmptyLayoutId: Int = 0

    private val isEmpty: Boolean
        get() = (mEmptyView != null || mEmptyLayoutId != 0) && mInnerAdapter.itemCount == 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (isEmpty) {
            val holder: ViewHolder
            if (mEmptyView != null) {
                holder = ViewHolder.createViewHolder(parent.context, mEmptyView!!)
            } else {
                holder = ViewHolder.createViewHolder(parent.context, parent, mEmptyLayoutId)
            }
            return holder
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, object : WrapperUtils.SpanSizeCallback {
            override fun getSpanSize(layoutManager: GridLayoutManager, oldLookup: GridLayoutManager.SpanSizeLookup, position: Int): Int {
                return if (isEmpty) {
                    layoutManager.spanCount
                } else oldLookup.getSpanSize(position)
            }
        })


    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        mInnerAdapter.onViewAttachedToWindow(holder)
        if (isEmpty) {
            WrapperUtils.setFullSpan(holder)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (isEmpty) {
            ITEM_TYPE_EMPTY
        } else mInnerAdapter.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isEmpty) {
            return
        }
        mInnerAdapter.onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int {
        return if (isEmpty) 1 else mInnerAdapter.itemCount
    }


    fun setEmptyView(emptyView: View) {
        mEmptyView = emptyView
    }

    //    public void setEmptyView(int layoutId)
    //    {
    //        mEmptyLayoutId = layoutId;
    //    }

    fun hideEmptyView() {
        mEmptyView!!.visibility = View.GONE
    }

    fun showEmptyView() {
        mEmptyView!!.visibility = View.VISIBLE
    }

    companion object {
        val ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1
    }

}

package cn.lvsong.lib.library.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.lvsong.lib.library.listener.OnClickFastListener

/** 原作者: zhy
 * Created by Jooyer on 16/6/28
 */
open class MultiItemTypeAdapter<T>(var mContext: Context, var mDatas: MutableList<T>) :
    RecyclerView.Adapter<ViewHolder>() {

    private var mItemViewDelegateManager: ItemViewDelegateManager<T> = ItemViewDelegateManager()
    var mOnItemClickListener: OnItemClickListener? = null

    val data: List<T>
        get() = mDatas


    override fun getItemViewType(position: Int): Int {
        return if (!useItemViewDelegateManager()) super.getItemViewType(position) else mItemViewDelegateManager.getItemViewType(
            mDatas[position],
            position
        )
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType)
        val layoutId = itemViewDelegate.itemViewLayoutId
        val holder = ViewHolder.createViewHolder(mContext, parent, layoutId)
        onViewHolderCreated(holder, holder.convertView)
        setListener(parent, holder, viewType)
        return holder
    }

    fun onViewHolderCreated(holder: ViewHolder, itemView: View?) {

    }

    fun convert(holder: ViewHolder, t: T) {
        mItemViewDelegateManager.convert(holder, t, holder.adapterPosition)
    }

    fun isEnabled(viewType: Int): Boolean {
        return true
    }


    private fun setListener(parent: ViewGroup, viewHolder: ViewHolder, viewType: Int) {
        if (!isEnabled(viewType)) return

        viewHolder.convertView!!.setOnClickListener(object : OnClickFastListener() {
            override fun onFastClick(v: View) {
                if (mOnItemClickListener != null) {
                    val position = viewHolder.adapterPosition
                    mOnItemClickListener!!.onItemClick(v, viewHolder, position)
                }
            }
        })


        viewHolder.convertView!!.setOnLongClickListener(View.OnLongClickListener { v ->
            if (mOnItemClickListener != null) {
                val position = viewHolder.adapterPosition
                return@OnLongClickListener mOnItemClickListener!!.onItemLongClick(v, viewHolder, position)
            }
            false
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        convert(holder, mDatas[position])
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    fun addItemViewDelegate(itemViewDelegate: ItemViewDelegate<T>): MultiItemTypeAdapter<*> {
        mItemViewDelegateManager.addDelegate(itemViewDelegate)
        return this
    }

    fun addItemViewDelegate(viewType: Int, itemViewDelegate: ItemViewDelegate<T>): MultiItemTypeAdapter<*> {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate)
        return this
    }

    protected fun useItemViewDelegateManager(): Boolean {
        return mItemViewDelegateManager.itemViewDelegateCount > 0
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int)

        fun onClick(view: View, type: Int, position: Int){
        }

        fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {
            return false
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    fun notifyItemChange(pos: Int, data: T) {
        val t = mDatas[pos]
        mDatas.remove(t)
        mDatas.add(pos, data)
        notifyItemChanged(pos + 1)
    }

}

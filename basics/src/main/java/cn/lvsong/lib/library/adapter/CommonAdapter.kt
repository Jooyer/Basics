package cn.lvsong.lib.library.adapter


import android.content.Context
import android.view.LayoutInflater



/** 原作者: zhy
 * Created by Jooyer on 16/6/28
 */
abstract class CommonAdapter<T>(mContext: Context, mLayoutId: Int, data: MutableList<T>) :
    MultiItemTypeAdapter<T>(mContext, data) {
    protected var mInflater: LayoutInflater = LayoutInflater.from(mContext)

    init {
        mDatas = data
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override val itemViewLayoutId: Int
                get() = mLayoutId

            override fun isForViewType(item: T, position: Int): Boolean {
                return true
            }

            override fun convert(holder: ViewHolder, bean: T, position: Int) {
                this@CommonAdapter.convert(holder, bean, position)
            }
        })
    }

    protected abstract fun convert(holder: ViewHolder, bean: T, position: Int)


}

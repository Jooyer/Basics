package cn.lvsong.lib.library.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ViewFlipper
import cn.lvsong.lib.library.listener.OnClickFastListener

/** 参考: https://github.com/Bamboy120315/ViewFlipperDemo
 * Desc: 仿淘宝公告滚动效果
 * Author: Jooyer
 * Date: 2020-10-12
 * Time: 13:52
 */

/*  用法

    <cn.lvsong.lib.library.view.NoticeView
        android:id="@+id/nv_notice_book_library"
        android:layout_width="match_parent"
        android:layout_height="@dimen/height_40"
        android:autoStart="true"
        android:flipInterval="3000"
        android:inAnimation="@anim/notice_up_in"
        android:outAnimation="@anim/notice_up_out" />

    ////////////////////////////////////////////////////////////////

    val noticeData = arrayListOf<String>(
            "Update good books every day1",
            "Update good books every day2", "Update good books every day3"
        )

        noticeView.setNoticeAdapter(object : NoticeView.NoticeAdapter() {
            override fun getItemCount() = noticeData.size

            override fun getChildLayout() = R.layout.item_notice_book_library

            override fun bindView(childRootView: View, position: Int) {
                childRootView.findViewById<AppCompatTextView>(R.id.atv_notice_book_library).text =
                    noticeData[position]
            }

            override fun onItemClick(position: Int, view: View) {
                ToastUtils.showShort("点击了 $position")
            }

        })

 */

class NoticeView(context: Context, attrs: AttributeSet?): ViewFlipper(context, attrs) {

    abstract class NoticeAdapter {
        abstract fun getItemCount(): Int

        abstract fun getChildLayout(): Int

        abstract fun bindView(childRootView: View, position: Int)

        /**
         * 点击事件,根据需要重写
         * @param position --> 点击位置,从0开始计算
         * @param view --> 当前点击的View
         */
       open fun onItemClick(position: Int, view: View) {

        }
    }

    fun setNoticeAdapter(adapter: NoticeAdapter){
        for (index in 0 until adapter.getItemCount()){
            val childRootView = LayoutInflater.from(context).inflate(adapter.getChildLayout(),null,false)
            childRootView.setOnClickListener(object :OnClickFastListener(){
                override fun onFastClick(v: View) {
                    adapter.onItemClick(index,v)
                }
            })
            adapter.bindView(childRootView,index)
            removeView(childRootView)
            addView(childRootView)
        }
    }

}
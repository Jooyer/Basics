package cn.lvsong.lib.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_nested_scroll_refresh.*

/**
 * NestedScroll嵌套滑动刷新加载
 */
class NestedScrollRefreshActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll_refresh)


        val title = arrayListOf<String>("语文", "数学", "英语")

        val fragments = ArrayList<LazyFragment>()
        for (i in 0..2) {
            fragments.add(LazyFragment())
        }
        sl_viewpager.offscreenPageLimit = 3
        sl_viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int) = fragments[position]

            override fun getCount() = 3

            override fun getPageTitle(position: Int): CharSequence? {
                return title[position]
            }

            override fun destroyItem(container: View, position: Int, `object`: Any) {
//                super.destroyItem(container, position, `object`)
            }
        }

        sl_tab.setupWithViewPager(sl_viewpager)

    }
}

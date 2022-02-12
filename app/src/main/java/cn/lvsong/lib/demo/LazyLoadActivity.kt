package cn.lvsong.lib.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class LazyLoadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lazy)
        val sl_viewpager = findViewById<ViewPager>(R.id.sl_viewpager)
        val sl_tab = findViewById<TabLayout>(R.id.sl_tab)

        val title = arrayListOf<String>("语文", "数学", "英语")
        val fragments = ArrayList<LazyFragment>()
        for (i in 0..2) {
            fragments.add(LazyFragment.newInstance())
        }

//        sl_viewpager.offscreenPageLimit = 3
        // FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT  --> 核心
        // 解析: https://juejin.im/post/5cdb7c15f265da036c57ac66
        sl_viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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

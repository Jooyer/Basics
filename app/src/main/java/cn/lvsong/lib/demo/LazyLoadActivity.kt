package cn.lvsong.lib.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import cn.lvsong.lib.ui.BaseFragment
import com.google.android.material.tabs.TabLayout

class LazyLoadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lazy)
        val sl_viewpager = findViewById<ViewPager>(R.id.sl_viewpager)
        val sl_tab = findViewById<TabLayout>(R.id.sl_tab)

        val title = arrayListOf("语文", "数学", "英语")
        sl_viewpager.offscreenPageLimit = title.size
        val fragments = ArrayList<Fragment>()
            fragments.add(LazyFragment.newInstance())
            fragments.add(LazyFragment2.newInstance())
            fragments.add(LazyFragment3.newInstance())

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

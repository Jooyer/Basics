package cn.lvsong.lib.demo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import cn.lvsong.lib.net.network.NetWorkMonitor
import cn.lvsong.lib.net.network.NetWorkMonitorManager
import cn.lvsong.lib.net.network.NetworkType
import kotlinx.android.synthetic.main.activity_nested_scroll_refresh.*

/**
 * NestedScroll嵌套滑动刷新加载
 */
class NestedScrollRefreshActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll_refresh)


        val title = arrayListOf<String>("政治", "历史", "地理")

        val fragments = ArrayList<LazyFragment>()
        for (i in 0..2) {
            fragments.add(LazyFragment())
        }
        sl_viewpager.offscreenPageLimit = 3
        sl_viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {


            override fun getCount() = 3

            override fun getPageTitle(position: Int): CharSequence? {
                return title[position]
            }

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun destroyItem(container: View, position: Int, `object`: Any) {
//                super.destroyItem(container, position, `object`)
            }
        }

        sl_tab.setupWithViewPager(sl_viewpager)

    }

    override fun onStart() {
        super.onStart()
        NetWorkMonitorManager.INSTANCE.register(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        NetWorkMonitorManager.INSTANCE.unregister(this)
    }

    @NetWorkMonitor([NetworkType.NETWORK_4G, NetworkType.NETWORK_WIFI, NetworkType.NETWORK_NONE])
    fun onNetWorkStateChange(state: NetworkType) {
        Log.e("NestedRefresh","onNetWorkStateChange============state: ${state.name}")
    }

}

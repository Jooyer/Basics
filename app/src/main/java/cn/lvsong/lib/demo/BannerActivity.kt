package cn.lvsong.lib.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.lvsong.lib.demo.adapter.CustomAdapter
import cn.lvsong.lib.demo.adapter.CustomAdapter2
import cn.lvsong.lib.demo.adapter.GalleryAdapter
import cn.lvsong.lib.library.banner.BannerLayout
import cn.lvsong.lib.library.banner.OnPositionChangeListener
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_banner.*

/**
 * 使用 Banner
 */
class BannerActivity : AppCompatActivity() {

    private val data = arrayListOf(
        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3820948238,3810516733&fm=26&gp=0.jpg",
        "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1545980553,2413955112&fm=26&gp=0.jpg"
//        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2412068931,3031791558&fm=26&gp=0.jpg",
//        "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=286946846,3770652173&fm=26&gp=0.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)

        // 普通用法
        val adapter = CustomAdapter(data, R.layout.item_banner_normal)
        banner.setBannerAdapter(adapter, DensityUtil.dp2pxRtInt(10))

        /**
         * Gallary用法
         *
         */
        val ratio = 1F / 15
        val adapter2 = GalleryAdapter(
            (ScreenUtils.getRealWidth(this) * (1 - 4 * ratio)).toInt(),
            data,
            R.layout.item_banner_normal
        )
        // 上面 4 * ratio, 这里 multiple = 4/2
        banner2.setGalleryBannerAdapter(adapter2, ratio, 2F)
        banner2.postDelayed({ banner2.onStop() }, 2000)


        // Item 带横幅用法
        val adapter3 = CustomAdapter2(data, R.layout.item_banner_normal2)
        banner3.setBannerAdapter(adapter3)

        // 独立横幅用法
        val titles = arrayListOf("818活动刚刚过去...", "双十一活动报名现在开始...", "双十二活动即将到来...", "活动多得停不下来啦...")
        tv_text.text = titles[0]
        val adapter4 = CustomAdapter(data, R.layout.item_banner_normal)
        banner4.setBannerAdapter(adapter4)
        banner4.setOnPositionChangeListener(object : OnPositionChangeListener {
            override fun onPositionChange(position: Int) { // 此时banner2下方的横幅是随 Item 移动的,如果不想要其移动可以如下方式
                tv_text.text = titles[position]
            }
        })


    }
}

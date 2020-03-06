package cn.lvsong.lib.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.lvsong.lib.demo.adapter.CustomAdapter2
import cn.lvsong.lib.library.banner.OnPositionChangeListener
import cn.lvsong.lib.library.utils.DensityUtil
import com.meirenmeitu.banner.demo.CustomAdapter
import kotlinx.android.synthetic.main.activity_banner.*

/**
 * 使用 Banner
 */
class BannerActivity : AppCompatActivity() {

    private val data = arrayListOf(
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573379127723&di=bd32e4115b494ce9267fc15f09206047&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fc142bd27742defa1abedf5c3e389f4e9645462ea7e1ba-M4oq6P_fw658",
        "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3598308963,3250463486&fm=26&gp=0.jpg",
        "http://img0.imgtn.bdimg.com/it/u=2326305792,2139565828&fm=26&gp=0.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)

        // 普通用法
        val adapter = CustomAdapter(data, R.layout.item_banner_normal)
        banner.setBannerAdapter(adapter,DensityUtil.dp2pxRtInt(10))

        // Item 带横幅用法
        val adapter2 = CustomAdapter2(data, R.layout.item_banner_normal2)
        banner2.setBannerAdapter(adapter2)

        // 独立横幅用法
        val titles = arrayListOf("818活动刚刚过去...", "双十一活动报名现在开始...", "双十二活动即将到来...")
        tv_text.text = titles[0]
        val adapter3 = CustomAdapter(data, R.layout.item_banner_normal)
        banner3.setBannerAdapter(adapter3)
        banner3.setOnPositionChangeListener(object : OnPositionChangeListener {
            override fun onPositionChange(position: Int) { // 此时banner2下方的横幅是随 Item 移动的,如果不想要其移动可以如下方式
                tv_text.text = titles[position]
            }
        })


    }
}

package cn.lvsong.lib.demo

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import cn.lvsong.lib.demo.adapter.CustomAdapter
import cn.lvsong.lib.library.adapter.CommonAdapter
import cn.lvsong.lib.library.adapter.HeaderAndFooterWrapper
import cn.lvsong.lib.library.adapter.ViewHolder
import cn.lvsong.lib.library.banner.*
import cn.lvsong.lib.library.utils.DensityUtil
import coil.load

/**
 * 使用 Banner
 */
class BannerActivity : AppCompatActivity() {

    private val heads = arrayListOf(
        "https://www.lvsong.cn/images/ic_phonetic_banner001.webp",
        "https://www.lvsong.cn/images/ic_phonetic_banner002.webp"
    )

    private val datas = arrayListOf(
        "https://www.lvsong.cn/images/ic_phonetic_banner001.webp",
        "https://www.lvsong.cn/images/ic_phonetic_banner002.webp"
    )

    private val list = arrayListOf(
        "https://www.lvsong.cn/images/ic_phonetic_banner001.webp",
        "https://www.lvsong.cn/images/ic_phonetic_banner002.webp"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)
        val rv_banner_list = findViewById<RecyclerView>(R.id.rv_banner_list)

        val headerView =
            LayoutInflater.from(this).inflate(R.layout.layout_banner_header, rv_banner_list, false)
        val footerView =
            LayoutInflater.from(this).inflate(R.layout.layout_banner_footer, rv_banner_list, false)

        val headerBanner = headerView.findViewById<BannerLayout>(R.id.banner)
        val footerBanner = footerView.findViewById<BannerLayout>(R.id.banner)
        val tv_text = footerView.findViewById<AppCompatTextView>(R.id.tv_text)

        // 普通用法
        val headerBannerAdapter = object : CommonAdapter<String>(this, R.layout.item_banner_normal, heads) {
            override fun convert(holder: ViewHolder, bean: String, position: Int) {
                holder.itemView.findViewById<AppCompatTextView>(R.id.tv_position).text = "$position"
                holder.itemView.findViewById<AppCompatImageView>(R.id.iv_banner).load(bean)
            }
        }
        headerBanner.setIndicatorView(IndicatorView(this).setSpacing(DensityUtil.dp2pxRtInt(2)))
            .setManager(HorizontalLayoutManager(this@BannerActivity, DensityUtil.dp2pxRtInt(10)))
            .setBannerBottomMargin(DensityUtil.dp2pxRtInt(20))
            .setRadius(DensityUtil.dp2pxRtFloat(10))
            .setAdapter(headerBannerAdapter)
            .loop()


        // 独立横幅用法
        val titles = arrayListOf("818活动刚刚过去...", "双十一活动报名现在开始...", "双十二活动即将到来...", "活动多得停不下来啦...")
        tv_text.text = titles[0]
        val footerAdapter = CustomAdapter(datas, R.layout.item_banner_normal)
        footerBanner.setIndicatorView(IndicatorView(this))
            .setManager(GalleryLayoutManager(this@BannerActivity))
            .setAdapter(footerAdapter)
            .loop()

        footerBanner.setOnPositionChangeListener(object : OnPositionChangeListener {
            override fun onPositionChange(position: Int) { // 此时banner2下方的横幅是随 Item 移动的,如果不想要其移动可以如下方式
                tv_text.text = titles[position]
            }
        })

        val adapter = object : CommonAdapter<String>(this, R.layout.item_banner_test, list) {
            override fun convert(holder: ViewHolder, bean: String, position: Int) {
                holder.getView<AppCompatTextView>(R.id.tv_text).text = bean
                when (position) {
                    1 -> {
                        holder.getView<BannerLayout>(R.id.banner)
                            .setIndicatorView(IndicatorView(this@BannerActivity))
                            .setManager(GalleryLayoutManager(this@BannerActivity))
                            .setAdapter(CustomAdapter(list, R.layout.item_banner_normal))
                            .loop()
                    }
                    2 -> {
                        holder.getView<BannerLayout>(R.id.banner)
                            .setIndicatorView(IndicatorView(this@BannerActivity))
                            .setManager(HorizontalLayoutManager(this@BannerActivity, DensityUtil.dp2pxRtInt(30)))
                            .setAdapter(CustomAdapter(list, R.layout.item_banner_normal))
                            .loop()
                    }
                    else -> {
                        holder.getView<BannerLayout>(R.id.banner)
                            .setIndicatorView(IndicatorView(this@BannerActivity))
                            .setManager(HorizontalLayoutManager(this@BannerActivity, DensityUtil.dp2pxRtInt(30)))
                            .setAdapter(CustomAdapter(list, R.layout.item_banner_normal))
                            .loop()
                    }
                }
            }
        }
        val headerAndFooterWrapper = HeaderAndFooterWrapper(adapter)
        headerAndFooterWrapper.addHeaderView(headerView)
        headerAndFooterWrapper.addFootView(footerView)

        rv_banner_list.adapter = headerAndFooterWrapper

        rv_banner_list.postDelayed({
            heads.clear()
            heads.add("https://www.lvsong.cn/images/ic_phonetic_banner001.webp")
            heads.add("https://www.lvsong.cn/images/ic_phonetic_banner002.webp")
            heads.add("https://www.lvsong.cn//images/banner1.png")
            headerBanner.notifyDataSetChanged()
        }, 5000)

    }
}

package cn.lvsong.lib.demo

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import cn.lvsong.lib.demo.databinding.ActivityCustom3Binding
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.view.NineImageLayout
import cn.lvsong.lib.ui.BaseActivity
import cn.lvsong.lib.ui.BaseViewModel
import coil.Coil
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.RoundedCornersTransformation

class CustomActivity3 : BaseActivity<ActivityCustom3Binding, BaseViewModel>() {
    private val mImages = arrayListOf(
        "https://www.lvsong.cn/images/ic_phonetic_banner002.webp"
    )

    private val mImages2 = arrayListOf(
        "https://www.lvsong.cn/images/ic_phonetic_banner001.webp",
        "https://www.lvsong.cn/images/ic_phonetic_banner002.webp",
        "https://www.lvsong.cn/images/ic_phonetic_banner001.webp",
        "https://www.lvsong.cn/images/ic_phonetic_banner002.webp",
        "https://www.lvsong.cn/images/ic_phonetic_banner001.webp",
        "https://www.lvsong.cn/images/ic_phonetic_banner002.webp",
        "https://www.lvsong.cn/images/ic_phonetic_banner001.webp",
        "https://www.lvsong.cn/images/ic_phonetic_banner002.webp",
        "https://www.lvsong.cn/images/ic_phonetic_banner001.webp"
    )

    override fun needUseImmersive() = 1

    override fun getLayoutId() = R.layout.activity_custom3

    override fun getViewBinging(view: View): ActivityCustom3Binding {
        return ActivityCustom3Binding.bind(view)
    }

    override fun setLogic() {

        mBinding?.nlImages?.setAdapter(object : NineImageLayout.NineImageAdapter() {
            override fun getItemCount() = mImages.size

            override fun bindView(view: View, pos: Int) {
                if (1 == mImages.size) { //处理单张图片
//                    Glide.with(view)
//                        .asBitmap()
//                        .load(mImages[pos])
//                        .into(object : CustomTarget<Bitmap>() {
//                            override fun onResourceReady(
//                                resource: Bitmap,
//                                transition: Transition<in Bitmap>?
//                            ) {
//                                nl_images.setSingleImage(
//                                    resource.width,
//                                    resource.height,
//                                    view.findViewById(R.id.iv_nine_item)
//                                )
//                            }
//
//                            override fun onLoadCleared(placeholder: Drawable?) {
//                            }
//                        })
//
//                    ImageLoad.loader.loadImage(
//                        view.findViewById<AppCompatImageView>(R.id.iv_nine_item),
//                        mImages[pos]
//                    )


                    ImageLoader(this@CustomActivity3).enqueue(
                        ImageRequest.Builder(this@CustomActivity3)
                            .data(mImages[pos])
                            .target {
                                val iv = view.findViewById<AppCompatImageView>(R.id.iv_nine_item)
                                mBinding?.nlImages?.setSingleImage(
                                    it.intrinsicWidth,
                                    it.intrinsicHeight,
                                    iv
                                )
//                                it.setBounds(0,0,iv.width,iv.height)
                                iv.load(it)
                            }.build()
                    )

                } else {
                    view.findViewById<AppCompatImageView>(R.id.iv_nine_item).load(mImages[pos])
                }
            }

            override fun createView(inflater: LayoutInflater, parent: ViewGroup, position: Int) =
                layoutInflater.inflate(R.layout.item_nine_image, parent, false)
        })

        mBinding?.nlImages2?.setAdapter(object : NineImageLayout.NineImageAdapter() {
            override fun getItemCount() = mImages2.size

            override fun bindView(view: View, pos: Int) {
                view.findViewById<AppCompatImageView>(R.id.iv_nine_item).load(mImages2[pos])
                // 下面是高斯模糊效果
//                    {
//                        transformations(BlurTransformation(context = applicationContext, radius = 5F, sampling = 5F))
//                    }
            }

            override fun onItemClick(position: Int, view: View) {
                // 点击事件,根据需要重写
            }

            override fun createView(inflater: LayoutInflater, parent: ViewGroup, position: Int) =
                layoutInflater.inflate(R.layout.item_nine_image, parent, false)
        })
    }

    override fun bindEvent() {
        mBinding?.svStar?.setOnClickListener {
            mBinding?.svStar?.toggle()
            Toast.makeText(this@CustomActivity3, "点击了星星图标", Toast.LENGTH_SHORT).show()
        }

        mBinding?.psvTest?.setOnClickListener {
            mBinding?.psvTest?.toggle()
            Toast.makeText(this@CustomActivity3, "点击了设置图标", Toast.LENGTH_SHORT).show()
        }

    }

}
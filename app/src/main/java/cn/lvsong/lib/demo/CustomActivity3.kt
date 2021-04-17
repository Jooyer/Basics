package cn.lvsong.lib.demo

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import cn.lvsong.lib.library.view.NineImageLayout
import cn.lvsong.lib.ui.BaseActivity
import coil.Coil
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import kotlinx.android.synthetic.main.activity_custom3.*

class CustomActivity3 : BaseActivity() {
    private val mImages = arrayListOf(
//        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3820948238,3810516733&fm=26&gp=0.jpg",
//        "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1545980553,2413955112&fm=26&gp=0.jpg",
//        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2412068931,3031791558&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=286946846,3770652173&fm=26&gp=0.jpg"
    )

    private val mImages2 = arrayListOf(
        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3820948238,3810516733&fm=26&gp=0.jpg",
        "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1545980553,2413955112&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2412068931,3031791558&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=286946846,3770652173&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3820948238,3810516733&fm=26&gp=0.jpg",
        "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1545980553,2413955112&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2412068931,3031791558&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=286946846,3770652173&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3820948238,3810516733&fm=26&gp=0.jpg"
    )

    override fun needUseImmersive() = 1

    override fun getLayoutId() = R.layout.activity_custom3

    override fun setLogic() {

        nl_images.setAdapter(object : NineImageLayout.NineImageAdapter() {
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
                            .data(mImages[pos]).target{
                                nl_images.setSingleImage(
                                    it.intrinsicWidth,
                                    it.intrinsicHeight,
                                    view.findViewById(R.id.iv_nine_item)
                                )
                                view.findViewById<AppCompatImageView>(R.id.iv_nine_item).load(it)
                            }.build()
                    )

                } else {
                    view.findViewById<AppCompatImageView>(R.id.iv_nine_item).load(mImages[pos])
                }
            }

            override fun createView(inflater: LayoutInflater, parent: ViewGroup, position: Int) =
                layoutInflater.inflate(R.layout.item_nine_image, parent, false)
        })

        nl_images2.setAdapter(object : NineImageLayout.NineImageAdapter() {
            override fun getItemCount() = mImages2.size

            override fun bindView(view: View, pos: Int) {
                if (1 == mImages2.size) { //处理单张图片
                    ImageLoader(this@CustomActivity3).enqueue(
                        ImageRequest.Builder(this@CustomActivity3)
                            .data(mImages[pos]).target{
                                nl_images.setSingleImage(
                                    it.intrinsicWidth,
                                    it.intrinsicHeight,
                                    view.findViewById(R.id.iv_nine_item)
                                )
                                view.findViewById<AppCompatImageView>(R.id.iv_nine_item).load(it)
                            }.build()
                    )

                } else {
                    view.findViewById<AppCompatImageView>(R.id.iv_nine_item).load(mImages2[pos])
                }
            }

            override fun onItemClick(position: Int, view: View) {
                // 点击事件,根据需要重写
            }

            override fun createView(inflater: LayoutInflater, parent: ViewGroup, position: Int) =
                layoutInflater.inflate(R.layout.item_nine_image, parent, false)
        })
    }

    override fun bindEvent() {
        sv_star.setOnClickListener {
            sv_star.toggle()
            Toast.makeText(this@CustomActivity3, "点击了星星图标", Toast.LENGTH_SHORT).show()
        }

        psv_test.setOnClickListener {
            psv_test.toggle()
            Toast.makeText(this@CustomActivity3, "点击了设置图标", Toast.LENGTH_SHORT).show()
        }

    }

}
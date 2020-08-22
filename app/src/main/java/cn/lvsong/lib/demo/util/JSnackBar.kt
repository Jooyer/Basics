package cn.lvsong.lib.demo.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import cn.lvsong.lib.demo.R
import cn.lvsong.lib.library.utils.DensityUtil
import com.google.android.material.snackbar.Snackbar

/** https://www.jianshu.com/p/487dca941e15 (可以实现顶部弹出)
 * Desc:
 * Author: Jooyer
 * Date: 2018-10-30
 * Time: 16:52
 */

/*  图片 + 文字

        JSnackBar.Builder().attachView(cl_root_detail)
                .message("收藏成功")
                .textColor(Color.GREEN)
                .textGravity(Gravity.CENTER)
//                        .backgroundRadius(DensityUtils.dp2pxRtInt(5).toFloat())
                .build()
                .leftDrawable(ContextCompat.getDrawable(this@BookDetailActivity, R.drawable.city_book_flex_layout_bg)!!,
                        DensityUtils.dp2pxRtInt(20), DensityUtils.dp2pxRtInt(20))
                .frameLayout(Gravity.CENTER)
                .strokeAndColorWithRadius(DensityUtils.dp2pxRtInt(5).toFloat(), DensityUtils.dp2pxRtInt(1), Color.BLACK) // 会覆盖  backgroundRadius()
                .margin(DensityUtils.dp2pxRtInt(20)) // 这个必须放在  frameLayout() 后面,否则无效
                .show()

 */

/* 文字

   JSnackBar.Builder().attachView(cl_root_detail)
                .message("收藏失败,请重试!")
                .default()
                .build()
                .default()
                .show()

 */

class JSnackBar private constructor(builder: Builder) {

    /**
     * INFO_COLOR 一般信息展示背景颜色
     */
    private val INFO_COLOR = 0xffdf6b0d.toInt()
    /**
     * INFO_COLOR 确认信息展示背景颜色
     */
    private val CONFIRM_COLOR = 0xff4cb04e.toInt()
    /**
     * INFO_COLOR 警告信息展示背景颜色
     */
    private val WARNING_COLOR = 0xfffeC005.toInt()
    /**
     * INFO_COLOR 危险信息展示背景颜色
     */
    private val DENGER_COLOR = 0xfff44336.toInt()

    private lateinit var mSnackbar: Snackbar

    init {
        println("showSignError============3")
        builder.getAttachView()?.let { attachView ->
            mSnackbar = Snackbar.make(attachView, builder.getMessage(), builder.getDuration())
            mSnackbar.duration = builder.getDuration()

            // 设置文本相关
            val textView = mSnackbar.view.findViewById<TextView>(R.id.snackbar_text) as TextView
            textView.text = builder.getMessage()
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, builder.getTextSize().toFloat())
            textView.setTextColor(if (0 == builder.getTextColor()) Color.WHITE else builder.getTextColor())
            textView.gravity = builder.getTextGravity()
            textView.textAlignment = View.TEXT_ALIGNMENT_GRAVITY

            /**
             *  level 会覆盖  mBackgroundColor
             */
            mSnackbar.view.setBackgroundColor(if (0 == builder.getBackgroundColor()) INFO_COLOR else builder.getBackgroundColor())
            when (builder.getLevel()) {
                1 -> mSnackbar.view.setBackgroundColor(INFO_COLOR)
                2 -> mSnackbar.view.setBackgroundColor(CONFIRM_COLOR)
                3 -> mSnackbar.view.setBackgroundColor(WARNING_COLOR)
                4 -> mSnackbar.view.setBackgroundColor(DENGER_COLOR)
            }

            if (0F != builder.getBackgroundRadius()) {
                val gradientDrawable: GradientDrawable
                val background = mSnackbar.view.background
                if (null != background) {
                    if (background is GradientDrawable) {
                        gradientDrawable = background
                        gradientDrawable.cornerRadius = builder.getBackgroundRadius()
                        mSnackbar.view.background = gradientDrawable
                    } else if (background is ColorDrawable) {
                        gradientDrawable = GradientDrawable()
                        gradientDrawable.setColor(background.color)
                        gradientDrawable.cornerRadius = builder.getBackgroundRadius()
                        mSnackbar.view.background = gradientDrawable
                    }
                }
            }

            mSnackbar.view.alpha = builder.getBackgroundAlpha()
        }
    }

    fun frameLayout(gravity: Int): JSnackBar {
        val params = mSnackbar.view.layoutParams
        val layoutParams = FrameLayout.LayoutParams(params.width, params.height)
        layoutParams.gravity = gravity
        mSnackbar.view.layoutParams = layoutParams
        return this
    }

    fun default(): JSnackBar {
        frameLayout(Gravity.CENTER)
//                .strokeAndColorWithRadius(DensityUtils.dp2pxRtInt(5).toFloat(), DensityUtils.dp2pxRtInt(1), Color.BLACK) // 会覆盖  backgroundRadius()
                .margin(DensityUtil.dp2pxRtInt(20)) // 这个必须放在  frameLayout() 后面,否则无效
        return this
    }


    fun margin(margin: Int = 0): JSnackBar {
        val params = mSnackbar.view.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(margin, margin, margin, margin)
        mSnackbar.view.layoutParams = params
        return this
    }

    fun margin(leftMargin: Int = 0, topMargin: Int = 0, rightMargin: Int = 0, bottomMargin: Int = 0): JSnackBar {
        val params = mSnackbar.view.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin)
        mSnackbar.view.layoutParams = params
        return this
    }

    fun strokeAndColorWithRadius(radius: Float, strokeWidth: Int, strokeColor: Int): JSnackBar {
        val gradientDrawable: GradientDrawable
        val background = mSnackbar.view.background
        if (null != background) {
            if (background is GradientDrawable) {
                gradientDrawable = background
                gradientDrawable.cornerRadius = radius
                gradientDrawable.setStroke(strokeWidth, strokeColor)
                mSnackbar.view.background = gradientDrawable
            } else if (background is ColorDrawable) {
                gradientDrawable = GradientDrawable()
                gradientDrawable.setColor(background.color)
                gradientDrawable.cornerRadius = radius
                gradientDrawable.setStroke(strokeWidth, strokeColor)
                mSnackbar.view.background = gradientDrawable
            }
        }
        return this
    }

    fun leftDrawable(drawable: Drawable, drawableWidth: Int, drawableHeight: Int): JSnackBar {
        val textView = mSnackbar.view.findViewById<TextView>(R.id.snackbar_text) as TextView
        drawable.setBounds(0, 0, drawableWidth, drawableHeight)
        textView.setCompoundDrawables(drawable, null, null, null)
        return this
    }

    fun rightDrawable(drawable: Drawable, drawableWidth: Int, drawableHeight: Int): JSnackBar {
        val textView = mSnackbar.view.findViewById<TextView>(R.id.snackbar_text) as TextView
        drawable.setBounds(0, 0, textView.textSize.toInt(), textView.textSize.toInt())
        textView.setCompoundDrawables(null, null, drawable, null)
        return this
    }

    fun show() {
        mSnackbar.show()
    }

    class Builder {
        private var mAttachView: View? = null
        private var mLevel: Int = 0
        private var mDuration: Int = 2000
        private var mTextSize: Int = 15
        private var mTextGravity: Int = Gravity.CENTER
        private var mBackgroundColor: Int = 0
        private var mTextColor: Int = 0
        private var mBackgroundRadius: Float = DensityUtil.dp2pxRtInt(5).toFloat()
        private var mBackgroundAlpha: Float = 1F
        private var mMessage: String = ""

        fun attachView(attachView: View): Builder {
            mAttachView = attachView
            return this
        }

        fun level(level: Int): Builder {
            mLevel = level
            return this
        }

        fun duration(duration: Int): Builder {
            mDuration = duration
            return this
        }

        fun textSize(textSize: Int): Builder {
            mTextSize = textSize
            return this
        }

        fun textGravity(textGravity: Int): Builder {
            mTextGravity = textGravity
            return this
        }

        fun backgroundColor(@ColorInt backgroundColor: Int): Builder {
            mBackgroundColor = backgroundColor
            return this
        }

        fun textColor(@ColorInt textColor: Int): Builder {
            mTextColor = textColor
            return this
        }

        fun backgroundRadius(backgroundRadius: Float): Builder {
            mBackgroundRadius = backgroundRadius
            return this
        }

        fun backgroundAlpha(backgroundAlpha: Float): Builder {
            mBackgroundAlpha = backgroundAlpha
            return this
        }

        fun message(message: String): Builder {
            mMessage = message
            return this
        }

        fun default(): Builder{
            mTextColor = Color.WHITE
            mTextGravity = Gravity.CENTER
            return this
        }


        fun getAttachView(): View? {
            return mAttachView
        }

        fun getLevel(): Int {
            return mLevel
        }

        fun getDuration(): Int {
            return mDuration
        }

        fun getTextSize(): Int {
            return mTextSize
        }

        fun getTextGravity(): Int {
            return mTextGravity
        }

        fun getBackgroundColor(): Int {
            return mBackgroundColor
        }

        fun getTextColor(): Int {
            return mTextColor
        }

        fun getBackgroundRadius(): Float {
            return mBackgroundRadius
        }

        fun getBackgroundAlpha(): Float {
            return mBackgroundAlpha
        }

        fun getMessage(): String {
            return mMessage
        }


        fun build(): JSnackBar {
            println("showSignError============2")
            return JSnackBar(this)
        }
    }


}
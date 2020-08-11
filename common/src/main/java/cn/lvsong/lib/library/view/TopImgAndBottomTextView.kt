package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.R
import kotlinx.android.synthetic.main.common_ui_top_img_bottom_text_view.view.*

/**
 *
 * @ProjectName:    android
 * @Package:        cn.lvsong.lib.library.view
 * @ClassName:      TopImgAndBottomText
 * @Description:    顶部 ImageView,底部 TextView
 * @Author:         Jooyer
 * @CreateDate:     2020/6/4 17:02
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version:        1.0
 */
class TopImgAndBottomTextView(context: Context, attr: AttributeSet) : LinearLayout(context, attr) {

    private var mNormalDrawable: Drawable? = null
    private var mCheckedDrawable: Drawable? = null
    private var mChecked = false
    private var mImgWidth = DensityUtil.sp2pxRtFloat(30F).toInt()
    private var mImgHeight = DensityUtil.sp2pxRtFloat(30F).toInt()

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        parse(context, attr)
    }

    private fun parse(context: Context, attr: AttributeSet) {
        LayoutInflater.from(context).inflate(R.layout.common_ui_top_img_bottom_text_view, this, true)
        
        val arr = context.obtainStyledAttributes(attr, R.styleable.TopImgAndBottomTextView)
        mNormalDrawable = arr.getDrawable(R.styleable.TopImgAndBottomTextView_tibt_normal_drawable)
        mCheckedDrawable = arr.getDrawable(R.styleable.TopImgAndBottomTextView_tibt_checked_drawable)
        mChecked = arr.getBoolean(R.styleable.TopImgAndBottomTextView_tibt_iv_checked, mChecked)
        mImgWidth = arr.getDimensionPixelOffset(R.styleable.TopImgAndBottomTextView_tibt_iv_width, mImgWidth)
        mImgHeight = arr.getDimensionPixelOffset(R.styleable.TopImgAndBottomTextView_tibt_iv_height, mImgHeight)
        val marginTop = arr.getDimensionPixelSize(R.styleable.TopImgAndBottomTextView_tibt_tv_margin_top, DensityUtil.dp2pxRtInt(10F))
        val text = arr.getString(R.styleable.TopImgAndBottomTextView_tibt_tv_text)
        val textColor = arr.getColor(R.styleable.TopImgAndBottomTextView_tibt_tv_text_color, ContextCompat.getColor(context, R.color.color_333333))
        val textSize = arr.getDimensionPixelSize(R.styleable.TopImgAndBottomTextView_tibt_tv_text_size, DensityUtil.sp2pxRtFloat(14F).toInt()).toFloat()

        val ivLp = aiv_icon_top_image.layoutParams as LinearLayout.LayoutParams
        ivLp.width = mImgWidth
        ivLp.height = mImgHeight
        aiv_icon_top_image.setImageDrawable(if (mChecked) mCheckedDrawable else mNormalDrawable)

        val tvLp = tv_text_bottom_text.layoutParams as LinearLayout.LayoutParams
        tvLp.topMargin = marginTop
        tv_text_bottom_text.setTextColor(textColor)
        tv_text_bottom_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        tv_text_bottom_text.text = text

        arr.recycle()
    }


    fun setChecked(isChecked: Boolean) {
        mChecked = isChecked
        aiv_icon_top_image.setImageDrawable(if (mChecked) mCheckedDrawable else mNormalDrawable)
    }

}
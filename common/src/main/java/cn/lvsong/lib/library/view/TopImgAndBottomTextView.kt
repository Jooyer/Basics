package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Checkable
import android.widget.LinearLayout
import androidx.annotation.ColorRes
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

/* 用法:

            <cn.lvsong.lib.library.view.TopImgAndBottomTextView
            android:layout_width="match_parent"
            android:layout_height="@dimen/height_80"
            android:layout_marginTop="@dimen/padding_10"
            app:tibt_icon_drawable_checked="@drawable/ic_baseline_assignment_returned_24"
            app:tibt_icon_height="@dimen/height_20"
            app:tibt_icon_width="@dimen/width_20"
            app:tibt_icon_drawable="@drawable/ic_baseline_alarm_add_24"
            app:tibt_checked="false"
            app:tibt_spacing="@dimen/padding_2"
            app:tibt_text_info="TopImgAndBottomTextView实现上面图片,下面文字"
            app:tibt_text_color="@color/color_333333"
            app:tibt_text_size="@dimen/text_size_12" />

 */

class TopImgAndBottomTextView(context: Context, attr: AttributeSet) : LinearLayout(context, attr),
    Checkable {

    private var mNormalDrawable: Drawable? = null
    private var mCheckedDrawable: Drawable? = null
    private var mChecked = false
    private var mImgWidth = DensityUtil.sp2pxRtFloat(30F).toInt()
    private var mImgHeight = DensityUtil.sp2pxRtFloat(30F).toInt()
    private var mTextColor:Int =0
    private var mTextCheckedColor:Int =0

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        parse(context, attr)
    }

    private fun parse(context: Context, attr: AttributeSet) {
        LayoutInflater.from(context)
            .inflate(R.layout.common_ui_top_img_bottom_text_view, this, true)

        val arr = context.obtainStyledAttributes(attr, R.styleable.TopImgAndBottomTextView)
        mNormalDrawable = arr.getDrawable(R.styleable.TopImgAndBottomTextView_tibt_icon_drawable)
        mCheckedDrawable =
            arr.getDrawable(R.styleable.TopImgAndBottomTextView_tibt_icon_drawable_checked)
        mChecked = arr.getBoolean(R.styleable.TopImgAndBottomTextView_tibt_checked, mChecked)
        mImgWidth = arr.getDimensionPixelOffset(
            R.styleable.TopImgAndBottomTextView_tibt_icon_width,
            mImgWidth
        )
        mImgHeight = arr.getDimensionPixelOffset(
            R.styleable.TopImgAndBottomTextView_tibt_icon_height,
            mImgHeight
        )
        val spacing = arr.getDimensionPixelSize(
            R.styleable.TopImgAndBottomTextView_tibt_spacing,
            DensityUtil.dp2pxRtInt(10F)
        )
        val text = arr.getString(R.styleable.TopImgAndBottomTextView_tibt_text_info)

        mTextColor = arr.getColor(
            R.styleable.TopImgAndBottomTextView_tibt_text_color,
            ContextCompat.getColor(context, R.color.color_999999)
        )

        mTextCheckedColor = arr.getColor(
            R.styleable.TopImgAndBottomTextView_tibt_text_color_checked,
            ContextCompat.getColor(context, R.color.color_333333)
        )
        val textSize = arr.getDimensionPixelSize(
            R.styleable.TopImgAndBottomTextView_tibt_text_size,
            DensityUtil.sp2pxRtFloat(14F).toInt()
        ).toFloat()

        val ivLp = aiv_icon_top_image.layoutParams as LinearLayout.LayoutParams
        ivLp.width = mImgWidth
        ivLp.height = mImgHeight
        aiv_icon_top_image.setImageDrawable(if (mChecked) mCheckedDrawable else mNormalDrawable)

        val tvLp = tv_text_bottom_text.layoutParams as LinearLayout.LayoutParams
        tvLp.topMargin = spacing
        tv_text_bottom_text.setTextColor(if (mChecked) mTextCheckedColor else mTextColor)
        tv_text_bottom_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        tv_text_bottom_text.text = text
        arr.recycle()
    }



    /**
     * 设置文本内容
     */
    fun setText(text: String) {
        tv_text_bottom_text.text = text
    }

    /**
     * 设置文本颜色
     */
    fun setTextColor(@ColorRes textColor: Int) {
        mTextColor = ContextCompat.getColor(context, textColor)
        tv_text_bottom_text.setTextColor(mTextColor)
    }

    /**
     * 设置选中的文本颜色
     */
    fun setTextCheckedColor(@ColorRes textColor: Int) {
        mTextCheckedColor = ContextCompat.getColor(context, textColor)
        tv_text_bottom_text.setTextColor(mTextCheckedColor)
    }

    /**
     * 设置字体大小,默认14dp
     */
    fun setTextSize(textSize:Float){
        tv_text_bottom_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    /**
     * 设置图标
     */
    fun setNormalImage(drawable: Drawable) {
        mNormalDrawable = drawable
        aiv_icon_top_image.setImageDrawable(drawable)
    }

    /**
     * 设置选中的图标
     */
    fun setCheckedImage(drawable: Drawable) {
        mCheckedDrawable = drawable
        aiv_icon_top_image.setImageDrawable(drawable)
    }
    /**
     * 是否选中
     */
    override fun isChecked(): Boolean {
       return mChecked
    }
    /**
     * 切换当前check状态
     */
    override fun toggle() {
        setChecked(!mChecked)
    }

    /**
     * 设置是否选中
     */
    override fun setChecked(checked: Boolean) {
        mChecked = checked
        aiv_icon_top_image.setImageDrawable(if (mChecked) mCheckedDrawable else mNormalDrawable)
        tv_text_bottom_text.setTextColor(if (mChecked) mTextCheckedColor else mTextColor)
    }

}
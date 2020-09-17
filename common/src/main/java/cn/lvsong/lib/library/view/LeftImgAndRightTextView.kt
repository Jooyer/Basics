package cn.lvsong.lib.library.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Checkable
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import cn.lvsong.lib.library.R

/**
 * https://github.com/yuxingxin/TintStateImage/blob/master/library/src/main/res/values/attrs.xml  --> getColorStateList
 *
 *
 * Desc: 默认为左右结构，图片在左，文字在右
 * Author: Jooyer
 * Date: 2019-05-24
 * Time: 23:12
 */
/* 用法:

            <cn.lvsong.lib.library.view.LeftImgAndRightTextView
            android:id="@+id/lirt_test"
            android:layout_width="wrap_content"
            android:layout_height="@dimen/height_50"
            android:layout_marginTop="@dimen/padding_10"
            app:lirt_icon_drawable="@drawable/ic_baseline_alarm_add_24"
            app:lirt_icon_drawable_checked="@drawable/ic_baseline_assignment_returned_24"
            app:lirt_text_color="@color/color_999999"
            app:lirt_text_color_checked="@color/color_333333"
            app:lirt_back_color="@color/color_DDDDDD"
            app:lirt_back_color_checked="@color/color_8A8EA3"
            app:lirt_icon_width="@dimen/width_20"
            app:lirt_icon_height="@dimen/height_20"
            app:lirt_spacing="@dimen/padding_15"
            app:lirt_text_size="@dimen/text_size_14"
            app:lirt_text_info="左边图片,右边文字,点击试试"
            app:lirt_checked="false"
            />

 */
class LeftImgAndRightTextView(context: Context, attr: AttributeSet) : RelativeLayout(context, attr),
    Checkable {
    /**
     * 图片显示控件
     */
    private var ivIcon: AppCompatImageView

    /**
     * 文本显示控件
     */
    private var tvContent: MediumTextView

    /**
     * View的背景色
     */
    private var backColor = 0

    /**
     * View被按下时的背景色
     */
    private var backColorChecked = 0

    /**
     * icon的背景图片
     */
    private var iconDrawable: Drawable? = null

    /**
     * icon被按下时显示的背景图片
     */
    private var iconDrawableChecked: Drawable? = null

    /**
     * 图片宽度
     */
    private var mImgWidth = 0

    /**
     *图片高度
     */
    private var mImgHeight = 0

    /**
     * View文字的颜色
     */
    private var textColor: ColorStateList? = null

    /**
     * View被按下时文字的颜色
     */
    private var textColorChecked: ColorStateList? = null

    /**
     * 一般文本
     */
    private var mTextInfo:String? =""

    /**
     * 选中后文本
     */
    private var mTextInfoChecked:String? = ""

    /**
     * 两个控件之间的间距，默认为8dp
     */
    private var spacing = 8

    /**
     * 两个控件的位置结构
     */
    private var mPosition = STYLE_ICON_LEFT

    /**
     * 是否被选中
     */
    private var isChecked = false


    init {
        //加载布局
        LayoutInflater.from(context)
            .inflate(R.layout.common_ui_left_image_right_text_view, this, true)
        //初始化控件
        ivIcon = findViewById<View>(R.id.iv_icon) as AppCompatImageView
        tvContent = findViewById<View>(R.id.tv_content) as MediumTextView
        gravity = Gravity.CENTER
        parse(context, attr)
    }

    private fun parse(context: Context, attrs: AttributeSet) {
        val arr = getContext().obtainStyledAttributes(attrs, R.styleable.LeftImgAndRightTextView)

        isChecked = arr.getBoolean(R.styleable.LeftImgAndRightTextView_lirt_checked, false)

        //设置正常背景色
        val colorList = arr.getColorStateList(R.styleable.LeftImgAndRightTextView_lirt_back_color)
        if (colorList != null) {
            backColor = colorList.getColorForState(drawableState, 0)
        }
        //被按下时的背景色
        val colorListChecked =
            arr.getColorStateList(R.styleable.LeftImgAndRightTextView_lirt_back_color_checked)
        if (colorListChecked != null) {
            backColorChecked = colorListChecked.getColorForState(drawableState, 0)
        }

        //设置 ImageView 正常图片
        iconDrawable = arr.getDrawable(R.styleable.LeftImgAndRightTextView_lirt_icon_drawable)
        // ImageView被按下时的图片
        iconDrawableChecked =
            arr.getDrawable(R.styleable.LeftImgAndRightTextView_lirt_icon_drawable_checked)

        mImgWidth = arr.getDimensionPixelOffset(
            R.styleable.LeftImgAndRightTextView_lirt_icon_width,
            dp2px(context, 30)
        )
        mImgHeight = arr.getDimensionPixelOffset(
            R.styleable.LeftImgAndRightTextView_lirt_icon_height,
            dp2px(context, 30)
        )

        //设置 TextView 文字的颜色
        textColor = arr.getColorStateList(R.styleable.LeftImgAndRightTextView_lirt_text_color)
        // TextView被按下时文字的颜色
        textColorChecked =
            arr.getColorStateList(R.styleable.LeftImgAndRightTextView_lirt_text_color_checked)
        //文本内容
        mTextInfo = arr.getString(R.styleable.LeftImgAndRightTextView_lirt_text_info)
        mTextInfoChecked = arr.getString(R.styleable.LeftImgAndRightTextView_lirt_text_info_checked)

        //设置文本字体大小
        val textSize = arr.getDimensionPixelSize(
            R.styleable.LeftImgAndRightTextView_lirt_text_size,
            dp2px(context, 14)
        ).toFloat()
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)

        //设置两个控件之间的间距
        spacing = arr.getDimensionPixelSize(
            R.styleable.LeftImgAndRightTextView_lirt_spacing,
            dp2px(context, 8)
        )
        //设置两个控件的位置结构
        mPosition = arr.getInt(R.styleable.LeftImgAndRightTextView_lirt_icon_location, 0)


        setChecked(isChecked)
        setIconPosition(mPosition)

        arr.recycle()
    }

    /**
     * 设置图标位置
     * 通过重置LayoutParams来设置两个控件的摆放位置
     *
     * @param position --> 取值: {@link STYLE_ICON_LEFT,STYLE_ICON_RIGHT,STYLE_ICON_UP,STYLE_ICON_DOWN}
     */
    fun setIconPosition(position: Int) {
        mPosition = position
        var lp: LayoutParams
        when (position) {
            STYLE_ICON_LEFT -> {
                lp = LayoutParams(mImgWidth, mImgHeight)
                lp.addRule(CENTER_VERTICAL)
                ivIcon.layoutParams = lp

                lp = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                lp.addRule(CENTER_VERTICAL)
                lp.addRule(RIGHT_OF, ivIcon.id)
                lp.leftMargin = spacing
                tvContent.layoutParams = lp
            }
            STYLE_ICON_RIGHT -> {
                lp = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                lp.addRule(CENTER_VERTICAL)
                tvContent.layoutParams = lp

                lp = LayoutParams(mImgWidth, mImgHeight)
                lp.addRule(CENTER_VERTICAL)
                lp.addRule(RIGHT_OF, tvContent.id)
                lp.leftMargin = spacing
                ivIcon.layoutParams = lp
            }
            STYLE_ICON_UP -> {
                lp = LayoutParams(mImgWidth, mImgHeight)
                lp.addRule(CENTER_HORIZONTAL)
                ivIcon.layoutParams = lp

                lp = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                lp.addRule(CENTER_HORIZONTAL)
                lp.addRule(BELOW, ivIcon.id)
                lp.topMargin = spacing
                tvContent.layoutParams = lp
            }
            STYLE_ICON_DOWN -> {
                lp = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                lp.addRule(CENTER_HORIZONTAL)
                tvContent.layoutParams = lp

                lp = LayoutParams(mImgWidth, mImgHeight)
                lp.addRule(CENTER_HORIZONTAL)
                lp.addRule(BELOW, tvContent.id)
                lp.topMargin = spacing
                ivIcon.layoutParams = lp
            }
        }
    }

    /**
     * 设置控件背景色
     *
     * @param backColor
     */
    fun setBackColor(@ColorRes backColor: Int) {
        this.backColor = ContextCompat.getColor(context, backColor)
        setBackgroundColor(this.backColor)
    }

    /**
     * 设置控件被按下时的背景色
     *
     * @param backColorChecked
     */
    fun setBackColorChecked(@ColorRes backColorChecked: Int) {
        this.backColorChecked = backColorChecked
    }

    /**
     * 设置icon的图片
     *
     * @param iconDrawable
     */
    fun setIconDrawable(iconDrawable: Drawable) {
        this.iconDrawable = iconDrawable
        ivIcon.setImageDrawable(iconDrawable)
    }

    /**
     * 设置被按下时的icon的图片
     *
     * @param iconDrawableChecked
     */
    fun setIconDrawableChecked(iconDrawableChecked: Drawable) {
        this.iconDrawableChecked = iconDrawableChecked
        ivIcon.setImageDrawable(iconDrawableChecked)
    }

    /**
     * 设置文字的颜色
     *
     * @param textColor
     */
    fun setTextColor(@ColorRes textColor: Int) {
        if (textColor == 0) return
        this.textColor = ColorStateList.valueOf(
            ContextCompat.getColor(
                context,
                textColor
            )
        )
        tvContent.setTextColor(this.textColor)
    }

    /**
     * 设置被按下时文字的颜色
     *
     * @param textColorChecked
     */
    fun setTextColorChecked(@ColorRes textColorChecked: Int) {
        if (textColorChecked == 0) return
        this.textColorChecked = ColorStateList.valueOf(
            ContextCompat.getColor(
                context,
                textColorChecked
            )
        )
    }

    /**
     * 设置文本字体大小
     *
     * @param size
     */
    fun setTextSize(size: Float) {
        tvContent.textSize = size
    }

    /**
     * 设置两个控件之间的间距
     *
     * @param spacing --> 单位dp
     */
    fun setSpacing(spacing: Int) {
        this.spacing = dp2px(context, spacing)
        //设置完成后刷新一下两个控件的结构，避免先执行了setIconStyle后，setSpacing不生效
        setIconPosition(mPosition)
    }

    private fun dp2px(context: Context, spacing: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            spacing.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    /**
     * 设置是否选中
     */
    override fun setChecked(checked: Boolean) {
        isChecked = checked
        if (isChecked) {
            if (backColorChecked != 0) {
                setBackgroundColor(backColorChecked)
            }
            if (iconDrawableChecked != null) {
                ivIcon.setImageDrawable(iconDrawableChecked)
            }
            if (textColorChecked != null) {
                tvContent.setTextColor(textColorChecked)
            }
            if (!TextUtils.isEmpty(mTextInfoChecked)) {
                tvContent.text = mTextInfoChecked
            }

        } else {
            if (backColor != 0) {
                setBackgroundColor(backColor)
            }
            if (iconDrawable != null) {
                ivIcon.setImageDrawable(iconDrawable)
            }
            if (textColor != null) {
                tvContent.setTextColor(textColor)
            }
            if (!TextUtils.isEmpty(mTextInfo)) {
                tvContent.text = mTextInfo
            }
        }
    }

    /**
     * 是否选中
     */
    override fun isChecked(): Boolean {
        return isChecked
    }

    /**
     * 切换当前check状态
     */
    override fun toggle() {
        setChecked(!isChecked)
    }

    companion object {
        /**
         * 左右结构，图片在左，文字在右
         */
        const val STYLE_ICON_LEFT = 0

        /**
         * 左右结构，图片在右，文字在左
         */
        const val STYLE_ICON_RIGHT = 1

        /**
         * 上下结构，图片在上，文字在下
         */
        const val STYLE_ICON_UP = 2

        /**
         * 上下结构，图片在下，文字在上
         */
        const val STYLE_ICON_DOWN = 3
    }
}
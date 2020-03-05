package cn.lvsong.lib.ui.define

import android.content.Context
import android.text.InputFilter
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import cn.lvsong.lib.ui.R


/**
 * Desc: 自定义普通菜单,格式如下
 *  图片 -- 文字  --------------- 输入框  图片
 * Author: Jooyer
 * Date: 2018-08-08
 * Time: 14:44
 */

/*


                <cn.lvsong.lib.ui.define.CustomEditMenu
                        android:id="@+id/cm_cn_name_publish_help"
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        app:cem_height="@dimen/height_60"
                        app:cem_left_image_visible="true"
                        app:cem_left_image_margin="@dimen/padding_10"
                        app:cem_left_image_drawable="@mipmap/ic_gray_cn_name"
                        app:cem_left_image_width="@dimen/width_22"
                        app:cem_left_image_height="@dimen/height_22"
                        app:cem_left_text_info="书籍名称"
                        app:cem_left_text_size="15"
                        app:cem_left_text_left_margin="@dimen/padding_10"
                        app:cem_left_text_color="@color/color_333333"
                        app:cem_right_text_visible="true"
                        app:cem_right_hint_text_info="(必填)"
                        app:cem_right_text_right_margin="@dimen/padding_2"
                        app:cem_right_image_visible="false"
                        app:cem_bottom_divider_visible="true"
                        app:cem_bottom_divider_color="@color/color_EEF2FA"
                        app:cem_bottom_divider_left_margin="@dimen/padding_14"
                        app:cem_bottom_divider_right_margin="@dimen/padding_14"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toTopOf="parent"
                        app:layout_constraintEnd_toEndOf="parent"
                />

 */

class CustomEditMenu(context: Context, attr: AttributeSet, defStyleAttr: Int) :
    ConstraintLayout(context, attr, defStyleAttr) {

    /**
     * 自定义布局
     */
    private lateinit var cl_menu_container: View
    /**
     * 最左侧图标
     */
    private lateinit var iv_left_icon_menu: ImageView
    /**
     * 紧挨着左侧图标的文本
     */
    private lateinit var tv_left_name_menu: TextView
    /**
     * 紧挨着右侧的输入框
     */
    private lateinit var et_right_name_menu: FixedCursorEditText

    /**
     * 最右侧图标(一般是向右箭头 →)
     */
    private lateinit var iv_right_arrow_menu: ImageView
    /**
     * 底部分割线
     */
    private lateinit var view_bottom_divider_menu: View

    /**
     * 右侧文本距离右侧箭头的距离
     */
    private var rightTextRightMargin = 0

    constructor(context: Context, attr: AttributeSet) : this(context, attr, 0)

    init {
        initView()
        parseAttrs(context, attr)
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.common_ui_image_text_edit_image, this, true)
        cl_menu_container = findViewById(R.id.cl_menu_container)
        iv_left_icon_menu = findViewById(R.id.iv_left_icon_menu)
        tv_left_name_menu = findViewById(R.id.tv_left_name_menu)
        et_right_name_menu = findViewById(R.id.et_right_name_menu)
        iv_right_arrow_menu = findViewById(R.id.iv_right_arrow_menu)
        view_bottom_divider_menu = findViewById(R.id.view_bottom_divider_menu)
    }

    private fun parseAttrs(context: Context, attr: AttributeSet) {
        val arr = context.obtainStyledAttributes(attr, R.styleable.CustomEditMenu)
        val height = arr.getDimension(R.styleable.CustomEditMenu_cem_height, dp2px(50F)).toInt()

        val leftImageVisible = arr.getBoolean(R.styleable.CustomEditMenu_cem_left_image_visible, true)
        val leftImageDrawable = arr.getDrawable(R.styleable.CustomEditMenu_cem_left_image_drawable)
        val leftImageWidth = arr.getDimension(R.styleable.CustomEditMenu_cem_left_image_width, dp2px(22F)).toInt()
        val leftImageHeight = arr.getDimension(R.styleable.CustomEditMenu_cem_left_image_height, dp2px(22F)).toInt()
        val leftImageLeftMargin = arr.getDimension(R.styleable.CustomEditMenu_cem_left_image_margin, dp2px(20F)).toInt()

        val leftTextInfo = arr.getText(R.styleable.CustomEditMenu_cem_left_text_info)
        val leftTextSize = arr.getInteger(R.styleable.CustomEditMenu_cem_left_text_size, 14).toFloat()
        val leftTextLeftMargin =
            arr.getDimension(R.styleable.CustomEditMenu_cem_left_text_left_margin, dp2px(5F)).toInt()
        val leftTextColor = arr.getColor(
            R.styleable.CustomEditMenu_cem_left_text_color,
            ContextCompat.getColor(context, R.color.color_333333)
        )

        val rightTextInfo = arr.getText(R.styleable.CustomEditMenu_cem_right_text_info)
        val rightHintTextInfo = arr.getText(R.styleable.CustomEditMenu_cem_right_hint_text_info)
        val rightTextVisible = arr.getBoolean(R.styleable.CustomEditMenu_cem_right_text_visible, true)
        val rightTextSize = arr.getInt(R.styleable.CustomEditMenu_cem_right_text_size, 14).toFloat()
        val rightTextLength = arr.getInt(R.styleable.CustomEditMenu_cem_right_text_length,0)
        val rightInputType = arr.getInt(R.styleable.CustomEditMenu_cem_right_text_input_type,0)
        rightTextRightMargin =
            arr.getDimension(R.styleable.CustomEditMenu_cem_right_text_right_margin, dp2px(5F)).toInt()
        val rightTextColor = arr.getColor(
            R.styleable.CustomEditMenu_cem_right_text_color,
            ContextCompat.getColor(context, R.color.color_333333)
        )
        val rightTextHintColor = arr.getColor(
            R.styleable.CustomEditMenu_cem_right_text_hint_color,
            ContextCompat.getColor(context, R.color.color_999999)
        )

        val rightDrawableVisible = arr.getBoolean(R.styleable.CustomEditMenu_cem_right_image_visible, true)
        val rightImageDrawable = arr.getDrawable(R.styleable.CustomEditMenu_cem_right_image_drawable)
        val rightImageWidth = arr.getDimension(R.styleable.CustomEditMenu_cem_right_image_width, dp2px(22F)).toInt()
        val rightImageHeight = arr.getDimension(R.styleable.CustomEditMenu_cem_right_image_height, dp2px(22F)).toInt()
        val rightImageRightMargin =
            arr.getDimension(R.styleable.CustomEditMenu_cem_right_image_right_margin, dp2px(20F)).toInt()

        val bottomDividerVisible = arr.getBoolean(R.styleable.CustomEditMenu_cem_bottom_divider_visible, false)
        val bottomDividerColor = arr.getColor(
            R.styleable.CustomEditMenu_cem_bottom_divider_color,
            ContextCompat.getColor(context, R.color.color_EEEEEE)
        )
        val bottomDividerLeftMargin =
            arr.getDimension(R.styleable.CustomEditMenu_cem_bottom_divider_left_margin, dp2px(20F)).toInt()
        val bottomDividerRightMargin =
            arr.getDimension(R.styleable.CustomEditMenu_cem_bottom_divider_right_margin, 0F).toInt()

        val rootLp: ConstraintLayout.LayoutParams = cl_menu_container.layoutParams as LayoutParams
        rootLp.height = height
        cl_menu_container.layoutParams = rootLp

        iv_left_icon_menu.visibility = if (leftImageVisible) View.VISIBLE else View.GONE
        if (null != leftImageDrawable) {
            iv_left_icon_menu.setImageDrawable(leftImageDrawable)
        }
        val leftImageLp: ConstraintLayout.LayoutParams = iv_left_icon_menu.layoutParams as LayoutParams
        leftImageLp.width = leftImageWidth
        leftImageLp.height = leftImageHeight
        leftImageLp.leftMargin = leftImageLeftMargin
        iv_left_icon_menu.layoutParams = leftImageLp

        if (!TextUtils.isEmpty(leftTextInfo)) {
            tv_left_name_menu.text = leftTextInfo
            tv_left_name_menu.setTextColor(leftTextColor)
            tv_left_name_menu.setTextSize(TypedValue.COMPLEX_UNIT_DIP, leftTextSize)

            val leftTextLp: ConstraintLayout.LayoutParams = tv_left_name_menu.layoutParams as LayoutParams
            leftTextLp.marginStart = leftTextLeftMargin
            tv_left_name_menu.layoutParams = leftTextLp
        }

        et_right_name_menu.visibility = if (rightTextVisible) View.VISIBLE else View.GONE
        et_right_name_menu.setHintTextColor(rightTextHintColor)
        et_right_name_menu.setTextColor(rightTextColor)
        et_right_name_menu.setTextSize(TypedValue.COMPLEX_UNIT_DIP, rightTextSize)

        if (rightTextLength > 0){
            et_right_name_menu.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(rightTextLength))
        }

        if (1 == rightInputType){
            et_right_name_menu.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        if (!TextUtils.isEmpty(rightHintTextInfo)) {
            et_right_name_menu.hint = rightHintTextInfo
        }
        if (!TextUtils.isEmpty(rightTextInfo)) {
            et_right_name_menu.setText(rightTextInfo)
        }
        val rightTextLp: ConstraintLayout.LayoutParams = et_right_name_menu.layoutParams as LayoutParams
        if (rightDrawableVisible) {
            rightTextLp.marginEnd = rightTextRightMargin
        }
        et_right_name_menu.layoutParams = rightTextLp


        iv_right_arrow_menu.visibility = if (rightDrawableVisible) View.VISIBLE else View.GONE
        val rightImageLp: ConstraintLayout.LayoutParams = iv_right_arrow_menu.layoutParams as LayoutParams
        if (null != rightImageDrawable) {
            iv_right_arrow_menu.setImageDrawable(rightImageDrawable)
            rightImageLp.marginEnd = rightImageRightMargin
        }
        rightImageLp.width = rightImageWidth
        rightImageLp.height = rightImageHeight
        iv_right_arrow_menu.layoutParams = rightImageLp

        view_bottom_divider_menu.visibility = if (bottomDividerVisible) View.VISIBLE else View.GONE
        view_bottom_divider_menu.setBackgroundColor(bottomDividerColor)
        val bottomDividerLp: ConstraintLayout.LayoutParams = view_bottom_divider_menu.layoutParams as LayoutParams
        bottomDividerLp.leftMargin = bottomDividerLeftMargin
        bottomDividerLp.rightMargin = bottomDividerRightMargin
        view_bottom_divider_menu.layoutParams = bottomDividerLp

        arr.recycle()
    }

    fun setRightTextVisible(isVisible: Boolean) {
        et_right_name_menu.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setLeftText(text: String?) {
        text?.let {
            tv_left_name_menu.text = text
        }

    }

    fun setLeftText(text: String?, color: Int) {
        text?.let {
            tv_left_name_menu.text = text
            tv_left_name_menu.setTextColor(color)
        }
    }

    fun setRightText(text: String?, color: Int) {
        text?.let {
            et_right_name_menu.setText(it)
            et_right_name_menu.setTextColor(color)
        }
    }

    fun setRightText(text: String?) {
        text?.let {
            et_right_name_menu.setText(it)
        }
    }

    fun getRightEditText() = et_right_name_menu

    private fun dp2px(def: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, def, context.resources.displayMetrics)
    }

}
package cn.lvsong.lib.ui.define

import android.content.Context
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
 *  图片 -- 文字  --------------- 文字/图片  图片
 * Author: Jooyer
 * Date: 2018-08-08
 * Time: 14:44
 */

/*
    <cn.lvsong.lib.ui.define.CustomMenu
            android:id="@+id/cm_verify_personal_data"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:visibility="gone"
            app:cm_left_image_visible="false"
            app:cm_left_text_info="个人认证"
            app:cm_left_text_size="14"
            app:cm_right_text_size="14"
            app:cm_left_text_left_margin="@dimen/padding_20"
            app:cm_left_text_color="@color/color_333333"
            app:cm_right_text_visible="true"
            app:cm_right_text_info="www.123.com"
            app:cm_right_arrow_visible="true"
            app:cm_right_arrow_right_margin="@dimen/padding_10"
            app:cm_right_arrow_color="@color/color_999999"
            app:cm_bottom_divider_visible="true"
            app:cm_bottom_divider_color="@color/color_EEF2FA"
            app:cm_bottom_divider_left_margin="@dimen/padding_10"
            app:cm_bottom_divider_right_margin="@dimen/padding_10"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/toolbar"
            app:layout_constraintEnd_toEndOf="parent"
    />

 */

class CustomMenu(context: Context, attr: AttributeSet, defStyleAttr: Int) :
    ConstraintLayout(context, attr, defStyleAttr) {

    /**
     * 向左排列
     */
    val ORIENTATION_LEFT = 1
    /**
     * 向右排列
     */
    val ORIENTATION_RIGHT = 2
    /**
     * Material Design风格
     */
    private val ARROW_STYLE_MATERIAL_DESIGN = 1
    /**
     * 微信风格
     */
    private val ARROW_STYLE_WECHAT_DESIGN = 2

    /**
     * 自定义布局
     */
//    private lateinit var cl_menu_container: View
    /**
     * 最左侧图标
     */
    private lateinit var iv_left_icon_menu: ImageView
    /**
     * 紧挨着左侧图标的文本
     */
    private lateinit var tv_left_name_menu: TextView
    /**
     * 紧挨着右侧的文本(与 紧挨着右侧的图片 只显示一种)
     */
    private lateinit var tv_right_name_menu: TextView
    /**
     * 紧挨着右侧的图片(与 紧挨着右侧的文本 只显示一种)
     */
    private lateinit var iv_near_right_icon_menu: ImageView
    /**
     * 最右侧图标(一般是向右箭头 →)
     */
    private lateinit var iv_right_arrow_menu: BackArrowView
    /**
     * 底部分割线
     */
    private lateinit var view_bottom_divider_menu: View

    /**
     * 右侧文本距离右侧箭头的距离
     */
    private var rightTextRightMargin = 0
    /**
     * 右侧的图标距(不是箭头)离最右侧的距离
     */
    private var rightNearImageRightMargin = 0

    constructor(context: Context, attr: AttributeSet) : this(context, attr, 0)

    init {
        initView()
        parseAttrs(context, attr)
    }

    private fun initView() {
        LayoutInflater.from(context)
            .inflate(R.layout.common_ui_image_text_text_image, this, true)
//        cl_menu_container = findViewById(R.id.cl_menu_container)
        iv_left_icon_menu = findViewById(R.id.iv_left_icon_menu)
        tv_left_name_menu = findViewById(R.id.tv_left_name_menu)
        tv_right_name_menu = findViewById(R.id.tv_right_name_menu)
        iv_near_right_icon_menu = findViewById(R.id.iv_near_right_icon_menu)
        iv_right_arrow_menu = findViewById(R.id.iv_right_arrow_menu)
        view_bottom_divider_menu = findViewById(R.id.view_bottom_divider_menu)
    }

    private fun parseAttrs(context: Context, attr: AttributeSet) {
        val arr = context.obtainStyledAttributes(attr, R.styleable.CustomMenu)
        val leftImageVisible = arr.getBoolean(R.styleable.CustomMenu_cm_left_image_visible, true)
        val leftImageDrawable = arr.getDrawable(R.styleable.CustomMenu_cm_left_image_drawable)
        val leftImageWidth =
            arr.getDimension(R.styleable.CustomMenu_cm_left_image_width, dp2px(22F)).toInt()
        val leftImageHeight =
            arr.getDimension(R.styleable.CustomMenu_cm_left_image_height, dp2px(22F)).toInt()
        val leftImageLeftMargin =
            arr.getDimension(R.styleable.CustomMenu_cm_left_image_margin, dp2px(20F)).toInt()

        val leftTextInfo = arr.getText(R.styleable.CustomMenu_cm_left_text_info)
        val leftTextSize = arr.getInteger(R.styleable.CustomMenu_cm_left_text_size, 14).toFloat()
        val leftTextLeftMargin =
            arr.getDimension(R.styleable.CustomMenu_cm_left_text_left_margin, dp2px(5F)).toInt()
        val leftTextColor = arr.getColor(
            R.styleable.CustomMenu_cm_left_text_color,
            ContextCompat.getColor(context, R.color.color_333333)
        )

        val rightTextInfo = arr.getText(R.styleable.CustomMenu_cm_right_text_info)
        val rightHintTextInfo = arr.getText(R.styleable.CustomMenu_cm_right_hint_text_info)
        val rightTextVisible = arr.getBoolean(R.styleable.CustomMenu_cm_right_text_visible, true)
        val rightTextSize = arr.getInt(R.styleable.CustomMenu_cm_right_text_size, 14).toFloat()
        rightTextRightMargin =
            arr.getDimension(R.styleable.CustomMenu_cm_right_text_right_margin, dp2px(5F)).toInt()
        val rightInputType = arr.getInt(R.styleable.CustomMenu_cm_right_text_input_type, 0)
        val rightTextColor = arr.getColor(
            R.styleable.CustomMenu_cm_right_text_color,
            ContextCompat.getColor(context, R.color.color_333333)
        )
        val rightTextHintColor = arr.getColor(
            R.styleable.CustomMenu_cm_right_text_hint_color,
            ContextCompat.getColor(context, R.color.color_999999)
        )

        val rightNearImageVisible =
            arr.getBoolean(R.styleable.CustomMenu_cm_right_near_image_visible, false)
        val rightNearImageDrawable =
            arr.getDrawable(R.styleable.CustomMenu_cm_right_near_image_drawable)
        val rightNearImageWidth =
            arr.getDimension(R.styleable.CustomMenu_cm_right_near_image_width, dp2px(22F)).toInt()
        val rightNearImageCenterVertical =
            arr.getBoolean(R.styleable.CustomMenu_cm_right_near_image_center_vertical, true)
        val rightNearImageHeight =
            arr.getDimension(R.styleable.CustomMenu_cm_right_near_image_height, dp2px(22F)).toInt()

        rightNearImageRightMargin =
            arr.getDimension(R.styleable.CustomMenu_cm_right_near_image_right_margin, dp2px(5F))
                .toInt()
        val rightNearImageTopMargin =
            arr.getDimension(R.styleable.CustomMenu_cm_right_near_image_top_margin, 0F).toInt()

        val rightArrowVisible =
            arr.getBoolean(R.styleable.CustomMenu_cm_right_arrow_visible, true)

        val rightArrowColor = arr.getColor(
            R.styleable.CustomMenu_cm_right_arrow_color,
            ContextCompat.getColor(context, R.color.color_999999)
        )
        val rightArrowOrientation =
            arr.getInt(R.styleable.CustomMenu_cm_right_arrow_orientation, ORIENTATION_LEFT)

        val rightArrowStyle =
            arr.getInt(R.styleable.CustomMenu_cm_right_arrow_style, ARROW_STYLE_MATERIAL_DESIGN)
        val rightArrowPadding =
            arr.getDimension(R.styleable.CustomMenu_cm_right_arrow_padding, dp2px(1F))

        val rightImageWidth =
            arr.getDimension(R.styleable.CustomMenu_cm_right_arrow_width, dp2px(22F)).toInt()
        val rightImageHeight =
            arr.getDimension(R.styleable.CustomMenu_cm_right_arrow_height, dp2px(22F)).toInt()
        val rightImageRightMargin =
            arr.getDimension(R.styleable.CustomMenu_cm_right_arrow_right_margin, dp2px(5F)).toInt()

        val bottomDividerVisible =
            arr.getBoolean(R.styleable.CustomMenu_cm_bottom_divider_visible, false)
        val bottomDividerColor = arr.getColor(
            R.styleable.CustomMenu_cm_bottom_divider_color,
            ContextCompat.getColor(context, R.color.color_EEEEEE)
        )
        val bottomDividerLeftMargin =
            arr.getDimension(R.styleable.CustomMenu_cm_bottom_divider_left_margin, dp2px(20F))
                .toInt()
        val bottomDividerRightMargin =
            arr.getDimension(R.styleable.CustomMenu_cm_bottom_divider_right_margin, 0F).toInt()

        iv_left_icon_menu.visibility = if (leftImageVisible) View.VISIBLE else View.GONE
        if (null != leftImageDrawable) {
            iv_left_icon_menu.setImageDrawable(leftImageDrawable)
        }
        val leftImageLp: ConstraintLayout.LayoutParams =
            iv_left_icon_menu.layoutParams as LayoutParams
        leftImageLp.width = leftImageWidth
        leftImageLp.height = leftImageHeight
        leftImageLp.marginStart = leftImageLeftMargin
        iv_left_icon_menu.layoutParams = leftImageLp


        if (!TextUtils.isEmpty(leftTextInfo)) {
            tv_left_name_menu.text = leftTextInfo
            tv_left_name_menu.setTextColor(leftTextColor)
            tv_left_name_menu.setTextSize(TypedValue.COMPLEX_UNIT_DIP, leftTextSize)
            val leftTextLp: ConstraintLayout.LayoutParams =
                tv_left_name_menu.layoutParams as LayoutParams
            if (leftImageVisible) {
                leftTextLp.marginStart = leftTextLeftMargin
            } else {
                leftTextLp.goneStartMargin = leftTextLeftMargin
            }
            tv_left_name_menu.layoutParams = leftTextLp
        }

        tv_right_name_menu.visibility = if (rightTextVisible) View.VISIBLE else View.GONE
        tv_right_name_menu.setTextColor(rightTextColor)
        tv_right_name_menu.setTextSize(TypedValue.COMPLEX_UNIT_DIP, rightTextSize)
        tv_right_name_menu.setHintTextColor(rightTextHintColor)

        if (1 == rightInputType) {
            tv_right_name_menu.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        if (!TextUtils.isEmpty(rightHintTextInfo)) {
            tv_right_name_menu.hint = rightHintTextInfo
        }
        if (!TextUtils.isEmpty(rightTextInfo)) {
            tv_right_name_menu.text = rightTextInfo
        }

        val rightTextLp: ConstraintLayout.LayoutParams =
            tv_right_name_menu.layoutParams as LayoutParams
        rightTextLp.marginEnd = rightTextRightMargin
        tv_right_name_menu.layoutParams = rightTextLp



        iv_near_right_icon_menu.visibility = if (rightNearImageVisible) View.VISIBLE else View.GONE
        if (null != rightNearImageDrawable) {
            iv_near_right_icon_menu.setImageDrawable(rightNearImageDrawable)
        }

        val rightNearImageLp: ConstraintLayout.LayoutParams =
            iv_near_right_icon_menu.layoutParams as LayoutParams
        rightNearImageLp.width = rightNearImageWidth
        rightNearImageLp.height = rightNearImageHeight

//        if (rightNearImageCenterVertical) {
//            rightNearImageLp.addRule(ConstraintLayout.CENTER_VERTICAL)
//        } else {
//            rightNearImageLp.removeRule(ConstraintLayout.CENTER_VERTICAL)
//        }

        if (rightArrowVisible) {
            rightNearImageLp.marginEnd = rightNearImageRightMargin
        } else {
            rightNearImageLp.goneEndMargin = rightNearImageRightMargin
        }
        rightNearImageLp.topMargin = rightNearImageTopMargin
        iv_near_right_icon_menu.layoutParams = rightNearImageLp


        iv_right_arrow_menu.visibility = if (rightArrowVisible) View.VISIBLE else View.GONE
        val rightImageLp: ConstraintLayout.LayoutParams =
            iv_right_arrow_menu.layoutParams as LayoutParams
        iv_right_arrow_menu.setArrowColor(rightArrowColor)
        if (ORIENTATION_RIGHT == rightArrowOrientation) {
            iv_right_arrow_menu.rotation = 180F
        } else {
            iv_right_arrow_menu.rotation = 0F
        }
        iv_right_arrow_menu.setArrowStyle(rightArrowStyle)
        iv_right_arrow_menu.setArrowPadding(rightArrowPadding)

        rightImageLp.marginEnd = rightImageRightMargin
        rightImageLp.width = rightImageWidth
        rightImageLp.height = rightImageHeight
        iv_right_arrow_menu.layoutParams = rightImageLp

        view_bottom_divider_menu.visibility = if (bottomDividerVisible) View.VISIBLE else View.GONE
        view_bottom_divider_menu.setBackgroundColor(bottomDividerColor)
        val bottomDividerLp: ConstraintLayout.LayoutParams =
            view_bottom_divider_menu.layoutParams as LayoutParams
        bottomDividerLp.leftMargin = bottomDividerLeftMargin
        bottomDividerLp.rightMargin = bottomDividerRightMargin
        view_bottom_divider_menu.layoutParams = bottomDividerLp

        arr.recycle()
    }

//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        val rootLp: ConstraintLayout.LayoutParams = cl_menu_container.layoutParams as LayoutParams
//        rootLp.height = height
//        cl_menu_container.layoutParams = rootLp
//    }

    fun setRightTextVisible(isVisible: Boolean) {
        tv_right_name_menu.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setRightNearImageViewVisible(isVisible: Boolean) {
        iv_near_right_icon_menu.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun getRightNearImageView(): ImageView {
        return iv_near_right_icon_menu
    }

    fun setRightImageViewVisible(isVisible: Boolean) {
        val rightNearImageLp: ConstraintLayout.LayoutParams =
            iv_near_right_icon_menu.layoutParams as LayoutParams
        val rightTextLp: ConstraintLayout.LayoutParams =
            tv_right_name_menu.layoutParams as LayoutParams
        if (isVisible) {
            rightNearImageLp.marginEnd = rightTextRightMargin + dp2px(20F).toInt()
            rightTextLp.marginEnd = rightTextRightMargin + dp2px(20F).toInt()
            iv_right_arrow_menu.visibility = View.VISIBLE
        } else {
            rightNearImageLp.marginEnd = rightTextRightMargin
            rightTextLp.marginEnd = rightTextRightMargin
            iv_right_arrow_menu.visibility = View.GONE
        }

        iv_near_right_icon_menu.layoutParams = rightNearImageLp
        tv_right_name_menu.layoutParams = rightTextLp
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
            tv_right_name_menu.text = it
            tv_right_name_menu.setTextColor(color)
        }
    }

    fun setRightText(text: String?) {
        text?.let {
            tv_right_name_menu.text = it
        }
    }

    fun getRightTextView() = tv_right_name_menu

    private fun dp2px(def: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            def,
            context.resources.displayMetrics
        )
    }

}
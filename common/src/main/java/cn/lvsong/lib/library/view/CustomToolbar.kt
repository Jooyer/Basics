package cn.lvsong.lib.library.view

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import cn.lvsong.lib.library.R


/**
 * Desc: 自定义 Toolbar ,格式如下:
 * 图片--文字        文字          图片&图片 | 文字  ,  右侧有2张图片,一个文本,一般只显示文本或者2个图片
 * Author: Jooyer
 * Date: 2018-08-08
 * Time: 17:13
 */

/*

    <cn.lvsong.lib.library.view.CustomToolbar
        android:id="@+id/tool2"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:ct_bottom_divider_color="@color/color_EEEEEE"
        app:ct_bottom_divider_visible="true"
        app:ct_center_text_color="@color/color_333333"
        app:ct_center_text_info="右侧更多"
        app:ct_center_text_size="@dimen/text_size_16"
        app:ct_left_arrow_color="@color/color_333333"
        app:ct_left_arrow_padding="@dimen/padding_5"
        app:ct_left_arrow_style="wechat_design"
        app:ct_left_arrow_width="@dimen/width_36"
        app:ct_left_text_color="@color/color_2878FF"
        app:ct_left_text_info="返回"
        app:ct_left_text_left_margin="@dimen/padding_26"
        app:ct_left_text_size="@dimen/text_size_14"
        app:ct_left_text_visible="true"
        app:ct_right_image2_drawable="@mipmap/ic_launcher"
        app:ct_right_image2_height="@dimen/width_40"
        app:ct_right_image2_padding="@dimen/padding_1"
        app:ct_right_image2_right_margin="@dimen/padding_10"
        app:ct_right_image2_visible="true"
        app:ct_right_image2_width="@dimen/width_40"
        app:ct_right_image_drawable="@mipmap/ic_launcher"
        app:ct_right_image_height="@dimen/width_40"
        app:ct_right_image_padding="@dimen/padding_1"
        app:ct_right_image_right_margin="@dimen/padding_10"
        app:ct_right_image_visible="true"
        app:ct_right_image_width="@dimen/width_40"
        app:ct_right_mav_color="@color/color_2878FF"
        app:ct_right_mav_dot_radius="@dimen/padding_2"
        app:ct_right_mav_orientation="vertical"
        app:ct_right_mav_visible="false"
        app:ct_right_text_color="@color/color_2878FF"
        app:ct_right_text_info="保存"
        app:ct_right_text_right_margin="@dimen/padding_10"
        app:ct_right_text_visible="false"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/tool" />

 */

class CustomToolbar(context: Context, attr: AttributeSet, defStyleAttr: Int) :
    ConstraintLayout(context, attr, defStyleAttr) {

    /**
     * Material Design风格
     */
    private val ARROW_STYLE_MATERIAL_DESIGN = 1

    /**
     * 微信风格
     */
    private val ARROW_STYLE_WECHAT_DESIGN = 2

    /**
     * 水平排列
     */
    private val ORIENTATION_HORIZONTAL = 1

    /**
     * 垂直排列
     */
    private val ORIENTATION_VERTICAL = 2

    /**
     * 最左侧图标,默认显示
     */
    lateinit var iv_left_icon_menu: BackArrowView

    /**
     * 最左侧文本
     */
    lateinit var tv_left_name_menu: TextView

    /**
     * 中间文本
     */
    lateinit var tv_center_title_menu: TextView

    /**
     * 最右侧文本
     */
    lateinit var tv_right_name_menu: TextView

    /**
     * 最右侧图标,默认显示
     */
    lateinit var iv_right_icon_menu: ImageView

    /**
     * 最右侧图标2,默认隐藏
     */
    lateinit var iv_right_icon_menu2: ImageView

    /**
     * 最右侧更多图标,不能同时与最右侧的图片显示,否则会重叠
     */
    lateinit var mav_right_icon_menu: MoreActionView

    /**
     * 底部分割线
     */
    lateinit var view_bottom_divider_menu: View


    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 阴影颜色模糊度，越大越模糊
     */
    private var mShadowRadius = 0F

    /**
     * 阴影颜色
     */
    private var mShadowColor = 0

    /**
     * 当显示阴影时,如果需要设置控件背景色,则需要设置属性 ct_background_color
     */
    private var mBackgroundColor = 0

    /**
     * 阴影的垂直偏移量
     */
    private var mOffsetY = 0F

    /**
     * 底部阴影高度
     */
    private var mBottomShadowHeight = 0

    private var mBottomDividerVisible = true

    private var mBottomDividerStyle = 1

    constructor(context: Context, attr: AttributeSet) : this(context, attr, 0)

    init {
        initView()
        parseAttrs(context, attr)
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.common_ui_toolbar_custom, this, true)
        iv_left_icon_menu = findViewById(R.id.iv_left_icon_menu)
        tv_left_name_menu = findViewById(R.id.tv_left_name_menu)
        tv_center_title_menu = findViewById(R.id.tv_center_title_menu)
        tv_right_name_menu = findViewById(R.id.tv_right_name_menu)
        iv_right_icon_menu = findViewById(R.id.iv_right_icon_menu)
        iv_right_icon_menu2 = findViewById(R.id.iv_right_icon_menu2)
        mav_right_icon_menu = findViewById(R.id.mav_right_icon_menu)
        view_bottom_divider_menu = findViewById(R.id.view_bottom_divider_menu)

        mBottomShadowHeight = dp2px(5F).toInt()
        mShadowRadius = dp2px(3F)
        mOffsetY = dp2px(2F)

        //取消硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    private fun parseAttrs(context: Context, attr: AttributeSet) {
        val arr = context.obtainStyledAttributes(attr, R.styleable.CustomToolbar)
        val leftArrowVisible = arr.getBoolean(R.styleable.CustomToolbar_ct_left_arrow_visible, true)
        val leftArrowWidth =
            arr.getDimension(R.styleable.CustomToolbar_ct_left_arrow_width, dp2px(40F)).toInt()
        val leftArrowHeight =
            arr.getDimension(R.styleable.CustomToolbar_ct_left_arrow_height, dp2px(50F)).toInt()
        val leftArrowPadding =
            arr.getDimension(R.styleable.CustomToolbar_ct_left_arrow_padding, dp2px(3F))
        val leftArrowLeftMargin =
            arr.getDimension(R.styleable.CustomToolbar_ct_left_arrow_left_margin, 0F).toInt()

        val leftArrowColor = arr.getColor(
            R.styleable.CustomToolbar_ct_left_arrow_color,
            ContextCompat.getColor(context, R.color.color_999999)
        )
        val leftArrowStyle =
            arr.getInt(R.styleable.CustomToolbar_ct_left_arrow_style, ARROW_STYLE_MATERIAL_DESIGN)

        val leftTextVisible = arr.getBoolean(R.styleable.CustomToolbar_ct_left_text_visible, false)
        val leftTextInfo = arr.getText(R.styleable.CustomToolbar_ct_left_text_info)
        val leftTextSize = arr.getDimension(R.styleable.CustomToolbar_ct_left_text_size, dp2px(15F))
        val leftTextLeftMargin =
            arr.getDimension(R.styleable.CustomToolbar_ct_left_text_left_margin, dp2px(25F)).toInt()
        val leftTextColor = arr.getColor(
            R.styleable.CustomToolbar_ct_left_text_color,
            ContextCompat.getColor(context, R.color.color_FFFFFF)
        )

        val centerTextInfo = arr.getText(R.styleable.CustomToolbar_ct_center_text_info)
//        val centerTextDrawable = arr.getDrawable(R.styleable.CustomToolbar_ct_center_text_drawable)
//        val centerTextDrawablePadding =
//            arr.getDimension(R.styleable.CustomToolbar_ct_center_text_drawable_padding, dp2px(10F))
//                .toInt()
        val centerTextSize =
            arr.getDimension(R.styleable.CustomToolbar_ct_center_text_size, dp2px(18F))
        val centerTextColor = arr.getColor(
            R.styleable.CustomToolbar_ct_center_text_color,
            ContextCompat.getColor(context, R.color.color_FFFFFF)
        )

        val rightImageVisible =
            arr.getBoolean(R.styleable.CustomToolbar_ct_right_image_visible, false)
        val rightImageDrawable = arr.getDrawable(R.styleable.CustomToolbar_ct_right_image_drawable)
        val rightImageWidth =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_image_width, dp2px(20F)).toInt()
        val rightImageHeight =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_image_height, dp2px(20F)).toInt()
        val rightImagePadding =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_image_padding, dp2px(0F)).toInt()
        val rightImageRightMargin =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_image_right_margin, dp2px(5F))
                .toInt()

        val rightImage2Visible =
            arr.getBoolean(R.styleable.CustomToolbar_ct_right_image2_visible, false)
        val rightImage2Drawable =
            arr.getDrawable(R.styleable.CustomToolbar_ct_right_image2_drawable)
        val rightImage2Width =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_image2_width, dp2px(22F)).toInt()
        val rightImage2Height =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_image2_height, dp2px(22F)).toInt()
        val rightImage2Padding =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_image2_padding, 0F).toInt()
        val rightImage2RightMargin =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_image2_right_margin, dp2px(10F))
                .toInt()

        val rightTextBold = arr.getBoolean(R.styleable.CustomToolbar_ct_right_text_bold, true)
        val rightTextVisible =
            arr.getBoolean(R.styleable.CustomToolbar_ct_right_text_visible, false)
        val rightTextInfo = arr.getText(R.styleable.CustomToolbar_ct_right_text_info)
        val rightTextSize =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_text_size, dp2px(15F))
        val rightTextRightMargin =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_text_right_margin, dp2px(12F))
                .toInt()
        val rightTextColor = arr.getColor(
            R.styleable.CustomToolbar_ct_right_text_color,
            ContextCompat.getColor(context, R.color.color_FFFFFF)
        )

        val rightMoreViewVisible =
            arr.getBoolean(R.styleable.CustomToolbar_ct_right_mav_visible, false)
        val rightMoreViewWidth =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_mav_width, dp2px(40F)).toInt()
        val rightMoreViewHeight =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_mav_height, dp2px(40F)).toInt()
        val rightMoreViewRightMargin =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_mav_right_margin, dp2px(5F)).toInt()
        val rightMoreViewColor = arr.getColor(
            R.styleable.CustomToolbar_ct_right_mav_color,
            ContextCompat.getColor(context, R.color.color_2878FF)
        )
        val rightMoreViewDotRadius =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_mav_dot_radius, dp2px(2F))
        val rightMoreViewOrientation =
            arr.getInt(R.styleable.CustomToolbar_ct_right_mav_orientation, ORIENTATION_VERTICAL)

        mBottomDividerVisible =
            arr.getBoolean(R.styleable.CustomToolbar_ct_bottom_divider_visible, true)
        val bottomDividerColor = arr.getColor(
            R.styleable.CustomToolbar_ct_bottom_divider_color,
            ContextCompat.getColor(context, R.color.color_EEEEEE)
        )
        mBottomDividerStyle = arr.getInt(R.styleable.CustomToolbar_ct_bottom_divider_style, 1)
        mShadowColor = arr.getColor(
            R.styleable.CustomToolbar_ct_bottom_shadow_color,
            ContextCompat.getColor(context, R.color.color_26000000)
        )
        mBackgroundColor = arr.getColor(
            R.styleable.CustomToolbar_ct_background_color,
            ContextCompat.getColor(context, R.color.color_FFFFFF)
        )

        iv_left_icon_menu.visibility = if (leftArrowVisible) View.VISIBLE else View.GONE
        iv_left_icon_menu.setArrowColor(leftArrowColor)
        iv_left_icon_menu.setArrowStyle(leftArrowStyle)
        iv_left_icon_menu.setArrowPadding(leftArrowPadding)

        if (leftArrowVisible) {
            iv_left_icon_menu.setOnClickListener {
                if (context is Activity) {
                    context.finish()
                }
            }
        }
        val leftArrowLp: ConstraintLayout.LayoutParams =
            iv_left_icon_menu.layoutParams as LayoutParams
        leftArrowLp.width = leftArrowWidth
        leftArrowLp.height = leftArrowHeight
        leftArrowLp.leftMargin = leftArrowLeftMargin

        iv_left_icon_menu.layoutParams = leftArrowLp

        tv_left_name_menu.visibility = if (leftTextVisible) View.VISIBLE else View.GONE
        if (!TextUtils.isEmpty(leftTextInfo)) {
            tv_left_name_menu.text = leftTextInfo
            tv_left_name_menu.paint.textSize = leftTextSize
            tv_left_name_menu.setTextColor(leftTextColor)
        }
        if (leftTextVisible) {
            tv_left_name_menu.setOnClickListener {
                if (context is Activity) {
                    context.finish()
                }
            }
        }

        val leftTextLp: ConstraintLayout.LayoutParams =
            tv_left_name_menu.layoutParams as LayoutParams
        if (ARROW_STYLE_MATERIAL_DESIGN == leftArrowStyle && leftTextVisible) { // 由于此时箭头中间横线较长,不合适再有文本
            tv_left_name_menu.visibility = View.GONE
        } else if (leftTextVisible) {
            leftTextLp.leftMargin = leftTextLeftMargin
        }
        tv_left_name_menu.layoutParams = leftTextLp

        if (!TextUtils.isEmpty(centerTextInfo)) {
            tv_center_title_menu.text = centerTextInfo
            tv_center_title_menu.paint.textSize = centerTextSize
            tv_center_title_menu.paint.isFakeBoldText = true //  medium 效果
            tv_center_title_menu.setTextColor(centerTextColor)
        }

//        tv_center_title_menu.setShadowLayer(0.15F, 0.2F, 0.2F, ContextCompat.getColor(context, R.color.color_write))

//        if (null != centerTextDrawable) {
//            tv_center_title_menu.setCompoundDrawablesWithIntrinsicBounds(
//                centerTextDrawable,
//                null,
//                null,
//                null
//            )
//            tv_center_title_menu.compoundDrawablePadding = centerTextDrawablePadding
//        }


        iv_right_icon_menu.visibility = if (rightImageVisible) View.VISIBLE else View.GONE
        if (null != rightImageDrawable) {
            iv_right_icon_menu.setImageDrawable(rightImageDrawable)
        }
        val rightImageLp: ConstraintLayout.LayoutParams =
            iv_right_icon_menu.layoutParams as LayoutParams
        rightImageLp.width = rightImageWidth
        rightImageLp.height = rightImageHeight
        rightImageLp.rightMargin = rightImageRightMargin
        iv_right_icon_menu.setPadding(
            rightImagePadding,
            rightImagePadding,
            rightImagePadding,
            rightImagePadding
        )
        iv_right_icon_menu.layoutParams = rightImageLp


        iv_right_icon_menu2.visibility = if (rightImage2Visible) View.VISIBLE else View.GONE
        if (null != rightImage2Drawable) {
            iv_right_icon_menu2.setImageDrawable(rightImage2Drawable)
        }
        val rightImageLp2: ConstraintLayout.LayoutParams =
            iv_right_icon_menu2.layoutParams as LayoutParams
        rightImageLp2.width = rightImage2Width
        rightImageLp2.height = rightImage2Height

        if (rightImageVisible) {
            rightImageLp2.rightMargin = rightImage2RightMargin
        } else {
            rightImageLp2.goneEndMargin = rightImage2RightMargin
        }
        iv_right_icon_menu2.setPadding(
            rightImage2Padding,
            rightImage2Padding,
            rightImage2Padding,
            rightImage2Padding
        )
        iv_right_icon_menu2.layoutParams = rightImageLp2

        tv_right_name_menu.visibility = if (rightTextVisible) View.VISIBLE else View.GONE
        if (!TextUtils.isEmpty(rightTextInfo)) {
            tv_right_name_menu.text = rightTextInfo
            tv_right_name_menu.paint.textSize = rightTextSize
            tv_right_name_menu.paint.isFakeBoldText = rightTextBold //  medium 效果
            tv_right_name_menu.setTextColor(rightTextColor)
        }
        val rightTextLp: ConstraintLayout.LayoutParams =
            tv_right_name_menu.layoutParams as LayoutParams
        rightTextLp.rightMargin = rightTextRightMargin
        tv_right_name_menu.layoutParams = rightTextLp

        mav_right_icon_menu.visibility = if (rightMoreViewVisible) View.VISIBLE else View.GONE
        val moreViewLP = mav_right_icon_menu.layoutParams as ConstraintLayout.LayoutParams
        moreViewLP.width = rightMoreViewWidth
        moreViewLP.height = rightMoreViewHeight
        moreViewLP.rightMargin = rightMoreViewRightMargin
        mav_right_icon_menu.layoutParams = moreViewLP
        mav_right_icon_menu.setColor(rightMoreViewColor)
        mav_right_icon_menu.setDotRadius(rightMoreViewDotRadius)
        mav_right_icon_menu.setOrientation(rightMoreViewOrientation)

        if (mBottomDividerVisible) {
            view_bottom_divider_menu.visibility = View.VISIBLE
            if (1 == mBottomDividerStyle) {
                view_bottom_divider_menu.setBackgroundColor(Color.TRANSPARENT)
                setPadding(0, 0, 0, mBottomShadowHeight)
            } else {
                view_bottom_divider_menu.setBackgroundColor(bottomDividerColor)
            }
        } else {
            view_bottom_divider_menu.visibility = View.GONE
        }
        arr.recycle()
    }

    /**
     * 绘制 Toolbar 底部阴影
     */
    override fun dispatchDraw(canvas: Canvas) {
        if (1 == mBottomDividerStyle && mBottomDividerVisible) {
            val shadowRectF = RectF()
            shadowRectF.set(
                (-mBottomShadowHeight).toFloat(),
                0F,
                (width + mBottomShadowHeight).toFloat(),
                (height - mBottomShadowHeight).toFloat()
            )
            mPaint.style = Paint.Style.FILL
            mPaint.color = mBackgroundColor
            mPaint.setShadowLayer(mShadowRadius, 0F, mOffsetY, mShadowColor)
            canvas.drawRect(shadowRectF, mPaint)
        }
        super.dispatchDraw(canvas)
    }

    /**
     * 设置最右侧图片点击
     */
    fun setRightImageListener(listener: View.OnClickListener) {
        iv_right_icon_menu.setOnClickListener(listener)
    }

    /**
     * 设置右起倒数第二图片点击
     */
    fun setRightImage2Listener(listener: View.OnClickListener) {
        iv_right_icon_menu2.setOnClickListener(listener)
    }

    /**
     * 设置更多点击
     */
    fun setMoreViewListener(listener: View.OnClickListener) {
        mav_right_icon_menu.setOnClickListener(listener)
    }

    /**
     * 设置右侧显示的文本
     */
    fun setRightTextListener(listener: View.OnClickListener) {
        tv_right_name_menu.setOnClickListener(listener)
    }

    /**
     * 设置左侧箭头点击
     */
    fun setLeftArrowClickListener(listener: View.OnClickListener) {
        iv_left_icon_menu.setOnClickListener(listener)
    }

    /**
     * 设置左侧文本点击
     */
    fun setLeftTextViewClickListener(listener: View.OnClickListener) {
        tv_left_name_menu.setOnClickListener(listener)
    }

    /**
     * 设置左侧箭头颜色
     */
    fun setLeftArrowColor(@ColorRes leftArrowColor: Int) {
        iv_left_icon_menu.setArrowColor(ContextCompat.getColor(context, leftArrowColor))
    }

    /**
     * 设置右侧文本控件是否显示
     */
    fun setRightTextVisible(visible: Int) {
        tv_right_name_menu.visibility = visible
    }

    /**
     * 设置最右侧图片控件是否显示
     */
    fun setRightImageVisible(visible: Int) {
        iv_right_icon_menu.visibility = visible
    }

    /**
     * 设置右起倒数第二图片控件是否显示
     */
    fun setRightImage2Visible(visible: Int) {
        iv_right_icon_menu2.visibility = visible
    }

    /**
     * 设置右侧显示文本
     */
    fun setRightText(text: String) {
        tv_right_name_menu.text = text
    }

    /**
     * 设置右侧图片控件显示图片
     */
    fun setRightImage(@DrawableRes resource: Int) {
        iv_right_icon_menu.setImageResource(resource)
    }

    /**
     * 设置右起倒数第二图片控件显示图片
     */
    fun setRightImage2(@DrawableRes resource: Int) {
        iv_right_icon_menu2.setImageResource(resource)
    }

    /**
     * 设置中间文本内容
     */
    fun setCenterText(text: String) {
        tv_center_title_menu.text = text
    }

    /**
     * 设置左侧文本内容
     */
    fun setLeftText(text: String) {
        tv_left_name_menu.text = text
    }

    /**
     * 设置左侧箭头是否显示
     */
    fun setLeftArrowVisible(visible: Int) {
        iv_left_icon_menu.visibility = visible
    }


    private fun dp2px(def: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            def,
            context.resources.displayMetrics
        )
    }

}
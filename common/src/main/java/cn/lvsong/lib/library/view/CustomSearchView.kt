package cn.lvsong.lib.library.view

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import cn.lvsong.lib.library.listener.EditTextWatcher
import cn.lvsong.lib.library.listener.OnClickFastListener
import cn.lvsong.lib.library.R

/**
 * https://blog.csdn.net/qq_33666539/article/details/82421491 --> EditText与父控件点击事件冲突问题
 * https://www.jianshu.com/p/fcb53b6fe238  --> Android自定义View-带删除和搜索图标的EditText
 * Desc:搜索控件
 * Author: Jooyer
 * Date: 2020-03-06
 * Time: 11:51
 */

/*

    <cn.lvsong.lib.library.view.CustomSearchView
        android:id="@+id/csv_test"
        android:layout_width="match_parent"
        android:layout_height="@dimen/height_48"
        android:layout_marginTop="@dimen/padding_30"
        app:csv_left_arrow_padding="@dimen/padding_3"
        app:csv_bottom_divider_color="@color/color_2878FF"
        app:csv_bottom_divider_visible="true"
        app:csv_input_container_height="@dimen/height_30"
        app:csv_input_hint_text="@string/common_ui_search"
        app:csv_need_jump="true"
        app:csv_show_clear_icon="false"
        app:csv_show_search_icon="true"
        app:csv_show_search_btn="true"
        app:csv_search_btn_width="@dimen/width_50"
        app:csv_search_btn_text_size="12dp"
        app:csv_search_btn_margin_right="@dimen/padding_2"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/tool2" />

 */


class CustomSearchView(context: Context, attr: AttributeSet, defStyleAttr: Int) :
    ConstraintLayout(context, attr, defStyleAttr) {

    constructor(context: Context, attr: AttributeSet) : this(context, attr, 0)

    private var mListener: OnSearchListener? = null

    /**
     * Material Design风格
     */
    private val ARROW_STYLE_MATERIAL_DESIGN = 1

    private lateinit var bav_search_left_back: BackArrowView
    private lateinit var cl_search_input_container: ConstraintLayout
    private lateinit var asv_search_view_icon: AndroidSearchView
    private lateinit var et_search_view_search: AppCompatEditText
    private lateinit var cv_search_view_clean: CloseView
    private lateinit var act_search_right_btn: AppCompatTextView
    private lateinit var view_search_bottom_divider: View
    private lateinit var view_search_view_flow: View


    init {
        initView()
        parseAttrs(context, attr)
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.common_ui_search_view, this, true)
        bav_search_left_back = findViewById(R.id.bab_search_left_back)
        cl_search_input_container = findViewById(R.id.cl_search_input_container)
        asv_search_view_icon = findViewById(R.id.asv_search_view_icon)
        et_search_view_search = findViewById(R.id.et_search_view_search)
        cv_search_view_clean = findViewById(R.id.cv_search_view_clean)
        act_search_right_btn = findViewById(R.id.act_search_right_btn)
        view_search_bottom_divider = findViewById(R.id.view_search_bottom_divider)
        view_search_view_flow = findViewById(R.id.view_search_view_flow)
    }

    private fun parseAttrs(context: Context, attr: AttributeSet) {
        val arr = context.obtainStyledAttributes(attr, R.styleable.CustomSearchView)
        val showLeftArrow = arr.getBoolean(R.styleable.CustomSearchView_csv_left_arrow_visible, true)
        val leftArrowColor = arr.getColor(
            R.styleable.CustomSearchView_csv_left_arrow_color,
            ContextCompat.getColor(context, R.color.color_666666)
        )
        val leftArrowPadding =
            arr.getDimensionPixelOffset(
                R.styleable.CustomSearchView_csv_left_arrow_padding,
                dp2px(3F).toInt()
            )
        val leftArrowStyle = arr.getInt(
            R.styleable.CustomSearchView_csv_left_arrow_style,
            ARROW_STYLE_MATERIAL_DESIGN
        )

        val showSearchIcon = arr.getBoolean(R.styleable.CustomSearchView_csv_show_search_icon, true)
        val searchIconColor = arr.getColor(
            R.styleable.CustomSearchView_csv_search_icon_color,
            ContextCompat.getColor(context, R.color.color_666666)
        )

        var inputContainerHeight = arr.getDimensionPixelOffset(
            R.styleable.CustomSearchView_csv_input_container_height,
            dp2px(36F).toInt()
        )
        val inputContainerLeftMargin =
            arr.getDimensionPixelOffset(
                R.styleable.CustomSearchView_csv_input_container_margin_left,
                0
            )
        val inputContainerRightMargin =
            arr.getDimensionPixelOffset(
                R.styleable.CustomSearchView_csv_input_container_margin_right,
                dp2px(10F).toInt()
            )
        val inputContainerDrawable =
            arr.getDrawable(R.styleable.CustomSearchView_csv_input_container_drawable)

        val inputDefaultText = arr.getString(R.styleable.CustomSearchView_csv_default_search_text)
        val inputLeftPadding =
            arr.getDimensionPixelOffset(R.styleable.CustomSearchView_csv_input_left_padding, 0)
        val inputTextSize = arr.getDimensionPixelSize(
            R.styleable.CustomSearchView_csv_input_text_size,
            dp2px(14F).toInt()
        )
        val inputTextColor = arr.getColor(
            R.styleable.CustomSearchView_csv_input_text_color,
            ContextCompat.getColor(context, R.color.color_333333)
        )
        val inputHintText = arr.getString(R.styleable.CustomSearchView_csv_input_hint_text)
        val inputHintColor = arr.getColor(
            R.styleable.CustomSearchView_csv_input_hint_color,
            ContextCompat.getColor(context, R.color.color_333333)
        )
        val inputTextLength = arr.getInt(R.styleable.CustomSearchView_csv_input_max_length, 0)

        val showClearIcon = arr.getBoolean(R.styleable.CustomSearchView_csv_show_clear_icon, true)
        val clearIconColor = arr.getColor(
            R.styleable.CustomSearchView_csv_search_icon_color,
            ContextCompat.getColor(context, R.color.color_666666)
        )

        val showBtn = arr.getBoolean(R.styleable.CustomSearchView_csv_search_btn_visible, true)
        val btnText = arr.getString(R.styleable.CustomSearchView_csv_search_btn_text)
        val btnTextSize = arr.getDimensionPixelSize(
            R.styleable.CustomSearchView_csv_search_btn_text_size,
            dp2px(14F).toInt()
        )
        val btnTextColor = arr.getColor(
            R.styleable.CustomSearchView_csv_search_btn_text_color,
            ContextCompat.getColor(context, R.color.color_333333)
        )
        val btnBgColor = arr.getColor(
            R.styleable.CustomSearchView_csv_search_btn_bg_color,
            ContextCompat.getColor(context, android.R.color.transparent)
        )
        val btnWidth = arr.getDimensionPixelOffset(
            R.styleable.CustomSearchView_csv_search_btn_width,
            dp2px(40F).toInt()
        )
        val btnHeight = arr.getDimensionPixelOffset(
            R.styleable.CustomSearchView_csv_search_btn_height,
            dp2px(40F).toInt()
        )
        val btnRightMargin =
            arr.getDimensionPixelOffset(R.styleable.CustomSearchView_csv_search_btn_margin_right, 0)

        val showBottomDivider =
            arr.getBoolean(R.styleable.CustomSearchView_csv_bottom_divider_visible, false)
        val bottomDividerColor = arr.getColor(
            R.styleable.CustomSearchView_csv_bottom_divider_color,
            ContextCompat.getColor(context, R.color.color_EEEEEE)
        )

        val needSkip = arr.getBoolean(R.styleable.CustomSearchView_csv_need_jump, false)

        arr.recycle()

        bav_search_left_back.visibility = if (showLeftArrow) View.VISIBLE else View.GONE
        bav_search_left_back.setArrowColor(leftArrowColor)
        bav_search_left_back.setArrowPadding(leftArrowPadding.toFloat())
        bav_search_left_back.setArrowStyle(leftArrowStyle)
        if (showLeftArrow) {
            bav_search_left_back.setOnClickListener(object : OnClickFastListener() {
                override fun onFastClick(v: View) {
                    mListener?.onClickBackArrow(v)
                }
            })
        }

        val inputContainerLp = cl_search_input_container.layoutParams as LayoutParams
        val height = dp2px(30F).toInt()
        if (inputContainerHeight < height && showClearIcon) {
            // 如果需要显示右侧清除按钮,则必须对输入框高度进行限制
            inputContainerHeight = height
        }
        inputContainerLp.height = inputContainerHeight
        inputContainerLp.leftMargin =
            inputContainerLeftMargin
        inputContainerLp.rightMargin =
            inputContainerRightMargin
        cl_search_input_container.layoutParams = inputContainerLp
        if (null != inputContainerDrawable) {
            cl_search_input_container.background = inputContainerDrawable
        }

        asv_search_view_icon.visibility = if (showSearchIcon) View.VISIBLE else View.GONE
        asv_search_view_icon.setColor(searchIconColor)

        if (!TextUtils.isEmpty(inputDefaultText)) {
            et_search_view_search.setText(inputDefaultText)
        }
        et_search_view_search.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputTextSize.toFloat())
        et_search_view_search.setTextColor(inputTextColor)
        et_search_view_search.setHintTextColor(inputHintColor)

        if (TextUtils.isEmpty(inputHintText)) {
            et_search_view_search.hint = context.getString(R.string.common_ui_search_hint)
        } else {
            et_search_view_search.hint = inputHintText
        }
        if (showSearchIcon) {
            et_search_view_search.setPadding(
                inputLeftPadding + dp2px(30F).toInt(),
                dp2px(1F).toInt(),
                0,
                0
            )
        } else {
            et_search_view_search.setPadding(inputLeftPadding, dp2px(1F).toInt(), 0, 0)
        }
        if (inputTextLength > 0) {
            et_search_view_search.filters = arrayOf(InputFilter.LengthFilter(inputTextLength))
        }

        cv_search_view_clean.visibility = View.GONE
        cv_search_view_clean.setColor(clearIconColor)


        if (TextUtils.isEmpty(btnText)) {
            act_search_right_btn.text = context.getString(R.string.common_ui_search)
        } else {
            act_search_right_btn.text = btnText
        }

        if (showBtn) {
            act_search_right_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize.toFloat())
            act_search_right_btn.setTextColor(btnTextColor)
            act_search_right_btn.setBackgroundColor(btnBgColor)
            val searchBtnLp = act_search_right_btn.layoutParams as LayoutParams
            searchBtnLp.width = btnWidth
            searchBtnLp.height = btnHeight
            searchBtnLp.rightMargin = btnRightMargin
            act_search_right_btn.layoutParams = searchBtnLp
            act_search_right_btn.visibility = View.VISIBLE
        } else {
            act_search_right_btn.visibility = View.GONE
        }

        view_search_bottom_divider.visibility = if (showBottomDivider) View.VISIBLE else View.GONE
        view_search_bottom_divider.setBackgroundColor(bottomDividerColor)

        setLogic(showClearIcon, needSkip)
    }

    private fun setLogic(showClearIcon: Boolean, needSkip: Boolean) {
        if (showClearIcon) {
            cv_search_view_clean.setOnClickListener {
                et_search_view_search.setText("")
                mListener?.onClear()
                cv_search_view_clean.visibility = View.GONE
            }
        }

        if (needSkip) {
            view_search_view_flow.visibility = View.VISIBLE
            view_search_view_flow.setOnClickListener(object : OnClickFastListener() {
                override fun onFastClick(v: View) {
                    mListener?.onJump(v)
                }
            })
        } else {
            view_search_view_flow.visibility = View.GONE
        }

        act_search_right_btn.setOnClickListener(object : OnClickFastListener() {
            override fun onFastClick(v: View) {
                val text = et_search_view_search.text.toString()
                if (!TextUtils.isEmpty(text)) {
                    mListener?.onSearch(text)
                }
            }
        })

        et_search_view_search.addTextChangedListener(object : EditTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                val text = s.toString()
                mListener?.onChanged(text)
                if (TextUtils.isEmpty(text)) {
                    cv_search_view_clean.visibility = View.GONE
                } else {
                    cv_search_view_clean.visibility = if (showClearIcon) View.VISIBLE else View.GONE
                }
            }
        })

        et_search_view_search.setOnEditorActionListener { v, actionId, event ->
            val text = et_search_view_search.text.toString()
            if (EditorInfo.IME_ACTION_SEARCH == actionId && !TextUtils.isEmpty(text)) {
                mListener?.onSearch(text)
                true
            }
            false
        }
    }

    private fun dp2px(def: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            def,
            context.resources.displayMetrics
        )
    }

    interface OnSearchListener {

        /**
         * 点击返回键
         */
        fun onClickBackArrow(view: View) {

        }

        /**
         * 文本输入框发生改变
         */
        fun onChanged(text: String) {

        }

        /**
         * 清空输入内容
         */
        fun onClear() {

        }

        /**
         * 点击搜索框,进行跳转了
         */
        fun onJump(view: View) {

        }

        /**
         * 点击搜索按钮
         */
        fun onSearch(text: String) {

        }
    }

    fun setOnSearchListener(listener: OnSearchListener) {
        mListener = listener
    }

}
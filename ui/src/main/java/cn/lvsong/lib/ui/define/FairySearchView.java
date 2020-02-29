package cn.lvsong.lib.ui.define;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import cn.lvsong.lib.ui.R;

/**
 * https://blog.csdn.net/qq_33666539/article/details/82421491 --> EditText与父控件点击事件冲突问题
 * https://www.jianshu.com/p/fcb53b6fe238  --> Android自定义View-带删除和搜索图标的EditText
 * Desc: https://github.com/CodingEnding/FairySearchView
 * Author: Jooyer
 * Date: 2019-05-25
 * Time: 8:39
 */

/*
    <cn.lvsong.lib.ui.define.FairySearchView
            android:id="@+id/fsv_search_view_help"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            app:searchHint="CAS，中文名，英文名"
            app:showBackButton="true"
            app:searchViewHeight="@dimen/height_56"
            app:bottomDividerVisible="true"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
    />

 */

public class FairySearchView extends ConstraintLayout {
    private static final String TAG = "FairySearchView";
    /**
     * Material Design风格
     */
    private static final int ARROW_STYLE_MATERIAL_DESIGN = 1;

    private boolean showBackButton = false;//是否显示左侧[返回]按钮
    private boolean showSearchIcon = true;//是否显示输入框左侧的[搜索]图标
    private boolean showClearButton = true;//是否在输入内容后展示[清除]按钮
    private boolean rootClickable = false;//设置为true时则点击整个自定义控件都为一个事件
    private boolean bottomDividerVisible = false;//底部分割线是否可见

    private String searchBtnText = getResources().getString(R.string.common_ui_search);
    private int searchBtnTextSize = getResources().getDimensionPixelSize(R.dimen.text_size_18);
    private int searchBtnTextColor = getResources().getColor(R.color.color_333333);
    private int searchIconColor = getResources().getColor(R.color.color_333333);

    private float arrowPadding = 0F;
    private int arrowColor = 0;
    private int arrowStyle = 0;

    /**
     * 默认搜索关键字
     */
    private String searchText = "";
    private int inputEtSize = getResources().getDimensionPixelSize(R.dimen.text_size_15);
    private int inputEtColor = getResources().getColor(R.color.color_333333);
    private String inputEtHint = getResources().getString(R.string.common_ui_search_hint);
    private int inputEtHintColor = getResources().getColor(R.color.color_B1B6D1);
    private int inputEtContainer = getResources().getDimensionPixelSize(R.dimen.height_40);
    private int inputEtPaddingLeft = getResources().getDimensionPixelSize(R.dimen.padding_30);//输入框左侧内边距

    private int maxSearchLength = -1;//输入内容最大长度（默认不设限制）

    private OnClearClickListener onClearClickListener;
    private OnEditChangeListener onEditChangeListener;
    // 监听用户点击了虚拟键盘中右下角的回车/搜索键
    private OnEnterClickListener onEnterClickListener;

    //    private View searchRoot;//主体View
    private View flowSearchView;//浮层View
    private View btmDivider;//底部分割线
    private AndroidSearchView searchViewIcon;
    private BackArrowView backBtn;
    private AppCompatEditText inputEt;
    private ConstraintLayout inputContainer;
    private CloseView clareView;
    //  "搜索"
    private AppCompatTextView searchBtn;

    public FairySearchView(Context context) {
        this(context, null);
    }

    public FairySearchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FairySearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initViews(context);
    }

    //初始化属性
    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            Resources resources = getResources();
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FairySearchView);

            showBackButton = typedArray.getBoolean(R.styleable.FairySearchView_fsv_show_back_icon, true);
            showSearchIcon = typedArray.getBoolean(R.styleable.FairySearchView_fsv_show_search_icon, true);
            searchIconColor = typedArray.getColor(R.styleable.FairySearchView_fsv_search_icon_color, resources.getColor(R.color.color_333333));

            arrowColor = typedArray.getColor(R.styleable.FairySearchView_fsv_left_arrow_color, resources.getColor(R.color.color_333333));
            arrowStyle = typedArray.getInt(R.styleable.FairySearchView_fsv_left_arrow_style, ARROW_STYLE_MATERIAL_DESIGN);
            arrowPadding = typedArray.getDimension(R.styleable.FairySearchView_fsv_left_arrow_padding, dp2px(1));

            showClearButton = typedArray.getBoolean(R.styleable.FairySearchView_fsv_show_clear_icon, showClearButton);
            rootClickable = typedArray.getBoolean(R.styleable.FairySearchView_fsv_root_clickable, false);
            bottomDividerVisible = typedArray.getBoolean(R.styleable.FairySearchView_fsv_bottom_divider_visible, false);

            searchBtnText = getOrDefault(typedArray.getString(R.styleable.FairySearchView_fsv_search_btn_text), resources.getString(R.string.common_ui_search));
            searchBtnTextSize = typedArray.getDimensionPixelSize(R.styleable.FairySearchView_fsv_search_btn_text_size, resources.getDimensionPixelSize(R.dimen.text_size_14));
            searchBtnTextColor = typedArray.getColor(R.styleable.FairySearchView_fsv_search_btn_text_color, resources.getColor(R.color.color_333333));

            searchText = typedArray.getString(R.styleable.FairySearchView_fsv_default_search_text);
            inputEtSize = typedArray.getDimensionPixelSize(R.styleable.FairySearchView_fsv_input_et_text_size, resources.getDimensionPixelSize(R.dimen.text_size_15));
            inputEtColor = typedArray.getColor(R.styleable.FairySearchView_fsv_input_et_text_color, resources.getColor(R.color.color_333333));
            inputEtHint = getOrDefault(typedArray.getString(R.styleable.FairySearchView_fsv_input_et_hint_text), resources.getString(R.string.common_ui_search_hint));
            inputEtHintColor = typedArray.getColor(R.styleable.FairySearchView_fsv_input_et_hint_color, resources.getColor(R.color.color_B1B6D1));
            inputEtContainer = typedArray.getDimensionPixelSize(R.styleable.FairySearchView_fsv_input_container_height, resources.getDimensionPixelSize(R.dimen.height_36));
            inputEtPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.FairySearchView_fsv_input_et_left_padding, inputEtPaddingLeft);

            maxSearchLength = typedArray.getInteger(R.styleable.FairySearchView_fsv_input_et_max_length, -1);//默认不限制

            typedArray.recycle();//回收资源，否则再次使用会出错
        }
    }

    //初始化Views
    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.common_ui_fairy_search_view, this, true);
//        searchRoot = findViewById(R.id.layout_search_view);
        inputContainer = findViewById(R.id.cl_search_view_container);
        flowSearchView = findViewById(R.id.view_flow_search_view);
        searchViewIcon = findViewById(R.id.asv_search_view_icon);
        backBtn = findViewById(R.id.iv_search_view_back);
        inputEt = findViewById(R.id.et_search_view_search);
        clareView = findViewById(R.id.iv_search_view_clean);
        searchBtn = findViewById(R.id.tv_search_view_search);
        btmDivider = findViewById(R.id.view_bottom_divider);

        setAttr();
        setClicks();
    }

    private void setAttr() {
        searchBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, searchBtnTextSize);
        searchBtn.setTextColor(searchBtnTextColor);
        searchBtn.setText(searchBtnText);

        inputEt.setTextColor(inputEtColor);
        inputEt.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputEtSize);
        inputEt.setText(searchText);
        inputEt.setHintTextColor(inputEtHintColor);
        inputEt.setHint(inputEtHint);

        limitEditLength(maxSearchLength);//限制输入内容最大长度（默认不限制）
        limitSearchViewHeight(inputEtContainer);//设置输入框外层容器高度

        // 是否整个搜索自定义控件可以点击
        flowSearchView.setVisibility(rootClickable ? View.VISIBLE : View.GONE);
        // 显示或隐藏输入框左侧的搜索图标
        searchViewIcon.setVisibility(showSearchIcon ? VISIBLE : GONE);
        searchViewIcon.setColor(searchIconColor);

        //显示或隐藏控件
        backBtn.setVisibility(showBackButton ? VISIBLE : GONE);
        // 设置箭头各个属性
        backBtn.setArrowColor(arrowColor);
        backBtn.setArrowStyle(arrowStyle);
        backBtn.setPadding(arrowPadding);
        // 底部分割线
        btmDivider.setVisibility(bottomDividerVisible ? VISIBLE : GONE);

        // 设置输入框左侧 Padding
        // 注意: 如果隐藏输入框左侧的搜索图标,则必须重置 inputEtPaddingLeft
        if (showSearchIcon){
            inputEt.setPadding(inputEtPaddingLeft + dp2px(30), 0, dp2px(2F), 0);
        }else {
            inputEt.setPadding(inputEtPaddingLeft, 0, dp2px(2F), 0);
        }

        showOrHideClearButton(false, null);
    }

    private void setClicks() {
        //设置监听器
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof Activity) {
                    ((Activity) v.getContext()).finish();
                }
            }
        });
        inputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                showOrHideClearButton(showClearButton, s);
                if (onEditChangeListener != null) {
                    onEditChangeListener.onEditChanged(s.toString());
                }
            }
        });
        //输入法右下角回车/搜索按钮被点击
        inputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (onEnterClickListener != null && actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onEnterClickListener.onEnterClick(inputEt.getText().toString());
                    return true;
                }
                return false;
            }
        });

        clareView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClearClickListener != null) {
                    onClearClickListener.onClick(inputEt.getText().toString());
                } else {
                    clear();//默认实现
                }
            }
        });
    }

    private void showOrHideClearButton(boolean isShow, Editable text) {
        if (isShow && !TextUtils.isEmpty(text)) {
            clareView.setVisibility(VISIBLE);
        } else {
            clareView.setVisibility(GONE);
        }
    }

    //如果目标字符串为空，就获取一个默认字符
    private String getOrDefault(String target, String defaultStr) {
        if (TextUtils.isEmpty(target)) {
            return defaultStr;
        }
        return target;
    }

    //清除输入框中的内容
    private void clear() {
        searchText = "";
        inputEt.setText("");
    }

    //设置输入框的最大内容长度
    private void limitEditLength(int length) {
        if (length > 0) {
            inputEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxSearchLength)});
        }
    }

    //设置输入框外层容器高度
    private void limitSearchViewHeight(int searchViewHeight) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) inputContainer.getLayoutParams();
        layoutParams.height = searchViewHeight;
        inputContainer.setLayoutParams(layoutParams);
    }

    /************************************向外界暴露的方法*******************************/

    // 设置是否可以获得焦点,如果不可以,则可以用于点击事件
    public void setRootClickable(boolean rootClickable) {
        this.rootClickable = rootClickable;
    }


    /***************************设置监听器********************************/

    public void setOnClearClickListener(OnClearClickListener onClearClickListener) {
        this.onClearClickListener = onClearClickListener;
    }


    public void setOnEditChangeListener(OnEditChangeListener onEditChangeListener) {
        this.onEditChangeListener = onEditChangeListener;
    }

    public void setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
    }


    //清除所有监听器
    public void clearListeners() {
        onClearClickListener = null;
        onEditChangeListener = null;
        onEnterClickListener = null;
    }

    /**
     * 监听清除按钮点击事件
     */
    public interface OnClearClickListener {
        /**
         * @param oldContent 被删除的输入框内容
         */
        void onClick(String oldContent);
    }

    /**
     * 监听输入框内容变化
     */
    public interface OnEditChangeListener {
        /**
         * @param nowContent 输入框当前的内容
         */
        void onEditChanged(String nowContent);
    }

    /**
     * 监听用户点击了虚拟键盘中右下角的回车/搜索键
     * 此时可以选择执行搜索操作
     */
    public interface OnEnterClickListener {
        /**
         * @param content 输入框中的内容
         */
        void onEnterClick(String content);
    }

    private int dp2px(float def) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, def, getContext().getResources().getDisplayMetrics());
    }
}

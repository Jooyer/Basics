package cn.lvsong.lib.library.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import androidx.annotation.ColorRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import cn.lvsong.lib.library.R;


/**
 * https://github.com/yuxingxin/TintStateImage/blob/master/library/src/main/res/values/attrs.xml  --> getColorStateList
 * <p>
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
            app:lirt_spacing="@dimen/padding_15"
            app:lirt_text_size="@dimen/text_size_14"
            app:lirt_text="左边图片,右边文字"
            app:lirt_checked="false"
            />

 */

public class LeftImgAndRightTextView extends RelativeLayout implements Checkable {
    /**
     * 左右结构，图片在左，文字在右
     */
    public static final int STYLE_ICON_LEFT = 0;
    /**
     * 左右结构，图片在右，文字在左
     */
    public static final int STYLE_ICON_RIGHT = 1;
    /**
     * 上下结构，图片在上，文字在下
     */
    public static final int STYLE_ICON_UP = 2;
    /**
     * 上下结构，图片在下，文字在上
     */
    public static final int STYLE_ICON_DOWN = 3;

    /**
     * 定义控件
     */
    private AppCompatImageView ivIcon;
    private MediumTextView tvContent;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * View的背景色
     */
    private int backColor = 0;
    /**
     * View被按下时的背景色
     */
    private int backColorChecked = 0;
    /**
     * icon的背景图片
     */
    private Drawable iconDrawable = null;
    /**
     * icon被按下时显示的背景图片
     */
    private Drawable iconDrawableChecked = null;
    /**
     * View文字的颜色
     */
    private ColorStateList textColor = null;
    /**
     * View被按下时文字的颜色
     */
    private ColorStateList textColorChecked = null;
    /**
     * 两个控件之间的间距，默认为8dp
     */
    private int spacing = 8;
    /**
     * 两个控件的位置结构
     */
    private int mPosition = STYLE_ICON_LEFT;
    /**
     * 是否被选中
     */
    private boolean isChecked;

    public LeftImgAndRightTextView(Context context) {
        super(context);
        mContext = context;
    }

    public LeftImgAndRightTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftImgAndRightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context, attrs, defStyle);

    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        //加载布局
        LayoutInflater.from(context).inflate(R.layout.common_ui_left_image_right_text_view, this, true);
        //初始化控件
        ivIcon = (AppCompatImageView) findViewById(R.id.iv_icon);
        tvContent = (MediumTextView) findViewById(R.id.tv_content);
        setGravity(Gravity.CENTER);
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.LeftImgAndRightTextView, defStyle, 0);
        if (a != null) {
            //设置背景色
            ColorStateList colorList = a.getColorStateList(R.styleable.LeftImgAndRightTextView_lirt_back_color);
            if (colorList != null) {
                backColor = colorList.getColorForState(getDrawableState(), 0);
            }
            //记录View被按下时的背景色
            ColorStateList colorListChecked = a.getColorStateList(R.styleable.LeftImgAndRightTextView_lirt_back_color_checked);
            if (colorListChecked != null) {
                backColorChecked = colorListChecked.getColorForState(getDrawableState(), 0);
            }
            //设置icon
            iconDrawable = a.getDrawable(R.styleable.LeftImgAndRightTextView_lirt_icon_drawable);

            //记录View被按下时的icon的图片
            iconDrawableChecked = a.getDrawable(R.styleable.LeftImgAndRightTextView_lirt_icon_drawable_checked);
            //设置文字的颜色
            textColor = a.getColorStateList(R.styleable.LeftImgAndRightTextView_lirt_text_color);

            //记录View被按下时文字的颜色
            textColorChecked = a.getColorStateList(R.styleable.LeftImgAndRightTextView_lirt_text_color_checked);
            //设置显示的文本内容
            String text = a.getString(R.styleable.LeftImgAndRightTextView_lirt_text);
            if (text != null) {
                //默认为隐藏的，设置文字后显示出来
                tvContent.setVisibility(VISIBLE);
                tvContent.setText(text);
            }
            //设置文本字体大小
            float textSize = a.getDimensionPixelSize(R.styleable.LeftImgAndRightTextView_lirt_text_size,  dp2px(context, 14));
            if (textSize != 0) {
                tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
            }
            //设置两个控件之间的间距
            spacing = a.getDimensionPixelSize(R.styleable.LeftImgAndRightTextView_lirt_spacing, dp2px(context, 8));
            //设置两个控件的位置结构
            mPosition = a.getInt(R.styleable.LeftImgAndRightTextView_lirt_style, 0);

            isChecked = a.getBoolean(R.styleable.LeftImgAndRightTextView_lirt_checked, false);
            setChecked(isChecked);

            setIconPosition(mPosition);
            a.recycle();
        }
    }

    /**
     * 设置图标位置
     * 通过重置LayoutParams来设置两个控件的摆放位置
     *
     * @param position --> 取值: STYLE_ICON_LEFT...
     */
    public void setIconPosition(int position) {
        mPosition = position;
        LayoutParams lp;
        switch (position) {
            case STYLE_ICON_LEFT:
                lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                ivIcon.setLayoutParams(lp);
                lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                lp.addRule(RelativeLayout.RIGHT_OF, ivIcon.getId());
                lp.leftMargin = spacing;
                tvContent.setLayoutParams(lp);
                break;
            case STYLE_ICON_RIGHT:
                lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                tvContent.setLayoutParams(lp);
                lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                lp.addRule(RelativeLayout.RIGHT_OF, tvContent.getId());
                lp.leftMargin = spacing;
                ivIcon.setLayoutParams(lp);
                break;
            case STYLE_ICON_UP:
                lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                ivIcon.setLayoutParams(lp);
                lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                lp.addRule(RelativeLayout.BELOW, ivIcon.getId());
                lp.topMargin = spacing;
                tvContent.setLayoutParams(lp);
                break;
            case STYLE_ICON_DOWN:
                lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                tvContent.setLayoutParams(lp);
                lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                lp.addRule(RelativeLayout.BELOW, tvContent.getId());
                lp.topMargin = spacing;
                ivIcon.setLayoutParams(lp);
                break;
            default:
                break;
        }
    }

    /**
     * 设置控件背景色
     *
     * @param backColor
     */
    public void setBackColor(@ColorRes int backColor) {
        this.backColor = ContextCompat.getColor(getContext(), backColor);
        setBackgroundColor(this.backColor);
    }

    /**
     * 设置控件被按下时的背景色
     *
     * @param backColorChecked
     */
    public void setBackColorChecked(@ColorRes int backColorChecked) {
        this.backColorChecked = backColorChecked;
    }

    /**
     * 设置icon的图片
     *
     * @param iconDrawable
     */
    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
        ivIcon.setImageDrawable(iconDrawable);
    }

    /**
     * 设置被按下时的icon的图片
     *
     * @param iconDrawableChecked
     */
    public void setIconDrawableChecked(Drawable iconDrawableChecked) {
        this.iconDrawableChecked = iconDrawableChecked;
        ivIcon.setImageDrawable(iconDrawableChecked);
    }

    /**
     * 设置文字的颜色
     *
     * @param textColor
     */
    public void setTextColor(@ColorRes int textColor) {
        if (textColor == 0) return;
        this.textColor = ColorStateList.valueOf(ContextCompat.getColor(getContext(), textColor));
        tvContent.setTextColor(this.textColor);
    }

    /**
     * 设置被按下时文字的颜色
     *
     * @param textColorChecked
     */
    public void setTextColorChecked(@ColorRes int textColorChecked) {
        if (textColorChecked == 0) return;
        this.textColorChecked = ColorStateList.valueOf(ContextCompat.getColor(getContext(), textColorChecked));
    }

    /**
     * 设置显示的文本内容
     *
     * @param text
     */
    public void setText(CharSequence text) {
        //默认为隐藏的，设置文字后显示出来
        tvContent.setVisibility(VISIBLE);
        tvContent.setText(text);
    }

    /**
     * 获取显示的文本
     *
     * @return
     */
    public String getText() {
        return tvContent.getText().toString();
    }

    /**
     * 设置文本字体大小
     *
     * @param size
     */
    public void setTextSize(float size) {
        tvContent.setTextSize(size);
    }

    /**
     * 设置两个控件之间的间距
     *
     * @param spacing --> 单位dp
     */
    public void setSpacing(int spacing) {
        this.spacing = dp2px(mContext, spacing);
        //设置完成后刷新一下两个控件的结构，避免先执行了setIconStyle后，setSpacing不生效
        setIconPosition(mPosition);
    }

    private int dp2px(Context context, int spacing) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spacing, context.getResources().getDisplayMetrics());
    }


    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        if (isChecked) {
            if (backColorChecked != 0) {
                setBackgroundColor(backColorChecked);
            }
            if (iconDrawableChecked != null) {
                ivIcon.setImageDrawable(iconDrawableChecked);
            }
            if (textColorChecked != null) {
                tvContent.setTextColor(textColorChecked);
            }
        } else {
            if (backColor != 0) {
                setBackgroundColor(backColor);
            }
            if (iconDrawable != null) {
                ivIcon.setImageDrawable(iconDrawable);
            }
            if (textColor != null) {
                tvContent.setTextColor(textColor);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}

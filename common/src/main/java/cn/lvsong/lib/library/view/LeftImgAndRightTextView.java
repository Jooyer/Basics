package cn.lvsong.lib.library.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.lvsong.lib.library.R;


/** https://github.com/yuxingxin/TintStateImage/blob/master/library/src/main/res/values/attrs.xml  --> getColorStateList
 *
 * Desc: 默认为左右结构，图片在左，文字在右
 * Author: Jooyer
 * Date: 2019-05-24
 * Time: 23:12
 */
public class LeftImgAndRightTextView extends RelativeLayout {
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
    private ImageView ivIcon;
    private TextView tvContent;
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
    private int backColorPress = 0;
    /**
     * icon的背景图片
     */
    private Drawable iconDrawable = null;
    /**
     * icon被按下时显示的背景图片
     */
    private Drawable iconDrawablePress = null;
    /**
     * View文字的颜色
     */
    private ColorStateList textColor = null;
    /**
     * View被按下时文字的颜色
     */
    private ColorStateList textColorPress = null;
    /**
     * 两个控件之间的间距，默认为8dp
     */
    private int spacing = 8;
    /**
     * 两个控件的位置结构
     */
    private int mStyle = STYLE_ICON_LEFT;
    /**
     * 标示onTouch方法的返回值，用来解决onClick和onTouch冲突问题
     */
    private boolean isCost = true;

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
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tvContent = (TextView) findViewById(R.id.tv_content);
        setGravity(Gravity.CENTER);
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.LeftImgAndRightTextView, defStyle, 0);
        if (a != null) {
            //设置背景色
            ColorStateList colorList = a.getColorStateList(R.styleable.LeftImgAndRightTextView_itv_backColor);
            if (colorList != null) {
                backColor = colorList.getColorForState(getDrawableState(), 0);
                if (backColor != 0) {
                    setBackgroundColor(backColor);
                }
            }
            //记录View被按下时的背景色
            ColorStateList colorListPress = a.getColorStateList(R.styleable.LeftImgAndRightTextView_itv_backColorPress);
            if (colorListPress != null) {
                backColorPress = colorListPress.getColorForState(getDrawableState(), 0);
            }
            //设置icon
            iconDrawable = a.getDrawable(R.styleable.LeftImgAndRightTextView_itv_iconDrawable);
            if (iconDrawable != null) {
                ivIcon.setImageDrawable(iconDrawable);
            }
            //记录View被按下时的icon的图片
            iconDrawablePress = a.getDrawable(R.styleable.LeftImgAndRightTextView_itv_iconDrawablePress);
            //设置文字的颜色
            textColor = a.getColorStateList(R.styleable.LeftImgAndRightTextView_itv_textColor);
            if (textColor != null) {
                tvContent.setTextColor(textColor);
            }
            //记录View被按下时文字的颜色
            textColorPress = a.getColorStateList(R.styleable.LeftImgAndRightTextView_itv_textColorPress);
            //设置显示的文本内容
            String text = a.getString(R.styleable.LeftImgAndRightTextView_itv_text);
            if (text != null) {
                //默认为隐藏的，设置文字后显示出来
                tvContent.setVisibility(VISIBLE);
                tvContent.setText(text);
            }
            //设置文本字体大小
            float textSize = a.getFloat(R.styleable.LeftImgAndRightTextView_itv_textSize, 0);
            if (textSize != 0) {
                tvContent.setTextSize(textSize);
            }
            //设置两个控件之间的间距
            spacing = a.getDimensionPixelSize(R.styleable.LeftImgAndRightTextView_itv_spacing, dp2px(context, 8));
            //设置两个控件的位置结构
            mStyle = a.getInt(R.styleable.LeftImgAndRightTextView_itv_style, 0);
            setIconStyle(mStyle);
            a.recycle();
        }

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                //根据touch事件设置按下抬起的样式
                setTouchStyle(event.getAction());
                return LeftImgAndRightTextView.super.onTouchEvent(event);
            }
        });

    }

    /**
     * 根据按下或者抬起来改变背景和文字样式
     *
     * @param state
     * @return isCost
     */
    private boolean setTouchStyle(int state) {
        if (state == MotionEvent.ACTION_DOWN) {
            if (backColorPress != 0) {
                setBackgroundColor(backColorPress);
            }
            if (iconDrawablePress != null) {
                ivIcon.setImageDrawable(iconDrawablePress);
            }
            if (textColorPress != null) {
                tvContent.setTextColor(textColorPress);
            }
        }
        if (state == MotionEvent.ACTION_UP) {
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
        return isCost;
    }

    /**
     * 设置图标位置
     * 通过重置LayoutParams来设置两个控件的摆放位置
     *
     * @param style
     */
    public void setIconStyle(int style) {
        mStyle = style;
        LayoutParams lp;
        switch (style) {
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
                lp.bottomMargin = spacing;
                ivIcon.setLayoutParams(lp);
                break;
            default:
                break;
        }
    }

    /**
     * 设置View的背景色
     *
     * @param backColor
     */
    public void setBackColor(int backColor) {
        this.backColor = backColor;
        setBackgroundColor(backColor);
    }

    /**
     * 设置View被按下时的背景色
     *
     * @param backColorPress
     */
    public void setBackColorPress(int backColorPress) {
        this.backColorPress = backColorPress;
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
     * 设置View被按下时的icon的图片
     *
     * @param iconDrawablePress
     */
    public void setIconDrawablePress(Drawable iconDrawablePress) {
        this.iconDrawablePress = iconDrawablePress;
        ivIcon.setImageDrawable(iconDrawablePress);
    }

    /**
     * 设置文字的颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        if (textColor == 0) return;
        this.textColor = ColorStateList.valueOf(textColor);
        tvContent.setTextColor(this.textColor);
    }

    /**
     * 设置View被按下时文字的颜色
     *
     * @param textColorPress
     */
    public void setTextColorPress(int textColorPress) {
        if (textColorPress == 0) return;
        this.textColorPress = ColorStateList.valueOf(textColorPress);
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
     * @param spacing
     */
    public void setSpacing(int spacing) {
        this.spacing = dp2px(mContext, spacing);
        //设置完成后刷新一下两个控件的结构，避免先执行了setIconStyle后，setSpacing不生效
        setIconStyle(mStyle);
    }

    private int dp2px(Context context,int spacing) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spacing, context.getResources().getDisplayMetrics());
    }


}

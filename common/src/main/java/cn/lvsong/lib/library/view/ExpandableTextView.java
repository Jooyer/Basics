package cn.lvsong.lib.library.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import cn.lvsong.lib.library.R;


/**
 * 参考: https://github.com/MrTrying/ExpandableText-Example/tree/master
 * Desc: 展开折叠控件
 * Author: Jooyer
 * Date: 2020-09-14
 * Time: 18:40
 */

/*

    el_container_item_message.initWidth(
                    ScreenUtils.getRealWidth(rv_list_message.context) - DensityUtil.dp2pxRtInt(
                        84
                    )
                )
    el_container_item_message.setOriginalText("xxx")

 */

public class ExpandableTextView extends AppCompatTextView {

    private static final String TAG = ExpandableTextView.class.getSimpleName();

    // ...
    public static final String ELLIPSIS_STRING = new String(new char[]{'\u2026'});

    private static final int DEFAULT_MAX_LINE = 3;

    private static final String DEFAULT_OPEN_SUFFIX = "展开";

    private static final String DEFAULT_CLOSE_SUFFIX = "收起";
    // 是否正在动画中
    private volatile boolean animating = false;
    // 是否是折叠状态
    private boolean isClosed = false;
    // 是否可折叠,文本超过最大行数则会折叠
    private boolean mExpandable;
    // 是否在展开时 对折叠文本/图标 另起一行
    private boolean mCloseInNewLine = false;
    // 在展开和折叠时有无动画切换效果
    private boolean hasAnimation = false;
    // 默认的最大行数
    private int mMaxLines = DEFAULT_MAX_LINE;
    // TextView可展示宽度，包含paddingLeft和paddingRight
    private int initWidth = 0;
    // 设置显示模式
    private int mSwitchMode = 1;
    // 展开和折叠时 高度
    private int mOpenHeight, mCloseHeight;
    // 展开/折叠时  显示的文本内容(经过处理,eg: 增加展开/折叠按钮)
    private SpannableStringBuilder mOpenSpannableStr, mCloseSpannableStr;
    // 展开/折叠动画
    private ValueAnimator mOpenAnim, mCloseAnim;
    // 展开/折叠时显示的可点击的内容
    @Nullable
    private SpannableString mOpenSuffixSpan, mCloseSuffixSpan;
    // 折叠时显示的文本
    private String mOpenSuffixStr = DEFAULT_OPEN_SUFFIX;
    // 展开时显示的文本
    private String mCloseSuffixStr = DEFAULT_CLOSE_SUFFIX;
    // 展开/折叠文本颜色
    private int mOpenSuffixColor, mCloseSuffixColor;
    // 展开时图标
    private int mExpandDrawableId;
    // 折叠时图标
    private int mCollapsedDrawableId;
    // 展开/折叠回调
    public OnOpenAndCloseListener mOnOpenAndCloseListener;

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    /**
     * 初始化
     */
    private void initialize(Context context, AttributeSet attrs) {
        setOnTouchListener(new LinkMovementMethodOverride());
        if (null != attrs) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
            mMaxLines = array.getInt(R.styleable.ExpandableTextView_etv_collapse_max_lines, mMaxLines);
            mSwitchMode = array.getInt(R.styleable.ExpandableTextView_etv_switch_mode, mSwitchMode);
            mExpandDrawableId = array.getResourceId(R.styleable.ExpandableTextView_etv_expand_drawable, 0);
            mCollapsedDrawableId = array.getResourceId(R.styleable.ExpandableTextView_etv_collapse_drawable, 0);
            mOpenSuffixStr = array.getString(R.styleable.ExpandableTextView_etv_expand_text);
            mCloseSuffixStr = array.getString(R.styleable.ExpandableTextView_etv_collapse_text);
            mOpenSuffixColor = array.getColor(R.styleable.ExpandableTextView_etv_expand_text_color,
                    ContextCompat.getColor(context, R.color.color_2878FF));
            mCloseSuffixColor = array.getColor(R.styleable.ExpandableTextView_etv_collapse_text_color,
                    ContextCompat.getColor(context, R.color.color_2878FF));
            array.recycle();
            if (TextUtils.isEmpty(mOpenSuffixStr)) {
                mOpenSuffixStr = DEFAULT_OPEN_SUFFIX;
            }
            if (TextUtils.isEmpty(mCloseSuffixStr)) {
                mCloseSuffixStr = DEFAULT_CLOSE_SUFFIX;
            }
        }
        setIncludeFontPadding(false);
        updateOpenSuffixSpan();
        updateCloseSuffixSpan();
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    public void setOriginalText(CharSequence originalText) {
        mExpandable = false;

        mCloseSpannableStr = new SpannableStringBuilder();
        mOpenSpannableStr = charSequenceToSpannable(originalText);
        SpannableStringBuilder tempText = charSequenceToSpannable(originalText);

        if (mMaxLines != -1) {
            Layout layout = createStaticLayout(tempText);
            int originalLineCount = layout.getLineCount();
            // 如果文本行数 > 给定的最多行数则认为可折叠和展开
            mExpandable = layout.getLineCount() > mMaxLines;
            if (mExpandable) {
                //拼接展开内容
                if (mCloseInNewLine) { // 如果需要对展开后图标另起一行
                    mOpenSpannableStr.append("\n");
                }

                // 将图标放到最后一行末尾
                if (mCloseSuffixSpan != null) {
                    int closeSuffixSpanLength = mCloseSuffixSpan.length();
                    int openSpannableStrLength = mOpenSpannableStr.length();
                    Layout closeLayout = createStaticLayout(mOpenSpannableStr);
                    while (closeLayout.getLineCount() <= originalLineCount) {
                        mOpenSpannableStr.append("@");
                        closeLayout = createStaticLayout(mOpenSpannableStr);
                    }
                    // 删除多添加的占位符 @,同时留出 mCloseSuffixSpan 位置
                    if (1 == mSwitchMode) { // 文本模式
                        mOpenSpannableStr.delete(mOpenSpannableStr.length() - 2 - closeSuffixSpanLength, mOpenSpannableStr.length() - 1);
                    } else { // 图片模式(图片占据实际位置宽度<文本)
                        mOpenSpannableStr.delete(mOpenSpannableStr.length() - 1 - closeSuffixSpanLength, mOpenSpannableStr.length() - 1);
                    }
                    // 修正上面删除没有到位问题
                    int lineCount = createStaticLayout(charSequenceToSpannable(mOpenSpannableStr).append(mCloseSuffixSpan)).getLineCount();
                    if (lineCount > originalLineCount) {
                        mOpenSpannableStr.delete(mOpenSpannableStr.length() - 2, mOpenSpannableStr.length() - 1);
                    }
                    mOpenSpannableStr.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), openSpannableStrLength,
                            mOpenSpannableStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mOpenSpannableStr.append(mCloseSuffixSpan);
                }

                //计算原文截取位置,如折叠时最多显示3行, 下面就是计算第三行
                int endPos = layout.getLineEnd(mMaxLines - 1);
                if (originalText.length() <= endPos) { // 原文本刚好在第maxLines行末尾
                    mCloseSpannableStr = charSequenceToSpannable(originalText);
                } else { // 截取到maxLines行位置
                    mCloseSpannableStr = charSequenceToSpannable(originalText.subSequence(0, endPos));
                }
                SpannableStringBuilder tempText2 = charSequenceToSpannable(mCloseSpannableStr).append(ELLIPSIS_STRING);
                if (mOpenSuffixSpan != null) {
                    tempText2.append(mOpenSuffixSpan);
                }
                //循环判断，收起内容添加展开后缀后的内容是否超过折叠时最大显示行数
                Layout tempLayout = createStaticLayout(tempText2);
                while (tempLayout.getLineCount() > mMaxLines) {
                    int lastSpace = mCloseSpannableStr.length() - 1;
                    if (lastSpace == -1) { // mCloseSpannableStr没有内容
                        break;
                    }

                    if (originalText.length() <= lastSpace) { // 如果原文本长度 < mCloseSpannableStr
                        mCloseSpannableStr = charSequenceToSpannable(originalText);
                    } else { // 截取能显示的最多文本
                        mCloseSpannableStr = charSequenceToSpannable(originalText.subSequence(0, lastSpace));
                    }
                    // 再次拼接截取后的文本
                    tempText2 = charSequenceToSpannable(mCloseSpannableStr).append(ELLIPSIS_STRING);
                    if (mOpenSuffixSpan != null) {
                        tempText2.append(mOpenSuffixSpan);
                    }
                    tempLayout = createStaticLayout(tempText2);
                }
                //计算收起的文本高度
                mCloseHeight = tempLayout.getHeight() + getPaddingTop() + getPaddingBottom();

                mCloseSpannableStr.append(ELLIPSIS_STRING);
                if (mOpenSuffixSpan != null) {
                    mCloseSpannableStr.append(mOpenSuffixSpan);
                }
            }
        }
        // 意味着默认是折叠状态
        isClosed = mExpandable;
        if (mExpandable) {
            setText(mCloseSpannableStr);
        } else {
            setText(mOpenSpannableStr);
        }
    }

    private void switchOpenClose() {
        if (mExpandable) {
            isClosed = !isClosed;
            if (isClosed) {
                close();
            } else {
                open();
            }
        }
    }

    /**
     * 展开
     */
    private void open() {
        if (hasAnimation) {
            Layout layout = createStaticLayout(mOpenSpannableStr);
            mOpenHeight = layout.getHeight() + getPaddingTop() + getPaddingBottom();
            executeOpenAnim();
        } else {
            ExpandableTextView.super.setMaxLines(Integer.MAX_VALUE);
            setText(mOpenSpannableStr);
            if (mOnOpenAndCloseListener != null) {
                mOnOpenAndCloseListener.onOpen(this);
            }
        }
    }

    /**
     * 收起
     */
    private void close() {
        if (hasAnimation) {
            executeCloseAnim();
        } else {
            ExpandableTextView.super.setMaxLines(mMaxLines);
            setText(mCloseSpannableStr);
            if (mOnOpenAndCloseListener != null) {
                mOnOpenAndCloseListener.onClose(this);
            }
        }
    }

    /**
     * 执行展开动画
     */
    private void executeOpenAnim() {
        //创建展开动画
        if (mOpenAnim == null) {
            mOpenAnim = ValueAnimator.ofFloat(mCloseHeight, mOpenHeight);
            mOpenAnim.setDuration(400);
            mOpenAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float height = (float) animation.getAnimatedValue();
//                    Log.e("Expand", "executeOpenAnim=======height: " + height);
                    //计算出每次应该显示的高度,改变执行view的高度，实现动画
                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.height = (int) height;
                    //调用requestLayout刷新布局
                    requestLayout();
                }
            });

            mOpenAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animating = true;
                    ExpandableTextView.super.setMaxLines(Integer.MAX_VALUE);
                    setText(mOpenSpannableStr);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    animating = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animating = false;
                }
            });
        }

        if (animating) {
            return;
        }
        animating = true;
        mOpenAnim.start();
    }

    /**
     * 执行收起动画
     */
    private void executeCloseAnim() {
        //创建收起动画
        if (mCloseAnim == null) {
            mCloseAnim = ValueAnimator.ofFloat(mOpenHeight, mCloseHeight);
            mCloseAnim.setDuration(400);
            mCloseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setScrollY(0);
                    float height = (float) animation.getAnimatedValue();
//                    Log.e("Expand", "executeOpenAnim=======height: " + height);
                    //计算出每次应该显示的高度,改变执行view的高度，实现动画
                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.height = (int) height;
                    //调用requestLayout刷新布局
                    requestLayout();
                }
            });

            mCloseAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setScrollY(0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    animating = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animating = false;
                    ExpandableTextView.super.setMaxLines(mMaxLines);
                    setText(mCloseSpannableStr);
                }
            });
        }

        if (animating) {
            return;
        }
        animating = true;
        mCloseAnim.start();
    }

    /**
     * @param spannable --> 显示的内容
     * @return Layout
     */
    private Layout createStaticLayout(SpannableStringBuilder spannable) {
        int contentWidth = initWidth - getPaddingLeft() - getPaddingRight();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder builder = StaticLayout.Builder.obtain(spannable, 0, spannable.length(), getPaint(), contentWidth);
            builder.setAlignment(Layout.Alignment.ALIGN_NORMAL);
            builder.setIncludePad(getIncludeFontPadding());
            builder.setLineSpacing(getLineSpacingExtra(), getLineSpacingMultiplier());
            return builder.build();
        } else {
            return new StaticLayout(spannable, getPaint(), contentWidth, Layout.Alignment.ALIGN_NORMAL,
                    getLineSpacingMultiplier(), getLineSpacingExtra(), getIncludeFontPadding());
        }
    }

    /**
     * @param charSequence
     * @return SpannableStringBuilder
     */
    private SpannableStringBuilder charSequenceToSpannable(@NonNull CharSequence charSequence) {
        return new SpannableStringBuilder(charSequence);
    }

    /**
     * 初始化TextView的可展示宽度
     *
     * @param width
     */
    public ExpandableTextView initWidth(int width) {
        initWidth = width;
        return this;
    }

    /**
     * 设置是否有动画
     *
     * @param hasAnimation --> 默认false,即没有动画
     */
    public ExpandableTextView setHasAnimation(boolean hasAnimation) {
        this.hasAnimation = hasAnimation;
        return this;
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.mMaxLines = maxLines;
        super.setMaxLines(maxLines);
    }

    /**
     * 设置展开后缀
     *
     * @param openSuffix
     */
    public void setOpenSuffix(String openSuffix) {
        mOpenSuffixStr = openSuffix;
        updateOpenSuffixSpan();
    }

    /**
     * 设置展开后缀文本颜色
     *
     * @param openSuffixColor
     */
    public void setOpenSuffixColor(@ColorInt int openSuffixColor) {
        mOpenSuffixColor = openSuffixColor;
        updateOpenSuffixSpan();
    }

    /**
     * 设置收起后缀text
     *
     * @param closeSuffix
     */
    public void setCloseSuffix(String closeSuffix) {
        mCloseSuffixStr = closeSuffix;
        updateCloseSuffixSpan();
    }

    /**
     * 设置收起后缀文本颜色
     *
     * @param closeSuffixColor
     */
    public void setCloseSuffixColor(@ColorInt int closeSuffixColor) {
        mCloseSuffixColor = closeSuffixColor;
        updateCloseSuffixSpan();
    }

    /**
     * 是否在展开时 对折叠文本/图标 另起一行
     *
     * @param closeInNewLine
     */
    public void setCloseInNewLine(boolean closeInNewLine) {
        mCloseInNewLine = closeInNewLine;
        updateCloseSuffixSpan();
    }

    /**
     * 点击展开走这里
     * 更新展开后缀Spannable
     */
    private void updateOpenSuffixSpan() {
        if (TextUtils.isEmpty(mOpenSuffixStr)) {
            mOpenSuffixSpan = null;
            return;
        }
        mOpenSuffixSpan = new SpannableString(mOpenSuffixStr);
        if (1 == mSwitchMode) {
            mOpenSuffixSpan.setSpan(new ClickableSpan() {

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setColor(mOpenSuffixColor);
                }

                @Override
                public void onClick(@NonNull View widget) {
                    switchOpenClose();
                }
            }, 0, mOpenSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            mOpenSuffixSpan.setSpan(new CustomImageSpan(getContext(), mCollapsedDrawableId, DynamicDrawableSpan.ALIGN_CENTER) {
                                        @Override
                                        public void onClick(View view) {
                                            switchOpenClose();
                                        }
                                    },
                    0, mOpenSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 点击折叠走这里
     * 更新收起后缀Spannable
     */
    private void updateCloseSuffixSpan() {
        if (TextUtils.isEmpty(mCloseSuffixStr)) {
            mCloseSuffixSpan = null;
            return;
        }
        mCloseSuffixSpan = new SpannableString(mCloseSuffixStr);
        if (1 == mSwitchMode) {
            mCloseSuffixSpan.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setColor(mCloseSuffixColor);
                }

                @Override
                public void onClick(@NonNull View widget) {
                    switchOpenClose();
                }
            }, 0, mCloseSuffixSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            mCloseSuffixSpan.setSpan(new CustomImageSpan(getContext(), mExpandDrawableId, DynamicDrawableSpan.ALIGN_CENTER) {
                                         @Override
                                         public void onClick(View view) {
                                             switchOpenClose();
                                         }
                                     },
                    0, mCloseSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (mCloseInNewLine) {  // 如果需要对展开后图标另起一行
            AlignmentSpan alignmentSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE);
            mCloseSuffixSpan.setSpan(alignmentSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public void setOnOpenAndCloseListener(OnOpenAndCloseListener callback) {
        this.mOnOpenAndCloseListener = callback;
    }

    public interface OnOpenAndCloseListener {
        void onOpen(ExpandableTextView tv);

        void onClose(ExpandableTextView tv);
    }

    /**
     * 拷贝自: https://www.jianshu.com/p/2650357f7547
     * 自定义imageSpan实现图片与文字的居中对齐
     */
    static abstract class CustomImageSpan extends ImageSpan {

        //自定义对齐方式--与文字中间线对齐
        private static final int ALIGN_FONTCENTER = 2;

        public CustomImageSpan(Context context, int resourceId, int verticalAlignment) {
            super(context, resourceId, verticalAlignment);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                         Paint paint) {

            //draw 方法是重写的ImageSpan父类 DynamicDrawableSpan中的方法，在DynamicDrawableSpan类中，虽有getCachedDrawable()，
            // 但是私有的，不能被调用，所以调用ImageSpan中的getrawable()方法，该方法中 会根据传入的drawable ID ，获取该id对应的
            // drawable的流对象，并最终获取drawable对象
            Drawable drawable = getDrawable(); //调用imageSpan中的方法获取drawable对象
            canvas.save();

            //获取画笔的文字绘制时的具体测量数据
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();

            //系统原有方法，默认是Bottom模式)
            int transY = bottom - drawable.getBounds().bottom;
            if (mVerticalAlignment == ALIGN_BASELINE) {
                transY -= fm.descent;
            } else if (mVerticalAlignment == ALIGN_FONTCENTER) {    //此处加入判断， 如果是自定义的居中对齐
                //与文字的中间线对齐（这种方式不论是否设置行间距都能保障文字的中间线和图片的中间线是对齐的）
                // y+ascent得到文字内容的顶部坐标，y+descent得到文字的底部坐标，（顶部坐标+底部坐标）/2=文字内容中间线坐标
                transY = ((y + fm.descent) + (y + fm.ascent)) / 2 - drawable.getBounds().bottom / 2;
            }

            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }

        /**
         * 重写getSize方法，只有重写该方法后，才能保证不论是图片大于文字还是文字大于图片，都能实现中间对齐
         */
        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            Drawable d = getDrawable();
            Rect rect = d.getBounds();
            if (fm != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;

                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;

                fm.ascent = -bottom;
                fm.top = -bottom;
                fm.bottom = top;
                fm.descent = top;
            }
            return rect.right;
        }

        public abstract void onClick(View view);
    }

    // 参考: https://www.jianshu.com/p/5e547c8e0f37 和  https://www.cnblogs.com/luction/p/3645210.html
    static class LinkMovementMethodOverride implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int action = event.getAction();
            TextView widget = (TextView) view;
            CharSequence text = widget.getText();
            if (text instanceof Spanned) {
                Spanned buffer = (Spanned) text;
                if (action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= widget.getTotalPaddingLeft();
                    y -= widget.getTotalPaddingTop();

                    x += widget.getScrollX();
                    y += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y);
                    int off = layout.getOffsetForHorizontal(line, x);

                    ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
                    CustomImageSpan[] imageSpans = buffer.getSpans(off, off, CustomImageSpan.class);

                    if (link.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            link[0].onClick(widget);
                        }
                        return true;
                    } else if (imageSpans.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            imageSpans[0].onClick(widget);
                        }
                        return true;
                    }
                }
            }
            return false;
        }
    }


}

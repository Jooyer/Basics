package cn.lvsong.lib.library.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;


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
                    .setHasAnimation(true).maxLines = 3
                el_container_item_message.setOriginalText(bean)

 */

public class ExpandableTextView extends AppCompatTextView {
    private static final String TAG = ExpandableTextView.class.getSimpleName();

    public static final String ELLIPSIS_STRING = new String(new char[]{'\u2026'});
    private static final int DEFAULT_MAX_LINE = 3;
    private static final String DEFAULT_OPEN_SUFFIX = " 展开";
    private static final String DEFAULT_CLOSE_SUFFIX = " 收起";
    volatile boolean animating = false;
    boolean isClosed = false;
    private int mMaxLines = DEFAULT_MAX_LINE;
    /**
     * TextView可展示宽度，包含paddingLeft和paddingRight
     */
    private int initWidth = 0;

    private SpannableStringBuilder mOpenSpannableStr, mCloseSpannableStr;

    private boolean hasAnimation = false;
    private ValueAnimator mOpenAnim, mCloseAnim;
    private int mOpenHeight, mCloseHeight;
    private boolean mExpandable;
    private boolean mCloseInNewLine;
    @Nullable
    private SpannableString mOpenSuffixSpan, mCloseSuffixSpan;
    private String mOpenSuffixStr = DEFAULT_OPEN_SUFFIX;
    private String mCloseSuffixStr = DEFAULT_CLOSE_SUFFIX;
    private int mOpenSuffixColor, mCloseSuffixColor;

    private CharSequenceToSpannableHandler mCharSequenceToSpannableHandler;

    public ExpandableTextView(Context context) {
        super(context);
        initialize();
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    /**
     * 初始化
     */
    private void initialize() {
        mOpenSuffixColor = mCloseSuffixColor = Color.parseColor("#F23030");
        setOnTouchListener(new LinkMovementMethodOverride());
        setIncludeFontPadding(false);
        updateOpenSuffixSpan();
        updateCloseSuffixSpan();
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    public void setOriginalText(CharSequence originalText) {
        final int maxLines = mMaxLines;
        mExpandable = false;

        mCloseSpannableStr = new SpannableStringBuilder();
        mOpenSpannableStr = charSequenceToSpannable(originalText);
        SpannableStringBuilder tempText = charSequenceToSpannable(originalText);

        if (maxLines != -1) {
            Layout layout = createStaticLayout(tempText);
            // 如果文本行数 > 给定的最多行数则认为可折叠和展开
            mExpandable = layout.getLineCount() > maxLines;
            if (mExpandable) {
                //拼接展开内容
                if (mCloseInNewLine) {
                    mOpenSpannableStr.append("\n");
                }
                if (mCloseSuffixSpan != null) {
                    mOpenSpannableStr.append(mCloseSuffixSpan);
                }
                //计算原文截取位置
                int endPos = layout.getLineEnd(maxLines - 1);
                if (originalText.length() <= endPos) {
                    mCloseSpannableStr = charSequenceToSpannable(originalText);
                } else {
                    mCloseSpannableStr = charSequenceToSpannable(originalText.subSequence(0, endPos));
                }
                SpannableStringBuilder tempText2 = charSequenceToSpannable(mCloseSpannableStr).append(ELLIPSIS_STRING);
                if (mOpenSuffixSpan != null) {
                    tempText2.append(mOpenSuffixSpan);
                }
                //循环判断，收起内容添加展开后缀后的内容
                Layout tempLayout = createStaticLayout(tempText2);
                while (tempLayout.getLineCount() > maxLines) {
                    int lastSpace = mCloseSpannableStr.length() - 1;
                    if (lastSpace == -1) {
                        break;
                    }
                    if (originalText.length() <= lastSpace) {
                        mCloseSpannableStr = charSequenceToSpannable(originalText);
                    } else {
                        mCloseSpannableStr = charSequenceToSpannable(originalText.subSequence(0, lastSpace));
                    }
                    tempText2 = charSequenceToSpannable(mCloseSpannableStr).append(ELLIPSIS_STRING);
                    if (mOpenSuffixSpan != null) {
                        tempText2.append(mOpenSuffixSpan);
                    }
                    tempLayout = createStaticLayout(tempText2);

                }
                int lastSpace = mCloseSpannableStr.length() - mOpenSuffixSpan.length();
                if (lastSpace >= 0 && originalText.length() > lastSpace) {
                    CharSequence redundantChar = originalText.subSequence(lastSpace, lastSpace + mOpenSuffixSpan.length());
                    int offset = hasEnCharCount(redundantChar) - hasEnCharCount(mOpenSuffixSpan) + 1;
                    lastSpace = offset <= 0 ? lastSpace : lastSpace - offset;
                    mCloseSpannableStr = charSequenceToSpannable(originalText.subSequence(0, lastSpace));
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

    private int hasEnCharCount(CharSequence str) {
        int count = 0;
        if (!TextUtils.isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c >= ' ' && c <= '~') {
                    count++;
                }
            }
        }
        return count;
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
     * 设置是否有动画
     *
     * @param hasAnimation --> 默认false,即没有动画
     */
    public void setHasAnimation(boolean hasAnimation) {
        this.hasAnimation = hasAnimation;
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
            if (mOpenCloseCallback != null) {
                mOpenCloseCallback.onOpen();
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
            if (mOpenCloseCallback != null) {
                mOpenCloseCallback.onClose();
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
                    Log.e("Expand", "executeOpenAnim=======height: " + height);
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
                    Log.e("Expand", "executeOpenAnim=======height: " + height);
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
        SpannableStringBuilder spannableStringBuilder = null;
        if (mCharSequenceToSpannableHandler != null) {
            spannableStringBuilder = mCharSequenceToSpannableHandler.charSequenceToSpannable(charSequence);
        }
        if (spannableStringBuilder == null) {
            spannableStringBuilder = new SpannableStringBuilder(charSequence);
        }
        return spannableStringBuilder;
    }

    /**
     * 初始化TextView的可展示宽度
     *
     * @param width
     */
    public void initWidth(int width) {
        initWidth = width;
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.mMaxLines = maxLines;
        super.setMaxLines(maxLines);
    }

    /**
     * 设置展开后缀text
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
     * 收起后缀是否另起一行
     *
     * @param closeInNewLine
     */
    public void setCloseInNewLine(boolean closeInNewLine) {
        mCloseInNewLine = closeInNewLine;
        updateCloseSuffixSpan();
    }

    /**
     * 更新展开后缀Spannable
     */
    private void updateOpenSuffixSpan() {
        if (TextUtils.isEmpty(mOpenSuffixStr)) {
            mOpenSuffixSpan = null;
            return;
        }
        mOpenSuffixSpan = new SpannableString(mOpenSuffixStr);
//        mOpenSuffixSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, mOpenSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mOpenSuffixSpan.setSpan(new CustomImageSpan(getContext(), android.R.mipmap.sym_def_app_icon, DynamicDrawableSpan.ALIGN_CENTER) {
                                    @Override
                                    public void onClick(View view) {
                                        switchOpenClose();
                                    }
                                },
                0, mOpenSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//		mOpenSuffixSpan.setSpan(new ClickableSpan() {
//			@Override
//			public void onClick(@NonNull View widget) {
//				switchOpenClose();
//			}
//
//			@Override
//			public void updateDrawState(@NonNull TextPaint ds) {
//				super.updateDrawState(ds);
//				ds.setColor(mOpenSuffixColor);
//				ds.setUnderlineText(false);
//			}
//		}, 0, mOpenSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    /**
     * 更新收起后缀Spannable
     */
    private void updateCloseSuffixSpan() {
        if (TextUtils.isEmpty(mCloseSuffixStr)) {
            mCloseSuffixSpan = null;
            return;
        }
        mCloseSuffixSpan = new SpannableString(mCloseSuffixStr);
//        mCloseSuffixSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, mCloseSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mCloseSuffixSpan.setSpan(new CustomImageSpan(getContext(), android.R.mipmap.sym_def_app_icon, DynamicDrawableSpan.ALIGN_CENTER) {
                                     @Override
                                     public void onClick(View view) {
                                         switchOpenClose();
                                     }
                                 },
                0, mCloseSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (mCloseInNewLine) {
            AlignmentSpan alignmentSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE);
            mCloseSuffixSpan.setSpan(alignmentSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
//		mCloseSuffixSpan.setSpan(new ClickableSpan() {
//			@Override
//			public void onClick(@NonNull View widget) {
//				switchOpenClose();
//			}
//
//			@Override
//			public void updateDrawState(@NonNull TextPaint ds) {
//				super.updateDrawState(ds);
//				ds.setColor(mCloseSuffixColor);
//				ds.setUnderlineText(false);
//			}
//		}, 1, mCloseSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public OpenAndCloseCallback mOpenCloseCallback;

    public void setOpenAndCloseCallback(OpenAndCloseCallback callback) {
        this.mOpenCloseCallback = callback;
    }

    public interface OpenAndCloseCallback {
        void onOpen();

        void onClose();
    }

    /**
     * 设置文本内容处理
     *
     * @param handler
     */
    public void setCharSequenceToSpannableHandler(CharSequenceToSpannableHandler handler) {
        mCharSequenceToSpannableHandler = handler;
    }

    public interface CharSequenceToSpannableHandler {
        @NonNull
        SpannableStringBuilder charSequenceToSpannable(CharSequence charSequence);
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
            Spanned buffer = (Spanned) widget.getText();
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
            return false;
        }
    }


}

package cn.lvsong.lib.library.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * https://www.jianshu.com/p/e3e4098f1c1d  --> 光标设置
 * 参考: https://www.jianshu.com/p/e64fbac202bb
 * Desc: Android 处理EditText光标显示在hint文字之前的问题,即输入之前光标在提示文本最前面,写入文字后光标跑到了输入文本后面,
 * 修改为输入前光标在提示文本末尾
 * Author: Jooyer
 * Date: 2020-02-28
 * Time: 9:48
 * 原理: 在初始化时拿到设置的hint保存起来，然后清空EditText本身的hint，最后在onDraw()方法中绘制自己的hint
 */

/**
 * 用法
 * <cn.lvsong.lib.ui.define.FixedCursorEditText
 * android:layout_width="280dp"
 * android:layout_height="50dp"
 * android:layout_marginTop="@dimen/padding_20"
 * android:hint="Android 自定义View绘制箭头"
 * android:gravity="center_vertical|right"
 * android:background="@null"
 * app:layout_constraintStart_toStartOf="parent"
 * app:layout_constraintEnd_toEndOf="parent"
 * app:layout_constraintTop_toBottomOf="@id/toolbar"
 * />
 */
public class FixedCursorEditText extends AppCompatEditText {

    private CharSequence mHint;

    private Paint mHintPaint;
    private int mCurHintTextColor;

    public FixedCursorEditText(Context context) {
        this(context, null);
    }

    public FixedCursorEditText(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.editTextStyle);
    }

    public FixedCursorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mHintPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mHintPaint.setTextSize(getTextSize());
        mHintPaint.setTextAlign(Paint.Align.RIGHT);
    }

    /**
     * 在绘制前获取原本设置的提示文本
     */
    @Override
    public boolean onPreDraw() {
        if (null == mHint) { // 开始为null
            mHint = getHint(); // 保存提示文本
            setHint(""); // 首先绘制一次没有提示文本的情况
        }
        return super.onPreDraw();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (TextUtils.isEmpty(mHint) || !TextUtils.isEmpty(getText())) {
            return;
        }
        canvas.save();
        ColorStateList hintTextColors = getHintTextColors();
        if (hintTextColors != null) {
            int color = hintTextColors.getColorForState(getDrawableState(), 0);
            if (color != mCurHintTextColor) {
                mCurHintTextColor = color;
                mHintPaint.setColor(color);
            }
        }

        Paint.FontMetricsInt fontMetrics = mHintPaint.getFontMetricsInt();
        int baseline = (getHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mHint, 0, mHint.length(),
                getWidth() - getPaddingRight() + getScrollX(),
                baseline, mHintPaint);
        canvas.restore();
    }


//    private float dp2px(float def) {
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, def, getContext().getResources().getDisplayMetrics());
//    }

}

package cn.lvsong.lib.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import cn.lvsong.lib.library.R;


/**
 * https://www.jianshu.com/p/4d611e73d9bb?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=weixin
 * Desc: Android 自定义View绘制关闭按钮
 * Author: Jooyer
 * Date: 2020-02-28
 * Time: 13:33
 */

/* 用法:


 */

public class CloseView extends View {
    /**
     * 普通模式
     */
    private static final int MODE_NORMAL = 1;
    /**
     * 有圆模式
     */
    private static final int MODE_CIRCLE = 2;

    /**
     * View默认最小宽度
     */
    private static final int DEFAULT_MIN_WIDTH = 100;

    /**
     * 控件宽
     */
    private int mViewWidth;
    /**
     * 控件高
     */
    private int mViewHeight;
    /**
     * 线的长度
     */
    private float mLineLength;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 线颜色
     */
    private int mColor;
    /**
     * 线宽
     */
    private float mLineWidth;
    /**
     * 内padding
     */
    private float mPadding;
    /**
     * 背景内padding
     */
    private float mBgPadding;
    /**
     * 模式
     */
    private int mMode;
    /**
     * 圆的颜色
     */
    private int mCircleColor;
    /**
     * 圆的边线宽
     */
    private float mCircleLineWidth;
    /**
     * 控件背景色
     */
    private int mBgColor = Color.TRANSPARENT;
    /**
     * 是否绘制背景色
     */
    private boolean mHasBg = false;

    public CloseView(Context context) {
        this(context, null);
    }

    public CloseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        initAttr(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mLineWidth);
    }

    private void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CloseView, defStyleAttr, 0);
        mColor = array.getColor(R.styleable.CloseView_cv_line_color, Color.DKGRAY);
        mPadding = array.getDimension(R.styleable.CloseView_cv_line_padding, dip2px(context, 4F));
        mLineWidth = array.getDimension(R.styleable.CloseView_cv_line_width, dip2px(context, 1.5F));
        mMode = array.getInt(R.styleable.CloseView_cv_mode, MODE_NORMAL);
        //如果不指定圆的颜色，颜色和线的颜色一致
        mCircleColor = array.getColor(R.styleable.CloseView_cv_circle_line_color, mColor);
        //如果不指定圆的线宽，则和线的线宽的一致
        mCircleLineWidth = array.getDimension(R.styleable.CloseView_cv_circle_line_width, 0);
        mHasBg = array.getBoolean(R.styleable.CloseView_cv_circle_has_bg, mHasBg);
        mBgColor = array.getColor(R.styleable.CloseView_cv_circle_bg_color, mBgColor);
        mBgPadding = array.getDimension(R.styleable.CloseView_cv_circle_bg_padding, 0);
        if (mBgPadding < mCircleLineWidth) {
            mBgPadding = mCircleLineWidth;
        }
        array.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        mLineLength = (Math.min(mViewWidth, mViewHeight) * 0.65f) / 2f - mPadding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //将画布中心移动到中心点
        canvas.translate(mViewWidth / 2F, mViewHeight / 2F);
        //线旋转45，将十字旋转
        canvas.rotate(45);
        // 绘制背景
        if (mHasBg && mMode == MODE_CIRCLE) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mBgColor);
            float radius = Math.min(mViewWidth - mBgPadding * 2, mViewHeight - mBgPadding * 2) / 2f;
            canvas.drawCircle(0, 0, radius, mPaint);
        }
        //画交叉线
        drawCrossLine(canvas);
        //画圆模式
        if (mMode == MODE_CIRCLE && mCircleLineWidth > 0) { // 如果指定为0 ,则表示不绘制圆环
            drawCircle(canvas);
        }
    }

    /**
     * 画交叉线
     */
    private void drawCrossLine(Canvas canvas) {
        int count = 4;
        int angle = (int) (360f / count);
        //画十字
        canvas.save();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mLineWidth);
        for (int i = 0; i < count; i++) {
            //旋转4次，每次画一条线，每次90度，合起来就是一个十字了
            canvas.rotate(angle * i);
            canvas.drawLine(0, 0, mLineLength, 0, mPaint);
        }
        canvas.restore();
    }

    /**
     * 画圆环
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mCircleColor);
        mPaint.setStrokeWidth(mCircleLineWidth);
        float radius = (Math.min(mViewWidth, mViewHeight) * 0.9f) / 2f;
        canvas.drawCircle(0, 0, radius, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(handleMeasure(widthMeasureSpec), handleMeasure(heightMeasureSpec));
    }

    /**
     * 处理MeasureSpec
     */
    private int handleMeasure(int measureSpec) {
        int result = DEFAULT_MIN_WIDTH;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            //处理wrap_content的情况
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    /**
     * 叉叉的颜色
     * @param color --> 默认#444444
     */
    public void setColor(@ColorInt int color) {
        this.mColor = color;
        mPaint.setColor(mColor);
        postInvalidate();
    }

    /**
     * 设置叉叉线的宽度(厚度)
     * @param lineWidth --> 默认1.5dp
     */
    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
    }

    /**
     *  使得里面 × 变小,这样不影响点击范围
     * @param padding --> 默认4dp
     */
    public void setLinePadding(float padding) {
        mPadding = padding;
    }

    /**
     * 设置控件是圆形(此时背景色和圆环才有效果)还是方形
     * @param mode --> 默认方形(1)
     */
    public void setMode(int mode) {
        mMode = mode;
    }

    /**
     * 设置控件是否拥有背景色
     * @param hasBg --> 默认false
     */
    public void setHasBg(boolean hasBg) {
        this.mHasBg = hasBg;
    }

    /**
     * 设置控件背景色
     * @param bgColor --> 默认透明
     */
    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

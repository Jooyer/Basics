package cn.lvsong.lib.ui.define;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import cn.lvsong.lib.ui.R;

/** https://www.jianshu.com/p/4d611e73d9bb?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=weixin
 * Desc: Android 自定义View绘制关闭按钮
 * Author: Jooyer
 * Date: 2020-02-28
 * Time: 13:33
 */

/* 用法:

<com.zh.cavas.sample.widget.CloseView
    android:id="@+id/close_view"
    android:layout_width="40dp"
    android:layout_height="40dp"
    android:layout_marginTop="20dp"
    app:cv_circle_color="@android:color/darker_gray"
    app:cv_circle_line_width="1dp"
    app:cv_color="@android:color/black"
    app:cv_line_width="1.5dp"
    app:cv_mode="normal" />

<com.zh.cavas.sample.widget.CloseView
    android:id="@+id/close_view2"
    android:layout_width="40dp"
    android:layout_height="40dp"
    android:layout_marginTop="20dp"
    app:cv_circle_color="@android:color/darker_gray"
    app:cv_circle_line_width="1dp"
    app:cv_color="@android:color/darker_gray"
    app:cv_line_width="1.5dp"
    app:cv_mode="circle" />

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
        mColor = array.getColor(R.styleable.CloseView_cv_color, Color.argb(255, 0, 0, 0));
        mPadding = array.getDimension(R.styleable.CloseView_cv_padding,dip2px(context, 4f));
        mLineWidth = array.getDimension(R.styleable.CloseView_cv_line_width, dip2px(context, 1.5f));
        mMode = array.getInt(R.styleable.CloseView_cv_mode, MODE_NORMAL);
        //如果不指定圆的颜色，颜色和线的颜色一致
        mCircleColor = array.getColor(R.styleable.CloseView_cv_circle_color, mColor);
        //如果不指定圆的线宽，则和线的线宽的一致
        mCircleLineWidth = array.getDimension(R.styleable.CloseView_cv_circle_line_width, mLineWidth);
        array.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        mLineLength = (Math.min(mViewWidth, mViewHeight) * 0.65f) / 2f - mPadding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //将画布中心移动到中心点
        canvas.translate(mViewWidth / 2F, mViewHeight / 2F);
        //线旋转45，将十字旋转
        canvas.rotate(45);
        //画交叉线
        drawCrossLine(canvas);
        //画圆模式
        if (mMode == MODE_CIRCLE) {
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
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mLineWidth);
        for (int i = 0; i < count; i++) {
            //旋转4次，每次画一条线，每次45度，合起来就是一个十字了
            canvas.rotate(angle * i);
            canvas.drawLine(0, 0, mLineLength, 0, mPaint);
        }
        canvas.restore();
    }

    /**
     * 画圆
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setColor(mCircleColor);
        mPaint.setStrokeWidth(mCircleLineWidth);
        float radius = (Math.min(mViewWidth, mViewHeight) * 0.9f) / 2f;
        canvas.drawCircle(0,0, radius, mPaint);
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

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

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

/** 参考: https://www.jianshu.com/p/cdbb4dff4cac?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=weixin
 * Desc: Android 自定义View绘制搜索按钮
 * Author: Jooyer
 * Date: 2020-02-28
 * Time: 13:35
 */

public class AndroidSearchView extends View {

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
     * 画笔
     */
    private Paint mPaint;
    /**
     * 圆的半径
     */
    private float mCircleRadius;
    /**
     * 线的长度
     */
    private float mLineLength;
    /**
     * 颜色
     */
    private int mColor;
    /**
     * 线宽
     */
    private float mLineWidth;

    public AndroidSearchView(Context context) {
        this(context, null);
    }

    public AndroidSearchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AndroidSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        initAttr(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineWidth);
    }

    private void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AndroidSearchView, defStyleAttr, 0);
        mColor = array.getColor(R.styleable.AndroidSearchView_asv_color, Color.argb(255, 0, 0, 0));
        mLineWidth = array.getDimension(R.styleable.AndroidSearchView_asv_line_width, dip2px(context, 1.5f));
        array.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        //计算半径
        mCircleRadius = (Math.min(mViewWidth, mViewHeight) * 0.35f) / 2f;
        //计算把柄的长度
        mLineLength = (Math.min(mViewWidth, mViewHeight) * 0.18f) * 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mViewWidth / 2F, mViewHeight / 2F);
        canvas.rotate(45f);
        canvas.drawCircle(0, 0, mCircleRadius, mPaint);
        canvas.drawLine(mCircleRadius, 0, mLineLength, 0, mPaint);
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

    public void setColor(int color) {
        this.mColor = color;
        mPaint.setColor(mColor);
        postInvalidate();
    }
}

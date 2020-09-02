package cn.lvsong.lib.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import androidx.annotation.Nullable;

import cn.lvsong.lib.library.R;


/**
 * 来自: https://www.jianshu.com/p/471ac4eb1ab6
 * Desc: Android 自定义View 绘制六边形设置按钮
 * Author: Jooyer
 * Date: 2020-03-03
 * Time: 17:00
 */

/*
    <cn.lvsong.lib.library.view.PolygonSettingView
            android:id="@+id/psv_test"
            android:layout_width="@dimen/width_30"
            android:layout_height="@dimen/height_30"
            android:layout_marginTop="@dimen/padding_20"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@id/sv_star"
            app:psv_color="@color/main_theme_color"
            app:psv_line_width="@dimen/padding_2"
            app:psv_num="6"
            />

 */
public class PolygonSettingView extends View implements Checkable {
    /**
     * View默认最小宽度
     */
    private static final int DEFAULT_MIN_WIDTH = 100;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 控件宽
     */
    private int mViewWidth;
    /**
     * 控件高
     */
    private int mViewHeight;

    /**
     * 多边形的边数
     */
    private int mNum;
    /**
     * 最小的多边形的半径
     */
    private float mRadius;
    /**
     * 360度对应的弧度（为什么2π就是360度？弧度的定义：弧长 / 半径，一个圆的周长是2πr，如果是一个360度的圆，它的弧长就是2πr，如果这个圆的半径r长度为1，那么它的弧度就是，2πr / r = 2π）
     */
    private final double mPiDouble = 2 * Math.PI;
    /**
     * 多边形中心角的角度（每个多边形的内角和为360度，一个多边形2个相邻角顶点和中心的连线所组成的角为中心角
     * 中心角的角度都是一样的，所以360度除以多边形的边数，就是一个中心角的角度），这里注意，因为后续要用到Math类的三角函数
     * Math类的sin和cos需要传入的角度值是弧度制，所以这里的中心角的角度，也是弧度制的弧度
     */
    private float mCenterAngle;
    /**
     * 颜色
     */
    private int mColor;
    /**
     * 中心小圆的半径
     */
    private float mSmallCircleRadius;
    /**
     * 线宽
     */
    private float mLineWidth;
    /**
     * 是否被选中
     */
    private boolean isChecked;

    public PolygonSettingView(Context context) {
        this(context, null);
    }

    public PolygonSettingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolygonSettingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        initAttr(context, attrs, defStyleAttr);
        //取消硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        //画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mLineWidth);
    }

    private void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        //默认边数和最小边数
        int defaultNum = 6;
        int minNum = 3;
        int defaultColor = Color.argb(255, 0, 0, 0);
        int defaultLineWidth = dip2px(context, 1.5f);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PolygonSettingView, defStyleAttr, 0);
            mColor = array.getColor(R.styleable.PolygonSettingView_psv_color, defaultColor);
            int num = array.getInt(R.styleable.PolygonSettingView_psv_num, defaultNum);
            mNum = Math.max(num, minNum);
            mLineWidth = array.getDimension(R.styleable.PolygonSettingView_psv_line_width, defaultLineWidth);
            array.recycle();
        } else {
            mColor = defaultColor;
            mNum = defaultNum;
        }
        //计算中心角弧度
        mCenterAngle = (float) (mPiDouble / mNum);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        //计算最小的多边形的半径
        mRadius = (Math.min(mViewWidth, mViewHeight) / 2f) * 0.95f;
        //计算中心小圆的半径
        mSmallCircleRadius = (Math.min(mViewWidth, mViewHeight) / 2f) * 0.3f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //将画布中心移动到中心点
        canvas.translate(mViewWidth / 2F, mViewHeight / 2F);
        //画小圆
        drawSmallCircle(canvas);
        //画多边形
        drawPolygon(canvas);
    }

    /**
     * 画小圆
     */
    private void drawSmallCircle(Canvas canvas) {
        canvas.drawCircle(0, 0, mSmallCircleRadius, mPaint);
    }

    /**
     * 画多边形
     */
    private void drawPolygon(Canvas canvas) {
        //多边形边角顶点的x坐标
        float pointX;
        //多边形边角顶点的y坐标
        float pointY;
        //总的圆的半径，就是全部多边形的半径之和
        Path path = new Path();
        //画前先重置路径
        path.reset();
        for (int i = 1; i <= mNum; i++) {
            //cos三角函数，中心角的邻边 / 斜边，斜边的值刚好就是半径，cos值乘以斜边，就能求出邻边，而这个邻边的长度，就是点的x坐标
            pointX = (float) (Math.cos(i * mCenterAngle) * mRadius);
            //sin三角函数，中心角的对边 / 斜边，斜边的值刚好就是半径，sin值乘以斜边，就能求出对边，而这个对边的长度，就是点的y坐标
            pointY = (float) (Math.sin(i * mCenterAngle) * mRadius);
            //如果是一个点，则移动到这个点，作为起点
            if (i == 1) {
                path.moveTo(pointX, pointY);
            } else {
                //其他的点，就可以连线了
                path.lineTo(pointX, pointY);
            }
        }
        path.close();
        canvas.drawPath(path, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(handleMeasure(widthMeasureSpec), handleMeasure(heightMeasureSpec));
    }

    /**
     * 实现自定义点击的方式一: 重写 onTouchEvent()
     * 实现自定义点击的方式二: 实现checkable
     * @param event
     * @return
     */
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int x = (int) event.getX();
//        int y = (int) event.getY();
//        if (MotionEvent.ACTION_UP == event.getAction()) {
//            //点击在View区域内
//            if (x + getLeft() < getRight() && y + getTop() < getBottom()) {
//
//            }
//        }
//        return true;
//    }

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

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
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

package cn.lvsong.lib.ui.define;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.widget.AppCompatImageView;

import cn.lvsong.lib.ui.R;

/** RoundedBitmapDrawable 是 support-v4 下一个类
 * Desc: 圆角矩形
 * Author: Jooyer
 * Date: 2018-08-15
 * Time: 10:02
 */

/**
        ImageView mm1 = (ImageView) findViewById(R.id.mm1);
        ImageView mm2 = (ImageView) findViewById(R.id.mm2);

        RoundedBitmapDrawable roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.ns2));
        RoundedBitmapDrawable roundedBitmapDrawable2 = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.ns2));
        // 圆形
        roundedBitmapDrawable1.setCircular(true);
        mm1.setImageDrawable(roundedBitmapDrawable1);
        // 圆角矩形
        roundedBitmapDrawable2.setCornerRadius(10);
        mm2.setImageDrawable(roundedBitmapDrawable2);

 */

/**
  <com.shijigui.app.library.define.RoundImageView
                android:id="@+id/riv_cover_item"
                android:layout_width="@dimen/width_70"
                android:layout_height="@dimen/height_70"
                android:layout_marginStart="@dimen/padding_14"
                android:layout_marginTop="@dimen/padding_20"
                android:src="@drawable/shape_rect_blue_gradient"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:RoundImageView_borderColor_Ri="@android:color/transparent"
                app:RoundImageView_borderWidth_Ri="4dp"
                app:RoundImageView_maskType="ROUNDRECTANGLE"
        />
 */
public class RoundImageView extends AppCompatImageView {

    private MaskType mMaskType;
    private Path mPath;
    private float mRadius;
    private float mBorderWidth;
    private Paint mBorderPaint;
    private int mBorderColor;
    private static final int DEFAULT_BORDER_COLOR = Color.WHITE;

    private static final MaskType[] sMaskTypeArray = {
            MaskType.RECTANGLE,
            MaskType.CIRCLE,
            MaskType.ROUNDRECTANGLE,
            MaskType.ROUNDRECTANGLETOP
    };

    public RoundImageView(Context context) {
        super(context);
        initRoundImageView();
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initRoundImageView();
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RoundImageView, defStyle, 0);
        int index = a.getInt(R.styleable.RoundImageView_RoundImageView_maskType, -1);
        if (index >= 0) {
            setMaskType(sMaskTypeArray[index]);
        }
        mRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_RoundImageView_roundRadius,10);
        mBorderColor = a.getColor(R.styleable.RoundImageView_RoundImageView_borderColor_Ri, Color.BLACK);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.RoundImageView_RoundImageView_borderWidth_Ri, 0);

        a.recycle();
    }

    private void initRoundImageView() {
        mMaskType = MaskType.CIRCLE;
        mRadius = 10;
        mPath = new Path();
        mBorderPaint = new Paint();
        mBorderColor = DEFAULT_BORDER_COLOR;
        mBorderPaint.setColor(mBorderColor);
    }

    /**
     *
     * @MaskType.ROUNDRECTANGLE
     * @MaskType.ROUNDRECTANGLETOP
     * @param radius
     */
    public void setRadius(int radius) {
        if (mRadius == radius) {
            return;
        }

        mRadius = radius;
        invalidate();
    }

    public void setBorderColor(@ColorInt int color) {
        if (mBorderColor == color) {
            return;
        }

        mBorderColor = color;
        mBorderPaint.setColor(color);
        invalidate();
    }


    public void initAll(float borderWidth,MaskType maskType,int radius,@ColorInt int borderColor){
        mBorderWidth = borderWidth;
        mMaskType = maskType;
        mRadius = radius;
        mBorderColor = borderColor;
    }

    public void setBorderColorResource(@ColorRes int colorResource) {
        setBorderColor(getContext().getResources().getColor(colorResource));
    }

    public void setBorderWidth(float borderWidth) {
        if (mBorderWidth == borderWidth) {
            return;
        }
        mBorderWidth = borderWidth;
        invalidate();
    }

    public void setMaskType(MaskType maskType) {
        if (maskType == null) {
            throw new NullPointerException();
        }

        if (mMaskType != maskType) {
            mMaskType = maskType;

            requestLayout();
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        drawPath();
        canvas.clipPath(mPath);
        super.onDraw(canvas);
        canvas.restore();
        drawCanvas(canvas);

    }

    private void drawPath() {
        int width = getWidth();
        int height = getHeight();
        switch (mMaskType) {
            case RECTANGLE:
                mPath.reset();
                mPath.addRect(new RectF(mBorderWidth / 2, mBorderWidth / 2, width - mBorderWidth / 2, height - mBorderWidth / 2), Path.Direction.CW);
                mPath.close();
                break;
            case CIRCLE:
                float r = Math.min(width, height) / 2;
                mPath.reset();
                mPath.addCircle(width / 2, height / 2, r, Path.Direction.CW);
                mPath.close();
                break;
            case ROUNDRECTANGLE:
                mPath.reset();
                mPath.addRoundRect(new RectF(mBorderWidth / 4, mBorderWidth / 4, width - mBorderWidth / 4, height - mBorderWidth / 4), mRadius, mRadius, Path.Direction.CW);
                mPath.close();
                break;
            case ROUNDRECTANGLETOP:
                mPath.reset();
                mPath.addCircle(mRadius, mRadius, mRadius, Path.Direction.CW);
                mPath.addCircle(width - mRadius, mRadius, mRadius, Path.Direction.CW);
                mPath.addRect(mRadius, 0, width - mRadius, 2 * mRadius, Path.Direction.CW);
                mPath.addRect(0, mRadius, width, height, Path.Direction.CW);
                mPath.close();
                break;
        }
    }

    private void drawCanvas(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        if (mBorderWidth <= 0) {
            return;
        }
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        switch (mMaskType) {
            case RECTANGLE:
                canvas.drawRect(new RectF(0, 0, width, height), mBorderPaint);
                break;
            case CIRCLE:
                float r = Math.min(width, height) / 2;
                canvas.drawCircle(width / 2, height / 2, r - mBorderWidth / 2, mBorderPaint);
                break;
            case ROUNDRECTANGLE:
                canvas.drawRoundRect(new RectF(0, 0, width, height), mRadius, mRadius,mBorderPaint);
                break;
        }

    }


    /**
     * options for mask the imageview
     */
    public enum MaskType {

        /**
         * a parallelogram with four right angles
         */
        RECTANGLE(0),
        /**
         *
         */
        CIRCLE(1),
        /**
         *a parallelogram with four circle angles
         */
        ROUNDRECTANGLE(2),

        /**
         * a parallelogram with two top circle angles
         */
        ROUNDRECTANGLETOP(3);

        final int mNativeInt;

        MaskType(int ni) {
            mNativeInt = ni;
        }
    }
}

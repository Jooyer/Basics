package cn.lvsong.lib.demo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import cn.lvsong.lib.demo.R;

/**
 * Desc: 学习自定义控件阴影
 * Author: Jooyer
 * Date: 2020-09-07
 * Time: 12:28
 */

/*

 <cn.lvsong.lib.demo.views.ShadowViewCard
        android:layout_marginTop="@dimen/padding_20"
        android:layout_gravity="center_horizontal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="@dimen/height_100"
            android:background="@color/color_333333"
            android:layout_gravity="center"
            />

    </cn.lvsong.lib.demo.views.ShadowViewCard>

 */

public class ShadowViewCard extends FrameLayout {
    private static final int DEFAULT_VALUE_SHADOW_COLOR = R.color.shadow_default_color;
    private static final int DEFAULT_VALUE_SHADOW_RADIUS = 10;
    private static final int DEFAULT_VALUE_SHADOW_BOTTOM_HEIGHT = 10;
    private static final int DEFAULT_VALUE_SHADOW_OFFSET_Y = 5;
    private int shadowColor;
    private int shadowRadius;
    private int shadowOffsetY;
    private int shadowBottomHeight;

    public ShadowViewCard(Context context) {
        this(context, null);
    }

    public ShadowViewCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowViewCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShadowViewCard);
        shadowColor = a.getColor(R.styleable.ShadowViewCard_shadowColor, getResources().getColor(DEFAULT_VALUE_SHADOW_COLOR));
        shadowBottomHeight = a.getDimensionPixelSize(R.styleable.ShadowViewCard_shadowBottomHeight, dp2px(getContext(), DEFAULT_VALUE_SHADOW_BOTTOM_HEIGHT));
        shadowOffsetY = a.getDimensionPixelSize(R.styleable.ShadowViewCard_shadowOffsetY, dp2px(getContext(), DEFAULT_VALUE_SHADOW_OFFSET_Y));
        shadowRadius = a.getInteger(R.styleable.ShadowViewCard_shadowRadius, DEFAULT_VALUE_SHADOW_RADIUS);
        a.recycle();
        setPadding(0, 0, 0, shadowBottomHeight);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint shadowPaint = new Paint();
        shadowPaint.setColor(Color.WHITE);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setAntiAlias(true);
        shadowPaint.setShadowLayer(shadowRadius, 0F, shadowOffsetY, shadowColor);
        RectF rectF = new RectF(-shadowBottomHeight, 0, getWidth() + shadowBottomHeight, getHeight() - shadowBottomHeight);
        canvas.drawRect(rectF, shadowPaint);
//        canvas.save();
        super.dispatchDraw(canvas);
    }
}
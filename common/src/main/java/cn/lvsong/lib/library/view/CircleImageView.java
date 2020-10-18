package cn.lvsong.lib.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import cn.lvsong.lib.library.R;


/** https://blog.csdn.net/u013293125/article/details/105200571
 * Created by Jooyer on 2017/8/24
 * 自定义圆形ImageView
 */

public class CircleImageView extends AppCompatImageView {

    // 图片缩放类型
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    // 图片加载到内存时模式
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    //
    private static final int COLOR_DRAWABLE_DIMENSION = 2;
    // 默认边界(最外面一圈边框)宽度
    private static final int DEFAULT_BOARD_WIDTH = 0;
    private int mBoardWidth = DEFAULT_BOARD_WIDTH;
    // 默认边界颜色
    private static final int DEFAULT_BOARD_COLOR = Color.BLACK;
    private int mBoardColor = DEFAULT_BOARD_COLOR;
    //
    private static final boolean DEFAULT_BOARD_OVERLAY = false;
    // 原始 drawable 大小
    private final RectF mDrawableRect = new RectF();
    // 边框所在矩形大小
    private final RectF mBoardRect = new RectF();

    private Matrix mShaderMatrix = new Matrix();
    private Paint mBitmapPaint = new Paint();
    private Paint mBoardPaint = new Paint();

    private ColorFilter mColorFilter;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader; // 位图渲染
    private int mBitmapWidth; //位图宽度
    private int mBitmapHeight; // 位图高度

    private int mDrawableRadius;// 原图半径
    private int mBoardRadius;// 带边框的图片半径

    private boolean mReady; // 初始化 TypedArray 和设置图片缩放模式完成
    private boolean mSetupPending; //
    private boolean mBoardOverlay; // 是否覆盖


    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0);
        mBoardWidth = array.getDimensionPixelSize(R.styleable.CircleImageView_circle_imageview_board_width, mBoardWidth);
        mBoardColor = array.getColor(R.styleable.CircleImageView_circle_imageview_board_color, mBoardColor);
        mBoardOverlay = array.getBoolean(R.styleable.CircleImageView_circle_imageview_board_overlay, false);
        array.recycle();
        initialize();
    }

    private void initialize() {
        // 在这里强制设置ScaleType 为 CENTER_CROP,将图片水平垂直居中,便于缩放
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if (mSetupPending) {
            setUp();
            mSetupPending = false; // 因为在ImageView.setImageXxx() 会优先于构造方法执行,保证正确执行一次setup
        }
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (SCALE_TYPE != scaleType) {
            throw new IllegalArgumentException(String.format("ScaleType %1$s not supported", scaleType));
        }
    }

    // mageView的android:adjustViewBounds属性为是否保持原图的长宽比，
    // 单独设置不起作用，需要配合maxWidth或maxHeight一起使用。
    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 没有设置图片则不绘制
        if (null == getDrawable()) {
            return;
        }

        // 绘制圆心内部的区域
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);

        // 如果圆形的边框宽度不为零,则绘制
        if (0 != mBoardWidth) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBoardRadius, mBoardPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setUp();
    }

    public int getBoardColor() {
        return mBoardColor;
    }

    public void setBoardColor(int boardColor) {
        if (mBoardColor == boardColor) {
            return;
        }
        mBoardColor = boardColor;
        mBoardPaint.setColor(boardColor);
        invalidate();
    }

    public void setBoardColorResource(@ColorRes int colorResource) {
        setBoardColor(getContext().getResources().getColor(colorResource));
    }

    public int getBoardWidth() {
        return mBoardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        if (mBoardWidth == boardWidth) {
            return;
        }
        mBoardWidth = boardWidth;
        setUp();
    }

    public boolean isBoardOverlay() {
        return mBoardOverlay;
    }

    public void setBoardOverlay(boolean boardOverlay) {
        if (mBoardOverlay == boardOverlay) {
            return;
        }
        mBoardOverlay = boardOverlay;
        setUp();
    }

    /* 复写 setXxx() ,注意它将先于构造方法执行 */
    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setUp();
    }


    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setUp();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setUp();
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setUp();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        if (mColorFilter == colorFilter) {
            return;
        }
        mColorFilter = colorFilter;
        mBitmapPaint.setColorFilter(colorFilter);
        invalidate();
    }

    private void setUp() {
        //因为mReady默认值为false,所以第一次进这个函数的时候if语句为真进入括号体内
        //设置mSetupPending为true然后直接返回，后面的代码并没有执行。
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (null == mBitmap) {
            return;
        }

        // 构造渲染器,后面第二,第三参数为如果图片小于控件,则会被拉伸
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBoardPaint.setStyle(Paint.Style.STROKE);
        mBoardPaint.setAntiAlias(true);
        mBoardPaint.setColor(mBoardColor);
        mBoardPaint.setStrokeWidth(mBoardWidth);

        // 获取原图的宽高
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();

        // 设置包含边界的显示区域,也就是得到的ImageView控件大小
        mBoardRect.set(0, 0, getWidth(), getHeight());
        // 计算边界圆环的半径
        mBoardRadius = (int) Math.min((mBoardRect.width() - mBoardWidth) / 2, (mBoardRect.height() - mBoardWidth) / 2);
        // 初始化图片显示区域
        mDrawableRect.set(mBoardRect);
        if (!mBoardOverlay) {
            //通过 insert() 可以使得图片显示区域按 mBoardRect 大小 从四个方向往内缩 mBoardWidth ,形成一个新的图片显示区域
            mDrawableRect.inset(mBoardWidth, mBoardWidth);
        }

        // 计算内部图片的外边的半径
        mDrawableRadius = (int) Math.min(mDrawableRect.width() / 2, mDrawableRect.height() / 2);

        // 设置渲染器的变换矩阵,就是什么缩放
        updateShaderMatrix();
        // 手动触发 onDraw(),完成绘制
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale = 0f;
        float dx = 0f, dy = 0f;

        mShaderMatrix.set(null);

        // 获取最小的缩放比例
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            // X轴平移 , Y 轴缩放
            scale = mDrawableRect.height() * 1f / mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f; // 平移时类似于左右各平移一部分,所以 *0.5
        } else {
            //x轴缩放 y轴平移 使得图片的x轴方向的边的尺寸缩放到图片显示区域（mDrawableRect）一样）
            scale = mDrawableRect.width() * 1f / mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        // 设置缩放
        mShaderMatrix.setScale(scale, scale);
        // 设置平移
        mShaderMatrix.postTranslate(dx + 0.5f + mDrawableRect.left, dy + 0.5f + mDrawableRect.top);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mShaderMatrix);

    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (null == drawable) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap(); // 一般情况下,则代码只会执行到这里,就结束了
        }

        try {

            Bitmap bitmap = null;
            if (drawable instanceof ColorDrawable) { // TODO 没有看明白
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError error) {
            return null;
        }
    }
}

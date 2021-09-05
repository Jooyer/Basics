package cn.lvsong.lib.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import cn.lvsong.lib.library.R;


/**
 * 参考: https://www.jianshu.com/p/fa85186f12fb?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=weixin
 * Desc: Android 自定义View绘制箭头
 * Author: Jooyer
 * Date: 2020-02-28
 * Time: 13:26
 */

/* 用法:

<cn.lvsong.lib.library.view.BackArrowView
    android:id="@+id/back_arrow"
    android:layout_width="40dp"
    android:layout_height="40dp"
    android:layout_marginTop="20dp"
    app:bav_arrow_style="wechat_design"
    app:bav_arrow_color="@android:color/black"
    app:bav_stroke_width="1.5dp" />

<cn.lvsong.lib.library.view.BackArrowView
    android:id="@+id/back_arrow2"
    android:layout_width="40dp"
    android:layout_height="40dp"
    android:layout_marginTop="20dp"
    app:bav_arrow_style="material_design"
    app:bav_arrow_color="@android:color/black"
    app:bav_stroke_width="1.5dp" />

 */

public class BackArrowView extends View {
	/**
	 * View默认最小宽度
	 */
	private static final int DEFAULT_MIN_WIDTH = 100;
	/**
	 * Material Design风格
	 */
	private static final int ARROW_STYLE_MATERIAL_DESIGN = 1;
	/**
	 * 微信风格
	 */
	private static final int ARROW_STYLE_WECHAT_DESIGN = 2;

	/**
	 * 控件宽
	 */
	private int mViewWidth;
	/**
	 * 控件高
	 */
	private int mViewHeight;
	/**
	 * 箭头开始的距离
	 */
	private float mArrowStartDistance;
	/**
	 * 内padding
	 */
	private float mPadding;

	/**
	 * 箭头的2个边的长度
	 */
	private float mArrowLineLength;
	/**
	 * 箭头颜色
	 */
	private int mArrowColor;
	/**
	 * 箭头粗细
	 */
	private float mArrowStrokeWidth;
	/**
	 * 风格模式
	 */
	private int mArrowStyle;
	/**
	 * 存在背景图片,如果存在则不绘制叉叉
	 */
	private boolean mHasBackground;
	/**
	 * 画笔
	 */
	private Paint mPaint;
	/**
	 * 箭头Path
	 */
	private Path mTopArrowPath;
	private Path mMiddleArrowPath;
	private Path mBottomArrowPath;

	public BackArrowView(Context context) {
		this(context, null);
	}

	public BackArrowView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BackArrowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		initAttr(context, attrs, defStyleAttr);
		mPaint = new Paint();
		mPaint.setColor(mArrowColor);
		//使用Path必须使用STROKE，使用FILL是画不了的
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(mArrowStrokeWidth);
		//设置拐角形状为圆形，3条线相接处则不会有缝隙了
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mTopArrowPath = new Path();
		mMiddleArrowPath = new Path();
		mBottomArrowPath = new Path();
	}

	private void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BackArrowView, defStyleAttr, 0);
		mPadding = array.getDimension(R.styleable.BackArrowView_bav_arrow_padding, dip2px(context, 1f));
		mArrowColor = array.getColor(R.styleable.BackArrowView_bav_arrow_color, ContextCompat.getColor(context, R.color.color_666666));
		mArrowStrokeWidth = array.getDimension(R.styleable.BackArrowView_bav_stroke_width, dip2px(context, 2f));
		mArrowStyle = array.getInt(R.styleable.BackArrowView_bav_arrow_style, ARROW_STYLE_MATERIAL_DESIGN);
		array.recycle();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mViewWidth = w;
		mViewHeight = h;
		//计算半径
		float radius = Math.min(mViewWidth, mViewHeight) / 2f;
		//计算箭头起始位置
		if (mArrowStyle == ARROW_STYLE_MATERIAL_DESIGN) {
			mArrowStartDistance = radius / 3f;
		} else if (mArrowStyle == ARROW_STYLE_WECHAT_DESIGN) {
			mArrowStartDistance = radius / 4f;
		}
		//计算箭头长度
		mArrowLineLength = radius * 0.63f - mPadding;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mHasBackground){
			return;
		}
		//将画布中心移动到中心点偏左位置
		if (mArrowStyle == ARROW_STYLE_MATERIAL_DESIGN) {
			canvas.translate(mViewWidth / 2F - (mArrowLineLength + mArrowStartDistance) / 2F, mViewHeight / 2F);
		} else {
			canvas.translate(mViewWidth / 2F - mArrowLineLength / 2, mViewHeight / 2F);
		}
		//将画布旋转45度，让后面画的直角旋转
		canvas.rotate(45);
		//画第一条线
		mTopArrowPath.moveTo(0, 0);
		mTopArrowPath.lineTo(0, -mArrowLineLength);
//        mArrowPath.addArc(0,-mPadding,mPadding,0,45F,135F);
		//画第二条线
		mBottomArrowPath.moveTo(mArrowLineLength, 0);
		mBottomArrowPath.lineTo(0, 0);
		//Google Material Design风格才有中间的线
		if (mArrowStyle == ARROW_STYLE_MATERIAL_DESIGN) {
			//画中间的线
			mMiddleArrowPath.moveTo(0, 0);
			mMiddleArrowPath.lineTo(mArrowLineLength, -mArrowLineLength);
		}
		//闭合路径
		mTopArrowPath.close();
		mMiddleArrowPath.close();
		mBottomArrowPath.close();
		//画路径
		canvas.drawPath(mTopArrowPath, mPaint);
		canvas.drawPath(mMiddleArrowPath, mPaint);
		canvas.drawPath(mBottomArrowPath, mPaint);
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

	private static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 设置箭头颜色
	 */
	public void setArrowColor(int arrowColor) {
		if (arrowColor != mArrowColor) {
			this.mArrowColor = arrowColor;
			mPaint.setColor(mArrowColor);
			invalidate();
		}
	}

	/**
	 * 设置箭头样式,默认 Google Material
	 */
	public void setArrowStyle(int arrowStyle) {
		if (arrowStyle != mArrowStyle) {
			this.mArrowStyle = arrowStyle;
			invalidate();
		}
	}

	/**
	 * 设置padding,是的箭头变小,默认1dp
	 */
	public void setArrowPadding(float padding) {
		if (padding != mPadding) {
			this.mPadding = padding;
			invalidate();
		}
	}

	/**
	 * 设置线条宽度,默认2dp
	 */
	public void setArrowStrokeWidth(float arrowStrokeWidth) {
		if (arrowStrokeWidth != mArrowStrokeWidth) {
			mArrowStrokeWidth = arrowStrokeWidth;
			mPaint.setStrokeWidth(mArrowStrokeWidth);
			invalidate();
		}
	}

	/**
	 * 设置是否存在背景图片
	 */
	public void setExistBackground(boolean hsBackground) {
		mHasBackground = hsBackground;
		invalidate();
	}
}

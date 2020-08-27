package cn.lvsong.lib.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.lvsong.lib.library.R;
import cn.lvsong.lib.library.adapter.NineImageAdapter;

/**
 * @ProjectName: android
 * @Package: cn.lvsong.lib.library.view
 * @ClassName: NineImageLayout
 * @Description: 仿朋友圈图片展示
 * @Author: ChenYangQi
 * @CreateDate: 2020/6/3 17:22
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: 1.0
 */

/*

   <cn.lvsong.lib.library.view.NineImageLayout
            android:id="@+id/nl_images2"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/padding_50"
            app:nl_item_gap="@dimen/padding_10"
            app:nl_keep_place="false"
            app:nl_left_padding="@dimen/padding_10"
            app:nl_right_padding="@dimen/padding_10"
            app:nl_single_image_width_ratio="0.8" />

 */

public class NineImageLayout extends ViewGroup {
    /**
     * 当只有一张图时Item宽度
     */
    private int singleViewWidth = 0;
    /**
     * 当只有一张图时Item高度
     */
    private int singleViewHeight = 0;
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * 计算此控件宽度
     */
    private int calcWidth;
    /**
     * 2张图时,是否要和3张图大小保持一致,默认是false
     */
    private boolean needKeepPlace = false;
    /**
     * 图片之间间隔的大小
     */
    private int itemMargin = 5;
    /**
     * 控件 leftPadding
     */
    private int leftPadding = 5;
    /**
     * 控件 rightPadding
     */
    private int rightPadding = 5;
    /**
     * 单个图片的宽度和高度
     */
    private int itemWidth;
    /**
     * 一张图片允许的最大宽高范围
     */
    private float singleImageWidthRatio = 0.8F;

    public NineImageLayout(Context context) {
        this(context, null);
    }

    public NineImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NineImageLayout);
        singleImageWidthRatio = array.getFloat(R.styleable.NineImageLayout_nl_single_image_width_ratio, dip2px(getContext(), singleImageWidthRatio));
        itemMargin = array.getDimensionPixelSize(R.styleable.NineImageLayout_nl_item_gap, dip2px(getContext(), itemMargin));
        leftPadding = array.getDimensionPixelSize(R.styleable.NineImageLayout_nl_left_padding, dip2px(getContext(), leftPadding));
        rightPadding = array.getDimensionPixelSize(R.styleable.NineImageLayout_nl_right_padding, dip2px(getContext(), rightPadding));
        needKeepPlace = array.getBoolean(R.styleable.NineImageLayout_nl_keep_place, needKeepPlace);
        array.recycle();
        screenWidth = getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewHeight = 0;
        // 获取组件的宽度
        calcWidth = screenWidth - leftPadding - rightPadding;
        itemWidth = (calcWidth - 2 * itemMargin) / 3;
        int count = getChildCount();
        if (count == 1) {  //一张图片的宽高
            //TODO 单独处理
            setMeasuredDimension(singleViewWidth, singleViewHeight);
            return;
        } else if (2 == count || 4 == count) {
            if (needKeepPlace) {// 2张图(4张图) 和 3张图宽度一样
                viewHeight = itemWidth * (4 == count ? 2 : 1);
            } else {
                viewHeight = (calcWidth - itemMargin) / 2 * (4 == count ? 2 : 1);
            }
        } else if (count == 3) {
            viewHeight = itemWidth;
        } else if (count <= 6) {
            viewHeight = 2 * itemWidth + itemMargin;
        } else if (count <= 9) {
            viewHeight = screenWidth;
        }
        setMeasuredDimension(screenWidth, viewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int top = 0;
        int left = 0;
        int right = 0;
        int bottom = 0;
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (1 == count) { // 只有一张图
                left = leftPadding;
                right = left + singleViewWidth;
                bottom = top + singleViewHeight;
            } else if (2 == count || 4 == count) {
                if (needKeepPlace) {// 2张图(4张图) 和 3张图宽度一样
                    if (3 == i) {
                        top = itemWidth + itemMargin;
                    }
                    if (0 == i % 3) { // 左边第一张
                        left = leftPadding;
                    } else {
                        left += itemWidth + itemMargin;
                    }
                    right = left + itemWidth;
                    bottom = top + itemWidth;
                } else {
                    if (2 == i || 3 == i) {
                        top = (calcWidth - itemMargin) / 2 + itemMargin;
                    }
                    if (0 == i % 2) { // 左边第一张
                        left = leftPadding;
                    } else {
                        left += (calcWidth - itemMargin) / 2 + itemMargin;
                    }
                    right = left + (calcWidth - itemMargin) / 2;
                    bottom = top + (calcWidth - itemMargin) / 2;
                }
            } else  {
                if (0 == i % 3) { // 左边第一个
                    left = leftPadding;
                    right = left + itemWidth;
                } else if (1 == i % 3) { // 中间
                    left += itemWidth + itemMargin;
                    right = left + itemWidth;
                } else { // 最后一个
                    left += itemWidth + itemMargin;
                    right = screenWidth - rightPadding;
                }
                if (3 == i || 6 == i) {
                    top += itemWidth + itemMargin;
                }
                bottom = top + itemWidth;
            }
            childView.layout(left, top, right, bottom);
        }
    }

    /**
     * 单张图片的展示处理
     *
     * @param width --> 图片的宽度
     * @param height --> 图片的高度
     * @param view --> 展示图片的ImageView
     */
    public void setSingleImage(int width, int height, View view) {
        if (getChildCount() != 1) {
            removeAllViews();
            addView(view);
        }
        if (width >= height) {
            singleViewWidth = (int) (singleImageWidthRatio * view.getContext().getResources().getDisplayMetrics().widthPixels);
            singleViewHeight = (int) (singleImageWidthRatio * view.getContext().getResources().getDisplayMetrics().widthPixels * height / width);
        } else {
            singleViewHeight = (int) (singleImageWidthRatio * view.getContext().getResources().getDisplayMetrics().widthPixels);
            singleViewWidth = (int) (singleImageWidthRatio * view.getContext().getResources().getDisplayMetrics().widthPixels * width / height);
        }

        getChildAt(0).layout(0, 0, singleViewWidth, singleViewHeight);
        setMeasuredDimension(singleViewWidth, singleViewHeight);
    }

    /**
     * 设置数据源
     *
     * @param adapter
     */
    public void setAdapter(NineImageAdapter adapter) {
        removeAllViews();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            final View view = adapter.createView(LayoutInflater.from(getContext()), this, i);
            adapter.bindView(view, i);
            removeView(view);
            addView(view);
            bindClickEvent(i, view, adapter);
        }
    }

    /**
     * 绑定点击事件
     *
     * @param position
     * @param view
     */
    private void bindClickEvent(final int position, final View view, final NineImageAdapter adapter) {
        if (adapter == null) {
            return;
        }
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.OnItemClick(position, view);
            }
        });
    }

    /**
     * 获取MarginLayoutParams必须要重写这几个方法
     *
     * @return
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
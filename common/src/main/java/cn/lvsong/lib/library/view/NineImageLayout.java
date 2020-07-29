package cn.lvsong.lib.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import cn.lvsong.lib.library.adapter.NineImageAdapter;
import cn.lvsong.lib.library.R;

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
        itemMargin = array.getDimensionPixelSize(R.styleable.NineImageLayout_nl_image_gap, dip2px(getContext(), itemMargin));
        needKeepPlace = array.getBoolean(R.styleable.NineImageLayout_nl_keep_place,needKeepPlace);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewHeight = 0;
        int viewWidth = 0;

        // 获取父组件的宽度
        ConstraintLayout.LayoutParams layoutParams = ((ConstraintLayout.LayoutParams) this.getLayoutParams());
        calcWidth = getResources().getDisplayMetrics().widthPixels - layoutParams.leftMargin - layoutParams.rightMargin;
        itemWidth = (calcWidth - 2 * itemMargin) / 3;
        int count = getChildCount();
        if (count == 1) {  //一张图片的宽高
            //TODO 单独处理
            setMeasuredDimension(singleViewWidth, singleViewHeight);
            return;
        } else if (count <= 3) {
            viewHeight = itemWidth;
            if (count == 2) {
                if (needKeepPlace){ // 2张图和3张图宽度一样
                    viewWidth = 2 * itemWidth + itemMargin;
                }else { // 2张图和3张图宽度不一样时,2张占据整个父控件宽度
                    viewWidth = calcWidth;
                    itemWidth = (calcWidth -  itemMargin) / 2;
                }
            } else if (count == 3) {
                viewWidth = calcWidth;
            }
        } else if (count <= 6) {
            viewHeight = 2 * itemWidth + itemMargin;
            if (count == 4) {
                viewWidth = 2 * itemWidth + itemMargin;
            } else {
                viewWidth = calcWidth;
            }
        } else if (count <= 9) {
            viewHeight = calcWidth;
            viewWidth = calcWidth;
        }
        setMeasuredDimension(viewWidth, viewHeight);
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
            switch (i) {
                case 0:
                    left = 0;
                    top = 0;
                    if (count == 1) {
                        //TODO 单独处理
                        right = left + singleViewWidth;
                        bottom = top + singleViewHeight;
                    } else {
                        right = left + itemWidth;
                        bottom = top + itemWidth;
                    }
                    break;
                case 1:
                    left = itemWidth + itemMargin;
                    top = 0;
                    right = left + itemWidth;
                    bottom = top + itemWidth;
                    break;
                case 2:
                    if (count == 4) {
                        left = 0;
                        top = itemWidth + itemMargin;
                        right = left + itemWidth;
                    } else {
                        left = itemWidth * 2 + itemMargin * 2;
                        top = 0;
                        right = calcWidth;
                    }
                    bottom = top + itemWidth;
//                    Log.e("NineImgLayout", "onLayout============count: " + count + " =====left: " + left + " =====top: " + top + " =====right: " + right + " =====bottom: " + bottom);
                    break;
                case 3:
                    if (count == 4) {
                        left = itemWidth + itemMargin;
                    } else {
                        left = 0;
                    }
                    top = itemWidth + itemMargin;
                    right = left + itemWidth;
                    bottom = top + itemWidth;
                    break;
                case 4:
                    left = itemWidth + itemMargin;
                    top = itemWidth + itemMargin;
                    right = left + itemWidth;
                    bottom = top + itemWidth;
                    break;
                case 5:
                    left = (itemWidth + itemMargin) * 2;
                    top = itemWidth + itemMargin;
                    right = calcWidth;
                    bottom = top + itemWidth;
                    break;
                case 6:
                    left = 0;
                    top = (itemWidth + itemMargin) * 2;
                    right = left + itemWidth;
                    bottom = calcWidth;
                    break;
                case 7:
                    left = itemWidth + itemMargin;
                    top = (itemWidth + itemMargin) * 2;
                    right = left + itemWidth;
                    bottom = calcWidth;
                    break;
                case 8:
                    left = (itemWidth + itemMargin) * 2;
                    top = (itemWidth + itemMargin) * 2;
                    right = calcWidth;
                    bottom = calcWidth;
                    break;
                default:
                    break;
            }
            childView.layout(left, top, right, bottom);
        }
    }

    /**
     * 传入单张图片的宽高
     *
     * @param width
     * @param height
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
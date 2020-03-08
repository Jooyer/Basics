package cn.lvsong.lib.library.other;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * 参考: https://www.jianshu.com/p/85172c70bb98 || https://www.jianshu.com/p/a8b00a02a46a
 * Desc:RecyclerView添加分割线 ,适合网格 | 和线性
 * Author: Jooyer
 * Date: 2020-03-07
 * Time: 14:48
 */
public class NormalDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDrawable;
    //
    private int mTotalItems;//总Item数
    private int mSpanCount;//总列数
    private  boolean mGridLayout = false;

    public NormalDecoration(int dividerHeight, @ColorInt int dividerColor) {
        this.mDrawable = getItemDividerDrawable(dividerHeight,dividerColor);
    }

    private GradientDrawable getItemDividerDrawable(int dividerHeight, @ColorInt int drawableColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setSize(0,dividerHeight);
        drawable.setColor(drawableColor);
        return drawable;
    }

    /**
     * @param outRect 用于规定分割线的范围
     * @param view    进行分割线操作的子view
     * @param parent  父view
     * @param state   (这里暂时不使用)
     */
    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, RecyclerView parent, RecyclerView.State state) {
        //notification的时候要获取
        if (null == parent.getAdapter()){
            return;
        }
        mTotalItems = parent.getAdapter().getItemCount();
        if (0 == mSpanCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            //判断是否为GridLayoutManager
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                mSpanCount = gridLayoutManager.getSpanCount();
                mGridLayout = true;
            } else {
                mSpanCount = 1;
                mGridLayout = false;
            }
        }
        //在源码中有一个过时的方法，里面有获取当前ItemPosition
        int currentItemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        //
        if (!isLastRow(currentItemPosition, mTotalItems, mSpanCount)) {
            if (mGridLayout){
                outRect.bottom = mDrawable.getIntrinsicWidth();
            }else {
                outRect.bottom = mDrawable.getIntrinsicHeight();
            }
        } else {
            outRect.bottom = 0;
        }
        //将isLastColumn注释，添加下面代码
        //        if (!isLastColumn(currentItemPosition, mSpanCount)) {
        //            outRect.right = mDrawable.getIntrinsicWidth();
        //        } else {
        //            outRect.right = 0;
        //        }
        int averWidth = (mSpanCount - 1) * mDrawable.getIntrinsicWidth() / mSpanCount;
        int dX = mDrawable.getIntrinsicWidth() - averWidth;
        if (dX < 0) {
            dX = 0;
        }
        outRect.left = (currentItemPosition % mSpanCount) * dX;
        outRect.right = averWidth - outRect.left;
    }

    @Override
    public void onDraw(@NotNull Canvas c, RecyclerView parent, @NotNull RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            drawHorizontalDecoration(c, parent.getChildAt(i));
            drawVerticalDecoration(c, parent.getChildAt(i));
        }
    }

    private void drawHorizontalDecoration(Canvas canvas, View childView) {
        int currentItemPosition = ((RecyclerView.LayoutParams) childView.getLayoutParams()).getViewLayoutPosition();
        if (isLastRow(currentItemPosition, mTotalItems, mSpanCount)) {
            return;
        }
        //
        Rect rect = new Rect(0, 0, 0, 0);

        rect.top = childView.getBottom();
        if (mGridLayout){
            rect.bottom = rect.top + mDrawable.getIntrinsicWidth();
        }else {
            rect.bottom = rect.top + mDrawable.getIntrinsicHeight();
        }
        rect.left = childView.getLeft();
        rect.right = childView.getRight() + mDrawable.getIntrinsicWidth();
//        Log.e("Test", "drawHorizontalDecoration=======rect.top: " + rect.top + " ====rect.bottom: " + rect.bottom);
        mDrawable.setBounds(rect);
        mDrawable.draw(canvas);
    }

    private void drawVerticalDecoration(Canvas canvas, View childView) {
        Rect rect = new Rect(0, 0, 0, 0);

        rect.top = childView.getTop();
        rect.bottom = childView.getBottom();
        rect.left = childView.getRight();
        rect.right = rect.left + mDrawable.getIntrinsicWidth();
        mDrawable.setBounds(rect);
        mDrawable.draw(canvas);
    }

    private boolean isLastRow(int currentItemPosition, int totalItems, int spanCount) {
        boolean result = false;
        int rowCount = 0;

        if (0 == totalItems % spanCount) {
            rowCount = totalItems / spanCount;
        } else {
            rowCount = totalItems / spanCount + 1;
        }
        if ((currentItemPosition + 1) > (rowCount - 1) * spanCount)
            result = true;

        return result;
    }

    private boolean isLastColumn(int currentItemPosition, int spanCount) {
        boolean result = false;
        if (0 == (currentItemPosition + 1) % spanCount)
            result = true;
        return result;
    }
}
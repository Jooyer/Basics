package cn.lvsong.lib.library.decorator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

/** https://www.jianshu.com/p/fb7e1a0749d6
 *  https://www.jianshu.com/p/85172c70bb98 || https://www.jianshu.com/p/a8b00a02a46a
 * @ProjectName: android
 * @ClassName: LinearDividerItemDecoration
 * @Description: LinearLayoutManager分割线
 * @Author: Jooyer
 * @CreateDate: 2020/6/3 10:24
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: 1.0
 * 防止重复添加
 * if (RecyclerView.itemDecorationCount > 0) {
 * RecyclerView.removeItemDecorationAt(0)
 * }
 */
public class LinearDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Paint mPaint;
    private int mDividerWidth;
    private int mDividerPaddingLeft;
    private int mDividerPaddingRight;
    private boolean isSkipFirstPosition;
    private boolean isSkipSecondPosition;
    // 总Item数
    private int mTotalItems;
    /**
     * 左侧分割线留出的空白
     */
    private int mOccupyWidth = 0; // 如果为0则在计算间隔时,按照Item的宽度计算;如果不为0,则按照Item的宽度 - mOccupyWidth

    public LinearDividerItemDecoration(Context context, int dividerWidth, @ColorInt int color) {
        this(context, dividerWidth, color, false);
    }

    public LinearDividerItemDecoration(Context context, int dividerWidth, @ColorInt int color, boolean skipFirstPosition) {
        this(context, dividerWidth, color, skipFirstPosition,false);
    }

    public LinearDividerItemDecoration(Context context, int dividerWidth, @ColorInt int color, boolean skipFirstPosition, boolean isSkipSecondPosition) {
        this.mContext = context;
        this.mDividerWidth = dividerWidth;
        this.isSkipFirstPosition = skipFirstPosition;
        this.isSkipSecondPosition = isSkipSecondPosition;
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setColor(color);
        this.mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * @param outRect 用于规定分割线的范围
     * @param view    进行分割线操作的子view
     * @param parent  父view
     * @param state   (这里暂时不使用)
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (0 == mTotalItems)
            mTotalItems = parent.getAdapter().getItemCount();
        //在源码中有一个过时的方法，里面有获取当前ItemPosition
        int currentItemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (isSkipFirstPosition && 0 == currentItemPosition) {
            outRect.bottom = 0;
        } else if (!isLastRow(currentItemPosition, mTotalItems)) {
            outRect.bottom = this.mDividerWidth;
        } else {
            outRect.bottom = 0;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            drawHorizontalDecoration(c, parent.getChildAt(i));
        }
    }

    private void drawHorizontalDecoration(Canvas canvas, View childView) {
        int currentItemPosition = ((RecyclerView.LayoutParams) childView.getLayoutParams()).getViewLayoutPosition();
        if (isSkipFirstPosition && 0 == currentItemPosition) {
            return;
        }
        if (isSkipSecondPosition && 1 == currentItemPosition) {
            return;
        }
        if (isLastRow(currentItemPosition, mTotalItems)) {
            return;
        }
        Rect rect = new Rect(0, 0, 0, 0);
        rect.top = childView.getBottom();
        rect.bottom = rect.top + this.mDividerWidth;
        rect.left = childView.getLeft() + mOccupyWidth + mDividerPaddingLeft;
        rect.right = childView.getRight() - mDividerPaddingRight;
        canvas.drawRect(rect, mPaint);
    }

    private boolean isLastRow(int currentItemPosition, int totalItems) {
        boolean result = false;
        if (currentItemPosition + 1 >= totalItems)
            result = true;
        return result;
    }


    /**
     * 设置 RecyclerView 左侧分割线留出的空白
     *
     * @param occupyWidth
     */
    public LinearDividerItemDecoration setOccupyWidth(int occupyWidth) {
        mOccupyWidth = occupyWidth;
        return this;
    }

    /**
     * 设置分割线左侧间隔
     */
    public LinearDividerItemDecoration setDividerPaddingLeft(int dividerPaddingLeft) {
        this.mDividerPaddingLeft = dividerPaddingLeft;
        return this;
    }

    /**
     * 设置分割线右侧间隔
     */
    public LinearDividerItemDecoration setDividerPaddingRight(int dividerPaddingRight) {
        this.mDividerPaddingRight = dividerPaddingRight;
        return this;
    }
}

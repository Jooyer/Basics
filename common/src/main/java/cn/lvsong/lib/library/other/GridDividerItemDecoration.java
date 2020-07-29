package cn.lvsong.lib.library.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * https://www.jianshu.com/p/fb7e1a0749d6
 *
 * @ProjectName: android
 * @Package: com.weex.app.util
 * @ClassName: GridDividerItemDecoration
 * @Description: 网格分割线
 * @Author: Jooyer
 * @CreateDate: 2020/6/1 13:28
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: 1.0
 * PS: 添加了分割线,在刷新时如果发现分割线错乱(一般是内部嵌套的RV添加分割线才会出现此问题),则需要使用如下方法,或者找到对应的itemDecoration进行移除
 * if (RecyclerView.itemDecorationCount > 0) {
 * RecyclerView.removeItemDecorationAt(0)
 * }
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;

    private int spanCount = 0;

    private Paint mPaint;

    /**
     * 所需指定的间隔宽度，主要为第一列和最后一列与父控件的间隔；行间距，列间距将动态分配
     */
    private int mDividerWidth;
    /**
     * 可以理解RV没有在宽度/高度 占满整个屏幕,被其他控件占据了一部分
     */
    private int mOccupyWidth = 0; // 如果为0则在计算间隔时,按照屏幕的宽度/高度;如果不为0,则按照屏幕的宽度/高度 - mOccupyWidth

    /**
     * 第一行顶部是否需要间隔,如果需要就设置值
     */
    private int mFirstRowTopMargin = 0;

    /**
     * 第一列和最后一列挨着屏幕部分是否需要间隔
     * 即最左侧与最右侧是否需要间隔
     */
    private boolean isScreenBothSideNeedSpace = false;

    /**
     * 最后一行是否需要间隔(默认不需要)
     */
    private boolean isLastRowNeedSpace = false;
    /**
     * 是否跳过第一个Item,第一个可能需要占据整行,此时就用得上
     */
    private boolean isSkipFirstPosition = false;
    /**
     * 如果跳过第一个Item,则需要设置上下左右间隔,则可以设置
     */
    private Rect mFirstPositionRect = new Rect();

    /**
     * 是否在最上面绘制, {@link onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state)  }
     * PS: 在动态设置了 holder.itemView.setBackgroundColor ,会导致分割线被覆盖了,此时可以使用此变量,将绘制层级提高
     */
    private boolean needDrawOver = false;

    /**
     * @param dividerWidth              间隔宽度
     * @param isScreenBothSideNeedSpace 第一列和最后一列是否需要间隔
     */
    public GridDividerItemDecoration(Context context, int dividerWidth, boolean isScreenBothSideNeedSpace) {
        this(context, dividerWidth, 0, isScreenBothSideNeedSpace, false);
    }

    /**
     * @param dividerWidth              间隔宽度
     * @param isScreenBothSideNeedSpace 第一列和最后一列是否需要间隔
     * @param color  --> 分割线颜色
     */
    public GridDividerItemDecoration(Context context, int dividerWidth, boolean isScreenBothSideNeedSpace, @ColorInt int color) {
        this(context, dividerWidth, 0, isScreenBothSideNeedSpace, false, color);
    }

    /**
     * @param dividerWidth              间隔宽度
     * @param isScreenBothSideNeedSpace 第一列和最后一列是否需要间隔
     * @param firstRowTopMargin         第一行顶部是否需要间隔(根据间隔大小判断)
     */
    public GridDividerItemDecoration(Context context, int dividerWidth, int firstRowTopMargin, boolean isScreenBothSideNeedSpace) {
        this(context, dividerWidth, firstRowTopMargin, isScreenBothSideNeedSpace, false);
    }

    /**
     * @param dividerWidth              间隔宽度
     * @param firstRowTopMargin         第一行顶部是否需要间隔
     * @param isScreenBothSideNeedSpace 第一列和最后一列是否需要间隔
     * @param isLastRowNeedSpace        最后一行是否需要间隔
     */
    public GridDividerItemDecoration(Context context, int dividerWidth, int firstRowTopMargin, boolean isScreenBothSideNeedSpace, boolean isLastRowNeedSpace) {
        this(context, dividerWidth, firstRowTopMargin, isScreenBothSideNeedSpace, isLastRowNeedSpace, Color.WHITE);
    }

    /**
     * @param dividerWidth              间隔宽度
     * @param firstRowTopMargin         第一行顶部是否需要间隔
     * @param isScreenBothSideNeedSpace 第一列和最后一列是否需要间隔
     * @param isLastRowNeedSpace        最后一行是否需要间隔
     */
    public GridDividerItemDecoration(Context context, int dividerWidth, int firstRowTopMargin, boolean isScreenBothSideNeedSpace, boolean isLastRowNeedSpace, @ColorInt int color) {
        mDividerWidth = dividerWidth;
        this.isScreenBothSideNeedSpace = isScreenBothSideNeedSpace;
        this.mContext = context;
        this.isLastRowNeedSpace = isLastRowNeedSpace;
        this.mFirstRowTopMargin = firstRowTopMargin;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int top = 0;
        int left = 0;
        int right = 0;
        int bottom = 0;

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = params.getViewLayoutPosition();
//        Log.e("Test", "getItemOffsets========itemPosition: " + itemPosition);
        if (0 == itemPosition && isSkipFirstPosition) {
//            Log.e("Test","SkipFirstPosition========mFirstPositionRect: " + mFirstPositionRect);
            outRect.set(mFirstPositionRect.left, mFirstPositionRect.top, mFirstPositionRect.right, mFirstPositionRect.bottom);
            return;
        }
        spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int maxAllDividerWidth = getMaxDividerWidth(view); //计算总计留下的间隙

        int spaceWidth = 0;//首尾两列与父布局之间的间隔
        if (isScreenBothSideNeedSpace) {
            spaceWidth = mDividerWidth;
        }

        int eachItemWidth = maxAllDividerWidth / spanCount;//每个Item left+right
        int dividerItemWidth = (maxAllDividerWidth - 2 * spaceWidth) / (spanCount - 1);//item与item之间的距离

//        Log.e("Test", "========maxAllDividerWidth: " + maxAllDividerWidth + " ======eachItemWidth: " + eachItemWidth + " =====dividerItemWidth: " + dividerItemWidth);
        if (isSkipFirstPosition) { // 由于上面跳过第一个Item,方便计算,这里将Item位置减一
            itemPosition -= 1;
        }
        left = itemPosition % spanCount * (dividerItemWidth - eachItemWidth) + spaceWidth;
        right = eachItemWidth - left;
        bottom = mDividerWidth;
        if (mFirstRowTopMargin > 0 && isFirstRow(parent, itemPosition, spanCount, childCount))//第一行顶部是否需要间隔
            top = mFirstRowTopMargin;
        if (!isLastRowNeedSpace && isLastRow(parent, itemPosition, spanCount, childCount)) {//最后一行是否需要间隔
            bottom = 0;
        }
        outRect.set(left, top, right, bottom);
//        Log.e("Test", "========outRect: " + outRect);
    }

    /**
     * 获取Item View的大小，若无则自动分配空间
     * 并根据 屏幕宽度-View的宽度*spanCount 得到屏幕剩余空间
     *
     * @param view
     * @return
     */
    private int getMaxDividerWidth(View view) {
        int itemWidth = view.getLayoutParams().width;
        int itemHeight = view.getLayoutParams().height;
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels > mContext.getResources().getDisplayMetrics().heightPixels - mOccupyWidth
                ? mContext.getResources().getDisplayMetrics().heightPixels : mContext.getResources().getDisplayMetrics().widthPixels - mOccupyWidth;
        int totalMaxDividerWidth = screenWidth - itemWidth * spanCount;
//        Log.e("Test", "1========screenWidth: " + screenWidth + " ======itemWidth: " + itemWidth + " ======itemHeight: " + itemHeight + " =====totalMaxDividerWidth: " + totalMaxDividerWidth + " =====spanCount: " + spanCount);
        if (itemWidth < 0 || (isScreenBothSideNeedSpace && totalMaxDividerWidth <= spanCount * mDividerWidth)) { // totalMaxDividerWidth <= spanCount * mDividerWidth) 总共最多分割线比需要的分割线小需重新计算
            view.getLayoutParams().width = getAttachColumnWidth();
            // 这一句会使得Item为正方形
//            view.getLayoutParams().height = getAttachColumnWidth();
            totalMaxDividerWidth = screenWidth - view.getLayoutParams().width * spanCount;
//            Log.e("Test", "2========totalMaxDividerWidth: " + totalMaxDividerWidth);
        }
        return totalMaxDividerWidth;
    }

    /**
     * 根据屏幕宽度和item数量分配 item View的width和height
     *
     * @return
     */
    private int getAttachColumnWidth() {
        int itemWidth = 0;
        try {
            int width = mContext.getResources().getDisplayMetrics().widthPixels > mContext.getResources().getDisplayMetrics().heightPixels - mOccupyWidth
                    ? mContext.getResources().getDisplayMetrics().heightPixels : mContext.getResources().getDisplayMetrics().widthPixels - mOccupyWidth;
            if (isScreenBothSideNeedSpace) { // 挨着屏幕的两边也需要间隔
                // 3个Item则有4个间隔,4个Item则有5个间隔
                itemWidth = (width - (spanCount + 1) * mDividerWidth) / spanCount;
//                Log.e("Test", "getAttachColumnWidth11========itemWidth: " + itemWidth + " ======width: " + width + " =====mDividerWidth: " + mDividerWidth);
            } else { // 3个Item则有2个间隔,4个Item则有3个间隔
                itemWidth = (width - (spanCount - 1) * mDividerWidth) / spanCount;
//                Log.e("Test", "getAttachColumnWidth22========itemWidth: " + itemWidth + " ======width: " + width + " =====mDividerWidth: " + mDividerWidth);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemWidth;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (!needDrawOver) {
            draw(c, parent);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (needDrawOver) {
            draw(c, parent);
        }
    }

    //绘制item分割线
    private void draw(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            //画水平分隔线
            int left = child.getLeft();
            int right = child.getRight();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDividerWidth;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
            //画垂直分割线
            top = child.getTop();
            bottom = child.getBottom() + mDividerWidth;
            left = child.getRight() + layoutParams.rightMargin;
            right = left + mDividerWidth;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    /**
     * 判读是否是第一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isFirstColumn(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (pos % spanCount == 0) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if (pos % spanCount == 0) {// 第一列
                    return true;
                }
            } else {

            }
        }
        return false;
    }

    /**
     * 判断是否是最后一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0) {// 最后一列
                    return true;
                }
            } else {

            }
        }
        return false;
    }

    /**
     * 判读是否是最后一行
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastRow(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
            return lines == pos / spanCount + 1;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

        }
        return false;
    }

    /**
     * 判断是否是第一行
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isFirstRow(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos / spanCount + 1) == 1) {
                return true;
            } else {
                return false;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

        }
        return false;
    }

    /**
     * 获取列数
     *
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    /**
     * 设置 RecyclerView 被其他View占据的位置
     *
     * @param occupyWidth
     */
    public GridDividerItemDecoration setOccupyWidth(int occupyWidth) {
        mOccupyWidth = occupyWidth;
        return this;
    }

    /**
     * 是否设置第一个Item占据一行
     *
     * @param skipFirstPosition
     */
    public GridDividerItemDecoration setSkipFirstPosition(boolean skipFirstPosition) {
        isSkipFirstPosition = skipFirstPosition;
        return this;
    }

    /**
     * 如果第一个占据一行,那么它四周间隔,单位px
     *
     * @param left   --> 左侧间隔
     * @param top    --> 上侧间隔
     * @param right  --> 右侧间隔
     * @param bottom --> 下侧间隔
     */
    public GridDividerItemDecoration setFirstPositionRect(int left, int top, int right, int bottom) {
        this.mFirstPositionRect.set(left, top, right, bottom);
        return this;
    }

    /**
     * 是否将分割线绘制在最上面,当设置ItemView背景选中时,会发现它把分割线遮挡了,此时就需要整个属性了
     *
     * @param needDrawOver -> false ,默认不是的
     */
    public GridDividerItemDecoration setNeedDrawOver(boolean needDrawOver) {
        this.needDrawOver = needDrawOver;
        return this;
    }
}
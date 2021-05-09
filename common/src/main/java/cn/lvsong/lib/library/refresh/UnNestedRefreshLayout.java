package cn.lvsong.lib.library.refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import cn.lvsong.lib.library.R;

/**
 * 不知道嵌套刷新
 */

public class UnNestedRefreshLayout extends ViewGroup {
    @Nullable
    private IHeaderWrapper mHeaderListener;
    @Nullable
    private IFooterWrapper mFooterListener;
    /**
     * 回调监听
     */
    private OnNormalRefreshAndLoadListener listener;

    /**
     * 是否在拖拽
     */
    private boolean mIsBeingDragged;
    /**
     * 有效手指ID
     */
    private int mActivePointerId;
    /**
     * 按下时 Y 记录
     */
    private float mInitialDownY;
    /**
     * 移动 Y 距离
     */
    private float mRealMoveY;
    /**
     * 无效手指操作
     */
    private static final int INVALID_POINTER = -1;
    /**
     * 记录 HeaderView / FooterView  的状态
     */
    private int mStatus = RefreshState.DEFAULT;
    /**
     * 滑动辅助
     */
    private Scroller mScroller;
    /**
     * 恢复时移动时间
     */
    private float mScrollerMoveTime = 1500F;
    /**
     * HeaderView 滑动距离
     */
    private int mRefreshScrollY;
    /**
     * FooterView 滑动距离
     */
    private int mLoadScrollY;
    /**
     * 有效滑动距离阀值
     */
    private float mTouchSlop;

    /**
     * 内容View
     */
    private ViewGroup mTargetView;
    /**
     * HeaderView
     */
    private View mHeaderView;
    /**
     * FooterView
     */
    private View mFooterView;
    /**
     * HeaderView 高度
     */
    private int mHeaderViewHeight;
    /**
     * FooterView 高度
     */
    private int mFooterViewHeight;
    /**
     * 刷新时,下拉高度的比率 * mHeaderViewHeight
     */
    private float mRefreshRatio = 2.0F;
    /**
     * 加载时,上拉高度的比率 * mFooterViewHeight
     */
    private float mLoadRatio = 2.0F;
    /**
     * 刷新时,下拉高度的比率 * mHeaderViewHeight
     */
    private int mRefreshHeight;
    /**
     * 加载时,上拉高度的比率 * mFooterViewHeight
     */
    private int mLoadHeight;
    /**
     * 是否可以刷新
     */
    private boolean mRefreshable = true;
    /**
     * 是否可以加载
     */
    private boolean mLoadable = true;
    /**
     * 刷新是否成功
     */
    private boolean isRefreshSuccess = false;
    /**
     * 加载是否成功
     */
    private boolean isLoadSuccess = false;
    /**
     * 是否正在刷新
     */
    private boolean mRefreshing;
    /**
     * 是否正在加载
     */
    private boolean mLoading;
    /**
     * 是否有更多数据
     */
    private boolean noMoreData = false;
    /**
     * 手指离开时,还在滑动,此时 getScrollY() 却达到了加载/刷新的距离
     */
    private boolean mFlingScroll = false;
    /**
     * 第一次加载时,如果没有更多数据,底部显示什么,也是为了解决列表数据不够屏幕高度
     */
    private int mShowStyleFirstTime = 1;
    /**
     * 刷新 Runnable
     */
    private Runnable refreshAction = new Runnable() {
        @Override
        public void run() {
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), calculateTopScrollTime(-getScrollY()));
            invalidate();
            updateStatus(RefreshState.DEFAULT);
            mRefreshing = false;
            if (null != mTargetView) {
                int height = 0;
                int count = mTargetView.getChildCount();
                for (int i = 0; i < count; i++) {
                    height += mTargetView.getChildAt(i).getHeight();
                }
                if (height < mTargetView.getHeight()) { // 列表实际高度小于控件高度
                    if (2 == mShowStyleFirstTime) { // 模式二显示更多
                        setNoMoreData(true);
                    } else { // 模式一没有加载更多
                        setLoadable(false);
                    }
                }
            }
        }
    };

    /**
     * 自动刷新 Runnable
     */
    private final Runnable autoRefreshAction = new Runnable() {
        @Override
        public void run() {
            updateStatus(RefreshState.HEADER_REFRESHING);
        }
    };

    /**
     * 延迟刷新HeaderView Runnable,用于延时自动刷新
     */
    private final Runnable delayAutoRefreshAction = new Runnable() {
        @Override
        public void run() {
            int duration = calculateTopScrollTime(mHeaderViewHeight);
            mScroller.startScroll(0, getScrollY(), 0, -mHeaderViewHeight, duration * 2);
            invalidate();
        }
    };

    /**
     * 加载完成
     */
    private final Runnable loadCompletedAction = new Runnable() {
        @Override
        public void run() {
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), calculateBottomScrollTime(getScrollY()));
            invalidate();
            updateStatus(RefreshState.DEFAULT);
            mLoading = false;
        }
    };

    /**
     * 加载 Runnable
     */
    private final Runnable loadingAction = new Runnable() {
        @Override
        public void run() {
            updateStatus(RefreshState.FOOTER_LOADING);
        }
    };

    /**
     * 没有数据的 Runnable
     */
    private final Runnable noDataAction = new Runnable() {
        @Override
        public void run() {
            updateStatus(noMoreData ? RefreshState.FOOTER_NO_MORE : RefreshState.FOOTER_PULL);
        }
    };

    /**
     * 加载/刷新回调
     */
    public void setOnRefreshAndLoadListener(OnNormalRefreshAndLoadListener listener) {
        this.listener = listener;
    }

    private void calculateRefreshHeight() {
        mRefreshHeight = (int) (mHeaderViewHeight * mRefreshRatio);
    }

    private void calculateLoadHeight() {
        mLoadHeight = (int) (mFooterViewHeight * mLoadRatio);
    }

    public UnNestedRefreshLayout(Context context) {
        this(context, null);
    }

    public UnNestedRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnNestedRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setNestedScrollingEnabled(true);
        mHeaderView = new DefaultHeaderView(context);
        mFooterView = new DefaultFooterView(context);
        addView(mHeaderView);
        addView(mFooterView);
        if (mHeaderView instanceof IHeaderWrapper) {
            mHeaderListener = (IHeaderWrapper) mHeaderView;
            mHeaderViewHeight = mHeaderListener.getRefreshHeight();
        }
        if (mFooterView instanceof IFooterWrapper) {
            mFooterListener = (IFooterWrapper) mFooterView;
            mFooterViewHeight = mFooterListener.getLoadHeight();
        }
        calculateRefreshHeight();
        calculateLoadHeight();

        parseAttrs(context, attrs);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.UnNestedRefreshLayout);
        mShowStyleFirstTime = arr.getInt(R.styleable.UnNestedRefreshLayout_unrl_no_more_first_time, 1);
        mScrollerMoveTime = arr.getFloat(R.styleable.UnNestedRefreshLayout_unrl_scroll_time, 800F);
        arr.recycle();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setEnabled(boolean enabled) {
        setNestedScrollingEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureTarget();
    }

    /**
     * 寻找内容View
     */
    private void ensureTarget() {
        if (mTargetView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mHeaderView) && !child.equals(mFooterView)) {
                    mTargetView = (ViewGroup) child;
                    break;
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 以默认方式测量自身尺寸
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTargetView == null) {
            ensureTarget();
        }
        if (mTargetView == null) {
            return;
        }

        // 测量目标View的尺寸
        mTargetView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));

        // 测量 HeaderView
        if (null != mHeaderView && null != mHeaderListener) {
            mHeaderView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                    MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mHeaderViewHeight, MeasureSpec.EXACTLY));
        }

        // 测量 FooterView
        if (null != mFooterView && null != mFooterListener) {
            mFooterView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                    MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mFooterViewHeight, MeasureSpec.EXACTLY));
        }

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        if (mTargetView == null) {
            ensureTarget();
        }
        if (mTargetView == null) {
            return;
        }
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();
        int childRight = getPaddingRight();
        int childWidth = width - childLeft - childRight;
        int childHeight = height - getPaddingBottom();


        if (null != mHeaderView) {
            mHeaderView.layout(childLeft, childTop - mHeaderViewHeight, childLeft + childWidth, childTop);
        }

        if (null != mTargetView) {
            mTargetView.layout(childLeft, childTop, childLeft + childWidth, childHeight);
        }

        if (null != mFooterView) {
            mFooterView.layout(childLeft, height, childLeft + childWidth, height + mFooterViewHeight);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void computeScroll() {
//		Log.e("UnNestedRefreshLayout", "computeScroll====mScroller.getCurrX: " + mScroller.getCurrX() + " =====mScroller.getCurrY: " + mScroller.getCurrY());
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);

        if (null != mHeaderListener && y < 0) {
            mHeaderListener.onMoveDistance(-y);
        }

        if (null != mFooterListener && y > 0) {
            mFooterListener.onMoveDistance(y);
        }

        if (null != listener) {
            listener.onMoveDistance(this, y, mRefreshHeight, mLoadHeight);
        }
    }

    /**
     * https://blog.csdn.net/xingchenxuanfeng/article/details/84790299
     * I. 传入 -1 ,判断可以下滑
     * II. 传入 1, 判断可以上滑
     * 滑到最顶部时，返回false，意思是不能下拉了
     * canScrollVertically(-1)的值表示是否能向下滚动，true表示能滚动，false表示已经滚动到顶部
     */
    private boolean canChildScrollUp() {
        return !mTargetView.canScrollVertically(-1);
    }

    /**
     * canScrollVertically(1)的值表示是否能向上滚动，true表示能滚动，false表示已经滚动到底部
     */
    private boolean canChildScrollDown() {
        return !mTargetView.canScrollVertically(1);
    }

    /**
     * 计算滑动时间
     */
    private int calculateTopScrollTime(int scrollDistance) {
        return (int) (mScrollerMoveTime * scrollDistance / mRefreshHeight);
    }

    private int calculateBottomScrollTime(int scrollDistance) {
        return (int) (mScrollerMoveTime * scrollDistance / mLoadHeight);
    }

    /**
     * 阻尼滑动
     */
    private int calculateRefreshScrollY(int overScroll) {
        float totalScrolledY = Math.abs(getScrollY()) * 1F;
        float curRatio = 1F - totalScrolledY / mRefreshHeight;
        if (curRatio < 0) {
            curRatio = 0F;
        } else if (curRatio > 1F) {
            curRatio = 1F;
        }
        return (int) (curRatio * overScroll);
    }

    private int calculateLoadScrollY(int overScroll) {
        float totalScrolledY = Math.abs(getScrollY()) * 1F;
        float curRatio = 1F - totalScrolledY / mLoadHeight;
        if (curRatio < 0) {
            curRatio = 0F;
        } else if (curRatio > 1F) {
            curRatio = 1F;
        }
        return (int) (curRatio * overScroll);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }

        if (!isEnabled() || mRefreshing || mLoading) {
            return false;
        }

        ensureTarget();
        final int action = ev.getActionMasked();
        int pointerIndex;

//        Log.e("UnNestedRefreshLayout", "onInterceptTouchEvent========mIsBeingDragged: " + mIsBeingDragged + " ====action: " + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                mInitialDownY = ev.getY(pointerIndex);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float y = ev.getY(pointerIndex);
                startDragging(y); // 这里调用仅仅为了将 mIsBeingDragged = true, 也就是为了拦截滑动事件
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }
//        Log.e("UnNestedRefreshLayout", "onInterceptTouchEvent=========mIsBeingDragged: " + mIsBeingDragged);
        return mIsBeingDragged;
    }

    /**
     * java.lang.IllegalArgumentException:pointerIndex out of range
     * https://www.jianshu.com/p/b65d21c909e5
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        int pointerIndex;
        if (!isEnabled() || mRefreshing || mLoading) {
            return false;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float y = ev.getY(pointerIndex);
//                startDragging(y);
                if (mIsBeingDragged) {
                    final float overScrollTop = (y - mRealMoveY);
                    if (canChildScrollUp() && mRefreshable) {
                        refreshScroll(-(int) overScrollTop);
                    } else if (canChildScrollDown() && mLoadable) {
                        loadScroll(-(int) overScrollTop);
                    }
                }
//                Log.e("UnNestedRefreshLayout", "onTouchEvent=======y: " + y + " ====mRealMoveY: " + mRealMoveY);
                mRealMoveY = y;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                pointerIndex = ev.getActionMasked();
                if (pointerIndex < 0) {
                    return false;
                }
                mActivePointerId = ev.getPointerId(pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    resetScroll();
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            case MotionEvent.ACTION_CANCEL:
                resetScroll();
                return false;
        }
        return true;
    }

    /**
     * 不支持嵌套滑动时刷新滑动处理
     */
    private void refreshScroll(int dy) {
//        Log.e("UnNestedRefreshLayout", "refreshScroll=======dy: " + dy + " =====mRefreshScrollY: " + mRefreshScrollY + " =====ScrollY: " + getScrollY() + " ===mRefreshHeight: " + mRefreshHeight);
        if (!mRefreshing && dy < 0 && mRefreshScrollY < mRefreshHeight && canChildScrollUp()) { // 下滑
            mRefreshScrollY += -dy;
            if (mRefreshScrollY > mHeaderViewHeight) { // 松手可以刷新
                updateStatus(RefreshState.HEADER_RELEASE);
            } else {
                updateStatus(RefreshState.HEADER_DRAG);
            }
            if (mRefreshScrollY > mRefreshHeight) { //零界点,再加上本次滑动,大于了 HeaderView 允许滑动距离
                int consumedY = mRefreshScrollY + dy - mRefreshHeight;
                mRefreshScrollY = mRefreshHeight;
//                Log.e("UnNestedRefreshLayout", "refreshScroll=======  1 ");
                scrollBy(0, consumedY);
            } else {
//                Log.e("UnNestedRefreshLayout", "refreshScroll=======  2 ");
                scrollBy(0, dy);
            }
        } else if (!mRefreshing && dy > 0 && mRefreshScrollY > 0) { // 上滑
            mRefreshScrollY -= dy;
            if (mRefreshScrollY > mHeaderViewHeight) { // 松手可以刷新
                updateStatus(RefreshState.HEADER_RELEASE);
            } else {
                updateStatus(RefreshState.HEADER_DRAG);
            }
            if (mRefreshScrollY < 0) { // 此时将不能滑动, consumedY实际上为0
//                Log.e("UnNestedRefreshLayout", "refreshScroll=======  3 ");
                mRefreshScrollY = 0;
            } else {
//                Log.e("UnNestedRefreshLayout", "refreshScroll=======  4 ");
                scrollBy(0, dy);
            }
        } else if (0 == mRefreshScrollY && getScrollY() < 0) { // 修复误差
//            Log.e("UnNestedRefreshLayout", "refreshScroll=======ScrollY: " + getScrollY());
            scrollBy(0, -getScrollY());
        }
    }

    /**
     * 不支持嵌套滑动时加载滑动处理
     */
    private void loadScroll(int dy) {
//        Log.e("UnNestedRefreshLayout", "loadScroll=====getScrollY: " + getScrollY() + " =====dy: " + dy + " ======mLoadScrollY: " + mLoadScrollY + " ===mFooterViewHeight: " + mFooterViewHeight + " ===mLoadHeight: " + mLoadHeight);
        if (!mLoading && dy < 0 && mLoadScrollY < 0 && canChildScrollDown()) { // 下滑
            mLoadScrollY += -dy;
            if (!noMoreData) { // 有更多数据则更新状态
                if (Math.abs(mLoadScrollY) > mFooterViewHeight) { // 松手可以加载
//                    Log.e("UnNestedRefreshLayout", "loadScroll=======  111 mStatus: " + mStatus);
                    updateStatus(RefreshState.FOOTER_RELEASE);
                } else {
//                    Log.e("UnNestedRefreshLayout", "loadScroll=======  222 mStatus: " + mStatus);
                    updateStatus(RefreshState.FOOTER_PULL);
                }
            }
            if (mLoadScrollY > 0) {
//                Log.e("UnNestedRefreshLayout", "loadScroll=======  333 ");
                mLoadScrollY = 0;
            } else {
//                Log.e("UnNestedRefreshLayout", "loadScroll=======  444 ");
                scrollBy(0, dy);
            }
        } else if (!mLoading && dy > 0 && mLoadScrollY < mLoadHeight) { // 上滑
            mLoadScrollY += -dy;
            if (!noMoreData) { // 有更多数据则更新状态
                if (Math.abs(mLoadScrollY) > mFooterViewHeight) { // 松手可以加载
//                    Log.e("UnNestedRefreshLayout", "loadScroll=======  555 mStatus: " + mStatus);
                    updateStatus(RefreshState.FOOTER_RELEASE);
                } else {
//                    Log.e("UnNestedRefreshLayout", "loadScroll=======  666 mStatus: " + mStatus);
                    updateStatus(RefreshState.FOOTER_PULL);
                }
            }
            if (Math.abs(mLoadScrollY) > mLoadHeight) { //零界点,再加上本次滑动,大于了 FooterView 允许滑动距离
//                Log.e("UnNestedRefreshLayout", "loadScroll=======  777 ");
                int consumedY = mLoadHeight + mLoadScrollY + dy;
                mLoadScrollY = -mLoadHeight;
                scrollBy(0, consumedY);
            } else {
//                Log.e("UnNestedRefreshLayout", "loadScroll=======  888 ");
                scrollBy(0, dy);
            }
        } else if (0 == mLoadScrollY && getScrollY() > 0) { // 修复误差
//            Log.e("UnNestedRefreshLayout", "loadScroll=======ScrollY: " + getScrollY());
            scrollBy(0, -getScrollY());
        }
    }

    @SuppressLint("NewApi")
    private void startDragging(float y) {
        final float yDiff = y - mInitialDownY;
//        Log.e("UnNestedRefreshLayout", "startDragging=========mIsBeingDragged: " + mIsBeingDragged + " ====== " + (yDiff > mTouchSlop) + " ====yDiff: " + yDiff);
        if (yDiff > 0 && yDiff > mTouchSlop && canChildScrollUp()) { // 下拉到底
            mRealMoveY = mInitialDownY + yDiff;
            mIsBeingDragged = true;
        } else if (yDiff < 0 && yDiff + mTouchSlop < 0 && canChildScrollDown()) { // 上拉到底
            mRealMoveY = mInitialDownY + yDiff;
            mIsBeingDragged = true;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 当手指抬起时, TargetView 非NestedScrollChild
     */
    private void resetScroll() {
        // 判断本次触摸系列事件结束时,Layout的状态
        switch (mStatus) {
            //下拉刷新
            case RefreshState.HEADER_DRAG:
                scrollToHeaderDefaultStatus();
                break;
            // 释放刷新
            case RefreshState.HEADER_RELEASE:
                scrollToRefreshStatus();
                break;
            //没有更多数据
            case RefreshState.FOOTER_NO_MORE:
                // 上拉加载
            case RefreshState.FOOTER_PULL:
                scrollToFooterDefaultStatus();
                break;
            // 释放加载
            case RefreshState.FOOTER_RELEASE:
                scrollToLoadStatus();
                break;
            default:
                break;
        }
    }

    /**
     * 还原到FooterView初始位置
     */
    private void scrollToFooterDefaultStatus() {
        mLoadScrollY = 0;
        int duration = calculateTopScrollTime(getScrollY());
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), duration);
        invalidate();
    }

    /**
     * 移动到正常加载的位置
     */
    private void scrollToLoadStatus() {
        mLoading = true;
        int duration = calculateTopScrollTime(getScrollY());
        mScroller.startScroll(0, getScrollY(), 0, mFooterViewHeight - getScrollY(), duration);
        invalidate();
        postDelayed(loadingAction, duration);
    }

    /**
     * 还原到HeaderView初始位置
     */
    private void scrollToHeaderDefaultStatus() {
        mRefreshScrollY = 0;
        int duration = calculateTopScrollTime(-getScrollY());
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), duration);
        invalidate();
    }

    /**
     * 移动到正常刷新的位置
     */
    private void scrollToRefreshStatus() {
        mRefreshing = true;
        int duration = calculateTopScrollTime(mHeaderViewHeight);
        mScroller.startScroll(0, getScrollY(), 0, -mHeaderViewHeight - getScrollY(), duration);
        invalidate();
        postDelayed(autoRefreshAction, duration);
    }

    private void updateStatus(int status) {
        this.mStatus = status;
        switch (status) {
            case RefreshState.DEFAULT:
                onDefault();
                break;
            case RefreshState.HEADER_AUTO:
                if (mHeaderListener != null) {
                    mHeaderListener.onAutoRefreshPreparing();
                }
                break;
            case RefreshState.HEADER_DRAG:
                if (mHeaderListener != null) {
                    mHeaderListener.onPullDown();
                }
                break;
            case RefreshState.HEADER_RELEASE:
                if (mHeaderListener != null) {
                    mHeaderListener.onPullDownAndReleasable();
                }
                break;
            case RefreshState.HEADER_REFRESHING:
                if (mHeaderListener != null) {
                    mHeaderListener.onRefreshing();
                }
                if (listener != null) {
                    listener.onRefresh(this);
                }
                break;
            case RefreshState.HEADER_COMPLETED:
                mRefreshScrollY = 0;
                if (mHeaderListener != null) {
                    mHeaderListener.onRefreshComplete(isRefreshSuccess);
                }
                break;
            case RefreshState.HEADER_FAILURE:
                if (mHeaderListener != null) {
                    mHeaderListener.onRefreshFailure();
                }
                break;
            case RefreshState.FOOTER_PULL:
                if (mFooterListener != null) {
                    mFooterListener.onPullUp();
                }
                break;
            case RefreshState.FOOTER_RELEASE:
                if (mFooterListener != null) {
                    mFooterListener.onPullUpAndReleasable();
                }
                break;
            case RefreshState.FOOTER_LOADING:
                if (mFooterListener != null) {
                    mFooterListener.onLoading();
                }
                if (listener != null) {
                    listener.onLoad(this);
                }
                break;
            case RefreshState.FOOTER_COMPLETED:
                mLoadScrollY = 0;
                if (mFooterListener != null) {
                    mFooterListener.onLoadComplete(isLoadSuccess);
                }
                break;
            case RefreshState.FOOTER_FAILURE:
                if (mFooterListener != null) {
                    mFooterListener.onLoadFailure();
                }
                break;
            case RefreshState.FOOTER_NO_MORE:
                if (mFooterListener != null) {
                    mFooterListener.onNoMore();
                }
                break;
        }
    }

    /**
     * 重置
     */
    private void onDefault() {
        isRefreshSuccess = false;
        isLoadSuccess = false;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// 对外公共方法 ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 设置是否可以刷新,默认true
     */
    public void setRefreshable(boolean refreshable) {
        this.mRefreshable = refreshable;
    }

    /**
     * 设置是否可以加载,默认true
     */
    public void setLoadable(boolean loadable) {
        this.mLoadable = loadable;
    }

    /**
     * 刷新完成
     *
     * @param isSuccess --> 可以根据这个值,设置刷新成功或者失败
     */
    public void setFinishRefresh(boolean isSuccess) {
        setFinishRefresh(isSuccess, (long) mScrollerMoveTime);
    }

    /**
     * 刷新完成
     *
     * @param isSuccess --> 可以根据这个值,设置刷新成功或者失败
     * @param delay     --> 延迟关闭动画,默认延迟 800ms
     */
    public void setFinishRefresh(boolean isSuccess, long delay) {
        isRefreshSuccess = isSuccess;
        updateStatus(isRefreshSuccess ? RefreshState.HEADER_COMPLETED : RefreshState.HEADER_FAILURE);
        postDelayed(refreshAction, delay);
    }

    /**
     * 加载完成
     *
     * @param isSuccess --> 可以根据这个值,设置加载成功或者失败
     */
    public void setFinishLoad(boolean isSuccess) {
        setFinishLoad(isSuccess, (long) mScrollerMoveTime);
    }

    /**
     * 加载完成
     *
     * @param isSuccess --> 可以根据这个值,设置加载成功或者失败
     * @param delay     --> 延迟关闭动画 ,默认 800 ms
     */
    public void setFinishLoad(boolean isSuccess, long delay) {
        isLoadSuccess = isSuccess;
        updateStatus(isLoadSuccess ? RefreshState.FOOTER_COMPLETED : RefreshState.FOOTER_FAILURE);
        postDelayed(loadCompletedAction, delay);
    }

    /**
     * 设置是否有更多数据
     *
     * @param noMoreData --> 默认是 false,即有更多数据
     */
    public void setNoMoreData(boolean noMoreData) {
        this.noMoreData = noMoreData;
        postDelayed(noDataAction, (long) (mScrollerMoveTime * 1.2));
    }

    /**
     * 设置自动刷新
     */
    public void setAutoRefresh() {
        if (null == mHeaderView) {
            throw new IllegalArgumentException("HeaderView is null");
        }
        if (mRefreshable) { // 刷新可用才能自动刷新
            autoRefresh(0L);
        }
    }

    public void setAutoRefresh(long delay) {
        if (null == mHeaderView) {
            throw new IllegalArgumentException("HeaderView is null");
        }
        if (mRefreshable) { // 刷新可用才能自动刷新
            autoRefresh(delay);
        }
    }

    private void autoRefresh(long delay) {
        mRefreshing = true;
        // 这里是为了改变自动刷新时默认显示文本,否则会显示下拉刷新
        updateStatus(RefreshState.HEADER_AUTO);
        int duration = calculateTopScrollTime(mHeaderViewHeight);
        if (delay >0L){
            postDelayed(delayAutoRefreshAction,delay);
        }else {
            mScroller.startScroll(0, getScrollY(), 0, -mHeaderViewHeight, duration * 2);
            invalidate();
        }
        postDelayed(autoRefreshAction, duration + delay);
    }

    /**
     * 添加 HeaderView
     */
    public void addHeader(@NonNull View header) {
        if (header instanceof IHeaderWrapper) {
            mHeaderListener = (IHeaderWrapper) header;
            this.mHeaderViewHeight = mHeaderListener.getRefreshHeight();
        } else {
            throw new IllegalArgumentException("HeaderView must be implement IHeaderWrapper");
        }

        // 移除掉默认添加的
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.equals(mHeaderView)) {
                removeView(child);
                break;
            }
        }
        this.mHeaderView = header;
        addView(mHeaderView);
    }

    /**
     * 添加 FooterView
     */
    public void addFooter(@NonNull View footer) {
        if (footer instanceof IFooterWrapper) {
            mFooterListener = (IFooterWrapper) footer;
            this.mFooterViewHeight = mFooterListener.getLoadHeight();
        } else {
            throw new IllegalArgumentException("FooterView must be implement IFooterWrapper");
        }
        // 移除掉默认添加的
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.equals(mFooterView)) {
                removeView(child);
                break;
            }
        }
        this.mFooterView = footer;
        addView(mFooterView);
    }

    /**
     * 设置头部可以下拉的比率, 默认是2倍HeaderView高度
     */
    public void setRefreshRatio(float refreshRatio) {
        this.mRefreshRatio = refreshRatio;
    }

    /**
     * 设置底部可以下拉的比率, 默认是2倍FooterView高度
     */
    public void setLoadRatio(float loadRatio) {
        this.mLoadRatio = loadRatio;
    }


    /**
     * 是否正在刷新
     *
     * @return true --> 正在刷新
     */
    public boolean isRefreshing() {
        return mRefreshing;
    }

    /**
     * 是否正在加载
     *
     * @return true --> 正在加载
     */
    public boolean isLoading() {
        return mLoading;
    }
}

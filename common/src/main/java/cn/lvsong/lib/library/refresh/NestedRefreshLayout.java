package cn.lvsong.lib.library.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import cn.lvsong.lib.library.R;

/**
 * https://blog.csdn.net/chaoyangsun/article/details/94398225  --> Scroller
 * https://www.jianshu.com/p/8be7458c644b --> NestedScrollingParent
 * https://github.com/baiduapp-tec/ELinkageScroll --> 多子view嵌套滚动通用解决方案
 * 1. 阻尼效果未完成
 * 2. 如果分页没有记得调用 setNoMoreData(true) , 特别是在 CoordinatorLayout 嵌套中,如果列表数据不满一屏时(即加载首页手机下面还有留白),
 * 往上刷动会触发上拉加载,而不是先滑动AppBarLayout等内部控件
 */

public class NestedRefreshLayout extends ViewGroup implements NestedScrollingParent2, NestedScrollingChild2 {
    @Nullable
    private IHeaderWrapper mHeaderListener;
    @Nullable
    private IFooterWrapper mFooterListener;
    /**
     * 回调监听
     */
    private OnNestedRefreshAndLoadListener listener;
    /**
     * 计算父类嵌套滑动消耗值
     */
    private final int[] mParentScrollConsumed = new int[2];
    /**
     * 计算父类偏移量
     */
    private final int[] mParentOffsetInWindow = new int[2];
    /**
     * 控制 TargetView 嵌套滑动=逻辑,一般不用
     */
    private OnChildScrollUpCallback mChildScrollUpCallback;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;

    /**
     * 滑动辅助
     */
    private Scroller mScroller;
    /**
     * 恢复时移动时间
     */
    private float mScrollerMoveTime = 1500F;
    /**
     * Fling时 速度界限,低于这个且达到刷新/加载距离,则可以刷新
     */
    private int mRequiredVelocityY = 0;
    /**
     * HeaderView 滑动距离
     */
    private int mRefreshScrollY;
    /**
     * FooterView 滑动距离
     */
    private int mLoadScrollY;

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
     * 第一次加载时,如果没有更多数据,底部显示什么
     * 也是为了解决 CoordinatorLayout + TabLayout + ViewPager + RecyclerView下
     * 列表数据 + 上部折叠部分一起不够屏幕高度,造成上滑联动异常(加载更多会提前显示)
     */
    private int mShowStyleFirstTime = 1;
    /**
     * 刷新 Runnable
     */
    private final Runnable refreshAction = new Runnable() {
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
                    if (2 == mShowStyleFirstTime) { // 模式二显示没有更多
                        setNoMoreData(true);
                    } else { // 模式一不显示加载更多
                        setLoadable(false);
                    }
                }
            }
            if (null != listener){ // 头部动画执行结束了
                listener.onRefreshAnimatorEnd();
            }
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
     * 自动刷新 Runnable
     */
    private final Runnable autoRefreshAction = new Runnable() {
        @Override
        public void run() {
            updateStatus(RefreshState.HEADER_REFRESHING);
        }
    };

    /**
     * 加载 Runnable
     */
    private final Runnable loadAction = new Runnable() {
        @Override
        public void run() {
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), calculateBottomScrollTime(getScrollY()));
            invalidate();
            updateStatus(RefreshState.DEFAULT);
            mLoading = false;
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
    public void setOnRefreshAndLoadListener(OnNestedRefreshAndLoadListener listener) {
        this.listener = listener;
    }

    private void calculateRefreshHeight() {
        mRefreshHeight = (int) (mHeaderViewHeight * mRefreshRatio);
    }

    private void calculateLoadHeight() {
        mLoadHeight = (int) (mFooterViewHeight * mLoadRatio);
    }

    public NestedRefreshLayout(Context context) {
        this(context, null);
    }

    public NestedRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        mRequiredVelocityY = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
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
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.NestedRefreshLayout);
        mShowStyleFirstTime = arr.getInt(R.styleable.NestedRefreshLayout_nrl_no_more_first_time, 1);
        mScrollerMoveTime = arr.getFloat(R.styleable.NestedRefreshLayout_nrl_scroll_time, 800F);
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

    //////////////////////////////////////                         ////////////////////////////////////////
    //////////////////////////////////////  NestedScrollingParent  ////////////////////////////////////////
    //////////////////////////////////////                         ////////////////////////////////////////

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    /**
     * 父控件接受嵌套滑动，不管是手势滑动还是fling 父控件都接受
     * PS: 如果使用了 int type, 则判断条件必须加上 ViewCompat.TYPE_TOUCH == type,否则fling会卡顿
     */
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && ViewCompat.TYPE_TOUCH == type && (mRefreshable || mLoadable);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
        // PS: 这里如果 startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL,type); 会导致协同滑动异常
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mRefreshScrollY = 0;
        mLoadScrollY = 0;
        mFlingScroll = false;
    }


    /**
     * 在嵌套滑动的子控件未滑动之前，判断父控件是否优先与子控件处理(也就是父控件可以先消耗，然后给子控件消耗）
     *
     * @param target   具体嵌套滑动的那个子类
     * @param dx       水平方向嵌套滑动的子控件想要变化的距离 dx<0 向右滑动 dx>0 向左滑动
     * @param dy       垂直方向嵌套滑动的子控件想要变化的距离 dy<0 向下滑动 dy>0 向上滑动
     * @param consumed 这个参数要我们在实现这个函数的时候指定，回头告诉子控件当前父控件消耗的距离
     *                 consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离 好让子控件做出相应的调整
     *                 <p>
     *                 PS: 增加 0 == mLoadScrollY | 0 == mRefreshScrollY 是防止先下拉不松手接着上滑(或者先下滑不松手接着上拉)导致头部脚部都移动
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
//        Log.e("NestedRefreshLayout", "onNestedPreScroll=======dy: " + dy + " ====getScrollY: " + getScrollY() + " ====mHeaderScrollY: " + mRefreshScrollY + " ====mFooterScrollY: " + mLoadScrollY + " ====mLoading: " + mLoading + " ===type: " + type);
//        Log.e("NestedRefreshLayout", "onNestedPreScroll=======到达顶部: " + canChildScrollUp() + " ====到达底部: " + canChildScrollDown());
        if (ViewCompat.TYPE_TOUCH == type) {
            if (mRefreshable && mRefreshing && dy < 0 && canChildScrollUp() && !mLoading && 0 == mLoadScrollY) { // HeaderView隐藏着,正在刷新时下滑
                int ddy = calculateRefreshScrollY(dy);
                if (-(getScrollY() + ddy) > mHeaderViewHeight) { // 当 HeaderView 向下滑动时, getScrollY() =  mHeaderViewHeight, 此时则不再滑动头部
                    scrollBy(0, -(mHeaderViewHeight + getScrollY()));
                    consumed[1] = -(mHeaderViewHeight + getScrollY());
                } else {
                    scrollBy(0, ddy);
                    consumed[1] = ddy;
                }
//                Log.e("NestedRefreshLayout", "onNestedPreScroll=======  1 " + " =====ddy: " + ddy);
            } else if (mRefreshable && !mRefreshing && dy > 0 && mRefreshScrollY > 0 && !mLoading && 0 == mLoadScrollY) { // 还没达到刷新状态(可以是没有达到必要高度/达到高度没有松手)往上滑动
                mRefreshScrollY -= dy;
                if (mRefreshScrollY > mHeaderViewHeight) { // 松手可以刷新
                    updateStatus(RefreshState.HEADER_RELEASE);
                } else {
                    updateStatus(RefreshState.HEADER_DRAG);
                }
                if (mRefreshScrollY < 0) {
                    int consumedY = mRefreshScrollY + dy;
                    scrollBy(0, consumedY);
                    consumed[1] = consumedY;
                    mRefreshScrollY = 0;
                } else {
                    scrollBy(0, dy);
                    consumed[1] = dy;
                }
//                Log.e("NestedRefreshLayout", "onNestedPreScroll=======  2 ");
            } else if (mRefreshable && mRefreshing && dy > 0 && getScrollY() < 0 && !mLoading && 0 == mLoadScrollY) { // 刷新时上滑
                if (getScrollY() + dy > 0) { // 接近零界点,此时如果还滑动 dy, 则滑多了
                    scrollBy(0, -getScrollY());
                    consumed[1] = -getScrollY();
                } else {
                    scrollBy(0, dy);
                    consumed[1] = dy;
                }
//                Log.e("NestedRefreshLayout", "onNestedPreScroll=======  3 ");
            }

            /////////////////////////////////////////////////////////////////////////////////

            if (mLoadable && !mLoading && dy > 0 && mLoadScrollY < mLoadHeight && canChildScrollDown() && !mRefreshing && 0 == mRefreshScrollY) { // 上滑且没有达到加载状态(可以是没有达到必要高度/达到高度没有松手)
                // 同样，将嵌套滑动向上传递
                final int[] parentConsumed = mParentScrollConsumed;
                // 之所以在这里先分发,因为在 CoordinatorLayout 嵌套中,不满一屏时会出现bug
                if (noMoreData && dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
//                    Log.e("NestedRefreshLayout", "onNestedPreScroll=======  4 ");
                    consumed[0] += parentConsumed[0];
                    consumed[1] += parentConsumed[1];
                } else {
                    int ddy = calculateLoadScrollY(dy);
                    mLoadScrollY += ddy;
                    if (noMoreData) { // 没有更多数据
                        updateStatus(RefreshState.FOOTER_NO_MORE);
                    } else if (mLoadScrollY > mFooterViewHeight) { // 松手可以加载
                        updateStatus(RefreshState.FOOTER_RELEASE);
                    } else {
                        updateStatus(RefreshState.FOOTER_PULL);
                    }
                    if (mLoadScrollY > mLoadHeight) { //零界点,再加上本次滑动,大于了 FooterView 允许滑动距离
                        int consumedY = mLoadHeight - mLoadScrollY + ddy;
                        mLoadScrollY = mLoadHeight;
                        scrollBy(0, consumedY);
                        consumed[1] = consumedY;// consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离
                    } else {
                        scrollBy(0, ddy);
                        consumed[1] = ddy;
                    }
                }
//                Log.e("NestedRefreshLayout", "onNestedPreScroll=======  5 ");
            } else if (mLoadable && mLoading && dy > 0 && canChildScrollDown() && !mRefreshing && 0 == mRefreshScrollY) { // FooterView隐藏着,加载时上滑
                int ddy = calculateLoadScrollY(dy);
                if ((getScrollY() + ddy) > mFooterViewHeight) { // 当 FooterView 向上滑动时, getScrollY() =  mFooterViewHeight, 此时则不再滑动头部
                    scrollBy(0, mFooterViewHeight - getScrollY());
                    consumed[1] = mFooterViewHeight - getScrollY();
                } else {
                    scrollBy(0, ddy);
                    consumed[1] = ddy;
                }
//                Log.e("NestedRefreshLayout", "onNestedPreScroll=======  6 ");
            } else if (mLoadable && !mLoading && dy < 0 && mLoadScrollY > 0 && !mRefreshing && 0 == mRefreshScrollY) { // 还没达到加载状态(可以是没有达到必要高度/达到高度没有松手)往下滑动
                mLoadScrollY += dy;
                if (noMoreData) { // 没有更多数据
                    updateStatus(RefreshState.FOOTER_NO_MORE);
                } else if (mLoadScrollY > mFooterViewHeight) { // 松手可以加载
                    updateStatus(RefreshState.FOOTER_RELEASE);
                } else {
                    updateStatus(RefreshState.FOOTER_PULL);
                }
                if (mLoadScrollY < 0) {
                    int consumedY = dy - mLoadScrollY;
                    scrollBy(0, consumedY);
                    consumed[1] = consumedY;
                    mLoadScrollY = 0;
                } else {
                    scrollBy(0, dy);
                    consumed[1] = dy;
                }
//                Log.e("NestedRefreshLayout", "onNestedPreScroll=======  7 ");
            } else if (mLoadable && mLoading && dy < 0 && getScrollY() > 0 && !mRefreshing && 0 == mRefreshScrollY) { // 加载时下滑
                if (getScrollY() + dy < 0) { // 接近零界点,此时如果还滑动 dy, 则滑多了
                    scrollBy(0, -getScrollY());
                    consumed[1] = -getScrollY();
                } else {
                    scrollBy(0, dy);
                    consumed[1] = dy;
                }
//                Log.e("NestedRefreshLayout", "onNestedPreScroll=======  8 ");
            }
        }

        // 同样，将嵌套滑动向上传递
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
//            Log.e("NestedRefreshLayout", "onNestedPreScroll=======  9 ");
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    /**
     * 嵌套滑动的子控件在滑动之后，判断父控件是否继续处理（也就是父消耗一定距离后，子再消耗，最后判断父消耗不）
     *
     * @param target       具体嵌套滑动的那个子类
     * @param dxConsumed   水平方向嵌套滑动的子控件滑动的距离(消耗的距离)
     * @param dyConsumed   垂直方向嵌套滑动的子控件滑动的距离(消耗的距离)
     * @param dxUnconsumed 水平方向嵌套滑动的子控件未滑动的距离(未消耗的距离)
     * @param dyUnconsumed 垂直方向嵌套滑动的子控件未滑动的距离(未消耗的距离)
     *                     <p>
     *                     PS:将下拉刷新放这里,主要是当  NestedRefreshLayout 为 子控件,即实现了  NestedScrollingChild2,
     *                     此时 NestedRefreshLayout 的父控件需要嵌套滑动,如 CoordinatorLayout,
     *                     如果在 onNestedPreScroll() 提前消费,会导致 CoordinatorLayout 中
     *                     AppBarLayout,CollapsingToolbarLayout 等协作滑动时异常
     */
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);
//        Log.e("NestedRefreshLayout", "onNestedScroll=======dyUnconsumed: " + dyUnconsumed + " ====getScrollY: " + getScrollY() + " ==== mParentOffsetInWindow[1]: " + mParentOffsetInWindow[1]);
        if (ViewCompat.TYPE_TOUCH == type) {
            final int dy = dyUnconsumed + mParentOffsetInWindow[1];
            if (mRefreshable && !mRefreshing && canChildScrollUp() && dy < 0 && !mLoading) { //准备刷新
                int ddy = calculateRefreshScrollY(dy);
                mRefreshScrollY += -ddy;
                if (mRefreshScrollY > mHeaderViewHeight) { // 松手可以刷新
                    updateStatus(RefreshState.HEADER_RELEASE);
                } else {
                    updateStatus(RefreshState.HEADER_DRAG);
                }
                if (mRefreshScrollY > mRefreshHeight) { //零界点,再加上本次滑动,大于了 HeaderView 允许滑动距离
                    int consumedY = mRefreshScrollY + ddy - mRefreshHeight;
                    mRefreshScrollY = mRefreshHeight;
                    scrollBy(0, consumedY);
                } else {
                    scrollBy(0, ddy);
                }
//                Log.e("NestedRefreshLayout", "onNestedScroll=====1====mHeaderScrollY: " + mRefreshScrollY + " ====ddy: " + ddy);
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
//        Log.e("NestedRefreshLayout", "onStopNestedScroll=====getScrollY: " + getScrollY() + " ====mFling: " + mFlingScroll);
        mNestedScrollingParentHelper.onStopNestedScroll(target, type);
        stopNestedScroll();
        if (!mFlingScroll) {
            // 此时判断是否是 HeaderView 处理
            if (!mRefreshing && getScrollY() < 0) {
//                Log.e("NestedRefreshLayout", "onStopNestedScroll=====1");
                if (mHeaderViewHeight <= -getScrollY()) { // 达到刷新要求
                    // 松手后,向上滑动
                    mRefreshing = true;
                    updateStatus(RefreshState.HEADER_REFRESHING);
                    mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() + mHeaderViewHeight), calculateTopScrollTime(-(getScrollY() + mHeaderViewHeight)));
                } else {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), calculateTopScrollTime(-getScrollY()));
                }
            } else if (mRefreshing && getScrollY() < 0) { // 刷新时往上滑动
//                Log.e("NestedRefreshLayout", "onStopNestedScroll=====2");
                if (mHeaderViewHeight > -getScrollY()) { // 隐藏 HeaderView
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), calculateTopScrollTime(-getScrollY()));
                } else { // 停留在刷新处
                    mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() + mHeaderViewHeight), calculateTopScrollTime(-(getScrollY() + mHeaderViewHeight)));
                }
            }

            // 此时判断是否是 FooterView 处理
            if (!mLoading && getScrollY() > 0) {
//                Log.e("NestedRefreshLayout", "onStopNestedScroll=====3");
                if (mFooterViewHeight <= getScrollY()) { // 达到加载要求
                    if (noMoreData) { // 没有更多数据
                        updateStatus(RefreshState.FOOTER_NO_MORE);
                        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), calculateBottomScrollTime(getScrollY()));
                    } else {
                        // 松手后,向下滑动
                        mLoading = true;
                        updateStatus(RefreshState.FOOTER_LOADING);
                        mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() - mFooterViewHeight), calculateBottomScrollTime(getScrollY() - mFooterViewHeight));
                    }
                } else {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), calculateBottomScrollTime(getScrollY()));
                }
            } else if (mLoading && getScrollY() > 0) { // 加载时往下滑动
//                Log.e("NestedRefreshLayout", "onStopNestedScroll=====4");
                if (getScrollY() < mFooterViewHeight) { // 隐藏 FooterView
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), calculateBottomScrollTime(getScrollY()));
                } else { // 停留在加载处
                    mScroller.startScroll(0, getScrollY(), 0, mFooterViewHeight - getScrollY(), calculateBottomScrollTime(getScrollY() - mFooterViewHeight));
                }
            }
            invalidate();
        } else if (0 != getScrollY()) {
//            Log.e("NestedRefreshLayout", "onStopNestedScroll=====5");
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), getScrollY() > 0 ? calculateBottomScrollTime(getScrollY()) : calculateTopScrollTime(-getScrollY()));
//            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 100);
            invalidate();
        }
    }

    //////////////////////////////////////                         ////////////////////////////////////////
    //////////////////////////////////////  NestedScrollingParent  ////////////////////////////////////////
    //////////////////////////////////////                         ////////////////////////////////////////


    //////////////////////////////////////                         ////////////////////////////////////////
    //////////////////////////////////////  NestedScrollingChild  ////////////////////////////////////////
    //////////////////////////////////////                         ////////////////////////////////////////

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return mNestedScrollingChildHelper.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        mNestedScrollingChildHelper.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return mNestedScrollingChildHelper.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    /**
     * 当子控件产生fling滑动时，判断父控件是否处拦截fling，如果父控件处理了fling，那子控件就没有办法处理fling了。
     *
     * @param target    具体嵌套滑动的那个子类
     * @param velocityX 水平方向上的速度 velocityX > 0  向左滑动，反之向右滑动
     * @param velocityY 竖直方向上的速度 velocityY > 0  向上滑动，反之向下滑动
     * @return 父控件是否拦截该fling
     */
    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
//        Log.e("NestedRefreshLayout", "onNestedPreFling=====velocity: " + ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity() + " =====velocityY: " + velocityY);
        mFlingScroll = dispatchNestedPreFling(velocityX, velocityY);
        if (getScrollY() < 0 && mHeaderViewHeight <= -getScrollY() && velocityY >= mRequiredVelocityY * 4) { // 达到刷新要求
//            Log.e("NestedRefreshLayout", "onNestedPreFling===== 1 -->mRefreshing: " + mRefreshing + " ====mLoading: " + mLoading + " ====getScrollY: " + getScrollY());
//            parentFling(velocityY);
            mFlingScroll = true;
        } else if (getScrollY() > 0 && mFooterViewHeight <= getScrollY() && -velocityY >= mRequiredVelocityY * 2) { // 达到加载要求
//            Log.e("NestedRefreshLayout", "onNestedPreFling===== 2 -->mRefreshing: " + mRefreshing + " ====mLoading: " + mLoading + " ====getScrollY: " + getScrollY());
            mFlingScroll = true;
        }
//        Log.e("NestedRefreshLayout", "onNestedPreFling===== 3 -->velocityY: " + velocityY + " ====getScrollY: " + getScrollY() + " ===mFlingScroll: " + mFlingScroll);
        return mFlingScroll;
    }


    /**
     * 当父控件不拦截该fling,那么子控件会将fling传入父控件
     *
     * @param target    具体嵌套滑动的那个子类
     * @param velocityX 水平方向上的速度 velocityX > 0  向左滑动，反之向右滑动
     * @param velocityY 竖直方向上的速度 velocityY > 0  向上滑动，反之向下滑动
     * @param consumed  子控件是否可以消耗该fling，也可以说是子控件是否消耗掉了该fling
     * @return 父控件是否消耗了该fling
     */
    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
//        Log.e("NestedRefreshLayout", "onNestedFling=====");
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    //////////////////////////////////////                         ////////////////////////////////////////
    //////////////////////////////////////  NestedScrollingChild  ////////////////////////////////////////
    //////////////////////////////////////                         ////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void computeScroll() {
//		Log.e("NestedRefreshLayout", "computeScroll====mScroller.getCurrX: " + mScroller.getCurrX() + " =====mScroller.getCurrY: " + mScroller.getCurrY());
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
        if (mChildScrollUpCallback != null) {
            return !mChildScrollUpCallback.canChildScrollUp(this, mTargetView);
        }
        return !mTargetView.canScrollVertically(-1);
    }

    /**
     * canScrollVertically(1)的值表示是否能向上滚动，true表示能滚动，false表示已经滚动到底部
     */
    private boolean canChildScrollDown() {
        return !mTargetView.canScrollVertically(1);
    }

    /**
     * SwipeRefreshLayout
     * 对 嵌套 View 的滑动逻辑的处理
     */
    public interface OnChildScrollUpCallback {
        boolean canChildScrollUp(NestedRefreshLayout parent, @Nullable View child);
    }

    @Deprecated
    public void setOnChildScrollUpCallback(@Nullable OnChildScrollUpCallback callback) {
        mChildScrollUpCallback = callback;
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

    private void updateStatus(int status) {
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
        setFinishRefresh(isSuccess, 800);
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
     * 需要等到 动画 结束后再将刷新状态回位, 在刷新结束后调用此方法, 在headerView动画结束后调用 {@link #setFinishRefreshByHeaderAnimatorEnd}
     * @param isSuccess
     */
    public void setFinishRefreshBeforeAnimator(boolean isSuccess){
        isRefreshSuccess = isSuccess;
        updateStatus(isRefreshSuccess ? RefreshState.HEADER_COMPLETED : RefreshState.HEADER_FAILURE);
    }

    /**
     * 在 HeaderView 动画结束后,如果调用过 {@link #setFinishRefreshBeforeAnimator } 需要 在 HeaderView 动画结束后的调用此方法,否则刷新头不会回位
     * eg: AnimatorListenerAdapter.onAnimationEnd() 中调用
     * @param delay --> 延迟关闭动画 ,建议最小 800ms
     */
    public void setFinishRefreshByHeaderAnimatorEnd(long delay){
        postDelayed(refreshAction, delay);
    }

    /**
     * 加载完成
     *
     * @param isSuccess --> 可以根据这个值,设置加载成功或者失败
     */
    public void setFinishLoad(boolean isSuccess) {
        setFinishLoad(isSuccess, 800);
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
        postDelayed(loadAction, delay);
    }

    /**
     * 设置是否有更多数据
     *
     * @param noMoreData --> 默认是 false,即有更多数据
     */
    public void setNoMoreData(boolean noMoreData) {
        this.noMoreData = noMoreData;
        postDelayed(noDataAction, (long) (mScrollerMoveTime / mLoadHeight));
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

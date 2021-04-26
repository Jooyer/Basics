package cn.lvsong.lib.library.banner;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * date:2021/4/23 16:09
 * author:Jooyer
 * desc: 参考 ViewPager2 中 ScrollEventAdapter
 */
public abstract class BannerScrollAdapter extends RecyclerView.OnScrollListener {
    @Retention(SOURCE)
    @IntDef({STATE_IDLE, STATE_IN_PROGRESS_MANUAL_DRAG, STATE_IN_PROGRESS_SMOOTH_SCROLL,
            STATE_IN_PROGRESS_IMMEDIATE_SCROLL, STATE_IN_PROGRESS_FAKE_DRAG})
    private @interface AdapterState {
    }

    @Retention(SOURCE)
    @IntDef({SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING})
    public @interface ScrollState {
    }

    public static final int SCROLL_STATE_IDLE = 0;

    public static final int SCROLL_STATE_DRAGGING = 1;

    public static final int SCROLL_STATE_SETTLING = 2;

    private static final int STATE_IDLE = 0;
    private static final int STATE_IN_PROGRESS_MANUAL_DRAG = 1;
    private static final int STATE_IN_PROGRESS_SMOOTH_SCROLL = 2;
    private static final int STATE_IN_PROGRESS_IMMEDIATE_SCROLL = 3;
    private static final int STATE_IN_PROGRESS_FAKE_DRAG = 4;

    private static final int NO_POSITION = -1;

    private final @NonNull
    RecyclerView mRecyclerView;
    private final @NonNull
    HorizontalLayoutManager mLayoutManager;

    private @AdapterState
    int mAdapterState;
    private @ScrollState
    int mScrollState;
    private final ScrollEventValues mScrollValues;
    private int mDragStartPosition;
    private int mTarget;
    private boolean mDispatchSelected;
    private boolean mScrollHappened;
    private boolean mDataSetChangeHappened;

    BannerScrollAdapter(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mLayoutManager = (HorizontalLayoutManager) mRecyclerView.getLayoutManager();
        mScrollValues = new ScrollEventValues();
        resetState();
    }

    private void resetState() {
        mAdapterState = STATE_IDLE;
        mScrollState = SCROLL_STATE_IDLE;
        mScrollValues.reset();
        mDragStartPosition = NO_POSITION;
        mTarget = NO_POSITION;
        mDispatchSelected = false;
        mScrollHappened = false;
        mDataSetChangeHappened = false;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        if ((mAdapterState != STATE_IN_PROGRESS_MANUAL_DRAG
                || mScrollState != SCROLL_STATE_DRAGGING)
                && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            startDrag(false);
            return;
        }

        if (isInAnyDraggingState() && newState == RecyclerView.SCROLL_STATE_SETTLING) {
            if (mScrollHappened) {
                dispatchStateChanged(SCROLL_STATE_SETTLING);
                mDispatchSelected = true;
            }
            return;
        }

        if (isInAnyDraggingState() && newState == RecyclerView.SCROLL_STATE_IDLE) {
            boolean dispatchIdle = false;
            updateScrollEventValues();
            if (!mScrollHappened) {
                if (mScrollValues.mPosition != RecyclerView.NO_POSITION) {
                    dispatchScrolled(mScrollValues.mPosition, 0f, 0);
                }
                dispatchIdle = true;
            } else if (mScrollValues.mOffsetPx == 0) {
                dispatchIdle = true;
                if (mDragStartPosition != mScrollValues.mPosition) {
                    dispatchSelected(mScrollValues.mPosition);
                }
            }
            if (dispatchIdle) {
                dispatchStateChanged(SCROLL_STATE_IDLE);
                resetState();
            }
        }

        if (mAdapterState == STATE_IN_PROGRESS_SMOOTH_SCROLL
                && newState == RecyclerView.SCROLL_STATE_IDLE && mDataSetChangeHappened) {
            updateScrollEventValues();
            if (mScrollValues.mOffsetPx == 0) {
                if (mTarget != mScrollValues.mPosition) {
                    dispatchSelected(
                            mScrollValues.mPosition == NO_POSITION ? 0 : mScrollValues.mPosition);
                }
                dispatchStateChanged(SCROLL_STATE_IDLE);
                resetState();
            }
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        mScrollHappened = true;
        updateScrollEventValues();

        if (mDispatchSelected) {
            mDispatchSelected = false;
            boolean scrollingForward = dy > 0 || (dy == 0 && dx < 0 && mLayoutManager.getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL);

            mTarget = scrollingForward && mScrollValues.mOffsetPx != 0
                    ? mScrollValues.mPosition + 1 : mScrollValues.mPosition;
            if (mDragStartPosition != mTarget) {
                dispatchSelected(mTarget);
            }
        } else if (mAdapterState == STATE_IDLE) {
            int position = mScrollValues.mPosition;
            dispatchSelected(position == NO_POSITION ? 0 : position);
        }

        dispatchScrolled(mScrollValues.mPosition == NO_POSITION ? 0 : mScrollValues.mPosition,
                mScrollValues.mOffset, mScrollValues.mOffsetPx);

        if ((mScrollValues.mPosition == mTarget || mTarget == NO_POSITION)
                && mScrollValues.mOffsetPx == 0 && !(mScrollState == SCROLL_STATE_DRAGGING)) {
            dispatchStateChanged(SCROLL_STATE_IDLE);
            resetState();
        }
    }

    /**
     * Calculates the current position and the offset (as a percentage and in pixels) of that
     * position from the center.
     */

    private void updateScrollEventValues() {
        mScrollValues.mPosition =  getPosition();
        if (mScrollValues.mPosition == RecyclerView.NO_POSITION) {
            mScrollValues.reset();
            return;
        }

        View firstVisibleView = mLayoutManager.findViewByPosition(mScrollValues.mPosition);
        if (firstVisibleView == null) {
            mScrollValues.reset();
            return;
        }

        int leftDecorations = mLayoutManager.getLeftDecorationWidth(firstVisibleView);
        int rightDecorations = mLayoutManager.getRightDecorationWidth(firstVisibleView);

        ViewGroup.LayoutParams params = firstVisibleView.getLayoutParams();
        if (params instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) params;
            leftDecorations += margin.leftMargin;
            rightDecorations += margin.rightMargin;
        }

        int decoratedWidth = firstVisibleView.getWidth() + leftDecorations + rightDecorations;
        int start, sizePx;
        sizePx = decoratedWidth;
        start = firstVisibleView.getLeft() - leftDecorations - mRecyclerView.getPaddingLeft();
        mScrollValues.mOffsetPx = Math.abs(start);
        mScrollValues.mOffset = sizePx == 0 ? 0 : (float) mScrollValues.mOffsetPx / sizePx;
    }

    private void startDrag(boolean isFakeDrag) {
        mAdapterState = isFakeDrag ? STATE_IN_PROGRESS_FAKE_DRAG : STATE_IN_PROGRESS_MANUAL_DRAG;
        if (mTarget != NO_POSITION) {
            mDragStartPosition = mTarget;
            mTarget = NO_POSITION;
        } else if (mDragStartPosition == NO_POSITION) {
            mDragStartPosition = getPosition();
        }
        dispatchStateChanged(SCROLL_STATE_DRAGGING);
    }

    private boolean isInAnyDraggingState() {
        return mAdapterState == STATE_IN_PROGRESS_MANUAL_DRAG
                || mAdapterState == STATE_IN_PROGRESS_FAKE_DRAG;
    }


    private void dispatchStateChanged(@ScrollState int state) {
        if (mAdapterState == STATE_IN_PROGRESS_IMMEDIATE_SCROLL
                && mScrollState == SCROLL_STATE_IDLE) {
            return;
        }
        if (mScrollState == state) {
            return;
        }

        mScrollState = state;
        onPageScrollStateChanged(state);
    }

    private void dispatchSelected(int target) {
        onPageSelected(target);
    }

    private void dispatchScrolled(int position, float offset, int offsetPx) {
        onPageScrolled(position, offset, offsetPx);
    }

    private int getPosition() {
        return ((RecyclerView.LayoutParams) mRecyclerView.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
    }

    private static final class ScrollEventValues {
        int mPosition;
        float mOffset;
        int mOffsetPx;

        ScrollEventValues() {
        }

        void reset() {
            mPosition = RecyclerView.NO_POSITION;
            mOffset = 0f;
            mOffsetPx = 0;
        }
    }


    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    public abstract void onPageScrolled(int position, float positionOffset, @Px int positionOffsetPixels);

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    public abstract void onPageSelected(int position);

    /**
     * Called when the scroll state changes. Useful for discovering when the user begins
     * dragging, when a fake drag is started, when the pager is automatically settling to the
     * current page, or when it is fully stopped/idle. {@code state} can be one of {@link
     * #SCROLL_STATE_IDLE}, {@link #SCROLL_STATE_DRAGGING} or {@link #SCROLL_STATE_SETTLING}.
     */
    public void onPageScrollStateChanged(@ScrollState int state) {

    }


}

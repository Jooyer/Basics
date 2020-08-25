package cn.lvsong.lib.library.topmenu;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.lvsong.lib.library.R;


/**
 * 封装一个PopupWindow 实现类似QQ,支付宝等右上角弹框效果
 * Created by Jooyer on 2017/2/10
 * <p>
 * mTopMenu = TopMenu(this@ChatActivity, adapter)
 * .setWidth(DensityUtil.dp2px(125F).toInt())
 * .setHeight(DensityUtil.dp2px(124F).toInt())
 * .setShowBackground(false)
 * // 使得弹框右侧距离屏幕间隔, 如果间隔够了,箭头位置还没有对准控件中间,可以在BubbleRecyclerView所在布局中使用 brv_arrow_offset
 * .setPopupXOffset(-DensityUtil.dp2px(2F).toInt())
 * // 使得弹框上下偏移
 * .setPopupYOffset(-DensityUtil.dp2px(5F).toInt())
 * .setItemDecoration(itemDecoration)
 * <p>
 * mTopMenu.show(it, null, null)
 */
public class TopMenu {
    private static final String TAG = "TopRightMenu";
    private static final int DEFAULT_AMEND = 200;
    private Context mContext;

    private PopupWindow mPopupWindow;
    private BubbleRecyclerView mRecyclerView;
    private View mParent;

    private RecyclerView.Adapter mMenuAdapter;

    /**
     * 弹窗默认高度
     */
    private static final int DEFAULT_HEIGHT = 480;
    /**
     * 弹窗默认高度
     */
    private static final int DEFAULT_WIDTH = 320;
    private int mPopupHeight = DEFAULT_HEIGHT;
    private int mPopupWidth = DEFAULT_WIDTH;

    /**
     * 默认显示背景 --> 背景变暗
     */
    private boolean isShowBackground = true;

    /**
     * 默认显示动画
     */
    private boolean isShowAnimationStyle = true;

    /**
     * 默认弹出或者关闭动画
     */
    private static final int DEFAULT_ANIM_STYLE = R.style.TopMenu_Anim;
    /**
     * 动画ID
     */
    private int mAnimationStyle;

    /**
     * 默认的透明度值
     */
    private float mAlpha = 0.7f;
    /**
     * 弹框在Y轴偏移量,正值向下偏移,反之向上
     */
    private int mPopupYOffset = 0;
    /**
     * 弹框偏移距离
     */
    private int mPopupXOffset = 0;


    /**
     * 分割线
     */
    private RecyclerView.ItemDecoration mItemDecoration;

    public TopMenu(Context context, RecyclerView.Adapter menuAdapter) {
        mContext = context;
        mMenuAdapter = menuAdapter;
        init();
    }

    private void init() {
        mParent = LayoutInflater.from(mContext).inflate(R.layout.menu_publish_dynamic, null);
        mRecyclerView = (BubbleRecyclerView) mParent.findViewById(R.id.rv_menu_publish_dynamic);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }


    /**
     * 设置背景颜色变化动画
     *
     * @param from     --> 开始值
     * @param to       --> 结束值
     * @param duration --> 持续时间
     */
    private void setBackgroundAlpha(float from, float to, int duration) {
        final WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float) animation.getAnimatedValue();
                ((Activity) mContext).getWindow().setAttributes(lp);
            }
        });
        animator.start();
    }

    /**
     * 确定 弹框的位置
     */
    private int[] reviseFrameAndOrigin(View anchor, Rect frame, Point origin) {
        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        if (origin.x < 0 || origin.y < 0) {
            origin.set(anchor.getWidth() >> 1, anchor.getHeight() >> 1);
        }
        if (frame.isEmpty() || !frame.contains(origin.x + location[0], origin.y + location[1])) {
            anchor.getWindowVisibleDisplayFrame(frame);
        }
        return location;
    }

    private PopupWindow getPopupWindow() {
        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setContentView(mParent);
        mPopupWindow.setWidth(mPopupWidth);
        mPopupWindow.setHeight(mPopupHeight + mRecyclerView.getNotAvailableSize());
        if (isShowAnimationStyle)
            mPopupWindow.setAnimationStyle(mAnimationStyle <= 0 ? DEFAULT_ANIM_STYLE : mAnimationStyle);

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isShowBackground)
                    setBackgroundAlpha(mAlpha, 1f, 300);
            }
        });
        // 防止重复添加
        if (mRecyclerView.getItemDecorationCount() > 0) {
            mRecyclerView.removeItemDecorationAt(0);
        }
        if (null != mItemDecoration) {
            mRecyclerView.addItemDecoration(mItemDecoration);
        }
        mRecyclerView.setAdapter(mMenuAdapter);
        return mPopupWindow;
    }

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 设置宽度
     */
    public TopMenu setWidth(int width) {
        if (width > 0) {
            this.mPopupWidth = width;
        } else {
            throw new IllegalArgumentException("宽度不能为空,且必须大于0!");
        }
        return this;
    }

    /**
     * 设置高度
     */
    public TopMenu setHeight(int height) {
        if (height > 0) {
            this.mPopupHeight = height;
        }
        return this;
    }

    /**
     * 设置弹窗偏X方向移距离,默认0
     */
    public TopMenu setPopupXOffset(int popupOffset) {
        mPopupXOffset = popupOffset;
        return this;
    }

    /**
     * 设置弹窗偏Y方向移距离,默认0
     */
    public TopMenu setPopupYOffset(int yOffset) {
        mPopupYOffset = yOffset;
        return this;
    }

    /**
     * 设置背景是否变暗,默认true
     */
    public TopMenu setBackDark(boolean isShowBackground) {
        this.isShowBackground = isShowBackground;
        return this;
    }

    /**
     * 设置是否显示动画,默认true,如果没有调用 setAnimationStyle() 则使用默认动画
     */
    public TopMenu setShowAnimationStyle(boolean isShowAnimationStyle) {
        this.isShowAnimationStyle = isShowAnimationStyle;
        return this;
    }

    /**
     * 设置动画
     */
    public TopMenu setAnimationStyle(int animationStyle) {
        this.mAnimationStyle = animationStyle;
        return this;
    }

    /**
     * 设置箭头偏移量
     *
     */
    public TopMenu setArrowOffset(float offset) {
        if (mRecyclerView != null) {
            ((BubbleRecyclerView) mRecyclerView).setArrowOffset(offset);
        }
        return this;
    }

    /**
     * 设置分割线
     */
    public TopMenu setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mItemDecoration = itemDecoration;
        return this;
    }

    /**
     * 显示弹框
     */
    public TopMenu showAsDropDown(View anchor) {
        showAsDropDown(anchor, 0, 0);
        return this;
    }

    /**
     * 显示弹框
     */
    public TopMenu showAsDropDown(View anchor, int offsetX, int offsetY) {
        if (null == mPopupWindow) {
            mPopupWindow = getPopupWindow();
        }

        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(anchor, offsetX, offsetY);
            if (isShowBackground)
                setBackgroundAlpha(1f, mAlpha, 300);
        }

        return this;
    }

    /**
     * 显示 PopupWindow
     *
     * @return https://blog.csdn.net/xiey94/article/details/93174035 --> 对showAsDropDown解析
     */
    public TopMenu show(View anchor, Rect frame, Point origin) {
        if (null == mPopupWindow) {
            mPopupWindow = getPopupWindow();
        }

        if (null == frame) frame = new Rect();
        if (null == origin) origin = new Point(-1, -1);

        int[] location = reviseFrameAndOrigin(anchor, frame, origin);
        int x = location[0];
        int y = location[1];
        int width = anchor.getWidth();
        int height = anchor.getHeight();

        int contentHeight = mPopupWindow.getContentView().getMeasuredHeight();
        if (!mPopupWindow.isShowing()) {
            if (y + height + contentHeight < frame.bottom) {
                mPopupWindow.showAsDropDown(anchor, -mPopupWidth + width + mPopupXOffset, mPopupYOffset);
            }
            if (isShowBackground) {
                setBackgroundAlpha(1f, mAlpha, 300);
            }
        }
        return this;
    }

    /**
     * 取消弹框
     */
    public void dismiss() {
        if (null != mPopupWindow && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
    }


}

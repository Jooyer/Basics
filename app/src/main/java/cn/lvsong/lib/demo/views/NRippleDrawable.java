package cn.lvsong.lib.demo.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 来自  https://github.com/EshelGuo/RippleDrawableDemo (https://blog.csdn.net/qq_27070117/article/details/107879004)
 * 自定义ViewGroup实现 https://github.com/ChenLittlePing/RippleLayout
 * Desc: 波纹选中 Drawable
 * Author: Jooyer
 * Date: 2021-05-18
 * Time: 20:19
 */
public class NRippleDrawable extends Drawable {

    private static final int STATE_IDLE = 0;
    private static final int STATE_ENTER = 1;
    private static final int STATE_EXIT = 2;

    private final Paint mPaint;

    private final PointF mPressedPointF = new PointF();
    private final PointF mCurrentPointF = new PointF();

    private int progress = 0;

    private final int maxProgress = 100;

    private int mState = STATE_IDLE;

    private boolean pendingExit;

    private long animationTime = 300;

    private float mMaxRadius;
    private ValueAnimator mRunningAnimator;
    private final int mRealAlpha;

    public NRippleDrawable() {
        this(1000);
    }


    public NRippleDrawable(long animationTime) {
        this.animationTime = animationTime;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#CC6AA188"));
        mRealAlpha = mPaint.getAlpha();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mState == STATE_ENTER) {
            if (mPaint.getAlpha() != mRealAlpha) {
                mPaint.setAlpha(mRealAlpha);
            }
            canvas.drawCircle(mPressedPointF.x, mPressedPointF.y, mMaxRadius * progress / maxProgress, mPaint);
        } else if (mState == STATE_EXIT) {
            mPaint.setAlpha(mRealAlpha * progress / maxProgress);
            canvas.drawRect(getBounds(), mPaint);
        }else {
            mPaint.setAlpha(200);
            canvas.drawRect(getBounds(), mPaint);
        }
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        boolean enable = false;
        boolean pressed = false;

        for (int state : stateSet) {
            switch (state) {
                case android.R.attr.state_pressed:
                    pressed = true;
                    break;
                case android.R.attr.state_enabled:
                    enable = true;
                    break;
            }
        }

        if (!enable) {
            return false;
        }

        if (pressed) {
            enter();
            return true;
        } else if (mState == STATE_ENTER) {
            exit();
            return true;
        } else {
            return false;
        }
    }

    private void exit() {
        if (progress != maxProgress && mState == STATE_ENTER) {
            pendingExit = true;
        } else {
            mState = STATE_EXIT;
            startExitAnimation();
        }
    }

    private void startExitAnimation() {
        if (mRunningAnimator != null && mRunningAnimator.isRunning()) {
            mRunningAnimator.cancel();
        }

        mRunningAnimator = ValueAnimator.ofInt(progress, 0);
        mRunningAnimator.setInterpolator(new LinearInterpolator());
        mRunningAnimator.setDuration(animationTime);
        mRunningAnimator.addUpdateListener(animation -> {
            progress = (int) animation.getAnimatedValue();
            invalidateSelf();
        });
        mRunningAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mState = STATE_IDLE;
            }
        });
        mRunningAnimator.start();
    }

    private void enter() {
        mState = STATE_ENTER;
        progress = 0;
        mPressedPointF.set(mCurrentPointF);
        Rect bounds = getBounds();
        mMaxRadius = Math.max(bounds.width(), bounds.height());
        startEnterAnimation();
    }

    private void startEnterAnimation() {
        mRunningAnimator = ValueAnimator.ofInt(progress, maxProgress);
        mRunningAnimator.setInterpolator(new LinearInterpolator());
        mRunningAnimator.setDuration(animationTime);
        mRunningAnimator.addUpdateListener(animation -> {
            progress = (int) animation.getAnimatedValue();
            invalidateSelf();
        });
        mRunningAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (pendingExit) {
                    pendingExit = false;
                    mState = STATE_EXIT;
                    startExitAnimation();
                }
            }
        });
        mRunningAnimator.start();
    }

    @Override
    public void setHotspot(float x, float y) {
        mCurrentPointF.set(x, y);
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}

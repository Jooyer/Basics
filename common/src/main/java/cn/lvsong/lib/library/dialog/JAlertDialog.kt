package cn.lvsong.lib.library.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import cn.lvsong.lib.library.R


/**
 * Created by Jooyer on 2017/7/22
 */

/*

          val mDialog = JAlertDialog.Builder(this@ADActivity)
                            .setCancelable(false)
                            .setContentView(R.layout.item)
                            .setHasAnimation(true)
                            .setFromBottom()
                            .setOnJAlertDialogCLickListener(object : OnJAlertDialogCLickListener {
                                override fun onClick(view: View, position: Int) {

                                }
                            }).show()


// 也可以 把 .create()  换成  .show(),则直接显示 ...

 */

class JAlertDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    private val mAlert: JAlertController = JAlertController(this, window!!)

    class Builder @JvmOverloads constructor(
        context: Context,
        @StyleRes themeRes: Int = R.style.JDialogStyle
    ) {
        private val mAlertParams: JAlertController.AlertParams =
            JAlertController.AlertParams(context, themeRes)

        /**
         * 设置Dialog的视图
         */
        fun setContentView(view: View): Builder {
            mAlertParams.mView = view
            mAlertParams.mViewLayoutResId = 0
            return this
        }

        /**
         * 设置Dialog的视图
         */
        fun setContentView(@IdRes layoutResId: Int): Builder {
            mAlertParams.mView = null
            mAlertParams.mViewLayoutResId = layoutResId
            return this
        }

        /**
         * 设置在弹框外面是否可以点击取消
         */
        fun setCancelable(cancelable: Boolean): Builder {
            mAlertParams.mCancelable = cancelable
            return this
        }

        /**
         * 放入TextView的id 和 在此控件显示的内容
         */
        fun setText(@IdRes viewId: Int, text: CharSequence): Builder {
            mAlertParams.mTextArr.put(viewId, text)
            return this
        }

        /**
         * 距离顶部距离,此时则弹框顶部显示
         *
         * @param marginTop --> 单位 px
         */
        fun setFromTop(marginTop: Int): Builder {
            mAlertParams.mGravity = Gravity.TOP
            mAlertParams.mMarginTop = marginTop
            return this
        }

        /**
         * 设置Dialog在底部显示
         */
        fun setFromBottom(): Builder {
            mAlertParams.mGravity = Gravity.BOTTOM
            return this
        }

        /**
         * 设置Dialog动画
         */
        fun setAnimation(@StyleRes styleAnim: Int): Builder {
            mAlertParams.mAnimation = styleAnim
            return this
        }

        /**
         * 设置是否使用动画,默认是true
         */
        fun setHasAnimation(hasAnimation: Boolean): Builder {
            mAlertParams.mHasAnimation = hasAnimation
            return this
        }

        /**
         * 设置Dialog宽度占满
         */
        fun setFullWidth(): Builder {
            mAlertParams.mWidth = ViewGroup.LayoutParams.MATCH_PARENT
            return this
        }

        /**
         * 设置Dialog宽高
         */
        fun setWidthAndHeight(width: Int, height: Int): Builder {
            mAlertParams.mWidth = width
            mAlertParams.mHeight = height
            return this
        }

        /**
         * 设置Dialog点击View
         */
        fun setOnClick(@IdRes viewId: Int): Builder {
            mAlertParams.mClickArr.put(mAlertParams.mClickArr.size(), viewId)
            return this
        }

        /**
         * 设置点击事件
         */
        fun setOnJAlertDialogCLickListener(onJAlertDialogCLickListener: OnJAlertDialogCLickListener): Builder {
            mAlertParams.mOnJAlertDialogCLickListener = onJAlertDialogCLickListener
            return this
        }

        /**
         * 设置取消Dialog事件
         */
        fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): Builder {
            mAlertParams.mOnCancelListener = onCancelListener
            return this
        }

        /**
         * 设置Dialog消失回调
         */
        fun setOnOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): Builder {
            mAlertParams.mOnDismissListener = onDismissListener
            return this
        }

        /**
         * 设置Dialog OnKeyListener
         */
        fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener): Builder {
            mAlertParams.mOnKeyListener = onKeyListener
            return this
        }

        /**
         * 生成Dialog返回
         */
        fun create(): JAlertDialog {
            val dialog = JAlertDialog(mAlertParams.mContext, mAlertParams.mThemeRes)
            mAlertParams.apply(dialog.mAlert)
            dialog.setCancelable(mAlertParams.mCancelable)
            if (mAlertParams.mCancelable) {
                dialog.setCanceledOnTouchOutside(true)
            }
            dialog.setOnCancelListener(mAlertParams.mOnCancelListener)
            dialog.setOnDismissListener(mAlertParams.mOnDismissListener)
            if (mAlertParams.mOnKeyListener != null) {
                dialog.setOnKeyListener(mAlertParams.mOnKeyListener)
            }
            return dialog
        }

        /**
         * 显示Dialog并返回
         */
        fun show(): JAlertDialog {
            val dialog = create()
            dialog.show()
            return dialog
        }
    }

    /**
     * 延迟显示,必须 Dialog 来调用
     * @param view --> 用来发送延时 Runnable
     * @param delay --> 单位毫秒
     */
    fun delayShow(view: View, delay: Int) {
        view.postDelayed({ show() }, delay.toLong())
    }

    /**
     * 延迟关闭,必须 Dialog 来调用(慎用)
     * @param view --> 用来发送延时 Runnable
     * @param delay --> 单位毫秒
     */
    fun delayDismiss(view: View, delay: Int) {
        view.postDelayed({ dismiss() }, delay.toLong())
    }

}

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

    fun delayShow(delay: Int) {
        if (null != window)
            window!!.decorView.postDelayed({ show() }, delay.toLong())
    }

    class Builder @JvmOverloads constructor(context: Context,
                                            @StyleRes themeRes: Int = R.style.JDialogStyle) {
        private val mAlertParams: JAlertController.AlertParams = JAlertController.AlertParams(context, themeRes)

        fun setContentView(view: View): Builder {
            mAlertParams.mView = view
            mAlertParams.mViewLayoutResId = 0
            return this
        }

        fun setContentView(layoutResId: Int): Builder {
            mAlertParams.mView = null
            mAlertParams.mViewLayoutResId = layoutResId
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            mAlertParams.mCancelable = cancelable
            return this
        }


        fun setText(@IdRes viewId: Int, text: CharSequence): Builder {
            mAlertParams.mTextArr.put(viewId, text)
            return this
        }

        /**
         * 距离顶部距离
         *
         * @param marginTop --> 单位 px
         */
        fun setFromTop(marginTop: Int): Builder {
            mAlertParams.mGravity = Gravity.TOP
            mAlertParams.mMarginTop = marginTop
            return this
        }

        fun setFromBottom(): Builder {
            mAlertParams.mGravity = Gravity.BOTTOM
            return this
        }

        fun setAnimation(@StyleRes styleAnim: Int): Builder {
            mAlertParams.mAnimation = styleAnim
            return this
        }

        fun setHasAnimation(hasAnimation: Boolean): Builder {
            mAlertParams.mHasAnimation = hasAnimation
            return this
        }

        fun setFullWidth(): Builder {
            mAlertParams.mWidth = ViewGroup.LayoutParams.MATCH_PARENT
            return this
        }

        fun setWidthAndHeight(width: Int, height: Int): Builder {
            mAlertParams.mWidth = width
            mAlertParams.mHeight = height
            return this
        }

        fun setOnClick(@IdRes viewId: Int): Builder {
            mAlertParams.mClickArr.put(mAlertParams.mClickArr.size(), viewId)
            return this
        }

        fun setOnJAlertDialogCLickListener(onJAlertDialogCLickListener: OnJAlertDialogCLickListener): Builder {
            mAlertParams.mOnJAlertDialogCLickListener = onJAlertDialogCLickListener
            return this
        }

        fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): Builder {
            mAlertParams.mOnCancelListener = onCancelListener
            return this
        }

        fun setOnOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): Builder {
            mAlertParams.mOnDismissListener = onDismissListener
            return this
        }

        fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener): Builder {
            mAlertParams.mOnKeyListener = onKeyListener
            return this
        }


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

        fun show(): JAlertDialog {
            val dialog = create()
            dialog.show()
            return dialog
        }


    }

}

package cn.lvsong.lib.library.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.util.SparseArray
import android.util.SparseIntArray
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.StyleRes
import cn.lvsong.lib.library.R


/**
 * Created by Jooyer on 2017/7/22
 */

internal class JAlertController(val dialog: JAlertDialog, val window: Window) {

    /**
     * Dialog 主题,有一个默认主题
     */
    class AlertParams(var mContext: Context,
                      @param:StyleRes var mThemeRes: Int) {

        /**
         * 存放显示文本的控件和文本内容
         */
        var mTextArr = SparseArray<CharSequence>()
        /**
         * 存放点击事件的控件和监听
         */
        var mClickArr = SparseIntArray()

        /**
         * 点击空白是否可以取消,默认不可以
         */
        var mCancelable = false
        /**
         * Dialog 取消监听
         */
        var mOnCancelListener: DialogInterface.OnCancelListener? = null
        /**
         * Dialog 消失监听
         */
        var mOnDismissListener: DialogInterface.OnDismissListener? = null
        /**
         * Dialog 按键监听
         */
        var mOnKeyListener: DialogInterface.OnKeyListener? = null
        /**
         * Dialog 布局 View
         */
        var mView: View? = null
        /**
         * Dialog 布局 ID
         */
        var mViewLayoutResId: Int = 0
        var mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
        var mHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        var mMarginTop = 0
        var mGravity = Gravity.CENTER
        var mAnimation = R.style.JDialogAnimation
        var mHasAnimation = true
        var mOnJAlertDialogCLickListener: OnJAlertDialogCLickListener? = null

        /**
         * 设置参数
         */
        fun apply(alert: JAlertController) {
            var viewHelper: JDialogViewHelper? = null
            // 1. 设置布局
            if (0 != mViewLayoutResId) {
                viewHelper = JDialogViewHelper(mContext, mViewLayoutResId)
            }

            if (null != mView) {
                viewHelper = JDialogViewHelper(mContext, mView)
            }

            if (null == viewHelper) {
                throw IllegalArgumentException("请设置Dialog布局")
            }

            alert.dialog.setContentView(viewHelper.contentView)
            viewHelper.setOnJAlertDialogCLickListener(mOnJAlertDialogCLickListener)

            // 2. 设置文本
            run {
                var i = 0
                val len = mTextArr.size()
                while (i < len) {
                    viewHelper.setText(mTextArr.keyAt(i), mTextArr.valueAt(i))
                    i++
                }
            }

            // 3. 设置点击事件
            var i = 0
            val len = mClickArr.size()
            while (i < len) {
                viewHelper.setOnClick(mClickArr.keyAt(i), mClickArr.valueAt(i))
                i++
            }

            // 4. 设置dialog宽高动画等
            val window = alert.window
            window.setGravity(mGravity)
            window.setBackgroundDrawable(ColorDrawable(0x00000000))
            if (mHasAnimation) {
                window.setWindowAnimations(mAnimation)
            }
            val params = window.attributes
            params.y = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    mMarginTop.toFloat(), mContext.resources.displayMetrics).toInt()
            params.width = mWidth
            params.height = mHeight
            window.attributes = params
            alert.dialog.setOnCancelListener(mOnCancelListener)
            alert.dialog.setOnDismissListener(mOnDismissListener)

        }
    }


}

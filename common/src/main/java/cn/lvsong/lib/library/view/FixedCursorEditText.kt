package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatEditText

/**
 * https://www.jianshu.com/p/e3e4098f1c1d  --> 光标设置
 * 参考: https://www.jianshu.com/p/e64fbac202bb
 * Desc: Android 处理EditText光标显示在hint文字之前的问题,即输入之前光标在提示文本最前面,写入文字后光标跑到了输入文本后面,
 * 修改为输入前光标在提示文本末尾
 * Author: Jooyer
 * Date: 2020-02-28
 * Time: 9:48
 * 原理: 在初始化时拿到设置的hint保存起来，然后清空EditText本身的hint，最后在onDraw()方法中绘制自己的hint
 */
/*
   用法
       <cn.lvsong.lib.ui.define.FixedCursorEditText
           android:layout_width="280dp"
           android:layout_height="50dp"
           android:layout_marginTop="@dimen/padding_20"
           android:hint="Android 自定义View绘制箭头"
           android:gravity="center_vertical|right"
           android:background="@null"
           app:layout_constraintStart_toStartOf="parent"
           app:layout_constraintEnd_toEndOf="parent"
           app:layout_constraintTop_toBottomOf="@id/toolbar"
       />
 */
class FixedCursorEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {
    
    private var mHint: CharSequence? = null
    
    private var mHintPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    
    private var mCurHintTextColor = 0

    init {
        mHintPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mHintPaint.textSize = textSize
        mHintPaint.textAlign = Paint.Align.RIGHT
    }

    /**
     * 在绘制前获取原本设置的提示文本
     */
    override fun onPreDraw(): Boolean {
        if (null == mHint) { // 开始为null
            mHint = hint // 保存提示文本
            hint = "" // 首先绘制一次没有提示文本的情况
        }
        return super.onPreDraw()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (TextUtils.isEmpty(mHint) || !TextUtils.isEmpty(text)) {
            return
        }
        canvas.save()
        val hintTextColors = hintTextColors
        if (hintTextColors != null) {
            val color = hintTextColors.getColorForState(drawableState, 0)
            if (color != mCurHintTextColor) {
                mCurHintTextColor = color
               mHintPaint.color = color
            }
        }
        val fontMetrics =mHintPaint.fontMetricsInt
        val baseline = (height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top
        canvas.drawText(
            mHint!!, 0, mHint!!.length, (
                    width - paddingRight + scrollX).toFloat(),
            baseline.toFloat(),mHintPaint
        )
        canvas.restore()
    }
}
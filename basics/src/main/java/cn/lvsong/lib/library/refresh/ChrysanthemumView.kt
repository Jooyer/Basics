package cn.lvsong.lib.library.refresh

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import cn.lvsong.lib.library.R

/** https://www.jianshu.com/p/cb42e75711f1
 * Desc: 自定义 仿 IOS 菊花
 * Author: Jooyer
 * Date: 2018-09-15
 * Time: 22:37
 */

/* 用法:
<cn.lvsong.lib.library.refresh.ChrysanthemumView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginTop="10dp"
    app:chrysanthemum_view_color="@color/color_333333"
    app:chrysanthemum_view_width="@dimen/padding_10"
    app:chrysanthemum_view_height="@dimen/padding_6"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    />

 */

class ChrysanthemumView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var rectF: RectF

    private var paint = Paint()

    private var flowerRadius = 0 //半径,花瓣长度
    private var flowerHeight = 0 // 花瓣厚度

    private var count = 0

    private var run = true //动画控制

    init {
        initialize(attrs)
    }

    private fun initialize(attrs: AttributeSet) {
        paint = Paint()
        paint.isAntiAlias = true
        val array = context.obtainStyledAttributes(attrs, R.styleable.ChrysanthemumView)
        flowerRadius = array.getDimensionPixelOffset(
            R.styleable.ChrysanthemumView_chrysanthemum_view_width,
            dp2px(15F).toInt()
        )
        flowerHeight = array.getDimensionPixelOffset(
            R.styleable.ChrysanthemumView_chrysanthemum_view_height,
            dp2px(6F).toInt()
        )
        paint.color = array.getColor(
            R.styleable.ChrysanthemumView_chrysanthemum_view_color,
            ContextCompat.getColor(context, R.color.color_FFFFFF)
        )
        array.recycle()

        rectF = RectF((flowerRadius - dp2px(1F)), 0F, (flowerRadius + dp2px(1F)), flowerHeight * 1F)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(flowerRadius * 2, flowerRadius * 2)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.rotate((count * 30).toFloat(), flowerRadius.toFloat(), flowerRadius.toFloat())
        for (i in 0 until 12) {
            paint.alpha = 255 - i * 20
            canvas.drawRoundRect(rectF, 10f, 10f, paint)
            canvas.rotate(30f, flowerRadius.toFloat(), flowerRadius.toFloat())
        }
        count++
        if (run) {
            postInvalidateDelayed(100)
        }
    }

    private fun dp2px(def: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            def,
            context.resources.displayMetrics
        )
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (VISIBLE == visibility) {
            start()
        } else {
            stop()
        }
    }

    /**
     * 开始旋转
     */
    fun start() {
        if (!run) {
            postInvalidateDelayed(100)
            run = true
        }
    }

    /**
     * 暂停旋转,比如隐藏后
     */
    fun stop() {
        run = false
    }
}

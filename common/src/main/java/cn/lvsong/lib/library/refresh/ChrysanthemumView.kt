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
        android:layout_marginTop="@dimen/padding_20"
        android:layout_marginStart="@dimen/padding_20"
        app:cv_view_color="@color/color_E95C5B5B"
        app:cv_view_radius="@dimen/padding_15"
        app:cv_view_width="@dimen/padding_8"
        app:cv_view_height="@dimen/padding_3"
        app:cv_flower_count="10"
        />
 */

class ChrysanthemumView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var rectF: RectF

    private var paint = Paint()

    private var flowerRadius = 0 //半径,花瓣长度
    private var flowerWidth = 0F // 花瓣宽度
    private var flowerHeight = 0F // 花瓣厚度
    private var flowerCount = 12 // 花瓣数量

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
            R.styleable.ChrysanthemumView_cv_view_radius,
            dp2px(15F).toInt()
        )
        flowerWidth = array.getDimensionPixelOffset(
            R.styleable.ChrysanthemumView_cv_view_width,
            dp2px(6F).toInt()
        ).toFloat()

        flowerHeight = array.getDimensionPixelOffset(
            R.styleable.ChrysanthemumView_cv_view_height,
            dp2px(6F).toInt()
        ).toFloat()

        flowerCount =
            array.getInt(R.styleable.ChrysanthemumView_cv_flower_count, flowerCount)

        paint.color = array.getColor(
            R.styleable.ChrysanthemumView_cv_view_color,
            ContextCompat.getColor(context, R.color.color_FFFFFF)
        )
        array.recycle()
        rectF =
            RectF(flowerRadius - flowerHeight / 2, 0F, flowerRadius + flowerHeight / 2, flowerWidth)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(flowerRadius * 2, flowerRadius * 2)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.rotate(
            count * 360F / flowerCount,
            flowerRadius.toFloat(),
            flowerRadius.toFloat()
        )
        for (i in 0 until flowerCount) {
            paint.alpha = 255 - i * 20
            canvas.drawRoundRect(rectF, flowerHeight / 2, flowerHeight / 2, paint)
            canvas.rotate(360F / flowerCount, flowerRadius.toFloat(), flowerRadius.toFloat())
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
     * 开始旋转,如果控件可见就会自动执行,不必手动调用
     */
    fun start() {
        if (!run) {
            postInvalidateDelayed(100)
            run = true
        }
    }

    /**
     * 暂停旋转,如果控件不可见就会自动执行,不必手动调用
     */
    fun stop() {
        run = false
    }
}

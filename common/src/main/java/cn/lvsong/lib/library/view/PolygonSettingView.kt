package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import androidx.core.content.ContextCompat
import cn.lvsong.lib.library.R
import kotlin.math.cos
import kotlin.math.sin

/**
 * 来自: https://www.jianshu.com/p/471ac4eb1ab6
 * Desc: Android 自定义View 绘制六边形设置按钮
 * Author: Jooyer
 * Date: 2020-03-03
 * Time: 17:00
 */
class PolygonSettingView(context: Context, attrs: AttributeSet? = null) : View(context, attrs), Checkable {

    /**
     * View默认最小宽度
     */
    private val DEFAULT_MIN_WIDTH = dip2px(context, 40F)

    /**
     * 画笔
     */
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 控件宽
     */
    private var mViewWidth = 0

    /**
     * 控件高
     */
    private var mViewHeight = 0

    /**
     * 多边形的边数
     */
    private var mNum = 0

    /**
     * 最小的多边形的半径
     */
    private var mRadius = 0f

    /**
     * 360度对应的弧度（为什么2π就是360度？弧度的定义：弧长 / 半径，一个圆的周长是2πr，如果是一个360度的圆，它的弧长就是2πr，如果这个圆的半径r长度为1，那么它的弧度就是，2πr / r = 2π）
     */
    private val mPiDouble = 2 * Math.PI

    /**
     * 多边形中心角的角度（每个多边形的内角和为360度，一个多边形2个相邻角顶点和中心的连线所组成的角为中心角
     * 中心角的角度都是一样的，所以360度除以多边形的边数，就是一个中心角的角度），这里注意，因为后续要用到Math类的三角函数
     * Math类的sin和cos需要传入的角度值是弧度制，所以这里的中心角的角度，也是弧度制的弧度
     */
    private var mCenterAngle = 0f

    /**
     * 中心小圆的半径
     */
    private var mSmallCircleRadius = 0f

    /**
     * 线宽
     */
    private var mLineWidth = 0f

    /**
     * 是否被选中
     */
    private var isChecked = false


    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mNum = 6
        mLineWidth = DEFAULT_MIN_WIDTH / 18F
        //计算中心角弧度
        mCenterAngle = (mPiDouble / mNum).toFloat()
        mPaint.color = ContextCompat.getColor(context, android.R.color.holo_red_light)
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mLineWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        //计算最小的多边形的半径
        mRadius = mViewWidth.coerceAtMost(mViewHeight) / 2f * 0.95f
        //计算中心小圆的半径
        mSmallCircleRadius = mViewWidth.coerceAtMost(mViewHeight) / 2f * 0.3f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //将画布中心移动到中心点
        canvas.translate(mViewWidth / 2f, mViewHeight / 2f)
        //画小圆
        drawSmallCircle(canvas)
        //画多边形
        drawPolygon(canvas)
    }

    /**
     * 画小圆
     */
    private fun drawSmallCircle(canvas: Canvas) {
        canvas.drawCircle(0f, 0f, mSmallCircleRadius, mPaint)
    }

    /**
     * 画多边形
     */
    private fun drawPolygon(canvas: Canvas) {
        //多边形边角顶点的x坐标
        var pointX: Float
        //多边形边角顶点的y坐标
        var pointY: Float
        //总的圆的半径，就是全部多边形的半径之和
        val path = Path()
        //画前先重置路径
        path.reset()
        for (i in 1..mNum) {
            //cos三角函数，中心角的邻边 / 斜边，斜边的值刚好就是半径，cos值乘以斜边，就能求出邻边，而这个邻边的长度，就是点的x坐标
            pointX = (cos((i * mCenterAngle).toDouble()) * mRadius).toFloat()
            //sin三角函数，中心角的对边 / 斜边，斜边的值刚好就是半径，sin值乘以斜边，就能求出对边，而这个对边的长度，就是点的y坐标
            pointY = (sin((i * mCenterAngle).toDouble()) * mRadius).toFloat()
            //如果是一个点，则移动到这个点，作为起点
            if (i == 1) {
                path.moveTo(pointX, pointY)
            } else {
                //其他的点，就可以连线了
                path.lineTo(pointX, pointY)
            }
        }
        path.close()
        canvas.drawPath(path, mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(handleMeasure(widthMeasureSpec), handleMeasure(heightMeasureSpec))
    }

    private fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 处理MeasureSpec
     */
    private fun handleMeasure(measureSpec: Int): Int {
        var result = DEFAULT_MIN_WIDTH
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            //处理wrap_content的情况
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    /**
     * 设置是否选中
     */
    override fun setChecked(checked: Boolean) {
        isChecked = checked
    }

    /**
     * 是否选中
     */
    override fun isChecked(): Boolean {
        return isChecked
    }

    /**
     * 切换当前check状态
     */
    override fun toggle() {
        setChecked(!isChecked)
    }

}
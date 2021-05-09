package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import cn.lvsong.lib.library.R
import kotlin.math.cos
import kotlin.math.sin

/**
 * 来自: https://www.jianshu.com/p/bb27489385bd
 * Desc: Android 自定义View 绘制五角星
 * Author: Jooyer
 * Date: 2020-03-03
 * Time: 16:55
 */
/*
cn.lvsong.lib.library.view.StarsView
            android:id="@+id/sv_star"
            android:layout_width="@dimen/width_30"
            android:layout_height="@dimen/height_30"
            android:layout_marginTop="@dimen/padding_20"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@id/acc_test"
            app:stv_checked_color="@color/main_theme_color"
            app:stv_default_color="@color/color_2878FF"
            app:stv_edge_line_width="@dimen/padding_2"
            app:stv_num="5"
            app:stv_style="fill"
            />
 */
class StarsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Checkable {

    /**
     * View默认最小宽度
     */
    private val DEFAULT_MIN_WIDTH = 100

    /**
     * 风格，填满
     */
    private val STYLE_FILL = 1

    /**
     * 风格，描边
     */
    private val STYLE_STROKE = 2
    
    /**
     * 控件宽
     */
    private var mViewWidth = 0

    /**
     * 控件高
     */
    private var mViewHeight = 0

    /**
     * 外边大圆的半径
     */
    private var mOutCircleRadius = 0f

    /**
     * 里面小圆的的半径
     */
    private var mInnerCircleRadius = 0f

    /**
     * 画笔
     */
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 多少个角的星星
     */
    private var mAngleNum = 0

    /**
     * 星星的路径
     */
    private var mPath = Path()

    /**
     * 星星的默认颜色
     */
    private var mDefaultColor = 0

    /**
     * 星星的选中颜色
     */
    private var mCheckedColor = 0

    /**
     * 边的线宽
     */
    private var mEdgeLineWidth = 0f

    /**
     * 填充风格
     */
    private var mStyle = 0

    /**
     * 是否点击
     */
    private var isChecked = false


    init {
        initAttr(context, attrs, defStyleAttr)
        //取消硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        if (mStyle == STYLE_FILL) {
            mPaint.style = Paint.Style.FILL_AND_STROKE
        } else if (mStyle == STYLE_STROKE) {
            mPaint.style = Paint.Style.STROKE
        }
        mPaint.color = mDefaultColor
        mPaint.strokeWidth = mEdgeLineWidth
    }
    
    private fun initAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val defaultNum = 5
        val mineNum = 3
        val defaultEdgeLineWidth = dip2px(context, 1f).toFloat()
        val defaultStyle = STYLE_STROKE
        if (attrs != null) {
            val array =
                context.obtainStyledAttributes(attrs, R.styleable.StarsView, defStyleAttr, 0)
            mDefaultColor = array.getColor(R.styleable.StarsView_stv_default_color, Color.GREEN)
            mCheckedColor = array.getColor(R.styleable.StarsView_stv_checked_color, Color.RED)
            val num = array.getInt(R.styleable.StarsView_stv_num, defaultNum)
            mAngleNum = num.coerceAtLeast(mineNum)
            mEdgeLineWidth =
                array.getDimension(R.styleable.StarsView_stv_edge_line_width, defaultEdgeLineWidth)
            mStyle = array.getInt(R.styleable.StarsView_stv_style, defaultStyle)
            array.recycle()
        } else {
            mDefaultColor = Color.GREEN
            mCheckedColor = Color.RED
            mAngleNum = defaultNum
            mEdgeLineWidth = defaultEdgeLineWidth
            mStyle = defaultStyle
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        //计算外边大圆的半径
        mOutCircleRadius = Math.min(mViewWidth, mViewHeight) / 2f * 0.95f
        //计算里面小圆的的半径
        mInnerCircleRadius = Math.min(mViewWidth, mViewHeight) / 2f * 0.5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //将画布中心移动到中心点
        canvas.translate(mViewWidth / 2f, mViewHeight / 2f)
        //画星星
        drawStars(canvas)
    }

    /**
     * 画星星
     */
    private fun drawStars(canvas: Canvas) {
        //计算平均角度，例如360度分5份，每一份角都为72度
        val averageAngle = 360f / mAngleNum
        //计算大圆的外角的角度，从右上角为例计算，90度的角减去一份角，得出剩余的小角的角度，例如90 - 72 = 18 度
        val outCircleAngle = 90 - averageAngle
        //一份平均角度的一半，例如72 / 2 = 36度
        val halfAverageAngle = averageAngle / 2f
        //计算出小圆内角的角度，36 + 18 = 54 度
        val internalAngle = halfAverageAngle + outCircleAngle
        //创建2个点
        val outCirclePoint = Point()
        val innerCirclePoint = Point()
        mPath.reset()
        for (i in 0 until mAngleNum) {
            //计算大圆上的点坐标
            //x = Math.cos((18 + 72 * i) / 180f * Math.PI) * 大圆半径
            //y = -Math.sin((18 + 72 * i)/ 180f * Math.PI) * 大圆半径
            outCirclePoint.x =
                (cos(angleToRadian(outCircleAngle + i * averageAngle)) * mOutCircleRadius).toInt()
            outCirclePoint.y =
                (-(sin(angleToRadian(outCircleAngle + i * averageAngle)) * mOutCircleRadius)).toInt()
            //计算小圆上的点坐标
            //x = Math.cos((54 + 72 * i) / 180f * Math.PI ) * 小圆半径
            //y = -Math.sin((54 + 72 * i) / 180 * Math.PI ) * 小圆半径
            innerCirclePoint.x =
                (cos(angleToRadian(internalAngle + i * averageAngle)) * mInnerCircleRadius).toInt()
            innerCirclePoint.y =
                (-(sin(angleToRadian(internalAngle + i * averageAngle)) * mInnerCircleRadius)).toInt()

            //第一次，先移动到第一个大圆上的点
            if (i == 0) {
                mPath.moveTo(outCirclePoint.x.toFloat(), outCirclePoint.y.toFloat())
            }

            //坐标连接，先大圆角上的点，再到小圆角上的点
            mPath.lineTo(outCirclePoint.x.toFloat(), outCirclePoint.y.toFloat())
            mPath.lineTo(innerCirclePoint.x.toFloat(), innerCirclePoint.y.toFloat())
        }
        mPath.close()
        canvas.drawPath(mPath, mPaint)
    }

    /**
     * 角度转弧度，由于Math的三角函数需要传入弧度制，而不是角度值，所以要角度换算为弧度，角度 / 180 * π
     *
     * @param angle 角度
     * @return 弧度
     */
    private fun angleToRadian(angle: Float): Double {
        return angle / 180F * Math.PI
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(handleMeasure(widthMeasureSpec), handleMeasure(heightMeasureSpec))
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

    override fun setChecked(checked: Boolean) {
        isChecked = checked
        mPaint.style =
            if (STYLE_FILL == mStyle) Paint.Style.STROKE else Paint.Style.FILL_AND_STROKE
        mStyle = if (STYLE_FILL == mStyle) STYLE_STROKE else STYLE_FILL
        mPaint.color = if (isChecked) mCheckedColor else mDefaultColor
        invalidate()
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        setChecked(!isChecked)
    }

    private fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}
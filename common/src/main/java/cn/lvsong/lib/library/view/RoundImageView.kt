package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import cn.lvsong.lib.library.R

/**
 * RoundedBitmapDrawable 是 support-v4 下一个类
 * Desc: 圆角矩形
 * Author: Jooyer
 * Date: 2018-08-15
 * Time: 10:02
 *
 *
 * ImageView mm1 = (ImageView) findViewById(R.id.mm1);
 * ImageView mm2 = (ImageView) findViewById(R.id.mm2);
 *
 *
 * RoundedBitmapDrawable roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.ns2));
 * RoundedBitmapDrawable roundedBitmapDrawable2 = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.ns2));
 * // 圆形
 * roundedBitmapDrawable1.setCircular(true);
 * mm1.setImageDrawable(roundedBitmapDrawable1);
 * // 圆角矩形
 * roundedBitmapDrawable2.setCornerRadius(10);
 * mm2.setImageDrawable(roundedBitmapDrawable2);
 */
/**
 * ImageView mm1 = (ImageView) findViewById(R.id.mm1);
 * ImageView mm2 = (ImageView) findViewById(R.id.mm2);
 *
 * RoundedBitmapDrawable roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.ns2));
 * RoundedBitmapDrawable roundedBitmapDrawable2 = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.ns2));
 * // 圆形
 * roundedBitmapDrawable1.setCircular(true);
 * mm1.setImageDrawable(roundedBitmapDrawable1);
 * // 圆角矩形
 * roundedBitmapDrawable2.setCornerRadius(10);
 * mm2.setImageDrawable(roundedBitmapDrawable2);
 *
 */
/** 用法:
 * <cn.lvsong.lib.library.view.RoundImageView android:id="@+id/riv_cover_item" android:layout_width="@dimen/width_70" android:layout_height="@dimen/height_70" android:layout_marginStart="@dimen/padding_14" android:layout_marginTop="@dimen/padding_20" android:src="@drawable/ic_launcher_background" app:riv_border_color="@android:color/transparent" app:riv_border_width="4dp" app:riv_round_radius="@dimen/padding_5" app:riv_mask_type="ROUNDRECTANGLE"></cn.lvsong.lib.library.view.RoundImageView>
 *
 */
class RoundImageView : AppCompatImageView {
    
    private  val DEFAULT_BORDER_COLOR = Color.WHITE
    
    private val sMaskTypeArray = arrayOf(
        MaskType.RECTANGLE,
        MaskType.CIRCLE,
        MaskType.ROUNDRECTANGLE,
        MaskType.ROUNDRECTANGLETOP
    )
    private var mMaskType: MaskType? = null
    private var mPath = Path()
    private var mRadius = 0f
    private var mBorderWidth = 0f
    private var mBorderPaint: Paint =  Paint(Paint.ANTI_ALIAS_FLAG)
    private var mBorderColor = 0

    constructor(context: Context?) : super(context!!) {
        initRoundImageView()
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int = 0) : super(
        context,
        attrs,
        defStyle
    ) {
        initRoundImageView()
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.RoundImageView, defStyle, 0
        )
        val index = a.getInt(R.styleable.RoundImageView_riv_mask_type, -1)
        if (index >= 0) {
            setMaskType(sMaskTypeArray[index])
        }
        mRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_riv_round_radius, 10).toFloat()
        mBorderColor = a.getColor(R.styleable.RoundImageView_riv_border_color, Color.TRANSPARENT)
        mBorderWidth =
            a.getDimensionPixelSize(R.styleable.RoundImageView_riv_border_width, 0).toFloat()
        a.recycle()
    }

    private fun initRoundImageView() {
        mMaskType = MaskType.CIRCLE
        mRadius = 10f
        mPath = Path()
        mBorderColor = DEFAULT_BORDER_COLOR
        mBorderPaint.color = mBorderColor
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        drawPath()
        canvas.clipPath(mPath)
        super.onDraw(canvas)
        canvas.restore()
        drawCanvas(canvas)
    }

    private fun drawPath() {
        val width = width
        val height = height
        when (mMaskType) {
            MaskType.RECTANGLE -> {
                mPath.reset()
                mPath.addRect(
                    RectF(
                        mBorderWidth / 2,
                        mBorderWidth / 2,
                        width - mBorderWidth / 2,
                        height - mBorderWidth / 2
                    ), Path.Direction.CW
                )
                mPath.close()
            }
            MaskType.CIRCLE -> {
                val r = (width.coerceAtMost(height) / 2).toFloat()
                mPath.reset()
                mPath.addCircle(
                    (width / 2).toFloat(),
                    (height / 2).toFloat(),
                    r,
                    Path.Direction.CW
                )
                mPath.close()
            }
            MaskType.ROUNDRECTANGLE -> {
                mPath.reset()
                mPath.addRoundRect(
                    RectF(
                        mBorderWidth / 4,
                        mBorderWidth / 4,
                        width - mBorderWidth / 4,
                        height - mBorderWidth / 4
                    ), mRadius, mRadius, Path.Direction.CW
                )
                mPath.close()
            }
            MaskType.ROUNDRECTANGLETOP -> {
                mPath.reset()
                mPath.addCircle(mRadius, mRadius, mRadius, Path.Direction.CW)
                mPath.addCircle(width - mRadius, mRadius, mRadius, Path.Direction.CW)
                mPath.addRect(mRadius, 0f, width - mRadius, 2 * mRadius, Path.Direction.CW)
                mPath.addRect(0f, mRadius, width.toFloat(), height.toFloat(), Path.Direction.CW)
                mPath.close()
            }
        }
    }

    private fun drawCanvas(canvas: Canvas) {
        val width = width
        val height = height
        if (mBorderWidth <= 0) {
            return
        }
        mBorderPaint.color = mBorderColor
        mBorderPaint.strokeWidth = mBorderWidth
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.isAntiAlias = true
        when (mMaskType) {
            MaskType.RECTANGLE -> canvas.drawRect(
                RectF(0F, 0F, width.toFloat(), height.toFloat()),
                mBorderPaint
            )
            MaskType.CIRCLE -> {
                val r = (width.coerceAtMost(height) / 2F)
                canvas.drawCircle(
                    (width / 2).toFloat(),
                    (height / 2).toFloat(),
                    r - mBorderWidth / 2,
                    mBorderPaint
                )
            }
            MaskType.ROUNDRECTANGLE -> canvas.drawRoundRect(
                RectF(
                    0F,
                    0F,
                    width.toFloat(),
                    height.toFloat()
                ), mRadius, mRadius, mBorderPaint
            )
        }
    }

    /**
     * options for mask the imageview
     */
    enum class MaskType(val mNativeInt: Int) {
        /**
         * a parallelogram with four right angles
         */
        RECTANGLE(0),

        /**
         *
         */
        CIRCLE(1),

        /**
         * a parallelogram with four circle angles
         */
        ROUNDRECTANGLE(2),

        /**
         * a parallelogram with two top circle angles
         */
        ROUNDRECTANGLETOP(3);
    }

    /**
     * 四周圆角半径,默认10px,在下面2种情形下有效
     * @MaskType.ROUNDRECTANGLE
     * @MaskType.ROUNDRECTANGLETOP
     */
    fun setRadius(radius: Int) {
        if (mRadius == radius.toFloat()) {
            return
        }
        mRadius = radius.toFloat()
        invalidate()
    }

    /**
     * 边框颜色,默认透明,在下面2种情形下有效
     * @MaskType.ROUNDRECTANGLE
     * @MaskType.ROUNDRECTANGLETOP
     */
    fun setBorderColor(@ColorInt color: Int) {
        if (mBorderColor == color) {
            return
        }
        mBorderColor = color
        mBorderPaint.color = color
        invalidate()
    }

    /**
     * 边框颜色,默认透明,在下面2种情形下有效
     * @MaskType.ROUNDRECTANGLE
     * @MaskType.ROUNDRECTANGLETOP
     */
    fun setBorderColorResource(@ColorRes colorResource: Int) {
        setBorderColor(context.resources.getColor(colorResource))
    }

    /**
     * 边框半径,默认0,在下面2种情形下有效
     * @MaskType.ROUNDRECTANGLE
     * @MaskType.ROUNDRECTANGLETOP
     */
    fun setBorderWidth(borderWidth: Float) {
        if (mBorderWidth == borderWidth) {
            return
        }
        mBorderWidth = borderWidth
        invalidate()
    }

    /**
     * 设置圆角的类型,矩形(RECTANGLE),CIRCLE(圆形),ROUNDRECTANGLE(四周圆角),ROUNDRECTANGLETOP(左上和右上有圆角)
     */
    fun setMaskType(maskType: MaskType?) {
        if (maskType == null) {
            throw NullPointerException()
        }
        if (mMaskType != maskType) {
            mMaskType = maskType
            requestLayout()
            invalidate()
        }
    }
    
}
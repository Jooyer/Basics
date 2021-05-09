package cn.lvsong.lib.library.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import cn.lvsong.lib.library.R

/** https://blog.csdn.net/u013293125/article/details/105200571
 * Created by Jooyer on 2017/8/24
 * 自定义圆形ImageView
 */
class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    /**
     *  图片缩放类型
     */
    private val SCALE_TYPE = ScaleType.CENTER_CROP

    /**
     * 图片加载到内存时模式
     */
    private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888

    private val COLOR_DRAWABLE_DIMENSION = 2

    /**
     * 默认边界(最外面一圈边框)宽度
     */
    private val DEFAULT_BOARD_WIDTH = 0

    /**
     * 默认边界颜色
     */
    private val DEFAULT_BOARD_COLOR = Color.BLACK

    private val DEFAULT_BOARD_OVERLAY = false

    /**
     * 边框宽度
     */
    private var mBoardWidth = DEFAULT_BOARD_WIDTH

    /**
     * 边框颜色
     */
    private var mBoardColor = DEFAULT_BOARD_COLOR

    /**
     * 原始 drawable 大小
     */
    private val mDrawableRect = RectF()

    /**
     * 边框所在矩形大小
     */
    private val mBoardRect = RectF()
    private val mShaderMatrix = Matrix()
    private val mBitmapPaint = Paint()
    private val mBoardPaint = Paint()
    private var mColorFilter: ColorFilter? = null
    private var mBitmap: Bitmap? = null

    /**
     * 位图渲染
     */
    private var mBitmapShader: BitmapShader? = null

    /**
     * 位图宽度
     */
    private var mBitmapWidth = 0

    /**
     * 位图高度
     */
    private var mBitmapHeight = 0

    /**
     * 原图半径
     */
    private var mDrawableRadius = 0F

    /**
     * 带边框的图片半径
     */
    private var mBoardRadius = 0

    /**
     * 初始化 TypedArray 和设置图片缩放模式完成
     */
    private var mReady = false
    private var mSetupPending = false

    /**
     * 是否覆盖
     */
    private var mBoardOverlay = false

    init {
        val array =
            context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0)
        mBoardWidth = array.getDimensionPixelSize(
            R.styleable.CircleImageView_circle_imageview_board_width,
            mBoardWidth
        )
        mBoardColor =
            array.getColor(R.styleable.CircleImageView_circle_imageview_board_color, mBoardColor)
        mBoardOverlay =
            array.getBoolean(R.styleable.CircleImageView_circle_imageview_board_overlay, false)
        array.recycle()
        initialize()
    }

    private fun initialize() {
        // 在这里强制设置ScaleType 为 CENTER_CROP,将图片水平垂直居中,便于缩放
        super.setScaleType(SCALE_TYPE)
        mReady = true
        if (mSetupPending) {
            setUp()
            mSetupPending = false // 因为在ImageView.setImageXxx() 会优先于构造方法执行,保证正确执行一次setup
        }
    }

    override fun setScaleType(scaleType: ScaleType) {
        require(SCALE_TYPE == scaleType) {
            String.format(
                "ScaleType %1\$s not supported",
                scaleType
            )
        }
    }

    // mageView的android:adjustViewBounds属性为是否保持原图的长宽比，
    // 单独设置不起作用，需要配合maxWidth或maxHeight一起使用。
    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        require(!adjustViewBounds) { "adjustViewBounds not supported" }
    }

    override fun onDraw(canvas: Canvas) {
        // 没有设置图片则不绘制
        if (null == drawable) {
            return
        }

        // 绘制圆心内部的区域
        canvas.drawCircle(
            (width / 2F).toFloat(),
            (height / 2F).toFloat(),
            mDrawableRadius.toFloat(),
            mBitmapPaint
        )

        // 如果圆形的边框宽度不为零,则绘制
        if (0 != mBoardWidth) {
            canvas.drawCircle(
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                mBoardRadius.toFloat(),
                mBoardPaint
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setUp()
    }

    var boardColor: Int
        get() = mBoardColor
        set(boardColor) {
            if (mBoardColor == boardColor) {
                return
            }
            mBoardColor = boardColor
            mBoardPaint.color = boardColor
            invalidate()
        }

    fun setBoardColorResource(@ColorRes colorResource: Int) {
        boardColor = context.resources.getColor(colorResource)
    }

    var boardWidth: Int
        get() = mBoardWidth
        set(boardWidth) {
            if (mBoardWidth == boardWidth) {
                return
            }
            mBoardWidth = boardWidth
            setUp()
        }
    var isBoardOverlay: Boolean
        get() = mBoardOverlay
        set(boardOverlay) {
            if (mBoardOverlay == boardOverlay) {
                return
            }
            mBoardOverlay = boardOverlay
            setUp()
        }

    /* 复写 setXxx() ,注意它将先于构造方法执行 */
    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        mBitmap = getBitmapFromDrawable(drawable)
        setUp()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = getBitmapFromDrawable(drawable)
        setUp()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        mBitmap = bm
        setUp()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        mBitmap = getBitmapFromDrawable(drawable)
        setUp()
    }

    override fun setColorFilter(colorFilter: ColorFilter) {
        if (mColorFilter === colorFilter) {
            return
        }
        mColorFilter = colorFilter
        mBitmapPaint.colorFilter = colorFilter
        invalidate()
    }

    private fun setUp() {
        //因为mReady默认值为false,所以第一次进这个函数的时候if语句为真进入括号体内
        //设置mSetupPending为true然后直接返回，后面的代码并没有执行。
        if (!mReady) {
            mSetupPending = true
            return
        }
        if (null == mBitmap) {
            return
        }

        // 构造渲染器,后面第二,第三参数为如果图片小于控件,则会被拉伸
        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapPaint.isAntiAlias = true
        mBitmapPaint.shader = mBitmapShader
        mBoardPaint.style = Paint.Style.STROKE
        mBoardPaint.isAntiAlias = true
        mBoardPaint.color = mBoardColor
        mBoardPaint.strokeWidth = mBoardWidth.toFloat()

        // 获取原图的宽高
        mBitmapWidth = mBitmap!!.width
        mBitmapHeight = mBitmap!!.height

        // 设置包含边界的显示区域,也就是得到的ImageView控件大小
        mBoardRect[0F, 0F, width.toFloat()] = height.toFloat()
        // 计算边界圆环的半径
        mBoardRadius =
            ((mBoardRect.width() - mBoardWidth) / 2).coerceAtMost((mBoardRect.height() - mBoardWidth) / 2)
                .toInt()
        // 初始化图片显示区域
        mDrawableRect.set(mBoardRect)
        if (!mBoardOverlay) {
            //通过 insert() 可以使得图片显示区域按 mBoardRect 大小 从四个方向往内缩 mBoardWidth ,形成一个新的图片显示区域
            mDrawableRect.inset(mBoardWidth.toFloat(), mBoardWidth.toFloat())
        }

        // 计算内部图片的外边的半径
        mDrawableRadius = (mDrawableRect.width() / 2F).coerceAtMost(mDrawableRect.height() / 2F)

        // 设置渲染器的变换矩阵,就是什么缩放
        updateShaderMatrix()
        // 手动触发 onDraw(),完成绘制
        invalidate()
    }

    private fun updateShaderMatrix() {
        var scale = 0F
        var dx = 0F
        var dy = 0F
        mShaderMatrix.set(null)

        // 获取最小的缩放比例
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            // X轴平移 , Y 轴缩放
            scale = mDrawableRect.height() * 1F / mBitmapHeight
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5F // 平移时类似于左右各平移一部分,所以 *0.5
        } else {
            //x轴缩放 y轴平移 使得图片的x轴方向的边的尺寸缩放到图片显示区域（mDrawableRect）一样）
            scale = mDrawableRect.width() * 1F / mBitmapWidth
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5F
        }

        // 设置缩放
        mShaderMatrix.setScale(scale, scale)
        // 设置平移
        mShaderMatrix.postTranslate(dx + 0.5F + mDrawableRect.left, dy + 0.5F + mDrawableRect.top)
        // 设置变换矩阵
        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (null == drawable) {
            return null
        }
        return if (drawable is BitmapDrawable) {
            drawable.bitmap // 一般情况下,则代码只会执行到这里,就结束了
        } else try {
            var bitmap: Bitmap? = null
            bitmap = if (drawable is ColorDrawable) { // TODO 没有看明白
                Bitmap.createBitmap(
                    COLOR_DRAWABLE_DIMENSION,
                    COLOR_DRAWABLE_DIMENSION,
                    BITMAP_CONFIG
                )
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    BITMAP_CONFIG
                )
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (error: OutOfMemoryError) {
            null
        }
    }

}
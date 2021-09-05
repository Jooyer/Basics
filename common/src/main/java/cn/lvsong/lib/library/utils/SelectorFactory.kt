package cn.lvsong.lib.library.utils

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat

/**
 * Created by Jooyer
 * Date 2018/1/2
 * Des SelectorDrawable 动态设置背景工具类
 *
 *
 * //部分常用状态数组
 * public static final int[] STATE_DEFAULT = new int[0];
 * public static final int[] STATE_PRESSED = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
 * public static final int[] STATE_SELECTED = new int[]{android.R.attr.state_selected};
 * public static final int[] STATE_CHECKED = new int[]{android.R.attr.state_checked};
 * public static final int[] STATE_UNCHECKED = new int[]{-android.R.attr.state_checked};
 * public static final int[] STATE_DISABLED = new int[]{-android.R.attr.state_enabled};
 * public static final int[] STATE_FOCUSED = new int[]{android.R.attr.state_focused};
 */
/*
用法举例:
  Button.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.main_theme_color))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

PS: 使用时如果发现文本显示不完整,可以有2个解决方案:
    1. 使用 AppCompatTextView(或者 TextView) 代替 AppCompatButton(或者Button)
    2. AppCompatButton(或者Button)添加 android:background="@null"  --> 推荐使用此方法
    3. 在application对应的 style 里添加
        <!-- buttonStyle -->
        <item name="buttonStyle">@style/Widget.AppCompat.ActionButton</item>
 */
object SelectorFactory {
    fun newShapeSelector(): ShapeSelector {
        return ShapeSelector()
    }

    fun newColorSelector(): ColorSelector {
        return ColorSelector()
    }

    fun newGeneralSelector(): GeneralSelector {
        return GeneralSelector()
    }

    class ShapeSelector internal constructor() {
        @IntDef(
            GradientDrawable.RECTANGLE,
            GradientDrawable.OVAL,
            GradientDrawable.LINE,
            GradientDrawable.RING
        )
        private annotation class Shape

        private var mShape //the shape of background
                : Int
        private var mDefaultBgColor //default background color
                : Int
        private var mDisabledBgColor //state_enabled = false
                : Int
        private var mPressedBgColor //state_pressed = true
                : Int
        private var mSelectedBgColor //state_selected = true
                : Int
        private var mFocusedBgColor //state_focused = true
                : Int
        private var mStrokeWidth //stroke width in pixel
                : Int
        private var mDefaultStrokeColor //default stroke color
                : Int
        private var mDisabledStrokeColor //state_enabled = false
                : Int
        private var mPressedStrokeColor //state_pressed = true
                : Int
        private var mSelectedStrokeColor //state_selected = true
                : Int
        private var mFocusedStrokeColor //state_focused = true
                : Int
        private var mCornerRadius //corner radius
                : Int

        /**
         * 默认四个圆角大小一样,如果不一样需要设置这个值
         */
        private var mDifferentCorners: FloatArray? = null
        private var hasSetDisabledBgColor = false
        private var hasSetPressedBgColor = false
        private var hasSetSelectedBgColor = false
        private val hasSetFocusedBgColor = false
        private var hasSetDisabledStrokeColor = false
        private var hasSetPressedStrokeColor = false
        private var hasSetSelectedStrokeColor = false
        private var hasSetFocusedStrokeColor = false


        init {
            //initialize default values
            mShape = GradientDrawable.RECTANGLE
            mDefaultBgColor = Color.TRANSPARENT
            mDisabledBgColor = Color.TRANSPARENT
            mPressedBgColor = Color.TRANSPARENT
            mSelectedBgColor = Color.TRANSPARENT
            mFocusedBgColor = Color.TRANSPARENT
            mStrokeWidth = 0
            mDefaultStrokeColor = Color.TRANSPARENT
            mDisabledStrokeColor = Color.TRANSPARENT
            mPressedStrokeColor = Color.TRANSPARENT
            mSelectedStrokeColor = Color.TRANSPARENT
            mFocusedStrokeColor = Color.TRANSPARENT
            mCornerRadius = 0
            mDifferentCorners = null
        }

        fun setShape(@Shape shape: Int): ShapeSelector {
            mShape = shape
            return this
        }

        fun setDefaultBgColor(@ColorInt color: Int): ShapeSelector {
            mDefaultBgColor = color
            if (!hasSetDisabledBgColor) mDisabledBgColor = color
            if (!hasSetPressedBgColor) mPressedBgColor = color
            if (!hasSetSelectedBgColor) mSelectedBgColor = color
            if (!hasSetFocusedBgColor) mFocusedBgColor = color
            return this
        }

        fun setDisabledBgColor(@ColorInt color: Int): ShapeSelector {
            mDisabledBgColor = color
            hasSetDisabledBgColor = true
            return this
        }

        fun setPressedBgColor(@ColorInt color: Int): ShapeSelector {
            mPressedBgColor = color
            hasSetPressedBgColor = true
            return this
        }

        fun setSelectedBgColor(@ColorInt color: Int): ShapeSelector {
            mSelectedBgColor = color
            hasSetSelectedBgColor = true
            return this
        }

        fun setFocusedBgColor(@ColorInt color: Int): ShapeSelector {
            mFocusedBgColor = color
            hasSetPressedBgColor = true
            return this
        }

        fun setStrokeWidth(@Dimension width: Int): ShapeSelector {
            mStrokeWidth = width
            return this
        }

        fun setDefaultStrokeColor(@ColorInt color: Int): ShapeSelector {
            mDefaultStrokeColor = color
            if (!hasSetDisabledStrokeColor) mDisabledStrokeColor = color
            if (!hasSetPressedStrokeColor) mPressedStrokeColor = color
            if (!hasSetSelectedStrokeColor) mSelectedStrokeColor = color
            if (!hasSetFocusedStrokeColor) mFocusedStrokeColor = color
            return this
        }

        fun setDisabledStrokeColor(@ColorInt color: Int): ShapeSelector {
            mDisabledStrokeColor = color
            hasSetDisabledStrokeColor = true
            return this
        }

        fun setPressedStrokeColor(@ColorInt color: Int): ShapeSelector {
            mPressedStrokeColor = color
            hasSetPressedStrokeColor = true
            return this
        }

        fun setSelectedStrokeColor(@ColorInt color: Int): ShapeSelector {
            mSelectedStrokeColor = color
            hasSetSelectedStrokeColor = true
            return this
        }

        fun setFocusedStrokeColor(@ColorInt color: Int): ShapeSelector {
            mFocusedStrokeColor = color
            hasSetFocusedStrokeColor = true
            return this
        }

        fun setCornerRadius(@Dimension radius: Int): ShapeSelector {
            mCornerRadius = radius
            return this
        }

        /**
         *  r1, r2, r3, r4, r5, r6, r7, r8
         * 设置图片四个角圆形半径：1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
         */
        fun setDifferentCornerRadius(
            @Dimension leftTopRadius: Int, @Dimension rightTopRadius: Int,
            @Dimension rightBottomRadius: Int, @Dimension leftBottomRadius: Int, ): ShapeSelector {
            mDifferentCorners = floatArrayOf(
                leftTopRadius.toFloat(), leftTopRadius.toFloat(), rightTopRadius.toFloat(),rightTopRadius.toFloat(),
                rightBottomRadius.toFloat(),rightBottomRadius.toFloat(), leftBottomRadius.toFloat(),leftBottomRadius.toFloat())
            return this
        }

        fun create(): StateListDrawable {
            val selector = StateListDrawable()

            //enabled = false
            if (hasSetDisabledBgColor || hasSetDisabledStrokeColor) {
                val disabledShape = mDifferentCorners?.let {
                    getItemShape(mShape, it, mDisabledBgColor, mStrokeWidth, mDisabledStrokeColor)
                }
                    ?: getItemShape(
                        mShape, mCornerRadius.toFloat(),
                        mDisabledBgColor, mStrokeWidth, mDisabledStrokeColor
                    )
                selector.addState(intArrayOf(-R.attr.state_enabled), disabledShape)
            }

            //pressed = true
            if (hasSetPressedBgColor || hasSetPressedStrokeColor) {
                val pressedShape = mDifferentCorners?.let {
                    getItemShape(
                        mShape, it,
                        mPressedBgColor, mStrokeWidth, mPressedStrokeColor
                    )
                } ?: getItemShape(
                    mShape, mCornerRadius.toFloat(),
                    mPressedBgColor, mStrokeWidth, mPressedStrokeColor
                )
                selector.addState(intArrayOf(R.attr.state_pressed), pressedShape)
            }

            //selected = true
            if (hasSetSelectedBgColor || hasSetSelectedStrokeColor) {
                val selectedShape = mDifferentCorners?.let {
                    getItemShape(
                        mShape, it,
                        mSelectedBgColor, mStrokeWidth, mSelectedStrokeColor
                    )
                } ?: getItemShape(
                    mShape, mCornerRadius.toFloat(),
                    mSelectedBgColor, mStrokeWidth, mSelectedStrokeColor
                )
                selector.addState(intArrayOf(R.attr.state_selected), selectedShape)
            }

            //focused = true
            if (hasSetFocusedBgColor || hasSetFocusedStrokeColor) {
                val focusedShape = mDifferentCorners?.let {
                    getItemShape(
                        mShape, it,
                        mFocusedBgColor, mStrokeWidth, mFocusedStrokeColor
                    )
                } ?: getItemShape(
                    mShape, mCornerRadius.toFloat(),
                    mFocusedBgColor, mStrokeWidth, mFocusedStrokeColor
                )
                selector.addState(intArrayOf(R.attr.state_focused), focusedShape)
            }

            //default
            val defaultShape = mDifferentCorners?.let {
                getItemShape(mShape, it, mDefaultBgColor, mStrokeWidth, mDefaultStrokeColor)
            } ?: getItemShape(
                mShape, mCornerRadius.toFloat(),
                mDefaultBgColor, mStrokeWidth, mDefaultStrokeColor
            )
            selector.addState(intArrayOf(), defaultShape)
            return selector
        }

        /**
         * @param cornerRadius --> 四周圆角一样大小
         */
        private fun getItemShape(
            shape: Int, cornerRadius: Float,
            solidColor: Int, strokeWidth: Int, strokeColor: Int
        ): GradientDrawable {
            val drawable = GradientDrawable()
            drawable.shape = shape
            drawable.setStroke(strokeWidth, strokeColor)
            drawable.cornerRadius = cornerRadius
            drawable.setColor(solidColor)
            return drawable
        }

        /**
         * @param cornerRadius --> 设置图片四个角圆形半径：1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
         */
        private fun getItemShape(
            shape: Int, cornerRadius: FloatArray,
            solidColor: Int, strokeWidth: Int, strokeColor: Int
        ): GradientDrawable {
            val drawable = GradientDrawable()
            drawable.shape = shape
            drawable.setStroke(strokeWidth, strokeColor)
            drawable.cornerRadii = cornerRadius
            drawable.setColor(solidColor)
            return drawable
        }
    }

    class ColorSelector internal constructor() {
        private var mDefaultColor: Int
        private var mDisabledColor: Int
        private var mPressedColor: Int
        private var mSelectedColor: Int
        private var mFocusedColor: Int
        private var hasSetDisabledColor = false
        private var hasSetPressedColor = false
        private var hasSetSelectedColor = false
        private var hasSetFocusedColor = false

        init {
            mDefaultColor = Color.BLACK
            mDisabledColor = Color.GRAY
            mPressedColor = Color.BLACK
            mSelectedColor = Color.BLACK
            mFocusedColor = Color.BLACK
        }

        fun setDefaultColor(@ColorInt color: Int): ColorSelector {
            mDefaultColor = color
            if (!hasSetDisabledColor) mDisabledColor = color
            if (!hasSetPressedColor) mPressedColor = color
            if (!hasSetSelectedColor) mSelectedColor = color
            if (!hasSetFocusedColor) mFocusedColor = color
            return this
        }

        fun setDisabledColor(@ColorInt color: Int): ColorSelector {
            mDisabledColor = color
            hasSetDisabledColor = true
            return this
        }

        fun setPressedColor(@ColorInt color: Int): ColorSelector {
            mPressedColor = color
            hasSetPressedColor = true
            return this
        }

        fun setSelectedColor(@ColorInt color: Int): ColorSelector {
            mSelectedColor = color
            hasSetSelectedColor = true
            return this
        }

        fun setFocusedColor(@ColorInt color: Int): ColorSelector {
            mFocusedColor = color
            hasSetFocusedColor = true
            return this
        }

        fun create(): ColorStateList {
            val colors = intArrayOf(
                if (hasSetDisabledColor) mDisabledColor else mDefaultColor,
                if (hasSetPressedColor) mPressedColor else mDefaultColor,
                if (hasSetSelectedColor) mSelectedColor else mDefaultColor,
                if (hasSetFocusedColor) mFocusedColor else mDefaultColor,
                mDefaultColor
            )
            val states = arrayOfNulls<IntArray>(5)
            states[0] = intArrayOf(-R.attr.state_enabled)
            states[1] = intArrayOf(R.attr.state_pressed)
            states[2] = intArrayOf(R.attr.state_selected)
            states[3] = intArrayOf(R.attr.state_focused)
            states[4] = intArrayOf()
            return ColorStateList(states, colors)
        }
    }

    class GeneralSelector internal constructor() {
        private var mDefaultDrawable: Drawable?
        private var mDisabledDrawable: Drawable? = null
        private var mPressedDrawable: Drawable? = null
        private var mSelectedDrawable: Drawable? = null
        private var mFocusedDrawable: Drawable? = null
        private var hasSetDisabledDrawable = false
        private var hasSetPressedDrawable = false
        private var hasSetSelectedDrawable = false
        private var hasSetFocusedDrawable = false

        init {
            mDefaultDrawable = ColorDrawable(Color.TRANSPARENT)
        }

        fun setDefaultDrawable(drawable: Drawable?): GeneralSelector {
            mDefaultDrawable = drawable
            if (!hasSetDisabledDrawable) mDisabledDrawable = drawable
            if (!hasSetPressedDrawable) mPressedDrawable = drawable
            if (!hasSetSelectedDrawable) mSelectedDrawable = drawable
            if (!hasSetFocusedDrawable) mFocusedDrawable = drawable
            return this
        }

        fun setDisabledDrawable(drawable: Drawable?): GeneralSelector {
            mDisabledDrawable = drawable
            hasSetDisabledDrawable = true
            return this
        }

        fun setPressedDrawable(drawable: Drawable?): GeneralSelector {
            mPressedDrawable = drawable
            hasSetPressedDrawable = true
            return this
        }

        fun setSelectedDrawable(drawable: Drawable?): GeneralSelector {
            mSelectedDrawable = drawable
            hasSetSelectedDrawable = true
            return this
        }

        fun setFocusedDrawable(drawable: Drawable?): GeneralSelector {
            mFocusedDrawable = drawable
            hasSetFocusedDrawable = true
            return this
        }

        fun create(): StateListDrawable {
            val selector = StateListDrawable()
            if (hasSetDisabledDrawable) selector.addState(
                intArrayOf(-R.attr.state_enabled),
                mDisabledDrawable
            )
            if (hasSetPressedDrawable) selector.addState(
                intArrayOf(R.attr.state_pressed),
                mPressedDrawable
            )
            if (hasSetSelectedDrawable) selector.addState(
                intArrayOf(R.attr.state_selected),
                mSelectedDrawable
            )
            if (hasSetFocusedDrawable) selector.addState(
                intArrayOf(R.attr.state_focused),
                mFocusedDrawable
            )
            selector.addState(intArrayOf(), mDefaultDrawable)
            return selector
        }

        //overload
        fun setDefaultDrawable(context: Context?, @DrawableRes drawableRes: Int): GeneralSelector {
            return setDefaultDrawable(ContextCompat.getDrawable(context!!, drawableRes))
        }

        //overload
        fun setDisabledDrawable(context: Context?, @DrawableRes drawableRes: Int): GeneralSelector {
            return setDisabledDrawable(ContextCompat.getDrawable(context!!, drawableRes))
        }

        //overload
        fun setPressedDrawable(context: Context?, @DrawableRes drawableRes: Int): GeneralSelector {
            return setPressedDrawable(ContextCompat.getDrawable(context!!, drawableRes))
        }

        //overload
        fun setSelectedDrawable(context: Context?, @DrawableRes drawableRes: Int): GeneralSelector {
            return setSelectedDrawable(ContextCompat.getDrawable(context!!, drawableRes))
        }

        //overload
        fun setFocusedDrawable(context: Context?, @DrawableRes drawableRes: Int): GeneralSelector {
            return setFocusedDrawable(ContextCompat.getDrawable(context!!, drawableRes))
        }

    }
}
package cn.lvsong.lib.library.decorator

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

/** https://www.jianshu.com/p/fb7e1a0749d6
 *  https://www.jianshu.com/p/85172c70bb98 || https://www.jianshu.com/p/a8b00a02a46a
 * Desc: https://github.com/rubensousa/Decorator
 * Author: Jooyer
 * Date: 2021-09-07
 * Time: 23:33
 */
class LinearDividerDecoration(
    private var paint: Paint,
    @Px private var size: Int,
    @Px private var leftMargin: Int = 0,
    @Px private var topMargin: Int = 0,
    @Px private var rightMargin: Int = 0,
    @Px private var bottomMargin: Int = 0,
    private var orientation: Int = RecyclerView.VERTICAL,
    private var inverted: Boolean = false,
    private var addBeforeFirstPosition: Boolean = false,
    private var addAfterLastPosition: Boolean = false,
    private var decorationLookup: DecorationLookup? = null
) : AbstractMarginDecoration(decorationLookup) {

    companion object {

        /**
         * Creates a [LinearDividerDecoration] using a color
         */
        @JvmStatic
        fun create(
            @ColorInt color: Int,
            size: Int,
            leftMargin: Int = 0,
            topMargin: Int = 0,
            rightMargin: Int = 0,
            bottomMargin: Int = 0,
            orientation: Int = RecyclerView.VERTICAL,
            inverted: Boolean = false,
            addBeforeFirstPosition: Boolean = false,
            addAfterLastPosition: Boolean = false,
            decorationLookup: DecorationLookup? = null
        ): LinearDividerDecoration {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.isDither = true
            paint.color = color
            return LinearDividerDecoration(
                paint,
                size,
                leftMargin,
                topMargin,
                rightMargin,
                bottomMargin,
                orientation,
                inverted,
                addBeforeFirstPosition,
                addAfterLastPosition,
                decorationLookup
            )
        }

    }


    fun setPaint(paint: Paint) {
        this.paint = paint
    }

    fun setDividerSize(@Px size: Int) {
        this.size = size
    }

    fun getDividerSize() = size

    fun setHorizontalMargin(margin: Int) {
        leftMargin = margin
        rightMargin = margin
    }

    fun setVerticalMargin(margin: Int) {
        topMargin = margin
        bottomMargin = margin
    }

    fun setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        leftMargin = left
        topMargin = top
        rightMargin = right
        bottomMargin = bottom
    }

    fun getLeftMargin() = leftMargin

    fun getTopMargin() = topMargin

    fun getRightMargin() = rightMargin

    fun getBottomMargin() = bottomMargin

    fun setOrientation(orientation: Int) {
        this.orientation = orientation
    }

    fun getOrientation() = orientation

    /**
     * @param inverted true if the LayoutManager is inverted and items are laid out
     * from the bottom to the top or from the right to the left.
     */
    fun setInverted(inverted: Boolean) {
        this.inverted = inverted
    }

    /**
     * @param enable - true if the first item should have a divider before it, or false otherwise
     */
    fun enableBeforeFirstPosition(enable: Boolean) {
        addBeforeFirstPosition = enable
    }

    /**
     * @param enable - true if the last item should have a divider after it, or false otherwise
     */
    fun enableAfterLastPosition(enable: Boolean) {
        addAfterLastPosition = enable
    }

    fun isInverted() = inverted

    /**
     * @param decorationLookup an optional [DecorationLookup] to filter positions
     * that shouldn't have this decoration applied to
     */
    fun setDecorationLookup(decorationLookup: DecorationLookup?) {
        this.decorationLookup = decorationLookup
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        position: Int,
        parent: RecyclerView,
        state: RecyclerView.State,
        layoutManager: RecyclerView.LayoutManager
    ) {
        val itemCount = layoutManager.itemCount
        if (orientation == RecyclerView.VERTICAL) {
            applyVerticalOffsets(outRect, position, itemCount)
        } else {
            applyHorizontalOffsets(outRect, position, itemCount)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val layoutManager = parent.layoutManager ?: return
        val itemCount = layoutManager.itemCount
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(child)
            if (shouldApplyDecorationAt(adapterPosition, itemCount)) {
                if (orientation == RecyclerView.VERTICAL) {
                    drawVertical(c, child, adapterPosition, itemCount, layoutManager)
                } else {
                    drawHorizontal(c, child, adapterPosition, itemCount, layoutManager)
                }
            }
        }
    }

    private fun shouldApplyDecorationAt(position: Int, itemCount: Int): Boolean {
        if (position == RecyclerView.NO_POSITION) {
            return false
        }
        if (decorationLookup == null) {
            return true
        }
        return decorationLookup!!.shouldApplyDecoration(position, itemCount)
    }

    private fun applyVerticalOffsets(outRect: Rect, position: Int, itemCount: Int) {
        // Add space for the divider from the bottom if not inverted
        if (!inverted) {
            outRect.bottom = size + bottomMargin + topMargin
        } else {
            outRect.top = size + bottomMargin + topMargin
        }

        // If we're at the first position, check if we need to add an extra decoration
        if (position == 0 && addBeforeFirstPosition) {
            if (!inverted) {
                outRect.top = bottomMargin + size
            } else {
                outRect.bottom = topMargin + size
            }
        }

        // If we're at the last position, check if we need to add an extra decoration
        if (position == itemCount - 1) {
            if (addAfterLastPosition) {
                if (!inverted) {
                    outRect.bottom = topMargin + size
                } else {
                    outRect.top = bottomMargin + size
                }
            } else {
                // Reset offsets since we don't want a divider here
                if (!inverted) {
                    outRect.bottom = 0
                } else {
                    outRect.top = 0
                }
            }
        }
    }

    /**
     * Divider is drawn on bottom of item if [inverted] is false, or on top if [inverted] is true
     */
    private fun drawVertical(
        canvas: Canvas,
        view: View,
        position: Int,
        itemCount: Int,
        layoutManager: RecyclerView.LayoutManager
    ) {
        if (position == 0 && addBeforeFirstPosition) {
            if (!inverted) {
                drawVerticalDivider(
                    canvas, view, layoutManager,
                    layoutManager.getDecoratedTop(view).toFloat(),
                    layoutManager.getDecoratedTop(view).toFloat() + size
                )
            } else {
                drawVerticalDivider(
                    canvas, view, layoutManager,
                    layoutManager.getDecoratedBottom(view).toFloat() - size,
                    layoutManager.getDecoratedBottom(view).toFloat()
                )
            }
        }

        if (position != itemCount - 1) {
            if (!inverted) {
                drawVerticalDivider(
                    canvas, view, layoutManager,
                    layoutManager.getDecoratedBottom(view).toFloat() - bottomMargin - size,
                    layoutManager.getDecoratedBottom(view).toFloat() - bottomMargin
                )
            } else {
                drawVerticalDivider(
                    canvas, view, layoutManager,
                    layoutManager.getDecoratedTop(view).toFloat() + topMargin,
                    layoutManager.getDecoratedTop(view).toFloat() + topMargin + size
                )
            }
        }

        if (position == itemCount - 1 && addAfterLastPosition) {
            if (!inverted) {
                drawVerticalDivider(
                    canvas, view, layoutManager,
                    layoutManager.getDecoratedBottom(view).toFloat() - size,
                    layoutManager.getDecoratedBottom(view).toFloat()
                )
            } else {
                drawVerticalDivider(
                    canvas, view, layoutManager,
                    layoutManager.getDecoratedTop(view).toFloat(),
                    layoutManager.getDecoratedTop(view).toFloat() + size
                )
            }
        }

    }

    private fun drawVerticalDivider(
        canvas: Canvas, view: View, layoutManager: RecyclerView.LayoutManager,
        top: Float, bottom: Float
    ) {
        canvas.drawRect(
            layoutManager.getDecoratedLeft(view).toFloat() + leftMargin,
            top,
            layoutManager.getDecoratedRight(view).toFloat() - rightMargin,
            bottom,
            paint
        )
    }

    private fun applyHorizontalOffsets(outRect: Rect, position: Int, itemCount: Int) {
        if (!inverted) {
            outRect.right = size + leftMargin + rightMargin
        } else {
            outRect.left = size + leftMargin + rightMargin
        }

        if (position == 0 && addBeforeFirstPosition) {
            if (!inverted) {
                outRect.left = rightMargin + size
            } else {
                outRect.right = leftMargin + size
            }
        }

        if (position == itemCount - 1) {
            if (addAfterLastPosition) {
                if (!inverted) {
                    outRect.right = leftMargin + size
                } else {
                    outRect.left = rightMargin + size
                }
            } else {
                if (!inverted) {
                    outRect.right = 0
                } else {
                    outRect.left = 0
                }
            }
        }
    }

    private fun drawHorizontal(
        canvas: Canvas,
        view: View,
        position: Int,
        itemCount: Int,
        layoutManager: RecyclerView.LayoutManager
    ) {
        if (position == 0 && addBeforeFirstPosition) {
            if (!inverted) {
                drawHorizontalDivider(
                    canvas, view, layoutManager,
                    layoutManager.getDecoratedLeft(view).toFloat(),
                    layoutManager.getDecoratedLeft(view).toFloat() + size
                )
            } else {
                drawHorizontalDivider(
                    canvas, view, layoutManager,
                    layoutManager.getDecoratedRight(view).toFloat() - size,
                    layoutManager.getDecoratedRight(view).toFloat()
                )
            }
        }

        if (position != itemCount - 1) {
            if (!inverted) {
                drawHorizontalDivider(
                    canvas, view, layoutManager,
                    layoutManager.getDecoratedRight(view).toFloat() - rightMargin - size,
                    layoutManager.getDecoratedRight(view).toFloat() - rightMargin
                )
            } else {
                drawHorizontalDivider(
                    canvas, view, layoutManager,
                    layoutManager.getDecoratedLeft(view).toFloat() + leftMargin,
                    layoutManager.getDecoratedLeft(view).toFloat() + leftMargin + size
                )
            }
        }

        if (position == itemCount - 1 && addAfterLastPosition) {
            if (!inverted) {
                drawHorizontalDivider(
                    canvas, view, layoutManager,
                    layoutManager.getDecoratedRight(view).toFloat() - size,
                    layoutManager.getDecoratedRight(view).toFloat()
                )
            } else {
                drawHorizontalDivider(
                    canvas, view, layoutManager,
                    layoutManager.getDecoratedLeft(view).toFloat(),
                    layoutManager.getDecoratedLeft(view).toFloat() + size
                )
            }
        }

    }

    private fun drawHorizontalDivider(
        canvas: Canvas, view: View, layoutManager: RecyclerView.LayoutManager,
        left: Float, right: Float
    ) {
        canvas.drawRect(
            left,
            layoutManager.getDecoratedTop(view).toFloat() + topMargin,
            right,
            layoutManager.getDecoratedBottom(view).toFloat() - bottomMargin,
            paint
        )
    }

}
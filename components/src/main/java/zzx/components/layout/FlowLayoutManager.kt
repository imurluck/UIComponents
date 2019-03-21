package zzx.components.layout

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView

open class FlowLayoutManager: RecyclerView.LayoutManager() {

    private val TAG = javaClass.simpleName

    private var verticalOffset = 0
    private var firstVisiPos = 0
    private var lastVisiPos = 0

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        Log.e(TAG, "generateDefaultLayoutParams -> ")
        return RecyclerView.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount <= 0) {
            Log.d(TAG, "onLayoutChildren -> itemCount = 0")
            return
        }
        if (childCount == 0 && state.isPreLayout)
            return
        detachAndScrapAttachedViews(recycler)
        verticalOffset = 0
        firstVisiPos = 0
        lastVisiPos = 0

        layoutChildren(recycler, state)
    }

    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        super.onMeasure(recycler, state, widthSpec, heightSpec)
    }

    private fun layoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        var topOffset = paddingTop
        var leftOffset = paddingLeft
        var maxLineHeight = 0
        var minPos = firstVisiPos
        lastVisiPos = itemCount - 1
        for (i in minPos..lastVisiPos) {
            val child = recycler.getViewForPosition(i)
            addView(child)
            measureChildWithMargins(child, 0, 0)
            val childTotalWidth = getChildTotalWidth(child)
            val childTotalHeight = getChildTotalHeight(child)
            if (leftOffset + childTotalWidth <= width - paddingRight) {
                layoutDecoratedWithMargins(child, leftOffset, topOffset,
                    leftOffset + childTotalWidth, topOffset + childTotalHeight)
                leftOffset += childTotalWidth
                maxLineHeight = Math.max(maxLineHeight, childTotalHeight)
            } else {
                leftOffset = paddingLeft
                topOffset += maxLineHeight
                maxLineHeight = 0
                if (topOffset > height - paddingBottom) {
                    removeAndRecycleView(child, recycler)
                    lastVisiPos = i - 1
                } else {
                    layoutDecoratedWithMargins(child, leftOffset, topOffset,
                        leftOffset + childTotalWidth, topOffset + childTotalHeight)
                    leftOffset += childTotalWidth
                    maxLineHeight = Math.max(maxLineHeight, childTotalHeight)
                }

            }
        }
    }

    /**
     * 获取child所占宽度， 包括ItemDecorations设置的宽度和Margins
     */
    private fun getChildTotalWidth(child: View): Int {
        val params = child.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredWidth(child) + params.leftMargin + params.rightMargin
    }

    /**
     * 获取child所占高度, 包括ItemDecorations设置的高度和Margins
     */
    private fun getChildTotalHeight(child: View): Int {
        val params = child.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredHeight(child) + params.topMargin + params.bottomMargin
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        if (dy == 0 || childCount == 0) {
            return 0
        }

        var realOffset = dy
        if (verticalOffset + realOffset < 0) {
            realOffset = -verticalOffset
        } else if (realOffset > 0) {
            val lastChild = getChildAt(childCount - 1)
            if (getPosition(lastChild!!) == itemCount - 1) {
                val gap = height - paddingBottom - getDecoratedBottom(lastChild)
                if (gap > 0) {
                    realOffset = -gap
                } else if (gap == 0) {
                    realOffset = 0
                } else {
                    realOffset = Math.min(realOffset, -gap)
                }
            }
        }
//        realOffset = onLayoutChildren(recycler, state, realOffset)
        verticalOffset += realOffset
        offsetChildrenVertical(-dy)
        return dy
    }
}
package zzx.components.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.AT_MOST
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlin.math.max

class FlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var padding = 0
    private var lineSpace = 0
    /**
     * line count
     */
    private var lines = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var realWidth = maxWidth
        var realHeight = maxHeight
        for(i in 0..childCount) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec)
        }
        if (widthMode == AT_MOST) {
            realWidth = calculateRealWidth(maxWidth)
        }
        if (heightMode == AT_MOST) {
            realHeight = calculateRealHeight(realWidth, maxHeight)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun calculateRealHeight(realWidth: Int, maxHeight: Int): Int {
        var height = 0
        if (childCount == 0) {
            return 0
        }
        val childHeight = getChildAt(0).measuredHeight
        height += childHeight
        //temp width each line
        var lineWidth = 0
        for(i in 0..childCount) {
            lineWidth += getChildAt(i).measuredWidth
            if (lineWidth > realWidth) {
                lineWidth = getChildAt(i).measuredWidth
                lines++
                height += childHeight
                if (lines > 1) {
                    height += lineSpace
                }
            }
        }
        return height
    }

    private fun calculateRealWidth(maxWidth: Int): Int {
        var width = 0
        for(i in 0..childCount) {
            if (i > 0) {
                width += padding
            }
            width += getChildAt(i).measuredWidth
            if (width >= maxWidth) {
                return maxWidth
            }
        }
        return width
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

}
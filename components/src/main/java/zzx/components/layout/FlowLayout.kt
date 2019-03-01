package zzx.components.layout

import android.content.Context
import android.graphics.Color
import android.icu.util.Measure
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec.AT_MOST
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import zzx.components.R
import kotlin.math.max

open class FlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val TAG = this.javaClass.simpleName
    
    private var padding = 10
    private var lineSpace = 10
    private var childHeight = 0

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
        for(i in 0 until childCount) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec)
        }
        if (widthMode == AT_MOST) {
            realWidth = calculateRealWidth(maxWidth)
        }
        if (heightMode == AT_MOST) {
            realHeight = calculateRealHeight(realWidth, maxHeight)
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(realWidth, widthMode),
            MeasureSpec.makeMeasureSpec(realHeight, heightMode))
    }

    private fun calculateRealHeight(realWidth: Int, maxHeight: Int): Int {
        var height = 0
        if (childCount == 0) {
            lines = 0
            return 0
        }
        lines = 1
        childHeight = getChildAt(0).measuredHeight
        height += childHeight
        //temp width each line
        var lineWidth = 0
        for(i in 0 until childCount) {
            lineWidth += getChildAt(i).measuredWidth
            if (lineWidth > realWidth && lineWidth > getChildAt(i).measuredWidth) {
                lineWidth = getChildAt(i).measuredWidth
                lines++
            }
        }
        height = childHeight * lines + lineSpace * (lines - 1)
        if (height > maxHeight) {
            height = maxHeight
        }
        return height
    }

    private fun calculateRealWidth(maxWidth: Int): Int {
        var width = 0
        for(i in 0 until childCount) {
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
        var childL = 0
        var childT = 0
        var childR = 0
        var childB = childL + childHeight
        for(i in 0 until childCount) {
            var child = getChildAt(i)
            childR = childL + child.measuredWidth
            if (childR > measuredWidth) {
                childL = 0
                childT += childHeight + lineSpace
                childB = childT + child.measuredHeight
                childR = Math.min(child.measuredWidth, measuredWidth)
            }
            child.layout(childL, childT, childR, childB)
            childL += child.width + padding
        }
    }

    fun setList(list: List<String>) {
        if (list.isEmpty()) {
            return
        }
        list.forEach { text ->
            add(text)
        }
    }

    fun add(text: String) {
        add(text, R.layout.flow_layout_text_view)
    }

    fun add(text: String, tvId: Int) {
        addView(createTextView(text, tvId))
    }

    protected open fun createTextView(text: String, tvId: Int): TextView {
        var textView = LayoutInflater.from(context)
            .inflate(tvId, null)
            as TextView
        textView.text = text
        return textView
    }

}
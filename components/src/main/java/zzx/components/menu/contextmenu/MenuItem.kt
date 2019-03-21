package zzx.components.menu.contextmenu

import android.animation.*
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.dialog_context_menu_item.view.*
import zzx.components.R

/**
 * this class is used for save each item params and to provide open and close animations
 * create by zzx
 * create at 19-3-21
 */
class MenuItem {

    private var itemLayout: LinearLayout? = null
    private lateinit var imageWrapper: LinearLayout
    private lateinit var titleView: TextView

    var titleColor = INT_NO_OPTION
    var imageResourcesId = INT_NO_OPTION
    var imageDrawable: Drawable? = null

    var title = NO_TITLE

    var openAnimator: AnimatorSet? = null
    var closeAnimator: AnimatorSet? = null



    fun checkParams(context: Context) {
        if (titleColor == INT_NO_OPTION) {
            titleColor = context.getCompatColor(R.color.color_white)
        }
    }


    fun setOpenAnimator(gravity: MenuGravity, isHorizontal: Boolean) {
        openAnimator = AnimatorSet().apply {
            val imageBtnAnimator = if (isHorizontal) imageWrapper.rotationOpenHorizontal(gravity)
                                        else imageWrapper.rotationOpenVertical()
            playTogether(titleView.alphaOpen(), imageBtnAnimator)
            addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    itemLayout?.alpha = 1.0f
                }
            })

        }
    }

    private fun indexCompare(clickedItemIndex: Int): IndexCompare {
        val currentItemIndex = (itemLayout?.parent as ViewGroup).indexOfChild(itemLayout)
        return when {
            currentItemIndex == clickedItemIndex -> IndexCompare.EQUAL
            currentItemIndex < clickedItemIndex -> IndexCompare.SMALLER
            else -> IndexCompare.BIGGER
        }
    }

    fun setCloseAnimator(gravity: MenuGravity, clickedItemIndex: Int) {
        val indexCompare = indexCompare(clickedItemIndex)
        closeAnimator = AnimatorSet().apply {
            val imageBtnAnimator = when (indexCompare) {
                IndexCompare.EQUAL -> imageWrapper.rotationCloseHorizontal(gravity)
                else -> imageWrapper.rotationCloseVertical(indexCompare)
            }
            playTogether(titleView.alphaClose(), imageBtnAnimator)
        }
    }

    /**
     * create item layout
     */
    fun getItemLayout(container: ViewGroup, gravity: MenuGravity, isLastItem: Boolean): View {
        return LayoutInflater.from(container.context)
            .inflate(R.layout.dialog_context_menu_item, container, false)
            .apply {
                alpha = 0.0f
                layoutDirection = when (gravity) {
                    MenuGravity.START -> View.LAYOUT_DIRECTION_LTR
                    MenuGravity.END -> View.LAYOUT_DIRECTION_RTL
                }
                titleView.apply {
                    text = title
                    setTextColor(titleColor)
                }
                imageBtn.apply {
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                    when {
                        imageResourcesId != INT_NO_OPTION -> setImageResource(imageResourcesId)
                        imageDrawable != null -> setImageDrawable(imageDrawable)
                        else -> throw ImageNotFoundException()
                    }
                }
                if (isLastItem) {
                    divider.visibility = View.GONE
                }
                itemLayout = this as LinearLayout
                this@MenuItem.imageWrapper = imageWrapper
                this@MenuItem.titleView = titleView
            }
    }

    companion object {

        private const val INT_NO_OPTION = -1

        private const val TAG = "MenuItem"

        const val NO_TITLE = ""

        const val DEFAULT_DURATION = 100L
    }
}
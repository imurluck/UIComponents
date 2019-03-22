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
import zzx.components.menu.contextmenu.extensions.*

/**
 * this class is used for save each item params and to provide open and close animations
 * create by zzx
 * create at 19-3-21
 */
class MenuItem {

    var itemLayout: RelativeLayout? = null
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


    fun setOpenAnimator(gravity: MenuGravity, isFirstItem: Boolean) {
        openAnimator = AnimatorSet().apply {
            val imageBtnAnimator = if (isFirstItem) {
                when (gravity) {
                    MenuGravity.START -> imageWrapper.rotationOpenHorizontal(gravity)
                    MenuGravity.END -> imageWrapper.rotationOpenHorizontal(gravity)
                    else -> imageWrapper.rotationOpenVertical(gravity)
                }
            } else {
                when (gravity) {
                    MenuGravity.START -> imageWrapper.rotationOpenVertical(gravity)
                    MenuGravity.END -> imageWrapper.rotationOpenVertical(gravity)
                    else -> imageWrapper.rotationOpenHorizontal(gravity)
                }
            }
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
            val imageBtnAnimator = if (gravity == MenuGravity.START || gravity == MenuGravity.END) {
                when (indexCompare) {
                    IndexCompare.EQUAL -> imageWrapper.rotationCloseHorizontal(gravity, indexCompare)
                    else -> imageWrapper.rotationCloseVertical(gravity, indexCompare)
                }
            } else {
                when (indexCompare) {
                    IndexCompare.EQUAL -> imageWrapper.rotationCloseVertical(gravity, indexCompare)
                    else -> imageWrapper.rotationCloseHorizontal(gravity, indexCompare)
                }
            }
            playTogether(titleView.alphaClose(), imageBtnAnimator)
        }
    }

    /**
     * create item layout
     */
    fun getItemLayout(container: ViewGroup, gravity: MenuGravity, isLastItem: Boolean): View {
        val rootView = if (gravity == MenuGravity.START || gravity == MenuGravity.END) {
            LayoutInflater.from(container.context).inflate(R.layout.dialog_context_menu_item, container, false)
        } else if (gravity == MenuGravity.TOP) {
            LayoutInflater.from(container.context).inflate(R.layout.dialog_context_menu_item_top, container, false)
        } else {
            LayoutInflater.from(container.context).inflate(R.layout.dialog_context_menu_item_bottom, container, false)
        }
        return rootView.apply {
            alpha = 0.0f
            layoutDirection = when (gravity) {
                MenuGravity.END -> View.LAYOUT_DIRECTION_RTL
                else -> View.LAYOUT_DIRECTION_LTR
            }
            titleView.apply {
                text = getDisplayTitle(title, gravity)
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
            itemLayout = this as RelativeLayout
            this@MenuItem.imageWrapper = imageWrapper
            this@MenuItem.titleView = titleView
        }
    }

    /**
     * when [gravity] is [MenuGravity.TOP] or [MenuGravity.BOTTOM],
     * title string should be replace char('\0') with char('\n')
     */
    private fun getDisplayTitle(title: String, gravity: MenuGravity): CharSequence? {
        return if (gravity == MenuGravity.TOP || gravity == MenuGravity.BOTTOM) {
            title.replace(' ', '\n')
        } else title
    }

    companion object {

        private const val INT_NO_OPTION = -1

        private const val TAG = "MenuItem"

        const val NO_TITLE = ""

    }
}
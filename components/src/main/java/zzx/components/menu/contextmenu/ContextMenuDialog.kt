package zzx.components.menu.contextmenu

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import zzx.components.R
/**
 * ContextMenuDialog, this dialog is used to show menu items for some actions
 * with pretty animation
 * create by zzx
 * create at 19-3-21
 */
open class ContextMenuDialog: DialogFragment() {

    private val menuItemList = mutableListOf<MenuItem>()

    var onItemClickListener: ((MenuItem, Int) -> Unit)? =  null
    var onItemLongClickListener: ((MenuItem, Int) -> Unit)? = null

    private var openAnimatorSet: AnimatorSet? = null
    private var closeAnimatorSet: AnimatorSet? = null

    var gravity = MenuGravity.END
    var delay = DEFAULT_DELAY
    var duration = DEFAULT_DURATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.MenuDialogStyle)
    }

    /**
     * create view and create item layout
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        if (gravity == MenuGravity.START || gravity == MenuGravity.END) {
            inflater.inflate(R.layout.dialog_context_menu_vertical, container, false)
        } else {
            inflater.inflate(R.layout.dialog_context_menu_horizontal, container, false)
        }.apply {
            dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            findViewById<LinearLayout>(R.id.menuWrapper).apply {
                for (item in menuItemList) {
                    item.checkParams(context)
                    addView(item.getItemLayout(
                        this, this@ContextMenuDialog.gravity,
                        item == menuItemList.last()
                    ).apply {
                        setOnClickListener {
                            showCloseAnimation(indexOfChild(it), ON_ITEM_CLICK)
                        }
                        setOnLongClickListener {
                            showCloseAnimation(indexOfChild(it), ON_ITEM_LONG_CLICK)
                            return@setOnLongClickListener true
                        }
                    })
                }
            }
        }


    /**
     * start to show open animation
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getView()?.postDelayed({
            showOpenAnimation()
        }, delay)
    }

    /**
     * open animation should be just the same animation when dialog displaying
     */
    private fun showOpenAnimation() {
        openAnimatorSet = AnimatorSet().apply {
            val animatorList = mutableListOf<AnimatorSet>().apply {
                duration = this@ContextMenuDialog.duration
                for (menuItem in menuItemList) {
                    menuItem.setOpenAnimator(gravity, menuItem == menuItemList[0])
                    add(menuItem.openAnimator!!)
                }
            }
            playSequentially(animatorList as List<Animator>?)
            addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    disableItemsClickable()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    enableItemsClickable()
                }
            })
            start()
        }
    }

    private fun disableItemsClickable() {
        for (menuItem in menuItemList) {
            menuItem.itemLayout?.isClickable = false
            menuItem.itemLayout?.isLongClickable = false
        }
    }

    private fun enableItemsClickable() {
        for (menuItem in menuItemList) {
            menuItem.itemLayout?.isClickable = true
            menuItem.itemLayout?.isLongClickable = true
        }
    }

    /**
     * close animation should be created by the clicked view index
     * after the animation finished, dismiss the dialog, and notify
     * the [onItemClickListener] or [onItemLongClickListener]
     */
    private fun showCloseAnimation(clickIndex: Int, clickType: Int) {
        closeAnimatorSet = AnimatorSet().apply {
            val animatorList = mutableListOf<AnimatorSet>()
            var i = 0
            //first set each item close animator
            for (menuItem in menuItemList) {
                menuItem.setCloseAnimator(gravity, clickIndex)
            }
            //then, combine each item close animator by clicked item index
            while (i < clickIndex && (menuItemList.size - 1- i) > clickIndex) {
                animatorList.add(AnimatorSet().apply {
                    playTogether(menuItemList[i].closeAnimator,
                        menuItemList[menuItemList.size - 1 - i].closeAnimator)
                })
                i++
            }
            i--
            if ((i + 1) < clickIndex) {
                for (j in (i + 1) until clickIndex) {
                    animatorList.add(menuItemList[j].closeAnimator!!)
                }
            }
            if ((menuItemList.size - 1 - i - 1) > clickIndex) {
                for (j in menuItemList.size - 1 - i - 1 downTo clickIndex + 1) {
                    animatorList.add(menuItemList[j].closeAnimator!!)
                }
            }
            animatorList.add(menuItemList[clickIndex].closeAnimator!!)
            playSequentially(animatorList as List<AnimatorSet>)
        }.apply {
            duration = this@ContextMenuDialog.duration
            addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    disableItemsClickable()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    enableItemsClickable()
                    dismiss()
                    if (clickType == ON_ITEM_CLICK) {
                        onItemClickListener?.invoke(menuItemList[clickIndex], clickIndex)
                    } else if (clickType == ON_ITEM_LONG_CLICK) {
                        onItemLongClickListener?.invoke(menuItemList[clickIndex], clickIndex)
                    }
                }
            })
            start()
        }
    }

    /**
     * add item with drawable resources id, and without title
     */
    fun add(@DrawableRes resId: Int): ContextMenuDialog {
        return add(MenuItem.NO_TITLE, resId)
    }

    /**
     * add item with title string and drawable resources id
     */
    fun add(title: String, @DrawableRes resId: Int): ContextMenuDialog {
        return apply {
            menuItemList.add(MenuItem().apply {
                this.title = title
                this.imageResourcesId = resId
            })
        }
    }

    /**
     * add item with drawable, and without title
     */
    fun add(drawable: Drawable): ContextMenuDialog {
        return add(MenuItem.NO_TITLE, drawable)
    }

    /**
     * add item with title string and drawable
     */
    fun add(title: String, drawable: Drawable): ContextMenuDialog {
        return apply {
            menuItemList.add(MenuItem().apply {
                this.title = title
                this.imageDrawable = drawable
            })
        }
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        if (menuItemList.isEmpty()) {
            Log.e(TAG, "show -> has not set items")
            return
        }
        super.show(manager, tag)
    }

    companion object {

        private const val ON_ITEM_CLICK = 0
        private const val ON_ITEM_LONG_CLICK = 1

        private const val DEFAULT_DURATION = 100L
        private const val DEFAULT_DELAY = 0L

        const val TAG = "ContextMenuDialog"

        @JvmStatic
        fun newInstance(): ContextMenuDialog = ContextMenuDialog()
    }
}
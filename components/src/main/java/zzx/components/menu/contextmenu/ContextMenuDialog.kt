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
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_context_menu.view.*
import zzx.components.R
/**
 * ContextMenuDialog
 * create by zzx
 * create at 19-3-21
 */
open class ContextMenuDialog: DialogFragment() {

    private val menuItemList = mutableListOf<MenuItem>()

    private var openAnimatorSet: AnimatorSet? = null
    private var closeAnimatorSet: AnimatorSet? = null

    var gravity = MenuGravity.END
    var delay = 0L
    var duration = 100L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.MenuDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.dialog_context_menu, container, false).apply {
                dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                menuWrapper.apply {
                    for (item in menuItemList) {
                        item.checkParams(context)
                        addView(item.getItemLayout(this, this@ContextMenuDialog.gravity,
                            item == menuItemList.last()).apply {
                            setOnClickListener {
                                showCloseAnimation(indexOfChild(it))
                                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
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
            start()
        }
    }

    /**
     * close animation should be created by the clicked view index
     */
    private fun showCloseAnimation(clickIndex: Int) {
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
            duration = 100L
            playSequentially(animatorList as List<AnimatorSet>)
            start()
        }.apply {
            addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    dismiss()
                }
            })
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

        const val TAG = "ContextMenuDialog"

        @JvmStatic
        fun newInstance(): ContextMenuDialog = ContextMenuDialog()
    }
}
package zzx.components.menu.contextmenu

import android.animation.ObjectAnimator
import android.view.View
/**
 *  depend on {@see <a href="https://github.com/Yalantis/Context-Menu.Android
 *  /blob/master/lib/src/main/java/com/yalantis/contextmenu/lib/extensions/View.kt">View.kt</a>}
 * this file is used for build some public Animators base on {@link android.view.View},
 * create by zzx
 * create at 19-3-19
 */

/**
 * get animator when view closes horizontally
 */
internal fun View.rotationCloseHorizontal(gravity: MenuGravity): ObjectAnimator {
    val realPivotX = when (gravity) {
        MenuGravity.START -> 0.0f
        MenuGravity.END -> width.toFloat()
    }
    val startDegrees = 0f
    var endDegrees = when (gravity) {
        MenuGravity.START -> 90F
        MenuGravity.END -> -90F
    }
    return ObjectAnimator.ofFloat(this, View.ROTATION_Y, startDegrees, endDegrees).apply {
        pivotX = realPivotX
        pivotY = height.toFloat()
    }
}

/**
 * get animator when view opens horizontally
 */
internal fun View.rotationOpenHorizontal(gravity: MenuGravity): ObjectAnimator {
    val realPivotX = when (gravity) {
        MenuGravity.START -> 0.0f
        MenuGravity.END -> width.toFloat()
    }
    var startDegrees = when (gravity) {
        MenuGravity.START -> 90f
        MenuGravity.END -> -90f
    }
    val endDegree = 0f
    return ObjectAnimator.ofFloat(this, View.ROTATION_Y, startDegrees, endDegree).apply {
        pivotX = realPivotX
        pivotY = height.toFloat()
    }
}

/**
 * get animator when view closes vertically
 * [compare] means index compared with the clicked view
 */
internal fun View.rotationCloseVertical(compare: IndexCompare): ObjectAnimator {
    var realPivotY = when (compare) {
        IndexCompare.SMALLER -> height.toFloat()
        IndexCompare.BIGGER -> 0.0f
        else -> throw IllegalAccessException("close view index must be smaller or bigger!")
    }
    val startDegrees = 0.0f
    val endDegrees = when (compare) {
        IndexCompare.SMALLER -> 90f
        IndexCompare.BIGGER -> -90f
        else -> throw IllegalAccessException("close view index must be smaller or bigger!")
    }
    return ObjectAnimator.ofFloat(this, View.ROTATION_X, startDegrees, endDegrees).apply {
        pivotX = width / 2.0f
        pivotY = realPivotY
    }
}

/**
 * get animator when view open vertically
 */
internal fun View.rotationOpenVertical(): ObjectAnimator =
        ObjectAnimator.ofFloat(this, View.ROTATION_X, -90f, 0f).apply {
            pivotX = width / 2.0f
            pivotY = 0.0f
        }

fun View.alphaOpen(): ObjectAnimator =
        ObjectAnimator.ofFloat(this, View.ALPHA,  0.0f, 1.0f)

internal fun View.alphaClose(): ObjectAnimator =
        ObjectAnimator.ofFloat(this, View.ALPHA, 1.0f, 0.0f)
internal fun View.translationHorizontal(): ObjectAnimator =
    ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0.0f, 100.0f)



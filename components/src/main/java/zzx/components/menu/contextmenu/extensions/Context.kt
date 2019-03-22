package zzx.components.menu.contextmenu.extensions

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat

internal fun Context.isLayoutDirectionRtl() =
        resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

internal fun Context.getCompatColor(@ColorRes resId: Int) =
        ContextCompat.getColor(this, resId)

internal fun Context.getDimension(@DimenRes resId: Int) =
        resources.getDimension(resId).toInt()


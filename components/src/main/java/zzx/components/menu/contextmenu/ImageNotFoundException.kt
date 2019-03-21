package zzx.components.menu.contextmenu

import java.lang.RuntimeException


class ImageNotFoundException: RuntimeException(EXCEPTION_IMAGE_NOT_FOUND) {

    companion object {
        private const val EXCEPTION_IMAGE_NOT_FOUND = "not found any image resources or drawable"
    }

}
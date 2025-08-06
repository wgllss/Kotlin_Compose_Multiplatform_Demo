package com.wx.compose.multiplatform.core.utils


object TImeUtils {

    fun convertLongToTime(seconds: Long): String {
        return "%02d:%02d".format(seconds / 60, seconds % 60)
    }
}
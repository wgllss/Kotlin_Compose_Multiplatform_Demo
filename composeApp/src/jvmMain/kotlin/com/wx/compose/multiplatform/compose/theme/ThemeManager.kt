package com.wx.compose.multiplatform.compose.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.wx.compose.multiplatform.PlatformKVStore.PlatformKVStore

object ThemeManager {
    var skinThemeType by mutableStateOf(PlatformKVStore.getSkinType())

    var skinTheme by mutableStateOf(getColors(skinThemeType))

    private fun getColors(skinType: Int) = when (skinType) {
        0 -> LightColors
        1 -> DarkColors
        2 -> Colors72
        3 -> RedColors
        4 -> defaultColors
        5 -> Colors60
        6 -> Colors61
        7 -> Colors62
        8 -> Colors63
        9 -> Colors65
        10 -> Colors66
        11 -> Colors67
        12 -> Colors68
        13 -> Colors71
        14 -> Colors69
        else -> LightColors
    }
}

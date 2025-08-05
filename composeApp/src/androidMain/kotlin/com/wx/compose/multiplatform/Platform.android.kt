package com.wx.compose.multiplatform

import android.os.Build

class AndroidPlatform() : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val platformName: String = "Android"
    override val downloadPath: String = "自定义"
}

actual fun getPlatform(): Platform = AndroidPlatform()
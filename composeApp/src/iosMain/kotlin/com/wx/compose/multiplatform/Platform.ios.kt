package com.wx.compose.multiplatform

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override val platformName: String = "IOS"

    override val downloadPath: String = "自定义"
}

actual fun getPlatform(): Platform = IOSPlatform()
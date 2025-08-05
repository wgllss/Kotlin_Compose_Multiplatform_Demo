package com.wx.compose.multiplatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }

    fun getOsName() = platform.platformName

    fun getDefaultDownloadPath() = platform.downloadPath
}
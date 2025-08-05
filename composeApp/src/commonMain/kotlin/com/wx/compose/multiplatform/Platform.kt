package com.wx.compose.multiplatform

interface Platform {
    val name: String

    val platformName: String

    val downloadPath: String
}

expect fun getPlatform(): Platform
package com.wx.compose.multiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
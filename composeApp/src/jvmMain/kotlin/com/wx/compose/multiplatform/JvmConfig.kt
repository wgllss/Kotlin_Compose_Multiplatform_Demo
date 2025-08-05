package com.wx.compose.multiplatform

import java.io.File

object JvmConfig {

    val windowWidth = 1032
    val windowHeight = 600

    val defaultDownloadDir = StringBuilder(System.getProperty("user.home")).append(File.separator).append("Downloads").append(File.separator).toString()
}
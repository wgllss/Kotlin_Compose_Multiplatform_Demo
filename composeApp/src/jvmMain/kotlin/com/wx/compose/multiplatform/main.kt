package com.wx.compose.multiplatform

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kotlin_Compose_Multiplatform_Demo",
    ) {
        App()
    }
}
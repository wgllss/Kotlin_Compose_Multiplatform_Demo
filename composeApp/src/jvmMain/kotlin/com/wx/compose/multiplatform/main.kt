package com.wx.compose.multiplatform

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import moe.tlaster.precompose.PreComposeApp
import java.awt.Dimension

fun main() = application {
    val windowState = rememberWindowState().apply {
        size = DpSize(JvmConfig.windowWidth.dp, JvmConfig.windowHeight.dp)
        placement = WindowPlacement.Floating
    }
    var isMaximized by remember { mutableStateOf(false) }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kotlin_Compose_Multiplatform_Demo",
        state = windowState,
        undecorated = true, //是否隐藏显示标题栏
    ) {
        LaunchedEffect(Unit) {
            // 通过扩展函数设置最小尺寸
            window.minimumSize = Dimension(JvmConfig.windowWidth, JvmConfig.windowHeight)
//                window.isResizable = false//禁用窗口大小调整

        }
        LaunchedEffect(isMaximized) {
            if (isMaximized) {
                window.placement = WindowPlacement.Maximized
            } else {
                window.placement = WindowPlacement.Floating
            }
        }
        PreComposeApp {
            JvmApp(this, isMaximized, onCloseRequest = {
                exitApplication()
            }, maxRequest = {
                val b = !isMaximized
                isMaximized = b
            }) {
                windowState.isMinimized = true
            }
        }
    }
}
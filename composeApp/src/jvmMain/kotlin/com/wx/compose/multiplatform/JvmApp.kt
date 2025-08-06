package com.wx.compose.multiplatform

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowScope
import com.wx.compose.multiplatform.compose.theme.ThemeManager
import com.wx.compose.multiplatform.compose.main.VerticalTabHost
import androidx.compose.ui.res.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun JvmApp(scope: WindowScope, isMaximized: Boolean, onCloseRequest: () -> Unit, maxRequest: () -> Unit, minRequest: () -> Unit) {
    MaterialTheme(colorScheme = ThemeManager.skinTheme, content = {
        Surface(
            color = MaterialTheme.colorScheme.background, content = {
                Column {
                    // 官方推荐的窗口拖动方案
                    scope.WindowDraggableArea(Modifier.fillMaxWidth().height(40.dp)) {
                        // 自定义标题栏（可自由控制颜色和样式）
                        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary)) {
                            Text(text = "Kotlin_Compose_Multiplatform_Demo", color = MaterialTheme.colorScheme.onPrimary, fontSize = 20.sp, fontStyle = FontStyle.Italic, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp))

                            Icon(
                                modifier = Modifier.padding(0.dp, 0.dp, 80.dp, 0.dp).clickable {
                                    minRequest.invoke()
                                }.size(40.dp).padding(0.dp, 0.dp, 0.dp, 15.dp).align(Alignment.TopEnd), painter = painterResource("drawable/baseline_minimize_24.xml"), contentDescription = "最小化", tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Icon(
                                modifier = Modifier.padding(0.dp, 0.dp, 40.dp, 0.dp).clickable {
                                    maxRequest.invoke()
                                }.size(40.dp).padding(12.dp).align(Alignment.TopEnd), painter = painterResource(if (isMaximized) "drawable/baseline_close_fullscreen_24.xml" else "drawable/baseline_open_in_full_24.xml"), contentDescription = "放大缩小", tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Icon(
                                modifier = Modifier.size(40.dp).clickable {
                                    onCloseRequest.invoke()
                                }.padding(10.dp).align(Alignment.TopEnd), painter = painterResource("drawable/baseline_close_24.xml"), contentDescription = "关闭", tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    VerticalTabHost()
                }
            })
    })
}
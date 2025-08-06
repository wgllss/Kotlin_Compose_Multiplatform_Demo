package com.wx.compose.multiplatform.compose.toast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun showToastSample() {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(10.dp).fillMaxSize()) {
        Button(
            modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp).width(180.dp).height(40.dp), onClick = {
                scope.launch {
                    ToastManager.showToast("你好，我是 Kotlin Compose Multiplatform Demo")
                }
            }) {
            Text("显示 Toast", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
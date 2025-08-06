package com.wx.compose.multiplatform.compose.fileselect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wx.compose.multiplatform.viewmodels.SettingViewModel
import moe.tlaster.precompose.viewmodel.viewModel


@Composable
fun fileSelectSample() {
    val viewModel = viewModel { SettingViewModel() }
    val clipboard = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val text by viewModel.downloadPath.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = "当前下载路径为：${text}", color = MaterialTheme.colorScheme.secondary, textAlign = TextAlign.Start, modifier = Modifier.width(710.dp).height(50.dp).wrapContentHeight(Alignment.CenterVertically))
            Button(onClick = {
                viewModel.setDownloadPath()
            }, modifier = Modifier.width(180.dp).height(40.dp).align(Alignment.TopEnd)) {
                Text(text = "修改", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
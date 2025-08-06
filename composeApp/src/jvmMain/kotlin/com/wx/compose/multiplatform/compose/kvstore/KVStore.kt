package com.wx.compose.multiplatform.compose.kvstore

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
import com.wx.compose.multiplatform.PlatformKVStore.PlatformKVStore
import com.wx.compose.multiplatform.compose.toast.ToastManager
import kotlinx.coroutines.launch

@Composable
fun KVStoreSample() {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(10.dp).fillMaxSize()) {
        Button(
            modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp).width(180.dp).height(40.dp), onClick = {
//                viewModel.queryShiDaiMusic(text)
                scope.launch {
                    val datas = "AAAA66666"
                    PlatformKVStore.saveStringData("My_Key", datas)
                    ToastManager.showToast(datas)
                }
            }) {
            Text("存入数据", color = MaterialTheme.colorScheme.onPrimary)
        }

        Button(
            modifier = Modifier.padding(10.dp, 10.dp, 0.dp, 0.dp).width(180.dp).height(40.dp), onClick = {
//                viewModel.queryShiDaiMusic(text)
                scope.launch {
                    val datas = PlatformKVStore.getStringData("My_Key", "")
                    ToastManager.showToast(datas)
                }
            }) {
            Text("拿到数据", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
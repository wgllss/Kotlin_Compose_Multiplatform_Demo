package com.wx.compose.multiplatform.compose.sql

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wx.compose.multiplatform.viewmodels.PlayerViewModel

@Composable
fun SqlSample(viewModelMusic: PlayerViewModel) {
    val list by viewModelMusic.playlistStateFlow.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            modifier = Modifier.padding(10.dp, 10.dp, 0.dp, 0.dp).width(180.dp).height(40.dp), onClick = {
                viewModelMusic.loadMusic()
            }) {
            Text("模拟点击网络歌曲播放 等你归来", color = MaterialTheme.colorScheme.onPrimary)
        }
        LazyColumn {
            items(list.size, key = { list[it].musicID }) { index ->
                val item = list[index]
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp).height(30.dp).fillMaxWidth().clickable {
                        viewModelMusic.loadMusic(item)
                    }) {
                    Text(modifier = Modifier.weight(1f).fillMaxHeight(), textAlign = TextAlign.Start, color = if (viewModelMusic.isPlaying && viewModelMusic.currPositon == index) MaterialTheme.colorScheme.primary else Color.Gray, text = item.name, fontSize = 13.sp)
                    Text(modifier = Modifier.width(350.dp).fillMaxHeight(), textAlign = TextAlign.Start, color = if (viewModelMusic.isPlaying && viewModelMusic.currPositon == index) MaterialTheme.colorScheme.primary else Color.Gray, text = item.singer, fontSize = 13.sp)
                    Text(modifier = Modifier.width(40.dp).fillMaxHeight().fillMaxWidth().clickable {
                        viewModelMusic.download(item)
                    }, textAlign = TextAlign.End, color = Color.Gray, text = "下载", fontSize = 13.sp)
                    Text(modifier = Modifier.width(40.dp).fillMaxHeight().clickable {
                        viewModelMusic.remove(index)
                    }, textAlign = TextAlign.End, color = Color.Gray, text = "删除", fontSize = 13.sp)
                }
            }
        }
    }

}
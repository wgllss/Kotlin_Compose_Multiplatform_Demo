package com.wx.compose.multiplatform.compose.sql

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.wx.compose.multiplatform.viewmodels.PlayerViewModel
import com.wx.compose.multiplatform.viewmodels.TestGeDanViewModel
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun SqlSample(viewModelMusic: PlayerViewModel) {
    val viewModel = viewModel { TestGeDanViewModel() }

    val musicList by viewModel.musicList.collectAsState()
    val list by viewModelMusic.playlistStateFlow.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Text("歌单测试3首歌曲", modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 10.dp).height(30.dp).fillMaxWidth().background(MaterialTheme.colorScheme.primary).padding(15.dp, 2.dp, 0.dp, 0.dp), color = MaterialTheme.colorScheme.onPrimary)
        LazyRow {
            items(musicList.size) { index ->
                val item = musicList[index]
                Box(
                    modifier = Modifier.padding(5.dp).size(100.dp).clickable {
                        viewModelMusic.loadMusic(item)
                    }, contentAlignment = Alignment.BottomStart
                ) {

                    AsyncImage(
                        model = item.pic, contentDescription = "Network Image", modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10)), contentScale = ContentScale.Crop
                    )

                    Text(
                        item.name, color = MaterialTheme.colorScheme.onPrimary, fontSize = 11.sp, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10)).background(Color(0x90000000)), textAlign = TextAlign.Center
                    )
                }
            }
        }

        Text("播放列表", modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 10.dp).height(30.dp).fillMaxWidth().background(MaterialTheme.colorScheme.primary).padding(15.dp, 2.dp, 0.dp, 0.dp), color = MaterialTheme.colorScheme.onPrimary)

        LazyColumn {
            items(list.size, key = { list[it].musicID }) { index ->
                val item = list[index]
                Row(
                    horizontalArrangement = Arrangement.End, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp).height(30.dp).fillMaxWidth().clickable {
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
package com.wx.compose.multiplatform.compose.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wx.compose.multiplatform.compose.toast.Toast
import com.wx.compose.multiplatform.compose.viewmodel.TabViewModel
import kotlinx.coroutines.launch

sealed class TabPage(val title: String) {
    object ViewModel : TabPage("ViewModel")
    object SKin : TabPage("换肤皮肤")
    object NetIoFile : TabPage("网络IO文件")
    object Toast : TabPage("Toast")
    object Routers : TabPage("路由")
    object Music : TabPage("音乐")
    object KVSetting : TabPage("KV存储")
    object SQL : TabPage("数据库使用")
    object Video : TabPage("视频播放")
}


@Composable
fun VerticalTabHost() {
    val pages = listOf(
        TabPage.ViewModel,
        TabPage.SKin,
        TabPage.NetIoFile,
        TabPage.Toast,
        TabPage.Routers,
        TabPage.Music,
        TabPage.KVSetting,
        TabPage.SQL,
        TabPage.Video,
    )
    val pagerState = rememberPagerState(pageCount = {
        return@rememberPagerState pages.size
    })
    val coroutineScope = rememberCoroutineScope()
//    val viewModelMusic = viewModel { PlayerViewModel() }

    Row(Modifier.fillMaxSize()) {
        // 左侧竖向标签栏
        Column(
            modifier = Modifier.width(110.dp).fillMaxHeight().background(MaterialTheme.colorScheme.surfaceVariant).verticalScroll(rememberScrollState())
        ) {
            pages.forEachIndexed { index, page ->
                Tab(
                    selected = pagerState.currentPage == index, onClick = {
                        coroutineScope.launch {
//                            pagerState.animateScrollToPage(index)
                            pagerState.scrollToPage(index)
                        }
                    }, modifier = Modifier.fillMaxWidth().height(40.dp).background(
                        if (pagerState.currentPage == index) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.inverseOnSurface
                    ).padding(0.dp),
//                    icon = {
//                    Icon(page.icon, contentDescription = null)
//                },
                    text = {
                        Text(
                            text = page.title, color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary, fontSize = 13.sp, modifier = Modifier.padding(0.dp)
                        )
                    })
            }


        }
        Column {
            // 右侧内容区
            VerticalPager(
                state = pagerState, modifier = Modifier.fillMaxHeight().weight(1f), userScrollEnabled = false
            ) { index ->
                key(index) {
                    when (pages[index]) {
                        TabPage.ViewModel -> TabViewModel()
                        TabPage.SKin -> TabViewModel()
                        TabPage.NetIoFile -> TabViewModel()
                        TabPage.Toast -> TabViewModel()
                        TabPage.Routers -> TabViewModel()
                        TabPage.Music -> TabViewModel()
                        TabPage.KVSetting -> TabViewModel()
                        TabPage.SQL -> TabViewModel()
                        TabPage.Video -> TabViewModel()
                        TabPage.Video -> TabViewModel()
                    }
                }
            }
            Box {
//                ButtomPlay(viewModelMusic)
                Toast()
            }
        }
    }
}


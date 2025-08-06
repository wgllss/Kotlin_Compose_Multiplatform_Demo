package com.wx.compose.multiplatform.compose.main

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wx.compose.multiplatform.compose.kvstore.KVStoreSample
import com.wx.compose.multiplatform.compose.theme.setting
import com.wx.compose.multiplatform.compose.fileselect.fileSelectSample
import com.wx.compose.multiplatform.compose.music.ButtomPlay
import com.wx.compose.multiplatform.compose.router.RouterSample
import com.wx.compose.multiplatform.compose.toast.Toast
import com.wx.compose.multiplatform.compose.sql.SqlSample
import com.wx.compose.multiplatform.compose.video.VideoSample
import com.wx.compose.multiplatform.compose.music.MuSicSample
import com.wx.compose.multiplatform.compose.toast.showToastSample
import com.wx.compose.multiplatform.compose.news.TabViewModel
import com.wx.compose.multiplatform.compose.router.RouterFirst
import com.wx.compose.multiplatform.routers.RouterUrls
import com.wx.compose.multiplatform.viewmodels.PlayerViewModel
import com.wx.compose.multiplatform.viewmodels.TabViewModel1
import com.wx.compose.multiplatform.viewmodels.TabViewModel2
import com.wx.compose.multiplatform.viewmodels.TabViewModel3
import com.wx.compose.multiplatform.viewmodels.TabViewModel4
import com.wx.compose.multiplatform.viewmodels.TabViewModel5
import com.wx.compose.multiplatform.viewmodels.TabViewModel6
import com.wx.compose.multiplatform.viewmodels.TabViewModel7
import com.wx.compose.multiplatform.viewmodels.TabViewModel8
import com.wx.compose.multiplatform.viewmodels.TabViewModel9
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.query
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import moe.tlaster.precompose.viewmodel.viewModel

sealed class TabPage(val title: String) {
    object ViewModel : TabPage("手机")
    object NetIoFile1 : TabPage("新闻")
    object NetIoFile2 : TabPage("娱乐")
    object NetIoFile3 : TabPage("体育")
    object NetIoFile4 : TabPage("时尚")
    object NetIoFile5 : TabPage("财经")
    object NetIoFile6 : TabPage("汽车")
    object NetIoFile7 : TabPage("军事")

    //    object NetIoFile8 : TabPage("科技")
    object SKin : TabPage("换肤皮肤")
    object Toast : TabPage("Toast")
    object FileSelect : TabPage("选择文件")
    object Routers : TabPage("路由")
    object Music : TabPage("音乐播放")
    object KVSetting : TabPage("KV存储")
    object SQL : TabPage("数据库使用")
    object Video : TabPage("视频播放")
}


@Composable
fun VerticalTabHost() {
    val pages = listOf(
        TabPage.ViewModel,
        TabPage.NetIoFile1,
        TabPage.NetIoFile2,
        TabPage.NetIoFile3,
        TabPage.NetIoFile4,
        TabPage.NetIoFile5,
        TabPage.NetIoFile6,
        TabPage.NetIoFile7,
//        TabPage.NetIoFile8,
        TabPage.SKin,
        TabPage.Toast,
        TabPage.FileSelect,
        TabPage.KVSetting,
        TabPage.Routers,
        TabPage.SQL,
        TabPage.Music,
        TabPage.Video,
    )
    val pagerState = rememberPagerState(pageCount = {
        return@rememberPagerState pages.size
    })
    val coroutineScope = rememberCoroutineScope()
    val viewModelMusic = viewModel { PlayerViewModel() }

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
                        TabPage.ViewModel -> TabViewModel("BAI6I0O5wangning", viewModel { TabViewModel1() })
                        TabPage.NetIoFile1 -> TabViewModel("BBM54PGAwangning", viewModel { TabViewModel2() })
                        TabPage.NetIoFile2 -> TabViewModel("BA10TA81wangning", viewModel { TabViewModel3() })
                        TabPage.NetIoFile3 -> TabViewModel("BA8E6OEOwangning", viewModel { TabViewModel4() })
                        TabPage.NetIoFile4 -> TabViewModel("BA8F6ICNwangning", viewModel { TabViewModel5() })
                        TabPage.NetIoFile5 -> TabViewModel("BA8EE5GMwangning", viewModel { TabViewModel6() })
                        TabPage.NetIoFile6 -> TabViewModel("BA8DOPCSwangning", viewModel { TabViewModel7() })
                        TabPage.NetIoFile7 -> TabViewModel("BAI67OGGwangning", viewModel { TabViewModel8() })
//                        TabPage.NetIoFile8 -> TabViewModel("BA8D4A3Rwangning", viewModel { TabViewModel9() })
                        TabPage.SKin -> setting()
                        TabPage.FileSelect -> fileSelectSample()
                        TabPage.Toast -> showToastSample()
                        TabPage.Routers -> NavGraph()
                        TabPage.KVSetting -> KVStoreSample()
                        TabPage.SQL -> SqlSample(viewModelMusic)
                        TabPage.Music -> MuSicSample(viewModelMusic)
                        TabPage.Video -> VideoSample()
                    }
                }
            }
            Box {
                ButtomPlay(viewModelMusic)
                Toast()
            }
        }
    }
}

@Composable
fun NavGraph() {
    val navigator = rememberNavigator("key222222")
    val viewModel = viewModel { TabViewModel9() }
    NavHost(
        navigator = navigator, navTransition = remember {
            NavTransition(
                createTransition = fadeIn(),
                destroyTransition = fadeOut(),
                pauseTransition = fadeOut(),
                resumeTransition = fadeIn(),
            )
        }, initialRoute = RouterUrls.news_first
    ) {
        scene(RouterUrls.news_first) {
            RouterFirst("BA8D4A3Rwangning", viewModel) {
                navigator.navigate("${RouterUrls.news_second}?title=${it.title}&docid=${it.docid}")
            }
        }
        scene(RouterUrls.news_second) { backStackEntry ->
            val title = backStackEntry.query<String>("title") ?: ""
            val docid = backStackEntry.query<String>("docid") ?: ""
            RouterSample(navigator, docid, title)
        }
    }
}


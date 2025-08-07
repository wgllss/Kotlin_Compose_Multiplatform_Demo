package com.wx.compose.multiplatform.viewmodels

import com.wx.compose.multiplatform.core.utils.LrcTest
import com.wx.compose.multiplatform.dataSoruce.data.MusicItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import moe.tlaster.precompose.viewmodel.ViewModel

class TestGeDanViewModel : ViewModel() {
    private val _playlistFlow = MutableStateFlow(mutableListOf<MusicItem>())
    val musicList = _playlistFlow.asStateFlow()

    init {
        //模拟数据，可以是网络数据
        val list = mutableListOf(
            MusicItem(
                "152222",
                "等你归来",
                "程响",
                "http://imge.kugou.com/stdmusic/120/20250228/20250228162415988157.jpg",
                "https://gitee.com/wgllss888/WXDynamicPlugin/raw/main/WX-Resource/music/%E7%AD%89%E4%BD%A0%E5%BD%92%E6%9D%A5-%E7%A8%8B%E5%93%8D.mp3",
                LrcTest.lrcTest,
                ".mp3"
            ),
            MusicItem(
                "152225",
                "江南",
                "林俊杰",
                "http://imge.kugou.com/stdmusic/120/20250221/20250221180752107045.jpg",
                "https://gitee.com/wgllss888/WXDynamicPlugin/raw/main/WX-Resource/music/%E6%B1%9F%E5%8D%97-%E6%9E%97%E4%BF%8A%E6%9D%B0.mp3",
                LrcTest.lrcTest3,
                ".mp3"
            ),
            MusicItem(
                "152226",
                "起风了(live)",
                "林俊杰",
                "https://p3fx.kgimg.com//uploadpic//softhead//120//20230420//20230420151846938260.jpg",
                "https://gitee.com/wgllss888/WXDynamicPlugin/raw/main/WX-Resource/music/%E6%9E%97%E4%BF%8A%E6%9D%B0%20-%20%E8%B5%B7%E9%A3%8E%E4%BA%86%20(Live)-%E6%9E%97%E4%BF%8A%E6%9D%B0.mp3",
                LrcTest.lrcTest2,
                ".mp3"
            )
        )
        _playlistFlow.value = list
    }
}
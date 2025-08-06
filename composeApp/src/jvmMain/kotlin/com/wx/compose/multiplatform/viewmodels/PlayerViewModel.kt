package com.wx.compose.multiplatform.viewmodels

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.wx.compose.multiplatform.PlatformKVStore.PlatformKVStore
import com.wx.compose.multiplatform.compose.toast.ToastManager
import com.wx.compose.multiplatform.core.utils.DownLoadUtils
import com.wx.compose.multiplatform.core.utils.LrcTest
import com.wx.compose.multiplatform.core.utils.OKIOUtils
import com.wx.compose.multiplatform.core.utils.TImeUtils.convertLongToTime
import com.wx.compose.multiplatform.dataSoruce.data.LrcEntry
import com.wx.compose.multiplatform.dataSoruce.data.MusicItem
import com.wx.compose.multiplatform.system_feature.music.JavaFixAudioPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import java.io.File

class PlayerViewModel : ViewModel() {
    private val player = JavaFixAudioPlayer(viewModelScope)

    var currentTime by mutableStateOf("0:00")
    var totalTime by mutableStateOf("0:00")

    //当前Lrc 所在时间位置
    var currentTimeLrc by mutableStateOf(0L)


    val lyrics: State<List<LrcEntry>> = player._lyrics

    //当前Lrc 高亮位置
    var currentLrcPos by mutableStateOf(0)

    var playType by mutableIntStateOf(PlatformKVStore.getPlayType())

    var isPlaying by mutableStateOf(false)
    var volume by mutableStateOf(70)
    var progress by mutableStateOf(0f)
    private var duration by mutableStateOf(0L)
    val currentMusicName: StateFlow<String> = player._currentMusicName.asStateFlow()
    val currentPicUrl: StateFlow<String> = player._currentMusicPicUrl.asStateFlow()

    //播放列表
    val playlistStateFlow = player._playlistFlow.asStateFlow()
    val spectrumDataFlow = player._spectrumDataFlow.asStateFlow()
    var currPositon by mutableStateOf(0)

    // disk旋转动画
    val sheetDiskRotate by mutableStateOf(Animatable(0f))

    // 上一次disk旋转角度
    var lastSheetDiskRotateAngleForSnap = 0f


    init {
        viewModelScope.launch {
            player.autoShowHistoryPlayPosition()
        }
        player.currentTime.onEach {
            isPlaying = player.isPlaying()
            currPositon = player.currIndex
            if (isPlaying) {
                currentTimeLrc = it
                updateTime(it)
                currentTime = convertLongToTime(it)
                if (player.totalDuration() > 0) {
                    progress = (it * 1.00f / player.totalDuration())
                }
                if (duration != player.totalDuration()) {
                    duration = player.totalDuration()
                    totalTime = convertLongToTime(duration)
                }
            }
        }.catch {
            it.printStackTrace()
        }.launchIn(viewModelScope)
    }

    fun loadMusic() {
        val lrc = LrcTest.lrcTest
        val playItem = MusicItem("152222", "等你归来", "程响", "http://imge.kugou.com/stdmusic/120/20250228/20250228162415988157.jpg", "https://gitee.com/wgllss888/WXDynamicPlugin/raw/main/WX-Resource/music/%E7%AD%89%E4%BD%A0%E5%BD%92%E6%9D%A5-%E7%A8%8B%E5%93%8D.mp3", lrc, ".mp3")
        loadMusic(playItem)
    }

    @Synchronized
    fun loadMusic(playItem: MusicItem) {
        player.load(playItem)
        isPlaying = player.isPlaying()
    }

    fun togglePlay() = viewModelScope.launch {
        if (player.isPlaying()) player.pause() else player.play()
        isPlaying = !player.isPlaying()
    }

    fun switch() {
        viewModelScope.launch {
            val playTypeStyle = when (playType) {
                0 -> {
                    ToastManager.showToast("单曲循环")
                    1
                }

                1 -> {
                    ToastManager.showToast("随机播放")
                    2
                }

                2 -> {
                    ToastManager.showToast("列表循环")
                    0
                }

                else -> {
                    ToastManager.showToast("列表循环")
                    0
                }
            }
            playType = playTypeStyle
            PlatformKVStore.savePlayType(playTypeStyle)
        }
    }

    fun seekTo(float: Float) = viewModelScope.launch {
        player.seekTo((float * 1f * duration).toDouble())
        isPlaying = !player.isPlaying()
    }

    fun seekTo2(float: Float) {
        player.seekTo(float.toDouble())
        isPlaying = !player.isPlaying()
    }

    fun prev() = viewModelScope.launch {
        player.prev()
    }

    fun next() = viewModelScope.launch {
        player.next()
    }


    fun updateTime(timeMs: Long) {
        currentTimeLrc = 1000 * timeMs
        player._lyrics.value?.indexOfFirst {
            it.timeMs >= currentTimeLrc
        }?.takeIf { it >= 0 }?.let { index ->
            currentLrcPos = index
        }
    }

    fun download() {
        player.getCurrItem()?.let { download(it) }
    }

    fun download(item: MusicItem) {
        viewModelScope.launch {
            val downloadFileName = StringBuilder(item.name).append("-").append(item.singer).toString()
            flow {
                val downloads = PlatformKVStore.getDownloadDir()
                item.run {
                    OKIOUtils.writeJson(
                        lrc, File(downloads, "${downloadFileName}.lrc").absolutePath
                    )
                    val file = File(downloads, "${downloadFileName}${musicSuffer}")
                    DownLoadUtils.instance.download(url, file)
                }
                emit(0)
            }.flowOn(Dispatchers.IO).catch {
                ToastManager._errorText.value = "${downloadFileName}下载失败"
            }.collect {
                ToastManager._errorText.value = "${downloadFileName}下载成功"
            }
        }
    }

    fun remove(index: Int) = player.remove(index)
}
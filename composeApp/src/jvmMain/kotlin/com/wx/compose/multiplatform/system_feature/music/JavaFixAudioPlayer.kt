package com.wx.compose.multiplatform.system_feature.music

import androidx.compose.runtime.mutableStateOf
import com.wx.compose.multiplatform.PlatformKVStore.PlatformKVStore
import com.wx.compose.multiplatform.compose.toast.ToastManager
import com.wx.compose.multiplatform.core.utils.DownLoadUtils
import com.wx.compose.multiplatform.core.utils.LinkedListUtils.removeConsecutiveDuplicates
import com.wx.compose.multiplatform.dataSoruce.data.LrcEntry
import com.wx.compose.multiplatform.dataSoruce.data.LrcParser
import com.wx.compose.multiplatform.dataSoruce.data.MusicItem
import com.wx.compose.multiplatform.system_feature.sql.DatabaseHelper
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.media.AudioSpectrumListener
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.net.URL
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap
import javax.swing.SwingUtilities
import kotlin.random.Random

class JavaFixAudioPlayer(val scope: CoroutineScope) {

    private var mediaPlayer: MediaPlayer? = null
    private val playlist = mutableListOf<MusicItem>( )

    val _currentMusicName = MutableStateFlow("")
    val _currentMusicPicUrl = MutableStateFlow("")
    private val _currentTime = MutableStateFlow(0L)
    val currentTime: StateFlow<Long> = _currentTime.asStateFlow()
    var currIndex = 0

    val _playlistFlow = MutableStateFlow(mutableListOf<MusicItem>().apply { addAll(playlist) })
    private var pendingSeek: Double? = null
    private val sb = StringBuilder()

    private val spectrumData by lazy { LinkedList<Float>() }
    private val spectrumDataTemp by lazy { LinkedList<Float>() }
    private val tempList2 = LinkedList<Float>()

    //保证线程安全
    private val map = ConcurrentHashMap<String, String>()

    val _spectrumDataFlow = MutableStateFlow(LinkedList<Float>().apply { addAll(spectrumData) })

    val _lyrics = mutableStateOf(emptyList<LrcEntry>())

    private val dbHelp by lazy { DatabaseHelper() }


    init {
        Platform.startup { }
    }

    fun getCurrItem(): MusicItem? {
        return playlist.takeIf { it.size > currIndex && currIndex >= 0 }?.get(currIndex)
    }

    fun load(playItem: MusicItem) {
        playItem.run {
            if (map.containsKey(musicID)) {
                return
            } else {
                map.clear()
                map[musicID] = ""
            }
            if (playlist.size > 0) {
                if (isPlaying() && currIndex < playlist.size && playlist[currIndex].musicID == musicID) {
                    //正在播放该歌曲
                    ToastManager._errorText.value = "正在播放该歌曲"
                    return
                }
                currIndex++
            }
            playlist.takeUnless { it.contains(this) }?.let { list ->
                list.add(currIndex, this)
                scope.launch {
                    dbHelp.inserPlayItem(playItem)
                }
            }
            playlist.takeIf { it.contains(this) }?.let {
                currIndex = it.indexOf(this)
                PlatformKVStore.saveCurrentPlay(currIndex)
            }
            _playlistFlow.update {
                mutableListOf<MusicItem>().apply {
                    addAll(playlist)
                }
            }
            scope.launch {
                sb.delete(0, sb.toString().length)
                _currentMusicName.emit(sb.append(name).append(" - ").append(singer).toString())
                _currentMusicPicUrl.emit(pic)
            }
            playPosition(this)
        }
    }

    private fun playPosition(playItem: MusicItem, isStart: Boolean = false) {
        SwingUtilities.invokeLater {
            JFXPanel().apply {
                mediaPlayer?.stop()
                playItem.takeIf { it.url.isNotEmpty() }?.run {
                    val downloadFileName = StringBuilder(name).append("-").append(singer).append(musicSuffer).toString()
                    val cacheFile = if (!localFile) {
                        File(PlatformKVStore.getDownloadDir(), downloadFileName).apply {
                            DownLoadUtils.instance.WXDownload2(playItem.url, this@apply)
                        }
                    } else {
                        println("zou huan cun ")
                        File(PlatformKVStore.getDownloadDir(), downloadFileName)
                    }

                    val media = Media(cacheFile.toURI().toString())
                    mediaPlayer = MediaPlayer(media).apply {
                        audioSpectrumInterval = 0.03
                        audioSpectrumNumBands = 320
                        audioSpectrumThreshold = -60
//                    bufferProgressTimeProperty().addListener { _, _, bufferedTime ->
//                        println("huan chong dao : ${bufferedTime.toSeconds()} s")
//                    }
                        setOnReady {
                            playItem.localFile = true
                            println("huan chong wan cheng ")
                            dbHelp.updateField(musicID)
                            if (!isStart) play()
                            updateTime()
                        }
                        setOnPlaying { startTimeSync() }
                        setOnEndOfMedia {
                            val type = PlatformKVStore.getPlayType()
                            when (type) {
                                0 -> next()//列表循环
                                1 -> {
                                    //单曲循环
                                    playPosition()
                                }

                                2 -> {//随机播放
                                    currIndex = Random.nextInt(playlist.size).also { r ->
                                        if (r >= 0 && r < playlist.size) r else 0
                                    }
                                    playPosition()
                                }

                                else -> next()
                            }
                        }

                        audioSpectrumListener = AudioSpectrumListener { _, _, magnitudes, phases ->
                            spectrumData.clear()
                            spectrumDataTemp.clear()
                            tempList2.clear()

                            magnitudes.forEach { it ->
                                if (it > -60.0f) {
                                    spectrumDataTemp.add(it)
                                }
                                spectrumData.add(it)
//                                spectrumData.add((it + 60).coerceIn(0f, 60f)) // 标准化到0-60范围
                            }
                            spectrumData.takeIf { it.size > 60 }?.removeConsecutiveDuplicates(8)

                            val startPosition = 5 * spectrumDataTemp.size / 21
                            val endPosition = 35 * spectrumDataTemp.size / 50
                            spectrumDataTemp.forEachIndexed { index, fl ->
                                if (index in (startPosition + 1)..<endPosition) {
                                    tempList2.add(fl)
                                }
                            }
                            tempList2.forEach {
                                spectrumData.add(0, it)
                            }
                            _spectrumDataFlow.update {
                                LinkedList<Float>().apply {
                                    addAll(spectrumData)
                                }
                            }
                        }
                    }
                    _lyrics.value = LrcParser.parseLrc(lrc)
                }
            }
        }
//        }
    }

    fun isPlaying() = mediaPlayer?.status == MediaPlayer.Status.PLAYING

    fun play() = mediaPlayer?.play()
    fun pause() = mediaPlayer?.pause()

    fun seekTo(seconds: Double) {
        Platform.runLater {
            when (mediaPlayer?.status) {
                MediaPlayer.Status.READY, MediaPlayer.Status.PLAYING -> {
                    mediaPlayer?.seek(Duration.seconds(seconds))
                    mediaPlayer?.play() // 必须启动播放才能生效
                }

                else -> {
                    pendingSeek = seconds
                    mediaPlayer?.setOnReady {
                        mediaPlayer?.seek(Duration.seconds(seconds))
                        mediaPlayer?.play() // 必须启动播放才能生效
                    }
                }
            }
        }
    }

    fun prev() {
        currIndex--
        playPosition()
    }

    fun next() {
        currIndex++
        playPosition()
    }

    /**
     * 启动时候，展示上次播放位置
     */
    suspend fun autoShowHistoryPlayPosition() {
        dbHelp.getAllPlayList().takeIf { it.isNotEmpty() }?.let {
            playlist.addAll(it)
            _playlistFlow.update {
                mutableListOf<MusicItem>().apply {
                    addAll(playlist)
                }
            }
        }
        PlatformKVStore.getCurrentPlay().takeIf { it >= 0 && it < playlist.size }?.let {
            currIndex = it
            sb.delete(0, sb.toString().length)
            playlist[currIndex].takeIf { it.url.isNotEmpty() }?.run {
                _currentMusicName.emit(sb.append(name).append(" - ").append(singer).toString())
                _currentMusicPicUrl.emit(pic)
                playPosition(this, true)
            }
        }
    }

    private fun playPosition() {
        if (currIndex < 0) {
            currIndex = playlist.size - 1
        }
        if (currIndex > playlist.size - 1) {
            currIndex = 0
        }
        val playItem = playlist[currIndex]
        scope.launch {
            playItem.run {
                sb.delete(0, sb.toString().length)
                _currentMusicName.emit(sb.append(name).append(" - ").append(singer).toString())
                _currentMusicPicUrl.emit(pic)
                PlatformKVStore.saveCurrentPlay(currIndex)
            }
        }
        playPosition(playItem)
    }

    fun stop() = mediaPlayer?.stop()

    fun totalDuration(): Long = mediaPlayer?.totalDuration?.toSeconds()?.toLong() ?: 0L

    private fun startTimeSync() {
        scope.launch {
            while (mediaPlayer?.status == MediaPlayer.Status.PLAYING) {
                delay(500)
                updateTime()
            }
        }
    }

    private fun updateTime() {
        Platform.runLater {
            val time = mediaPlayer?.currentTime?.toSeconds()?.toLong() ?: 0L
            scope.launch {
                _currentTime.emit(time)
            }
        }
    }

    fun remove(index: Int) {
        if (isPlaying() && index == currIndex) {
            ToastManager._errorText.value = "正在播放不能删除该条目"
            return
        }
        playlist.takeIf { it.size > index }?.let {
            dbHelp.delItemByID(it.get(index).musicID)
            it.removeAt(index)
        }
        _playlistFlow.update {
            mutableListOf<MusicItem>().apply {
                addAll(playlist)
            }
        }
        if (currIndex > index) {
            currIndex -= 1
        }
    }
}
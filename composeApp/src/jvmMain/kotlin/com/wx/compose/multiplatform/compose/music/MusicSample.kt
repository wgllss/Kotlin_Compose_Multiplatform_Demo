package com.wx.compose.multiplatform.compose.music

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.wx.compose.multiplatform.JvmConfig.DISK_ROTATE_ANIM_CYCLE
import com.wx.compose.multiplatform.viewmodels.PlayerViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun MuSicSample(viewModel: PlayerViewModel) {
    val currentPicUrl by viewModel.currentPicUrl.collectAsState()
    Row {
        Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
            RecordImage(viewModel, currentPicUrl)
//            OutwardSpectrumVisualizer(viewModel)
            CircularAudioVisualizer(viewModel)
            AudioVisualizer(viewModel)
        }
        LrcPlayerScreen(viewModel)
    }
}

@Composable
fun AudioVisualizer(viewModel: PlayerViewModel) {
    var canvasWidth by remember { mutableFloatStateOf(0f) }
    val spectrumData by viewModel.spectrumDataFlow.collectAsState()
    Canvas(modifier = Modifier.padding(10.dp, 430.dp, 0.dp, 0.dp).width(300.dp).onSizeChanged { canvasWidth = it.width.toFloat() }) {
        if (spectrumData.isNotEmpty()) {
            drawRect(Color.Transparent)
            val barWidth = canvasWidth / spectrumData.size
            spectrumData.forEachIndexed { i, value ->

//                val newValue = value //if (value == -60.0f) randomInRange(-60f, -45f) else value
                val newValue = if (value == -60.0f) randomInRange(-60f, -45f) else value
                val height = (newValue + 60) * 3f  // 标准化幅度值
                val newIndex = i //if (i > haftSize) (i + haftSize) % dataSize else i + haftSize
                drawRect(
                    color = Color.hsv(newIndex * 360f / spectrumData.size, 1f, 1f), topLeft = Offset(newIndex * barWidth, size.height - height), size = Size(barWidth * 0.8f, height)
                )
            }
        }
    }
}

fun randomInRange(min: Float, max: Float): Float {
    return min + Random.nextFloat() * (max - min)
}

@Composable
fun CircularAudioVisualizer(viewModel: PlayerViewModel) {
    var center by remember { mutableStateOf(Offset.Zero) }
    var baseRadius by remember { mutableStateOf(0f) }
    val spectrumData by viewModel.spectrumDataFlow.collectAsState()
    Canvas(modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 80.dp).size(280.dp).onSizeChanged {
        center = Offset(it.width.toFloat() / 2, it.height.toFloat() / 2)
        baseRadius = min(it.width, it.height) * 0.55f
    }) {
        if (spectrumData.isNotEmpty()) {
            drawCircle(Color.Transparent, baseRadius, center, style = Stroke(2f))
            val angleStep = 2f * PI / spectrumData.size
            spectrumData.forEachIndexed { i, value ->
                val newValue = value //if (value == -60.0f) randomInRange(-60f, -46f) else value
                val normalized = newValue * 2 / 60f

                val spikeLength = baseRadius * 0.08f * normalized
                val angle = i * angleStep

                val startX = center.x + baseRadius * cos(angle).toFloat()
                val startY = center.y + baseRadius * sin(angle).toFloat()


                val endX = startX + spikeLength * cos(angle).toFloat()
                val endY = startY + spikeLength * sin(angle).toFloat()

                drawLine(
                    color = Color.hsv(i * 360f / spectrumData.size, 1f, 1f), start = Offset(startX, startY), end = Offset(endX, endY), strokeWidth = 6f
                )

                drawCircle(
                    color = Color.hsv(i * 360f / spectrumData.size, 1f, 1f), radius = 4f, center = Offset(endX, endY)
                )
            }
        }
    }
}

@Composable
fun RecordImage(viewModel: PlayerViewModel, picUrl: String) {
    LaunchedEffect(viewModel.currPositon) {
        viewModel.lastSheetDiskRotateAngleForSnap = 0f
        viewModel.sheetDiskRotate.snapTo(viewModel.lastSheetDiskRotateAngleForSnap)
    }

    LaunchedEffect(viewModel.isPlaying) {
        if (viewModel.isPlaying) {
            viewModel.sheetDiskRotate.animateTo(
                targetValue = 360f + viewModel.lastSheetDiskRotateAngleForSnap, animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = DISK_ROTATE_ANIM_CYCLE, easing = LinearEasing), repeatMode = RepeatMode.Restart
                )
            ) {
                viewModel.lastSheetDiskRotateAngleForSnap = viewModel.sheetDiskRotate.value
            }
        } else {
            viewModel.sheetDiskRotate.snapTo(viewModel.lastSheetDiskRotateAngleForSnap)
            viewModel.sheetDiskRotate.stop()
        }
    }

    AsyncImage(
        model = picUrl, contentDescription = "唱片 Network Image", modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 80.dp).size(300.dp).clip(CircleShape).border(
            width = 45.dp, color = Color.Black, shape = CircleShape
        ).padding(45.dp).graphicsLayer {
            rotationZ = viewModel.sheetDiskRotate.value
        }, contentScale = ContentScale.Crop
    )
}


@Composable
fun LrcPlayerScreen(viewModel: PlayerViewModel) {
    val lyrics by viewModel.lyrics
    val scrollState = rememberLazyListState()
    var heightDiv by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(viewModel.currentLrcPos) {
        val layoutInfo = scrollState.layoutInfo
        if (layoutInfo.visibleItemsInfo.isNotEmpty()) {
            heightDiv = layoutInfo.viewportSize.height / 2f
            scrollState.animateScrollToItem(
                index = viewModel.currentLrcPos, scrollOffset = -(layoutInfo.viewportSize.height / 2) + 20 // 居中偏移
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState, modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, userScrollEnabled = false  // 关键参数
        ) {
            items(lyrics.size) { index ->
                val entry = lyrics[index]
                val isActive = viewModel.currentTimeLrc >= entry.timeMs && (lyrics.getOrNull(lyrics.indexOf(entry) + 1)?.timeMs ?: Long.MAX_VALUE) > viewModel.currentTimeLrc

                Text(
                    fontSize = if (isActive) 18.sp else 13.sp, text = entry.text, modifier = Modifier.padding(8.dp).fillMaxWidth(), color = if (isActive) MaterialTheme.colorScheme.primary else Color.Gray, textAlign = TextAlign.Center
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtomPlay(viewModelMusic: PlayerViewModel) {
    var progress by remember(viewModelMusic.progress) { mutableStateOf(viewModelMusic.progress) }
    val currName by viewModelMusic.currentMusicName.collectAsState()
    val playTypeUI = when (viewModelMusic.playType) {
        0 -> "drawable/baseline_repeat_24.xml"
        1 -> "drawable/baseline_repeat_one_24.xml"
        2 -> "drawable/baseline_shuffle_24.xml"
        else -> "drawable/baseline_repeat_24.xml"
    }

    Column(modifier = Modifier.fillMaxWidth().height(100.dp)) {
        Text(maxLines = 1, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 0.dp).fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 20.sp, text = "${currName}")
        Row(
            modifier = Modifier.fillMaxWidth().height(40.dp), horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp).size(40.dp).clickable {
                    viewModelMusic.switch()
                }.padding(5.dp), painter = painterResource(playTypeUI), contentDescription = "播放模式", tint = MaterialTheme.colorScheme.primary
            )

            Icon(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp).size(40.dp).clickable {
                    viewModelMusic.prev()
                }, painter = painterResource("drawable/baseline_skip_previous_24.xml"), contentDescription = "上一首", tint = MaterialTheme.colorScheme.primary
            )
            Icon(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp).size(40.dp).clickable {
                    viewModelMusic.togglePlay()
                }, painter = painterResource(if (viewModelMusic.isPlaying) "drawable/baseline_pause_24.xml" else "drawable/baseline_play_arrow_24.xml"), contentDescription = "播放暂停", tint = MaterialTheme.colorScheme.primary
            )
            Icon(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp).size(40.dp).clickable {
                    viewModelMusic.next()
                }, painter = painterResource("drawable/baseline_skip_next_24.xml"), contentDescription = "下一首", tint = MaterialTheme.colorScheme.primary
            )

            Icon(
                modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp).size(40.dp).clickable {
                    viewModelMusic.download()
                }.padding(6.dp), painter = painterResource("drawable/baseline_cloud_download_24.xml"), contentDescription = "下载", tint = MaterialTheme.colorScheme.primary
            )
        }

        Row(modifier = Modifier.fillMaxWidth().height(20.dp), horizontalArrangement = Arrangement.Center) {
            Text(color = MaterialTheme.colorScheme.primary, modifier = Modifier.width(30.dp).height(20.dp), textAlign = TextAlign.Center, fontSize = 10.sp, text = "${viewModelMusic.currentTime}s")
            // 进度条
            Slider(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp).height(20.dp).width(500.dp), value = progress, onValueChange = {
                progress = it
            }, onValueChangeFinished = {
                viewModelMusic.seekTo(progress)
            }, track = { sliderState ->
                SliderDefaults.Track(
                    modifier = Modifier.height(1.dp).padding(0.dp, 3.dp, 0.dp, 0.dp), colors = SliderDefaults.colors(
                        thumbColor = Color.Magenta,          // 滑块颜色
                        activeTrackColor = MaterialTheme.colorScheme.primary,   // 激活轨道颜色
                        inactiveTrackColor = Color.Gray  // 未激活轨道颜色
                    ), enabled = true, sliderState = sliderState
                )
            }, thumb = {  // 自定义滑块形状
                SliderDefaults.Thumb(
                    modifier = Modifier.padding(0.dp, 3.dp, 0.dp, 0.dp).height(20.dp), interactionSource = remember { MutableInteractionSource() }, colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,          // 滑块颜色
                        activeTrackColor = Color.Red,   // 激活轨道颜色
                        inactiveTrackColor = Color.Gray
                    ), enabled = true
                )
            })
            Text(color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp).width(30.dp).height(20.dp), fontSize = 10.sp, text = "${viewModelMusic.totalTime}")
        }
    }
}
package com.wx.compose.multiplatform.compose.viewmodel

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.wx.compose.multiplatform.viewmodels.NetViewModel
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun TabViewModel() {
    val viewModel = viewModel { NetViewModel() }
    val items by viewModel.items.collectAsState()
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        viewModel.getNetData()
    }

    Box {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth().padding(5.dp, 5.dp, 5.dp, 10.dp).fillMaxHeight(), columns = GridCells.Fixed(7), state = gridState, horizontalArrangement = Arrangement.spacedBy(7.dp), verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            items(items.size) { index ->
                val item = items[index]
                Box(
                    modifier = Modifier.height(130.dp).fillMaxWidth()
//                        .clickable {
//                        onClick.invoke(item)
//                    }
                    , contentAlignment = Alignment.BottomStart
                ) {

                    AsyncImage(
                        model = item.imgsrc, contentDescription = "Network Image", modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10)), contentScale = ContentScale.Crop
                    )

                    Text(
                        item.title, color = MaterialTheme.colorScheme.onPrimary, fontSize = 11.sp, modifier = Modifier.fillMaxWidth().background(Color(0x90000000)), textAlign = TextAlign.Center
                    )
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().width(10.dp), adapter = rememberScrollbarAdapter(gridState)
        )
    }
}
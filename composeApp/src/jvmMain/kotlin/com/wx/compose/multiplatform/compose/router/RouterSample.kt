package com.wx.compose.multiplatform.compose.router

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.wx.compose.multiplatform.dataSoruce.data.NewsBean
import com.wx.compose.multiplatform.viewmodels.NetViewModel
import moe.tlaster.precompose.navigation.Navigator

@Composable
fun RouterFirst(key: String, viewModel: NetViewModel, onClick: (item: NewsBean) -> Unit) {
    val items by viewModel.items.collectAsState()
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        viewModel.getNetData(key)
    }

    Box {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth().padding(5.dp, 5.dp, 5.dp, 10.dp).fillMaxHeight(), columns = GridCells.Fixed(5), state = gridState, horizontalArrangement = Arrangement.spacedBy(7.dp), verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            items(items.size) { index ->
                val item = items[index]
                Box(
                    modifier = Modifier.height(130.dp).fillMaxWidth().clickable {
                        onClick.invoke(item)
                    }, contentAlignment = Alignment.BottomStart
                ) {

                    AsyncImage(
                        model = item.imgsrc, contentDescription = "Network Image", modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10)), contentScale = ContentScale.Crop
                    )

                    Text(
                        item.title, color = MaterialTheme.colorScheme.onPrimary, fontSize = 11.sp, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10)).background(Color(0x90000000)), textAlign = TextAlign.Center
                    )
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().width(10.dp), adapter = rememberScrollbarAdapter(gridState)
        )
    }
}

@Composable
fun RouterSample(navigator: Navigator, docid: String, name: String) {
    Column {
        Row {
            Row(modifier = Modifier.clickable {
                navigator.popBackStack()
            }) {
                Icon(
                    modifier = Modifier.size(23.dp).padding(0.dp), painter = painterResource("drawable/baseline_arrow_back_24.xml"), contentDescription = "返回", tint = MaterialTheme.colorScheme.primary
                )
                Text("返回", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.wrapContentWidth())
            }
            Text(docid, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 0.dp, 0.dp, 0.dp).wrapContentWidth())
            Text(name, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(30.dp, 0.dp, 0.dp, 0.dp).wrapContentWidth())
        }

        Text("AAAAAAAAAAAAA", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("BBBBBB", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("CCCCCCCCC", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("DDDDDDDDD", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("EEEEEEEEE", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("FFFFFFFFF", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("GGGGGGGGGGG", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("HHHHHHHHHHHH", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("IIIIIIIIIIII", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("JJJJJJJJJJJJJ", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("KKKKKKKKKKK", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("LLLLLLLLLL", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("MMMMMMMMMMMM", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
        Text("NNNNNNNNNNNNNN", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 0.dp).wrapContentWidth())
    }
}
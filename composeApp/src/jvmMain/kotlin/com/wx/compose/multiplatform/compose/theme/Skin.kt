package com.wx.compose.multiplatform.compose.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wx.compose.multiplatform.PlatformKVStore.PlatformKVStore


@Composable
fun setting() {
    val skins by lazy {
        listOf(
            LightColors,
            DarkColors,
            Colors72,
            RedColors,
            defaultColors,
            Colors60,
            Colors61,
            Colors62,
            Colors63,
            Colors65,
            Colors66,
            Colors67,
            Colors68,
            Colors71,
            Colors69
        )
    }
    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        Text(text = "皮肤选择:", color = MaterialTheme.colorScheme.secondary, textAlign = TextAlign.Start, modifier = Modifier.width(710.dp).height(30.dp))
        LazyRow {
            items(skins.size) {
                val item = skins[it]
                Box(
                    modifier = Modifier.padding(5.dp).size(40.dp)
                        .clip(CircleShape)
                        .border(
                            width = 8.dp, color = item.primary, shape = CircleShape
                        ).background(item.background)
                        .clickable {
                            PlatformKVStore.saveSkin(it)
                            ThemeManager.skinTheme = item
                        })
            }
        }
    }
}
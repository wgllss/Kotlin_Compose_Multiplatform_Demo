package com.wx.compose.multiplatform.dataSoruce.data

import kotlinx.serialization.Serializable
import java.util.Objects

@Serializable
data class MusicItem(
    val musicID: String,
    val name: String,
    val singer: String,
    val pic: String,
    val url: String,
    val lrc: String,
    val musicSuffer: String,
    var localFile: Boolean = false,
    val id: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MusicItem
        if (musicID != other.musicID) return false
        return name == other.name && singer == other.singer && pic == other.pic && musicSuffer == other.musicSuffer
    }

    override fun hashCode() = Objects.hash(musicID, name, singer, pic, musicSuffer)
}

package com.wx.compose.multiplatform.dataSoruce.data


data class NewsBean(
    val source: String,
    val docid: String,
    val title: String,
    val commentCount: Int,
    val imgsrc3gtype: Int,
    val imgsrc: String,
    val url: String,
    val ptime: String,
    val imgextra: MutableList<ImgExtraData>
)

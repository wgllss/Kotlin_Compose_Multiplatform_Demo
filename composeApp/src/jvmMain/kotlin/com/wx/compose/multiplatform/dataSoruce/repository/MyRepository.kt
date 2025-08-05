package com.wx.compose.multiplatform.dataSoruce.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wx.compose.multiplatform.dataSoruce.data.NewsBean
import com.wx.compose.multiplatform.dataSoruce.net.NetApi
import com.wx.compose.multiplatform.dataSoruce.net.retrofit.RetrofitUtils
import kotlinx.coroutines.flow.flow

object MyRepository {
    private val baseUrl = "http://3g.163.com/"
    private val apiL by lazy { RetrofitUtils.getInstance(baseUrl).create(NetApi::class.java) }

    fun getNetTabInfo(path: String, start: Int, end: Int) = flow {
        val str = apiL.getNetTabInfo(path, start.toString(), end.toString(), start).let {
            it.substring(9, it.length - 1)
        }
        val map = Gson().fromJson<MutableMap<String, MutableList<NewsBean>>>(str, object : TypeToken<Map<String, MutableList<NewsBean>>>() {}.type)
        emit(map[path]!!)
    }
}
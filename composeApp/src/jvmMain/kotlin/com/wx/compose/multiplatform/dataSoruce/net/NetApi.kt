package com.wx.compose.multiplatform.dataSoruce.net

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetApi {

    @GET("http://3g.163.com/touch/reconstruct/article/list/{path}/{start}-{end}.html?")
    suspend fun getNetTabInfo(@Path("path") path: String, @Path("start") start: String, @Path("end") end: String, @Query("hasad") hasad: Int, @Query("size") size: Int = 0): String
}
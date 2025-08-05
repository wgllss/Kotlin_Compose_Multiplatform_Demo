package com.wx.compose.multiplatform.dataSoruce.net.retrofit

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

object RequestBodyCreate {

    fun toBody(body: String) = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), body)

    fun toBody2(body: String) = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), body)

}
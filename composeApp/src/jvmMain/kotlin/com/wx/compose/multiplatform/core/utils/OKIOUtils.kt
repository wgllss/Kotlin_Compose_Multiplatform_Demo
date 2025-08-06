package com.wx.compose.multiplatform.core.utils

import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.nio.charset.Charset

object OKIOUtils {

    fun readJson(filePath: String): String {
        val file = File(filePath)
        val source = file.source()
        val bufferedSource = source.buffer()
        val json = bufferedSource.readByteString().string(Charset.forName("UTF-8"))
        bufferedSource.close()
        source.close()
        return json
    }

    fun writeJson(json: String, filePath: String) {
        val fileOut = File(filePath)
        fileOut.takeIf { it.exists() }?.delete()
        fileOut.takeIf { !it.exists() }?.createNewFile()
        val sink = fileOut.sink()
        val sinkBuffer = sink.buffer()
        val chunSize = 4096
        val totalSize = json.length
        var start = 0
        while (start < totalSize) {
            sinkBuffer.writeUtf8(json.substring(start, totalSize.coerceAtMost(start + chunSize)))
            start += chunSize
        }
        sink.flush()
        sinkBuffer.close()
        sink.close()
    }
}
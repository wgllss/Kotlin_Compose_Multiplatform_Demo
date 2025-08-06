package com.wx.compose.multiplatform.dataSoruce.data


data class LrcEntry(val timeMs: Long, val text: String)

object LrcParser {

    fun parseLrc(lrcText: String): List<LrcEntry> {
        val list = lrcText.lineSequence()
            .filter { it.contains("]") }
            .mapNotNull { line ->
                val timeRegex = Regex("""\[(\d+):(\d+)\.(\d+)]""")
                timeRegex.find(line)?.let { match ->
                    val (min, sec, ms) = match.destructured
                    val timeMs = min.toLong() * 60000 + sec.toLong() * 1000 + ms.padEnd(3, '0').take(3).toLong()
                    LrcEntry(timeMs, line.substringAfterLast(']'))
                }
            }
            .sortedBy { it.timeMs }.toList()
        return list
    }
}
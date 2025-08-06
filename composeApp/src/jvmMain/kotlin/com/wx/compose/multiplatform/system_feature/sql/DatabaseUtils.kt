package com.wx.compose.multiplatform.system_feature.sql

import java.nio.file.*

object DatabaseUtils {

    // 获取应用专属数据目录
    fun getDatabasePath(dbName: String): String {
        val baseDir = when {
            System.getProperty("os.name").contains("win", true) -> System.getenv("AppData") ?: System.getProperty("user.home")
            else -> System.getProperty("user.home")
        }
        val appDir = Paths.get(baseDir, "music_db").apply {
            Files.createDirectories(this)
        }
        return appDir.resolve(dbName).toString()
    }
}
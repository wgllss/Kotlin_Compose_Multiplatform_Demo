package com.wx.compose.multiplatform.system_feature.sql

import com.wx.compose.multiplatform.dataSoruce.data.MusicItem
import java.sql.Connection
import java.sql.DriverManager

class DatabaseHelper {

    private val dbName = "music.db"
    private val connection: Connection by lazy {
        val dbPath = DatabaseUtils.getDatabasePath(dbName)
        DriverManager.getConnection("jdbc:sqlite:$dbPath")
    }

    init {
        createTable()
    }

    private fun createTable() {
        connection.createStatement().executeUpdate(
            """
            CREATE TABLE IF NOT EXISTS play_list (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                musicID TEXT NOT NULL,
                name TEXT NOT NULL,
                singer TEXT NOT NULL,
                pic TEXT NOT NULL,
                url TEXT NOT NULL,
                lrc TEXT NOT NULL,
                musicSuffer TEXT NOT NULL,
                localFile INTEGER CHECK (localFile IN (0, 1))
            )
        """
        )
    }

    //插入播放列表
    fun inserPlayItem(user: MusicItem): Int {
        val sql = "INSERT INTO play_list (musicID, name, singer, pic, url, lrc, musicSuffer,localFile) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        val statement = connection.prepareStatement(sql)
        statement.setString(1, user.musicID)
        statement.setString(2, user.name)
        statement.setString(3, user.singer)
        statement.setString(4, user.pic)
        statement.setString(5, user.url)
        statement.setString(6, user.lrc)
        statement.setString(7, user.musicSuffer)
        statement.setBoolean(8, user.localFile)
        statement.executeUpdate()
        return statement.generatedKeys.getInt(1) // 返回生成的 ID
    }

    //查询播放列表
    fun getAllPlayList(): List<MusicItem> {
        val list = mutableListOf<MusicItem>()
        val resultSet = connection.createStatement().executeQuery("SELECT * FROM play_list ORDER BY id ASC")
        while (resultSet.next()) {
            list.add(
                MusicItem(
                    resultSet.getString("musicID"),
                    resultSet.getString("name"),
                    resultSet.getString("singer"),
                    resultSet.getString("pic"),
                    resultSet.getString("url"),
                    resultSet.getString("lrc"),
                    resultSet.getString("musicSuffer"),
                    resultSet.getBoolean("localFile"),
                    resultSet.getInt("id")
                )
            )
        }
        return list
    }

    //删除播放列表
    fun dropTable(): Int {
        val statement = connection.createStatement()
        // 执行删除表操作
        statement.executeUpdate("DROP TABLE IF EXISTS play_list")
        statement.close()
        return 1
    }


    //删除整个播放列表数据
    fun delList(): Int {
        val statement = connection.createStatement()
        // 执行清空操作
        statement.executeUpdate("DELETE FROM play_list")
        statement.close()
        return 1
    }

    //通过musicID 删除
    fun delItemByID(musicID: String): Int {
        val statement = connection.prepareStatement(
            "DELETE FROM play_list WHERE musicID = ?"
        ).apply {
            setString(1, musicID)
        }
        val deletedRows = statement.executeUpdate()
        statement.close()
        return 1
    }

    // 更新指定的字段值
    fun updateField(musicID: String): Int {
        val sql = "UPDATE play_list SET localFile = ? WHERE musicID = ?"
        val statement = connection.prepareStatement(sql).apply {
            setInt(1, 1)
            setString(2, musicID)
            executeUpdate()
        }
        statement.close()
        return 1
    }
}

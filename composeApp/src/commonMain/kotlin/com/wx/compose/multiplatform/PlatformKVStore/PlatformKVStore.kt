package com.wx.compose.multiplatform.PlatformKVStore

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.get
import com.wx.compose.multiplatform.Greeting
import java.util.prefs.Preferences

object PlatformKVStore {
    val greeting by lazy { Greeting() }

    private val settings by lazy { PreferencesSettings(Preferences.userRoot()) }

    fun saveDownloadDir(path: String) = settings.putString("download_path", path)

    fun getDownloadDir() = settings["download_path", greeting.getDefaultDownloadPath()]

    fun saveCurrentPlay(position: Int) = settings.putInt("current_play_position", position)

    fun getCurrentPlay() = settings["current_play_position", -1]

    //保存本地皮肤类型
    fun saveSkin(intSkin: Int) = settings.putInt("skin_key", intSkin)

    //获取皮肤类型
    fun getSkinType() = settings["skin_key", 0]

    //保存本地播放模式
    fun savePlayType(intSkin: Int) = settings.putInt("play_style", intSkin)

    //获取播放模式
    fun getPlayType() = settings["play_style", 0]

    //保存String类型
    fun saveStringData(key: String, data: String) = settings.putString(key, data)

    //获取
    fun getStringData(key: String, defaultStr: String) = settings[key, defaultStr]

}
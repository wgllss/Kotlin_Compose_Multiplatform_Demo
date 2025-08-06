package com.wx.compose.multiplatform.viewmodels

import com.wx.compose.multiplatform.PlatformKVStore.PlatformKVStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import moe.tlaster.precompose.viewmodel.ViewModel
import javax.swing.JFileChooser

class SettingViewModel : ViewModel() {

    private val _downloadPath = MutableStateFlow(PlatformKVStore.getDownloadDir())
    val downloadPath = _downloadPath.asStateFlow()

    fun setDownloadPath() {
        JFileChooser(PlatformKVStore.getDownloadDir()).apply {
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            if (showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                val selectedDir = selectedFile.absolutePath
                _downloadPath.value = selectedDir
                PlatformKVStore.saveDownloadDir(selectedDir)
            }
        }
    }

}

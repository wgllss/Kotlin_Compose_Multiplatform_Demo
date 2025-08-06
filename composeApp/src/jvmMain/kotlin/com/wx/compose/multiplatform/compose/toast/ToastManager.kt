package com.wx.compose.multiplatform.compose.toast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.wx.compose.multiplatform.compose.toast.ToastManager.errorText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object ToastManager {
    val _errorText = MutableStateFlow("")
    val errorText: StateFlow<String> = _errorText.asStateFlow()

    suspend fun clearToast() {
        _errorText.emit("")
    }

    suspend fun showToast(errorMessage: String) {
        _errorText.emit(errorMessage)
    }
}

@Composable
fun Toast() {
    val errorText by errorText.collectAsState()
    val scope = rememberCoroutineScope()
    val isToast = remember(errorText) { errorText.isNotBlank() }
    if (isToast) {
        WXToast(message = errorText) {
            scope.launch {
                ToastManager.clearToast()
            }
        }
    }
}
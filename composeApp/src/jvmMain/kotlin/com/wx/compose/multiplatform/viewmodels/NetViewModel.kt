package com.wx.compose.multiplatform.viewmodels

import com.wx.compose.multiplatform.compose.toast.ToastManager
import com.wx.compose.multiplatform.dataSoruce.data.NewsBean
import com.wx.compose.multiplatform.dataSoruce.repository.MyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class NetViewModel : ViewModel() {
    var isFirstLoad = true

    private val _state = MutableStateFlow(mutableListOf<NewsBean>())
    val items: StateFlow<MutableList<NewsBean>> = _state.asStateFlow()

    fun getNetData() {
        if (!isFirstLoad) return
        viewModelScope.launch {
            MyRepository.getNetTabInfo("BAI6I0O5wangning", 0, 20).flowOn(Dispatchers.IO).catch {
                    ToastManager.shouToast(it.message.toString())
                }.collect {
                    _state.value = it
                    isFirstLoad = false
                }
        }

    }

}
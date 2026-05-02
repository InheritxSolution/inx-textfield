package com.inheritx.customedittext

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PlaygroundState(
    val labelText: String = "Interactive Field",
    val helperText: String = "Try changing my properties below!",
    val primaryColor: Int = 0xFF6366F1.toInt(),
    val errorColor: Int = 0xFFEF4444.toInt(),
    val useSpacing: Boolean = false,
    val isEnabled: Boolean = true,
    val hasError: Boolean = false
)

class ShowcaseViewModel : ViewModel() {

    private val _playgroundState = MutableStateFlow(PlaygroundState())
    val playgroundState: StateFlow<PlaygroundState> = _playgroundState.asStateFlow()

    fun updateLabel(text: String) {
        _playgroundState.value = _playgroundState.value.copy(labelText = text)
    }

    fun updateHelper(text: String) {
        _playgroundState.value = _playgroundState.value.copy(helperText = text)
    }

    fun toggleSpacing(enabled: Boolean) {
        _playgroundState.value = _playgroundState.value.copy(useSpacing = enabled)
    }

    fun toggleEnabled(enabled: Boolean) {
        _playgroundState.value = _playgroundState.value.copy(isEnabled = enabled)
    }

    fun toggleError(hasError: Boolean) {
        _playgroundState.value = _playgroundState.value.copy(hasError = hasError)
    }
}

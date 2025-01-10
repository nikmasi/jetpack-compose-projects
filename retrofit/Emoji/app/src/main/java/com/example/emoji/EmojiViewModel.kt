package com.example.emoji

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emoji.data.Emoji
import com.example.emoji.data.EmojiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class EmojiViewModel @Inject constructor(
    private val emojiRepository: EmojiRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiStateP = MutableStateFlow(UiState())
    val uiStateP = _uiStateP.asStateFlow()

    fun refresh() = viewModelScope.launch {
        _uiState.update { it.copy(isRefreshing = true) }
        delay(100)

        try {
            val emojiResponse = emojiRepository.getEmojis()
            _uiState.update { it.copy(emojis = emojiResponse.results) }
        } catch (e: Exception) {
            _uiState.update { it.copy(emojis = listOf()) }
        } finally {
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    fun refreshPopular() = viewModelScope.launch {
        _uiStateP.update { it.copy(isRefreshing = true) }
        delay(50)

        try {
            val emojiResponse = emojiRepository.getPopular()
            _uiStateP.update { it.copy(emojis = emojiResponse.results) }
        } catch (e: Exception) {
            _uiStateP.update { it.copy(emojis = listOf()) }
        } finally {
            _uiStateP.update { it.copy(isRefreshing = false) }
        }
    }

}


data class UiState(
    val emojis: List<Emoji> = listOf(),
    val isRefreshing: Boolean = false,
)
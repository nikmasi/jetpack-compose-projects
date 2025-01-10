package com.example.emoji

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emoji.data.Category
import com.example.emoji.data.Emoji
import com.example.emoji.data.EmojiRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val emojiRepository: EmojiRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiStateC())
    val uiState: StateFlow<UiStateC> = _uiState

    init {
        fetchCategories()
    }

    // Funkcija za dohvat kategorija
    fun fetchCategories() = viewModelScope.launch {
        try {
            val response = emojiRepository.getCategories()
            _uiState.value = UiStateC(categories = response.results)
        } catch (e: Exception) {
            // Handling error, možeš dodati odgovarajuće obavještenje korisniku
            _uiState.value = UiStateC(categories = emptyList(), error = e.localizedMessage)
        }
    }
}

data class UiStateC(
    val categories: List<Category> = emptyList(),
    val isRefreshing: Boolean = false,
    val error: String? = null
)

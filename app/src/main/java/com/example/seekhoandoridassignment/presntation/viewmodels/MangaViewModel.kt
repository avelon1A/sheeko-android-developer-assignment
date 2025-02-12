package com.example.seekhoandoridassignment.presntation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seekhoandoridassignment.data.model.maga.Manga
import com.example.seekhoandoridassignment.domain.repository.AnimeRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MangaViewModel(private val animeRepository: AnimeRepository) : ViewModel() {

    private val _manga = MutableStateFlow<Manga?>(null)
    val manga: StateFlow<Manga?> = _manga

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchMangaDetails(mangaId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = animeRepository.getMangaDetails(mangaId)
                _manga.value = response
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

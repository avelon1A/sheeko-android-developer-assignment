package com.example.seekhoandoridassignment.presntation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seekhoandoridassignment.data.dto.AnimeListDto
import com.example.seekhoandoridassignment.domain.repository.AnimeRepository
import com.example.seekhoandoridassignment.uitl.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(val animeRepository: AnimeRepository):ViewModel() {

    private val _animeListState = MutableStateFlow<ApiState<AnimeListDto>>(ApiState.Loading)
    val animeListState: StateFlow<ApiState<AnimeListDto>> = _animeListState

     fun fetchAnimeList() {
        viewModelScope.launch {
            try {
                val animeList = animeRepository.getAnimeList()
                _animeListState.value = ApiState.Success(animeList)
            } catch (e: Exception) {
                _animeListState.value = ApiState.Error(e.message)
            }
        }
    }



}

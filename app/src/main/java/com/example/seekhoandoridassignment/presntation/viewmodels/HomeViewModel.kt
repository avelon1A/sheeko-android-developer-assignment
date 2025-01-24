package com.example.seekhoandoridassignment.presntation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.seekhoandoridassignment.data.dto.AnimeDetailsDto
import com.example.seekhoandoridassignment.data.network.AnimeListDtoListSource
import com.example.seekhoandoridassignment.domain.repository.AnimeRepository
import com.example.seekhoandoridassignment.uitl.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(val animeRepository: AnimeRepository): ViewModel() {
    private val _animeDetailState = MutableStateFlow<ApiState<AnimeDetailsDto>>(ApiState.Loading)
    val animeDetailState: StateFlow<ApiState<AnimeDetailsDto>> = _animeDetailState

    fun getAnimeDetail(animeId: Int){
        viewModelScope.launch {
            try {
                val animeDetail = animeRepository.getAnimeDetail(animeId)
                _animeDetailState.value = ApiState.Success(animeDetail)
                } catch (e: Exception) {
                _animeDetailState.value = ApiState.Error(e.message)
            }
        }
    }
    val animeList = Pager(PagingConfig(pageSize = 8)) {
        AnimeListDtoListSource( animeRepository)
    }.flow




}
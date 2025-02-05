package com.example.seekhoandoridassignment.presntation.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import coil.ImageLoader
import com.example.mypokedex.util.getDominantColorFromImage
import com.example.seekhoandoridassignment.data.dto.AnimeDetailsDto
import com.example.seekhoandoridassignment.data.dto.AnimeListDto
import com.example.seekhoandoridassignment.data.network.AnimeListDtoListSource
import com.example.seekhoandoridassignment.domain.repository.AnimeRepository
import com.example.seekhoandoridassignment.uitl.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(val animeRepository: AnimeRepository,val  context: Context): ViewModel() {
    private val _animeDetailState = MutableStateFlow<ApiState<AnimeDetailsDto>>(ApiState.Loading)
    val animeDetailState: StateFlow<ApiState<AnimeDetailsDto>> = _animeDetailState

    private val _animeSearch = MutableStateFlow<ApiState<AnimeListDto>>(ApiState.Loading)
    val animeSearch: StateFlow<ApiState<AnimeListDto>> = _animeSearch

    val animeList = Pager(PagingConfig(pageSize = 8)) {
        AnimeListDtoListSource(animeRepository)
    }.flow

    private val _color = MutableStateFlow(Color(0xFF656565))
    val color: StateFlow<Color> = _color


    fun getAnimeDetail(animeId: Int) {
        _animeDetailState.value = ApiState.Loading
        viewModelScope.launch {
            try {
                val animeDetail = animeRepository.getAnimeDetail(animeId)
                _animeDetailState.value = ApiState.Success(animeDetail)
            } catch (e: Exception) {
                _animeDetailState.value = ApiState.Error(e.message)
            }
        }
    }
    fun getSearchAnime(searchQuery: String){
        _animeDetailState.value = ApiState.Loading
        viewModelScope.launch{
            try {
                val searchAnime = animeRepository.getSearchAnime(searchQuery)
                _animeSearch.value = ApiState.Success(searchAnime)
        }
            catch (e: Exception){
                _animeSearch.value = ApiState.Error(e.message)
                Log.d("search", "getSearchAnime api: ${e.message}")
            }
        }
        Log.d("search", "getSearchAnime: $searchQuery")
    }



    fun getColor(url: String, imageLoader: ImageLoader) {
        viewModelScope.launch(Dispatchers.IO){
          val color =   getDominantColorFromImage(context,url,imageLoader)
            _color.value = color
        }

    }
    fun clearSearch(){
        _animeSearch.value = ApiState.Loading

    }



}
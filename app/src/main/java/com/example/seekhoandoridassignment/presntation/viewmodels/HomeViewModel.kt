package com.example.seekhoandoridassignment.presntation.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.mypokedex.util.getDominantColorFromImage
import com.example.seekhoandoridassignment.data.configModel.defaultAnimeSections
import com.example.seekhoandoridassignment.data.dto.AnimeDetailsDto
import com.example.seekhoandoridassignment.data.dto.AnimeListDto
import com.example.seekhoandoridassignment.data.network.AnimeListDtoListSource
import com.example.seekhoandoridassignment.data.network.TopAnimeListSource
import com.example.seekhoandoridassignment.domain.repository.AnimeRepository
import com.example.seekhoandoridassignment.uitl.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val animeRepository: AnimeRepository,
    private val context: Context,
    private val imageLoader: ImageLoader
) : ViewModel() {

    val animeList = Pager(PagingConfig(pageSize = 8)) {
        AnimeListDtoListSource(animeRepository)
    }.flow

    val topAiringList = Pager(PagingConfig(pageSize = 8)) {
        TopAnimeListSource(animeRepository)
    }.flow

    private val _homeUiState = MutableStateFlow(HomePageUiState())
    val homeUiState: StateFlow<HomePageUiState> = _homeUiState

    private val _animeDetailState = MutableStateFlow<ApiState<AnimeDetailsDto>>(ApiState.Loading)
    val animeDetailState: StateFlow<ApiState<AnimeDetailsDto>> = _animeDetailState

    private val _animeSearch = MutableStateFlow<ApiState<AnimeListDto>>(ApiState.Loading)
    val animeSearch: StateFlow<ApiState<AnimeListDto>> = _animeSearch

    private val _color = MutableStateFlow(Color(0xFF656565))
    val color: StateFlow<Color> = _color

    init {
        fetchInitialData()
    }


    private fun fetchInitialData() {
        viewModelScope.launch {
            _homeUiState.value = _homeUiState.value.copy(isLoading = true)

            val sectionList = mutableListOf<AnimeSection>()

            for (config in defaultAnimeSections) {
                try {
                    val animeList = animeRepository.getTopAnime(1, config.filter)
                    sectionList.add(AnimeSection(config.title, config.filter, animeList))
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error fetching ${config.title}", e)
                    sectionList.add(
                        AnimeSection(
                            config.title,
                            config.filter,
                            AnimeListDto(emptyList())
                        )
                    )
                }
            }

            _homeUiState.value = _homeUiState.value.copy(
                isLoading = false,
                sections = sectionList
            )
        }
    }


    private suspend fun fetchTopAnime(
        page: Int,
        filter: String,
        onSuccess: (AnimeListDto) -> Unit
    ) {
        try {
            val result = animeRepository.getTopAnime(nextPage = page, filter = filter)
            onSuccess(result)
        } catch (e: Exception) {
            Log.e("HomeViewModel", "fetchTopAnime($filter): ${e.localizedMessage}", e)
            _homeUiState.value = _homeUiState.value.copy(error = e.message)
        } finally {
            _homeUiState.value = _homeUiState.value.copy(isLoading = false)
        }
    }

    fun fetchAnimeDetail(animeId: Int) {
        _animeDetailState.value = ApiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val animeDetail = animeRepository.getAnimeDetail(animeId)
                _animeDetailState.value = ApiState.Success(animeDetail)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "fetchAnimeDetail: ${e.localizedMessage}", e)
                _animeDetailState.value = ApiState.Error(e.message)
            }
        }
    }

    fun fetchSearchAnime(query: String) {
        _animeSearch.value = ApiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = animeRepository.getSearchAnime(query)
                _animeSearch.value = ApiState.Success(result)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "fetchSearchAnime: ${e.localizedMessage}", e)
                _animeSearch.value = ApiState.Error(e.message)
            }
        }
    }

    fun clearSearch() {
        _animeSearch.value = ApiState.Loading
    }

    fun getDominantColorFromUrl(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val color = getDominantColorFromImage(context, url, imageLoader)
            _color.value = color
        }
    }
}

data class AnimeSection(
    val title: String,
    val filter: String,
    val animeList: AnimeListDto
)

data class HomePageUiState(
    val isLoading: Boolean = false,
    val sections: List<AnimeSection> = emptyList(),
    val searchResult: AnimeListDto = AnimeListDto(emptyList()),
    val error: String? = null
)

package com.example.seekhoandoridassignment.presntation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.seekhoandoridassignment.data.network.AnimeListDtoListSource
import com.example.seekhoandoridassignment.domain.repository.AnimeRepository

class HomeViewModel(val animeRepository: AnimeRepository):ViewModel() {


    val animeList = Pager(PagingConfig(pageSize = 8)) {
        AnimeListDtoListSource( animeRepository)
    }.flow




}

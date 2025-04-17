package com.example.seekhoandoridassignment.presntation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seekhoandoridassignment.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val animeRepository: AnimeRepository):ViewModel(  ) {
    private val _imageDownloaded = MutableStateFlow(false)
    val imageDownloaded: StateFlow<Boolean> = _imageDownloaded

    val topThree = MutableStateFlow(mutableListOf<String>())


    init {
        downloadImage()
    }
    fun markImageAsDownloaded() {
        _imageDownloaded.value = true
    }

    fun downloadImage() {
        _imageDownloaded.value = true
       viewModelScope.launch {
           try {
               val result = animeRepository.getTopAnime(1,"upcoming")
               for (i in 0..2) {
                   if (i < result.animeList.size) {
                       topThree.value.add(result.animeList[i].img)
                   }
               }

               _imageDownloaded.value = true
           }catch (e:Exception){
               _imageDownloaded.value = false
           }


       }

    }



}
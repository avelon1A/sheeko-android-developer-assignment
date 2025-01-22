package com.example.seekhoandoridassignment.data.network

import com.example.seekhoandoridassignment.data.model.animeDetail
import com.example.seekhoandoridassignment.data.model.animeList
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("top/anime")
    suspend fun getAnimeList(): Response<animeList>

    @GET("anime/{animeId}")
    suspend fun getAnimeDetail(animeId:Int): Response<animeDetail>

}
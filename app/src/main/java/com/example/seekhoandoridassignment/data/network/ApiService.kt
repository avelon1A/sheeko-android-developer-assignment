package com.example.seekhoandoridassignment.data.network

import com.example.seekhoandoridassignment.data.model.animeDetail
import com.example.seekhoandoridassignment.data.model.animeList
import com.example.seekhoandoridassignment.data.model.character.AnimeCharacters
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("top/anime")
    suspend fun getAnimeList(): Response<animeList>

    @GET("anime/{animeId}")
    suspend fun getAnimeDetail(@Path("animeId") animeId:Int): Response<animeDetail>

    @GET("anime/{animeId}/characters")
    suspend fun getAnimeCharacter(@Path("animeId") animeId:Int): Response<AnimeCharacters>

}
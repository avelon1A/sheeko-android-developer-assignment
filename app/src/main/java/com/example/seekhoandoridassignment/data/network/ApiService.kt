package com.example.seekhoandoridassignment.data.network

import com.example.seekhoandoridassignment.data.model.animeDetail
import com.example.seekhoandoridassignment.data.model.animeList
import com.example.seekhoandoridassignment.data.model.character.AnimeCharacters
import com.example.seekhoandoridassignment.data.model.maga.Manga
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("anime")
    suspend fun getAnimeList(@Query("page") page: Int): Response<animeList>

    @GET("anime/{animeId}")
    suspend fun getAnimeDetail(@Path("animeId") animeId:Int): Response<animeDetail>

    @GET("anime/{animeId}/characters")
    suspend fun getAnimeCharacter(@Path("animeId") animeId:Int): Response<AnimeCharacters>

    @GET("anime")
    suspend fun getSearchAnime(@Query("q") searchQuery: String): Response<animeList>
    @GET("manga/{id}")
    suspend fun getMangaDetails(@Path("id") mangaId: Int):  Response<Manga>

    @GET("top/anime")
    suspend fun getTopAnimeList(@Query("page") page: Int,
        @Query("filter") filter: String,
    ): Response<animeList>

    @GET("seasons/now")
    suspend fun getSeasonsList(@Query("page") page: Int): Response<animeList>

}
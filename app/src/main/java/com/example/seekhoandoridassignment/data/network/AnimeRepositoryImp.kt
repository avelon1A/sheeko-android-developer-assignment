package com.example.seekhoandoridassignment.data.network

import android.util.Log
import com.example.seekhoandoridassignment.data.dto.Anime
import com.example.seekhoandoridassignment.data.dto.AnimeCharactersDto
import com.example.seekhoandoridassignment.data.dto.AnimeDetailsDto
import com.example.seekhoandoridassignment.data.dto.AnimeListDto
import com.example.seekhoandoridassignment.data.model.animeDetail
import com.example.seekhoandoridassignment.domain.repository.AnimeRepository

class AnimeRepositoryImp(private val api:ApiService):AnimeRepository  {
    override suspend fun getAnimeList( nextPage: Int): AnimeListDto {
        val response = api.getAnimeList(nextPage)
        if (response.isSuccessful && response.body() != null) {
            val animeListDto = response.body()?.data?.map { animeItemResponse ->
                Anime(
                    title = animeItemResponse.title,
                    numberOfEpisode = animeItemResponse.episodes,
                    rating = animeItemResponse.score,
                    img = animeItemResponse.images.jpg.large_image_url,
                    id = animeItemResponse.mal_id
                )
            } ?: emptyList()

            return AnimeListDto(animeListDto)
        } else {
            throw Exception("animeList loading failed: ${response.message()}")
        }
    }

    override suspend fun getAnimeDetail(animeId: Int): AnimeDetailsDto {
        val response = api.getAnimeDetail(animeId)
        val animeCharactersResponse = api.getAnimeCharacter(animeId)
        if (response.isSuccessful && response.body() != null) {
            val animeDetailResponse: animeDetail = response.body()!!
            val animeCharactersResponse =
                animeCharactersResponse.body()?.data?.filter { it.role == "Main" }?.map {
                    AnimeCharactersDto(
                        name = it.character.name,
                        images = it.character.images.jpg.image_url
                    )
                }
            val animeDetailsDto = AnimeDetailsDto(
                title = animeDetailResponse.data.title,
                trailer = animeDetailResponse.data.trailer.youtube_id,
                plot = animeDetailResponse.data.synopsis,
                genres = animeDetailResponse.data.genres.joinToString { it.name },
                mainCast = animeCharactersResponse,
                noOfEpisodes = animeDetailResponse.data.episodes,
                imageUrl = animeDetailResponse.data.images.jpg.large_image_url
            )
        return animeDetailsDto
    } else {
            throw Exception("animeDetail loading failed: ${response.message()}")
        }

    }

    override suspend fun getSearchAnime(searchQuery: String): AnimeListDto {
        val response = api.getSearchAnime(searchQuery)
        Log.d("search", "getSearchAnime repo: $response")
        if (response.isSuccessful && response.body() != null) {
            val animeListDto = response.body()?.data?.map { animeItemResponse ->
                Anime(
                    title = animeItemResponse.title,
                    numberOfEpisode = animeItemResponse.episodes,
                    rating = animeItemResponse.score,
                    img = animeItemResponse.images.jpg.large_image_url,
                    id = animeItemResponse.mal_id
                )
            } ?: emptyList()

            return AnimeListDto(animeListDto)
        } else {
            throw Exception("Search Anime loading failed: ${response.message()}")
        }
    }
}
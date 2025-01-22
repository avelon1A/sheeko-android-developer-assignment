package com.example.seekhoandoridassignment.data.network

import com.example.seekhoandoridassignment.data.dto.Anime
import com.example.seekhoandoridassignment.data.dto.AnimeListDto
import com.example.seekhoandoridassignment.domain.repository.AnimeRepository

class AnimeRepositoryImp(val api:ApiService):AnimeRepository  {
    override suspend fun getAnimeList(): AnimeListDto {
        val response = api.getAnimeList()
        if (response.isSuccessful && response.body() != null) {
            val animeListDto = response.body()?.data?.map { animeItemResponse ->
                Anime(
                    title = animeItemResponse.title,
                    numberOfEpisode = animeItemResponse.episodes.toString(),
                    rating = animeItemResponse.score.toInt(),
                    img = animeItemResponse.images.jpg.large_image_url
                )
            } ?: emptyList()

            return AnimeListDto(animeListDto)
        } else {
            throw Exception("Failed to fetch anime list: ${response.message()}")
        }
    }
}
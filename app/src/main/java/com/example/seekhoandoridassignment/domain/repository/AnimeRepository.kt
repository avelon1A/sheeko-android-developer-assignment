package com.example.seekhoandoridassignment.domain.repository

import com.example.seekhoandoridassignment.data.dto.AnimeDetailsDto
import com.example.seekhoandoridassignment.data.dto.AnimeListDto
import com.example.seekhoandoridassignment.data.model.maga.Manga

interface AnimeRepository {
    suspend fun getAnimeList(nextPage: Int): AnimeListDto
    suspend fun getAnimeDetail(animeId: Int): AnimeDetailsDto
    suspend fun getSearchAnime(searchQuery: String): AnimeListDto
    suspend fun getMangaDetails(id: Int): Manga
}
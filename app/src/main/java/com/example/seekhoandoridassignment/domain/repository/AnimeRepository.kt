package com.example.seekhoandoridassignment.domain.repository

import com.example.seekhoandoridassignment.data.dto.AnimeDetailsDto
import com.example.seekhoandoridassignment.data.dto.AnimeListDto

interface AnimeRepository {
    suspend fun getAnimeList(): AnimeListDto
    suspend fun getAnimeDetail(animeId: Int): AnimeDetailsDto
}
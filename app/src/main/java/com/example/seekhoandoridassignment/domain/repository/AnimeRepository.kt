package com.example.seekhoandoridassignment.domain.repository

import com.example.seekhoandoridassignment.data.dto.AnimeListDto

interface AnimeRepository {
    suspend fun getAnimeList(): AnimeListDto
}
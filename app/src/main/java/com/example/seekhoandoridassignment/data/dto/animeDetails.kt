package com.example.seekhoandoridassignment.data.dto

data class AnimeDetailsDto(
    val title: String,
    val trailer: String?,
    val plot: String,
    val genres: String?,
    val mainCast: List<AnimeCharactersDto>?,
    val noOfEpisodes: Int?,
    val imageUrl: String
)
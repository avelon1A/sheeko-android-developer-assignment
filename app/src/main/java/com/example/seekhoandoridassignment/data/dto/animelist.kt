package com.example.seekhoandoridassignment.data.dto

data class AnimeListDto (
    val animeList:List<Anime>
)
data class Anime(
    val title: String,
    val numberOfEpisode: Int?,
    val rating: Double,
    val img: String,
    val id: Int
)
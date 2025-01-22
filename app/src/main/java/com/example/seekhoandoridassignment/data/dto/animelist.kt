package com.example.seekhoandoridassignment.data.dto

data class AnimeListDto (
    val animeList:List<Anime>
)
data class Anime(
    val title: String,
    val numberOfEpisode: String,
    val rating: Int,
    val img: String
)
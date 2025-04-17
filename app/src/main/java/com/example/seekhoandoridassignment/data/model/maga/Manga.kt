package com.example.seekhoandoridassignment.data.model.maga

data class Manga(
    val mal_id: Int,
    val title: String,
    val images: Images,
    val synopsis: String
)

data class Images(
    val jpg: Jpg
)

data class Jpg(
    val image_url: String
)
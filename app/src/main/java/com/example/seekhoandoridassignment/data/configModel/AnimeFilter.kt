package com.example.seekhoandoridassignment.data.configModel

data class AnimeSectionConfig(
    val title: String,
    val filter: String
)

val defaultAnimeSections = listOf(
    AnimeSectionConfig("Top Airing", "airing"),
    AnimeSectionConfig("Most Popular", "favorite"),
    AnimeSectionConfig("bypopularity", "bypopularity"),
    AnimeSectionConfig("upcoming", "upcoming")
)
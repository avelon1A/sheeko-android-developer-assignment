package com.example.seekhoandoridassignment.data.model.character

data class Data(
    val character: Character,
    val favorites: Int,
    val role: String,
    val voice_actors: List<VoiceActor>
)
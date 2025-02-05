package com.example.seekhoandoridassignment.presntation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable

@Composable
fun MangaPage() {
    Column(modifier = Modifier.fillMaxSize())  {
        Text(text = "Manga Page")
    }

}

@Serializable
object MangaPage
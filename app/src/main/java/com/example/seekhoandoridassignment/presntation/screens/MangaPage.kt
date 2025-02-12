package com.example.seekhoandoridassignment.presntation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.seekhoandoridassignment.data.model.maga.Manga
import com.example.seekhoandoridassignment.presntation.viewmodels.MangaViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun MangaPage() {
    val viewModel: MangaViewModel = koinViewModel()
    val manga by viewModel.manga.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
LaunchedEffect(Unit) {
    viewModel.fetchMangaDetails(10)
}
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text(text = "Error: $error")
        } else {
            manga?.let { mangaData ->
                MangaDetail(mangaData)
            }
        }
    }

}

@Composable
fun MangaDetail(manga: Manga) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = manga.title, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = rememberImagePainter(manga.images.jpg.image_url),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = manga.synopsis, style = MaterialTheme.typography.headlineSmall)
    }
}


@Serializable
object MangaPage
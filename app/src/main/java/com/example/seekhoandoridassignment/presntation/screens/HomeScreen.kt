package com.example.seekhoandoridassignment.presntation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.seekhoandoridassignment.data.dto.Anime
import com.example.seekhoandoridassignment.data.dto.AnimeListDto
import com.example.seekhoandoridassignment.presntation.viewmodels.HomeViewModel
import com.example.seekhoandoridassignment.uitl.ApiState
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = koinViewModel()
    val animeListState = viewModel.animeListState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAnimeList()
    }

    when (animeListState.value) {
        is ApiState.Loading -> {
            CircularProgressIndicator()
        }
        is ApiState.Success -> {
            val animeList:AnimeListDto = (animeListState.value as ApiState.Success<AnimeListDto>).data
            LazyColumn {
                items(animeList.animeList.size) { anime ->
                    AnimeItem(animeList.animeList[anime])
                }
            }
        }
        is ApiState.Error -> {
            val errorMessage = animeListState.value as ApiState.Error
            Text(text = "Error: $errorMessage")
        }

    }
}

@Composable
fun AnimeItem(anime: Anime) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.surface)
        .padding(8.dp)
    ) {
        SubcomposeAsyncImage(
            model = anime.img,
            contentDescription = anime.title,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = anime.title,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Episodes: ${anime.numberOfEpisode}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Rating: ${anime.rating}",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Serializable
object HomeScreen
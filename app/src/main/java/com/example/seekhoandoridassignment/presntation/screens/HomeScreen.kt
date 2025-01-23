package com.example.seekhoandoridassignment.presntation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import com.example.seekhoandoridassignment.data.dto.Anime
import com.example.seekhoandoridassignment.data.dto.AnimeListDto
import com.example.seekhoandoridassignment.presntation.viewmodels.HomeViewModel
import com.example.seekhoandoridassignment.uitl.ApiState
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import kotlin.collections.chunked
import kotlin.collections.forEach

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = koinViewModel()
    val animeListState = viewModel.animeListState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAnimeList()
    }

    when (animeListState.value) {
        is ApiState.Loading -> {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(100.dp),
                    color = Color.Red)

            }
        }

        is ApiState.Success -> {
            val animeList: AnimeListDto =
                (animeListState.value as ApiState.Success<AnimeListDto>).data
            LazyColumn {
                items(animeList.animeList.chunked(2), key = { item ->
                    item.hashCode()
                }) { rowButtons ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        rowButtons.forEach {
                            AnimeItem(anime = it, click = {
                                navController.navigate(
                                    DetailScreenNav(it.id,it.img)
                                )
                            })
                        }
                    }
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
fun AnimeItem(anime: Anime, click: () -> Unit) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(300.dp)
            .padding(12.dp)
            .clickable {
                click()
            }, contentAlignment = Alignment.BottomStart
    ) {
        SubcomposeAsyncImage(
            model = anime.img,
            contentDescription = anime.title,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(bottom = 20.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black)
                        )
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }
            Row(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
                    .background(Color.Black),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                Text(
                    text = "Episodes: ${anime.numberOfEpisode}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Rating: ${anime.rating}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun AnimeItemPreview() {
    AnimeItem(anime = Anime(
        id = 1,
        title = "TESTING",
        numberOfEpisode = 23,
        rating = 4.7,
        img = "https://cdn.myanimelist.net/images/anime/1222/108880.jpg"
    ), click = {})
}


@Serializable
object HomeScreen
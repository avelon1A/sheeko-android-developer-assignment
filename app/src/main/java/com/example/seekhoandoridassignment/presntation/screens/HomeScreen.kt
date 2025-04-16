package com.example.seekhoandoridassignment.presntation.screens

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.seekhoandoridassignment.R
import com.example.seekhoandoridassignment.data.dto.Anime
import com.example.seekhoandoridassignment.presntation.common.ImageCarousel
import com.example.seekhoandoridassignment.presntation.common.SectionWithAnimeRow
import com.example.seekhoandoridassignment.presntation.viewmodels.HomeViewModel
import kotlinx.serialization.Serializable
import java.io.File


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val animeDetailState = viewModel.animeDetailState.collectAsState()
    val state = viewModel.homeUiState.collectAsState().value
    val dominantColor = viewModel.color.collectAsState()
    val detailView = remember { mutableStateOf(false) }
    val scrollState = rememberLazyGridState()
    val scrollStateColumn = rememberLazyListState()
    val context = LocalContext.current


    BackHandler(enabled = detailView.value) {
        detailView.value = false
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (!detailView.value) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(80.dp),
                        color = Color.Red
                    )
                }

                state.error != null -> {
                    Text(text = state.error, color = Color.Red)
                }

                else -> {
                    val imageList = listOf(
                        getImagePath("splash_image.png",context)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = scrollStateColumn
                    ) {
                        item {
                            ImageCarousel(
                                imageList = imageList
                            )
                        }

                        state.sections.forEach { section ->
                            item {
                                SectionWithAnimeRow(
                                    title = section.title,
                                    animeList = section.animeList,
                                    onClick = { anime ->
                                        viewModel.fetchAnimeDetail(anime.id)
                                        detailView.value = true
                                        viewModel.getDominantColorFromUrl(anime.img)
                                    }
                                )
                            }
                        }

                    }


                    }
                }
            }
         else {
            DetailPageView(dominantColor, animeDetailState)
            Log.d("color home screen", "$dominantColor")
        }
    }


}

fun getImagePath(fileName: String,context: Context): String {
    val directory = context.filesDir
    val file = File(directory, fileName)
    return file.absolutePath
}


@Composable
fun AnimeItem(anime: Anime, click: () -> Unit,modifier:Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(150.dp)
            .height(200.dp)
            .padding(12.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable {
                click()
            }, contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            model = anime.img,
            contentDescription = anime.title,
            placeholder = painterResource(R.drawable.demo),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(bottom = 20.dp),
            contentScale = ContentScale.Crop,

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
                Text(modifier = Modifier.padding(start = 2.dp),
                    text = anime.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            }
            Row(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(start = 5.dp, end = 5.dp , bottom = 2.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.episodes),
                        contentDescription = "ep",
                    )
                    Text(
                        text = "${anime.numberOfEpisode}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "rating",
                    )
                    Text(
                        text = "${anime.rating}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                }
            }
        }
    }

    @Preview(showBackground = false)
    @Composable
    fun AnimeItemPreview() {
        AnimeItem(
            anime = Anime(
                id = 1,
                title = "TESTING",
                numberOfEpisode = 23,
                rating = 4.7,
                img = "https://cdn.myanimelist.net/images/anime/1222/108880.jpg"
            ), click = {},
            modifier = Modifier
        )
    }


    @Serializable
    object HomeScreen
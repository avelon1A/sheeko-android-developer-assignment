package com.example.seekhoandoridassignment.presntation.screens

import android.util.Log
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.SubcomposeAsyncImage
import com.example.seekhoandoridassignment.data.dto.Anime
import com.example.seekhoandoridassignment.presntation.viewmodels.HomeViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = koinViewModel()
    val animeListState = viewModel.animeList.collectAsLazyPagingItems()



    when (animeListState.loadState.refresh) {
        is LoadState.Error -> {
            Text(text = "Error")
        }
       is  LoadState.Loading -> {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(100.dp),
                    color = Color.Red)

            }
           Log.d("loading", "home page")
        }
       else -> {

           LazyVerticalGrid(columns =GridCells.Fixed(2) ) {
               items(animeListState.itemCount) { index ->
                   val anime = animeListState[index]
                   anime?.let {
                       AnimeItem(anime = it, click = {
                           navController.navigate(
                               DetailScreenNav(it.id, it.img)
                           )
                       })
                   }
               }
           }
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
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
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
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Rating: ${anime.rating}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
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
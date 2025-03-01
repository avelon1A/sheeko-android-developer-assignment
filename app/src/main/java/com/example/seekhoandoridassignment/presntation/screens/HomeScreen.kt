package com.example.seekhoandoridassignment.presntation.screens

import android.util.Log
import com.example.seekhoandoridassignment.R
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import com.example.seekhoandoridassignment.data.dto.Anime
import com.example.seekhoandoridassignment.presntation.common.ImageCarousel
import com.example.seekhoandoridassignment.presntation.common.MySearchBar
import com.example.seekhoandoridassignment.presntation.viewmodels.HomeViewModel
import com.example.seekhoandoridassignment.uitl.ApiState
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel



@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = koinViewModel()
    val animeListState = viewModel.animeList.collectAsLazyPagingItems()
    val animeDetailState = viewModel.animeDetailState.collectAsState()
    val animeSearchList = viewModel.animeSearch.collectAsState().value
    val dominantColor = viewModel.color.collectAsState()
    val detailView = remember { mutableStateOf(false) }
    val scrollState = rememberLazyGridState()
    val scrollStateColumn = rememberLazyListState()
    var showSearchBar by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build()
    }
    var lastVisibleItemIndex by remember { mutableIntStateOf(0) }
    var lastScrollOffset by remember { mutableIntStateOf(0) }

    LaunchedEffect(scrollStateColumn) {
        snapshotFlow { scrollStateColumn.firstVisibleItemIndex to scrollStateColumn.firstVisibleItemScrollOffset }
            .collect { (visibleItemIndex, scrollOffset) ->
               if (visibleItemIndex == 0) {
                   showSearchBar =  false
                }
               else {
                    showSearchBar  =   visibleItemIndex < lastVisibleItemIndex ||
              (visibleItemIndex == lastVisibleItemIndex && scrollOffset < lastScrollOffset)
                    lastVisibleItemIndex = visibleItemIndex
                    lastScrollOffset = scrollOffset
                }

            }
    }

    LaunchedEffect(animeSearchList) {
        Log.d("search", "home screen: $animeSearchList")
    }
    BackHandler(enabled = detailView.value) {
        detailView.value = false
    }
    BackHandler(enabled = true) {
        viewModel.clearSearch()
    }
    LaunchedEffect(Unit) {
        if (animeListState.loadState.refresh is LoadState.Loading) {

        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (!detailView.value) {
            when (animeListState.loadState.refresh) {
                is LoadState.Error -> {
                    Text(text = "Error")
                }

                is LoadState.Loading -> {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(100.dp),
                            color = Color.Red
                        )

                    }
                    Log.d("loading", "home page")
                }

                else -> {
                    if (animeSearchList is ApiState.Success) {
                            LazyVerticalGrid(columns = GridCells.Fixed(2), state = scrollState) {
                                items(animeSearchList.data.animeList.size) { index ->
                                    val anime = animeSearchList.data.animeList[index]
                                    anime.let {
                                        AnimeItem(anime = it, click = {
                                            viewModel.getAnimeDetail(it.id)
                                            viewModel.getColor(it.img, imageLoader)
                                        })
                                    }
                                }
                            }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = scrollStateColumn
                        ) {
                            item {
                                ImageCarousel(
                                    imageList = listOf(
                                        R.drawable.sololeveling,
                                        R.drawable.demo,
                                        R.drawable.applogo,
                                    )
                                )
                            }

                            items(animeListState.itemCount / 2 + animeListState.itemCount % 2) { rowIndex ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val firstIndex = rowIndex * 2
                                    val firstAnime = animeListState[firstIndex]
                                    firstAnime?.let {
                                        AnimeItem(
                                            anime = it,
                                            click = {
                                                viewModel.getAnimeDetail(it.id)
                                                detailView.value = true
                                                viewModel.getColor(it.img, imageLoader)

                                            },
                                        )
                                    }

                                    if (firstIndex + 1 < animeListState.itemCount) {
                                        val secondAnime = animeListState[firstIndex + 1]
                                        secondAnime?.let {
                                            AnimeItem(
                                                anime = it,
                                                click = {
                                                    viewModel.getAnimeDetail(it.id)
                                                    detailView.value = true
                                                    viewModel.getColor(it.img, imageLoader)
                                                },
                                            )
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }

                    }
                    if (showSearchBar) {
                        MySearchBar(search = {
                            viewModel.getSearchAnime(searchQuery = it)

                        })
                    }

                }
            }

        } else {
            DetailPageView(dominantColor, animeDetailState)
            Log.d("color home screen", "$dominantColor")

        }
    }


}


@Composable
fun AnimeItem(anime: Anime, click: () -> Unit) {
    Box(
        modifier = Modifier
            .width(170.dp)
            .height(300.dp)
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
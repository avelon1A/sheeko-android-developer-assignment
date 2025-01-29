package com.example.seekhoandoridassignment.presntation.screens

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import com.example.seekhoandoridassignment.data.dto.Anime
import com.example.seekhoandoridassignment.data.dto.AnimeDetailsDto
import com.example.seekhoandoridassignment.presntation.viewmodels.HomeViewModel
import com.example.seekhoandoridassignment.uitl.ApiState
import com.skydoves.cloudy.cloudy
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = koinViewModel()
    val animeListState = viewModel.animeList.collectAsLazyPagingItems()
    val animeDetailState = viewModel.animeDetailState.collectAsState()
    val animeSearchList = viewModel.animeSearch.collectAsState().value
    var animeDetails = AnimeDetailsDto(title = "title", trailer = null, plot = "", genres = null, mainCast = null, noOfEpisodes = null, imageUrl = "")
    var dominantColor = viewModel.color.collectAsState()
    val detailview = remember { mutableStateOf(false)}
    val scrollState = rememberLazyGridState()
    var showSearchBar by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build()
    }
    var lastVisibleItemIndex by remember { mutableStateOf(0) }
    var lastScrollOffset by remember { mutableStateOf(0) }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.firstVisibleItemIndex to scrollState.firstVisibleItemScrollOffset }
            .collect { (visibleItemIndex, scrollOffset) ->
                showSearchBar = visibleItemIndex < lastVisibleItemIndex ||
                        (visibleItemIndex == lastVisibleItemIndex && scrollOffset < lastScrollOffset)

                lastVisibleItemIndex = visibleItemIndex
                lastScrollOffset = scrollOffset


            }
    }


    LaunchedEffect(animeSearchList) {
        Log.d("search", "home screen: $animeSearchList")
    }
    BackHandler(enabled = detailview.value) {
        detailview.value = false
    }
    LaunchedEffect(Unit) {
        if (animeListState.loadState.refresh is LoadState.Loading) {
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        if (detailview.value == false) {
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
                    }
                    else{
                        LazyVerticalGrid(columns =GridCells.Fixed(2), state = scrollState ) {
                            items(animeListState.itemCount) { index ->
                                val anime = animeListState[index]
                                anime?.let {
                                    AnimeItem(anime = it, click = {
                                        viewModel.getAnimeDetail(it.id); detailview.value = true;viewModel.getColor(it.img,imageLoader) ; dominantColor
                                    })
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
            DetailPageView(dominantColor, animeDetailState, animeDetails)
            Log.d("color home screen", "$dominantColor")

        }
    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar( search: (searchQuery: String) -> Unit) {
    var textFieldState by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(Modifier.fillMaxSize().semantics { isTraversalGroup = true }) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .cloudy()
                .semantics { traversalIndex = 0f },
            query = textFieldState,
            onQueryChange = { textFieldState = it },
            onSearch = { search(textFieldState) },
            active = expanded,
            onActiveChange = { expanded = it },
            placeholder = { Text(text = "Hinted search text", color = Color.Black) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
            trailingIcon = {
                if (textFieldState.isNotEmpty()) {
                    IconButton(onClick = { textFieldState = "" }) {
                        Icon(Icons.Filled.Close, contentDescription = "Clear text")
                    }
                }
            },
            shape = MaterialTheme.shapes.small,
            colors = SearchBarDefaults.colors(
                containerColor = Color.Gray.copy(0.6f),
            ),
        )
        {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                repeat(4) { idx ->
                    val resultText = "Suggestion $idx"
                    ListItem(
                        headlineContent = { Text(resultText) },
                        supportingContent = { Text("Additional info") },
                        leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                textFieldState = resultText
                                expanded = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
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
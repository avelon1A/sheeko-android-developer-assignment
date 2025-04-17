package com.example.seekhoandoridassignment.presntation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seekhoandoridassignment.presntation.common.MySearchBar
import com.example.seekhoandoridassignment.presntation.viewmodels.HomeViewModel
import com.example.seekhoandoridassignment.uitl.ApiState
import kotlinx.serialization.Serializable


@Composable
fun SearchScreen(navController: NavController, viewModel: HomeViewModel) {
    val animeSearchList = viewModel.animeSearch.collectAsState().value
    val context = LocalContext.current
    var showSearchBar by remember { mutableStateOf(true) }
    val scrollStateColumn = rememberLazyListState()
    val scrollState = rememberLazyGridState()

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
    Box(modifier = Modifier.fillMaxSize()) {


        if (animeSearchList is ApiState.Success) {
            LazyVerticalGrid(columns = GridCells.Fixed(2), state = scrollState, modifier = Modifier.padding(top = 20.dp)) {
                items(animeSearchList.data.animeList.size) { index ->
                    val anime = animeSearchList.data.animeList[index]
                    anime.let {
                        AnimeItem(anime = it, click = {
                            viewModel.fetchAnimeDetail(it.id)
                            viewModel.getDominantColorFromUrl(it.img)
                        })
                    }
                }
            }
        }
        MySearchBar(search = {
            viewModel.fetchSearchAnime(query = it) })

    }






}

@Serializable
object SearchScreen
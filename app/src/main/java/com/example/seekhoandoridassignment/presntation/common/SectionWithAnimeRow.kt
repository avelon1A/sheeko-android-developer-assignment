package com.example.seekhoandoridassignment.presntation.common

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.seekhoandoridassignment.data.dto.Anime
import com.example.seekhoandoridassignment.data.dto.AnimeListDto
import com.example.seekhoandoridassignment.presntation.screens.AnimeItem

@Composable
fun SectionWithAnimeRow(
    title: String,
    animeList: AnimeListDto,
    onClick: (Anime) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(animeList.animeList.size) { index ->
                val anime = animeList.animeList[index]
                AnimeItem(
                    anime = anime,
                    click = { onClick(anime) },
                    modifier = Modifier
                        .width(150.dp)
                        .padding(end = 8.dp)
                )
            }
        }
    }
}

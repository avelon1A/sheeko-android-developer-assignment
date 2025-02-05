package com.example.seekhoandoridassignment.presntation.screens

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.SubcomposeAsyncImage
import com.example.seekhoandoridassignment.data.dto.AnimeCharactersDto
import com.example.seekhoandoridassignment.data.dto.AnimeDetailsDto
import com.example.seekhoandoridassignment.presntation.common.VideoPlayer
import com.example.seekhoandoridassignment.uitl.ApiState


@Composable
fun DetailView(animeDetails: AnimeDetailsDto) {
    Text(
        text = animeDetails.title,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 20.dp, top = 12.dp, start = 10.dp)
    )
    TrailerSection(
        trailerUrl = animeDetails.trailer,
        imageUrl = animeDetails.imageUrl,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )




    Spacer(modifier = Modifier.height(16.dp))

    animeDetails.mainCast?.let { cast ->
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Main Cast",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            CastSection(cast)

            Text(
                text = "Episodes: ${animeDetails.noOfEpisodes}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Genres: ${animeDetails.genres}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Plot",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = animeDetails.plot,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }


}

@Composable
fun TrailerSection(trailerUrl: String?, imageUrl: String, modifier: Modifier) {
    if (trailerUrl != null) {
        val lifecycle = LocalLifecycleOwner.current
        VideoPlayer(id = trailerUrl, modifier = modifier, lifecycleOwner = lifecycle)
    } else {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop,
            loading = {
                Crossfade(targetState = imageUrl) { targetImageUrl ->
                    if (targetImageUrl.isNotEmpty()) {
                        SubcomposeAsyncImage(
                            model = targetImageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                        )
                    }
                }

            },
            success = {
                Crossfade(targetState = it) { imageState ->
                    Image(
                        painter = imageState.painter,
                        contentDescription = null,
                        modifier = modifier,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        )
    }
}


@Composable
fun CastSection(cast: List<AnimeCharactersDto>) {
    val scrollable = rememberScrollState()
    Row(Modifier.horizontalScroll(scrollable)) {
        cast.forEach { character ->
            val scale by animateDpAsState(
                targetValue = if (character.name != null) 1.1.dp else 1.dp,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )

            Column(
                modifier = Modifier
                    .size(120.dp)
                    .padding(vertical = 4.dp)
                    .graphicsLayer(scaleX = scale.value, scaleY = scale.value),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SubcomposeAsyncImage(
                    model = character.images,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = character.name ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}


@Composable
fun DetailPageView(
    dominantColor: State<Color>,
    animeDetailState: State<ApiState<AnimeDetailsDto>>,
) {
    var animeDetails1: AnimeDetailsDto
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, dominantColor.value.copy(0.8f))
                )
            ), contentAlignment = Alignment.Center
    )
    {

        when (val state = animeDetailState.value) {
            is ApiState.Error -> {
                Log.d("error", state.message.toString())
                Text(text = state.message.toString())
            }

            is ApiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                )
                {
                    animeDetails1 = state.data
                    DetailView(animeDetails1)
                }
            }

            ApiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .width(150.dp)
                            .height(8.dp),
                        color = Color.Red
                    )

                }

            }


        }

    }
}


@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DetailView(
            animeDetails = AnimeDetailsDto(
                title = "test preview",
                trailer = "https://www.youtube.com/watch?v=KA_JwkKBtL8",
                plot = "\"Yukino Miyazawa is the female representative for her class and the most popular girl among the freshmen at her high school. Good at both academics and sports on top of being elegant and sociable, she has been an object of admiration all her life. However, in reality, she is an incredibly vain person who toils relentlessly to maintain her good grades, athleticism, and graceful appearance. She wants nothing more than to be the center of attention and praise—which is why she cannot stand Souichirou Arima, the male representative for her class and the only person more perfect than her. Since the first day of high school, she has struggled to steal the spotlight from her new rival but to no avail.\\n\\nAt last, on the midterm exams, Yukino gets the top score and beats Souichirou. But, to her surprise, he congratulates her on her achievement, leading her to question her deceptive lifestyle. When Souichirou confesses his love to Yukino, she turns him down and gloats about it at home with only a hint of regret. But the very next day, Souichirou visits Yukino house to bring her a CD and sees her uninhibited self in action; now equipped with the truth, he blackmails her into completing his student council duties. Coerced into spending time with Souichirou, Yukino learns that she is not the only one hiding secrets.\\n\\n[Written by MAL Rewrite]\"",
                genres = "test",
                mainCast = listOf(
                    AnimeCharactersDto(
                        name = "Character 1",
                        images = "https://example.com/image1.jpg"
                    ),
                    AnimeCharactersDto(
                        name = "Character 2",
                        images = "https://example.com/image2.jpg"
                    )
                ),
                noOfEpisodes = 3,
                imageUrl = "https://cdn.myanimelist.net/images/characters/4/123155.jpg?s=71a949a12df96189b1203bfcbbda625a"
            )
        )
    }

}




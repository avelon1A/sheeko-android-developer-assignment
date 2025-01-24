package com.example.seekhoandoridassignment.presntation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.seekhoandoridassignment.data.dto.AnimeDetailsDto
import com.example.seekhoandoridassignment.uitl.ApiState

@Composable
 fun DetailPageView(
    dominantColor: MutableState<Color>,
    animeDetailState: State<ApiState<AnimeDetailsDto>>,
    animeDetails: AnimeDetailsDto?
) {
    var animeDetails1 = animeDetails
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        )
        {
            when (val state = animeDetailState.value) {
                is ApiState.Error -> {
                    Log.d("error", state.message.toString())
                    Text(text = state.message.toString())
                }

                is ApiState.Success -> {
                    animeDetails1 = state.data
                    DetailView(animeDetails1)
                }

                ApiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(600.dp)
                            .fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp),
                            color = Color.Red
                        )

                    }

                }
            }

        }

    }
}
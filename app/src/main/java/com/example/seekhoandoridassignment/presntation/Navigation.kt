package com.example.seekhoandoridassignment.presntation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.seekhoandoridassignment.presntation.screens.DetailScreen
import com.example.seekhoandoridassignment.presntation.screens.DetailScreenNav
import com.example.seekhoandoridassignment.presntation.screens.HomeScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
) {
    SharedTransitionLayout {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = HomeScreen,
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            composable<HomeScreen> {
                HomeScreen( navController )
            }
        composable<DetailScreenNav> {
            val animeId = it.toRoute<DetailScreenNav>()
            DetailScreen(animeId = animeId.animeId,imageUrl = animeId.imageUrl)
        }

//            this@SharedTransitionLayout, this@composable

        }
    }
}